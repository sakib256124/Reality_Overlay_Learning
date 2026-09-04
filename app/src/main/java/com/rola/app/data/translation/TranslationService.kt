package com.rola.app.data.translation

import com.google.mlkit.nl.languageid.LanguageIdentification
import com.google.mlkit.nl.translate.DownloadConditions
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import com.rola.app.domain.model.ObjectInformation
import com.rola.app.domain.model.TranslatedObjectInformation
import com.rola.app.domain.model.TranslationResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

@Singleton
class TranslationService @Inject constructor(
    private val languageManager: LanguageManager,
) {
    private val languageIdentifier by lazy { LanguageIdentification.getClient() }
    private val translators = mutableMapOf<String, Translator>()

    suspend fun translateText(
        text: String,
        targetLanguage: String,
        sourceLanguage: String? = null,
    ): TranslationResult = withContext(Dispatchers.Default) {
        require(text.isNotBlank()) { "Translation text is empty." }
        val detectedSource = sourceLanguage?.takeIf { it.isNotBlank() }
            ?: detectLanguage(text).takeUnless { it == UNDETERMINED_LANGUAGE }
            ?: DEFAULT_SOURCE_LANGUAGE
        val normalizedSource = languageManager.normalizeLanguageCode(detectedSource)
        val normalizedTarget = languageManager.normalizeLanguageCode(targetLanguage)

        if (normalizedSource == normalizedTarget) {
            return@withContext TranslationResult(
                cacheId = TranslationRepository.cacheKey(text, normalizedSource, normalizedTarget),
                sourceText = text,
                sourceLanguage = normalizedSource,
                targetLanguage = normalizedTarget,
                translatedText = text,
            )
        }

        val source = languageManager.toTranslateLanguage(normalizedSource)
            ?: throw IllegalArgumentException("Unsupported source language: $normalizedSource")
        val target = languageManager.toTranslateLanguage(normalizedTarget)
            ?: throw IllegalArgumentException("Unsupported target language: $normalizedTarget")
        val translator = translatorFor(source, target)
        val conditions = DownloadConditions.Builder().build()

        translator.downloadModelIfNeeded(conditions).await()
        val translated = translator.translate(text).await()

        TranslationResult(
            cacheId = TranslationRepository.cacheKey(text, normalizedSource, normalizedTarget),
            sourceText = text,
            sourceLanguage = normalizedSource,
            targetLanguage = normalizedTarget,
            translatedText = translated,
        )
    }

    suspend fun detectLanguage(text: String): String = withContext(Dispatchers.Default) {
        if (text.isBlank()) return@withContext UNDETERMINED_LANGUAGE
        languageManager.normalizeLanguageCode(languageIdentifier.identifyLanguage(text).await())
    }

    fun getSupportedLanguages() = languageManager.supportedLanguages

    suspend fun downloadLanguagePack(
        targetLanguage: String,
        sourceLanguage: String = DEFAULT_SOURCE_LANGUAGE,
    ) = withContext(Dispatchers.Default) {
        val normalizedSource = languageManager.normalizeLanguageCode(sourceLanguage)
        val normalizedTarget = languageManager.normalizeLanguageCode(targetLanguage)
        if (normalizedSource == normalizedTarget) return@withContext

        val source = languageManager.toTranslateLanguage(normalizedSource)
            ?: throw IllegalArgumentException("Unsupported source language: $normalizedSource")
        val target = languageManager.toTranslateLanguage(normalizedTarget)
            ?: throw IllegalArgumentException("Unsupported target language: $normalizedTarget")

        translatorFor(source, target)
            .downloadModelIfNeeded(DownloadConditions.Builder().build())
            .await()
    }

    suspend fun translateObjectInformation(
        information: ObjectInformation,
        targetLanguage: String,
        sourceLanguage: String = DEFAULT_SOURCE_LANGUAGE,
    ): TranslatedObjectInformation {
        val name = translateText(information.name, targetLanguage, sourceLanguage)
        val scientificName = translateText(information.scientificName, targetLanguage, sourceLanguage)
        val category = translateText(information.category, targetLanguage, sourceLanguage)
        val description = translateText(information.description, targetLanguage, sourceLanguage)
        val uses = information.uses.map { translateText(it, targetLanguage, sourceLanguage).translatedText }
        val facts = information.facts.map { translateText(it, targetLanguage, sourceLanguage).translatedText }

        return TranslatedObjectInformation(
            original = information,
            translated = information.copy(
                name = name.translatedText,
                scientificName = scientificName.translatedText,
                category = category.translatedText,
                description = description.translatedText,
                uses = uses,
                facts = facts,
            ),
            sourceLanguage = sourceLanguage,
            targetLanguage = targetLanguage,
            fromCache = false,
        )
    }

    fun close() {
        translators.values.forEach { it.close() }
        translators.clear()
        languageIdentifier.close()
    }

    private fun translatorFor(
        sourceLanguage: String,
        targetLanguage: String,
    ): Translator {
        val key = "$sourceLanguage-$targetLanguage"
        return translators.getOrPut(key) {
            Translation.getClient(
                TranslatorOptions.Builder()
                    .setSourceLanguage(sourceLanguage)
                    .setTargetLanguage(targetLanguage)
                    .build(),
            )
        }
    }

    companion object {
        const val DEFAULT_SOURCE_LANGUAGE = "en"
        const val UNDETERMINED_LANGUAGE = "und"
    }
}
