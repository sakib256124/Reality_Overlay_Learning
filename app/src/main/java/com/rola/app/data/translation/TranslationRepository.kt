package com.rola.app.data.translation

import com.google.firebase.firestore.FirebaseFirestore
import com.rola.app.data.database.TranslationDao
import com.rola.app.data.database.entities.toEntity
import com.rola.app.domain.model.Answer
import com.rola.app.domain.model.Language
import com.rola.app.domain.model.ObjectInformation
import com.rola.app.domain.model.Question
import com.rola.app.domain.model.Quiz
import com.rola.app.domain.model.TranslatedObjectInformation
import com.rola.app.domain.model.TranslationResult
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

@Singleton
class TranslationRepository @Inject constructor(
    private val translationDao: TranslationDao,
    private val translationService: TranslationService,
    private val languageManager: LanguageManager,
    private val firestore: FirebaseFirestore,
) {
    fun observeLanguages(): Flow<List<Language>> =
        translationDao.observeLanguages().map { entities ->
            if (entities.isEmpty()) languageManager.supportedLanguages else entities.map { it.toDomain() }
        }

    fun observeSelectedLanguageCode(): Flow<String> = languageManager.selectedLanguageCode

    fun selectedLanguageCode(): String = languageManager.selectedLanguageCode.value

    fun setSelectedLanguage(languageCode: String): Language =
        languageManager.setSelectedLanguage(languageCode)

    suspend fun ensureLanguagesSeeded() {
        translationDao.upsertLanguages(languageManager.supportedLanguages.map { it.toEntity() })
    }

    fun supportedLanguages(): List<Language> = languageManager.supportedLanguages

    suspend fun detectLanguage(text: String): String = translationService.detectLanguage(text)

    suspend fun translateText(
        text: String,
        targetLanguage: String,
        sourceLanguage: String? = null,
    ): TranslationResult {
        val sanitizedText = sanitizeInput(text)
        val source = sourceLanguage
            ?: translationService.detectLanguage(sanitizedText).takeUnless { it == TranslationService.UNDETERMINED_LANGUAGE }
            ?: TranslationService.DEFAULT_SOURCE_LANGUAGE
        val normalizedSource = languageManager.normalizeLanguageCode(source)
        val normalizedTarget = languageManager.normalizeLanguageCode(targetLanguage)
        val cacheId = cacheKey(sanitizedText, normalizedSource, normalizedTarget)
        translationDao.getCachedTranslation(cacheId)?.let { return it.toDomain(fromCache = true) }

        fetchCloudTranslation(cacheId)?.let { cloudResult ->
            translationDao.upsertTranslation(cloudResult.toEntity())
            return cloudResult.copy(fromCache = true)
        }

        val result = translationService.translateText(sanitizedText, normalizedTarget, normalizedSource)
        translationDao.upsertTranslation(result.toEntity())
        translationDao.markLanguageDownloaded(normalizedTarget)
        uploadCloudTranslation(result)
        return result
    }

    suspend fun downloadLanguagePack(
        targetLanguage: String,
        sourceLanguage: String = TranslationService.DEFAULT_SOURCE_LANGUAGE,
    ) {
        translationService.downloadLanguagePack(targetLanguage, sourceLanguage)
        translationDao.markLanguageDownloaded(languageManager.normalizeLanguageCode(targetLanguage))
    }

    suspend fun translateObjectInformation(
        information: ObjectInformation,
        targetLanguage: String,
        sourceLanguage: String = TranslationService.DEFAULT_SOURCE_LANGUAGE,
    ): TranslatedObjectInformation {
        if (targetLanguage == sourceLanguage) {
            return TranslatedObjectInformation(
                original = information,
                translated = information,
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage,
                fromCache = true,
            )
        }

        val name = translateText(information.name, targetLanguage, sourceLanguage)
        val scientificName = translateText(information.scientificName, targetLanguage, sourceLanguage)
        val category = translateText(information.category, targetLanguage, sourceLanguage)
        val description = translateText(information.description, targetLanguage, sourceLanguage)
        val uses = information.uses.map { translateText(it, targetLanguage, sourceLanguage) }
        val facts = information.facts.map { translateText(it, targetLanguage, sourceLanguage) }
        val allResults = listOf(name, scientificName, category, description) + uses + facts

        return TranslatedObjectInformation(
            original = information,
            translated = information.copy(
                name = name.translatedText,
                scientificName = scientificName.translatedText,
                category = category.translatedText,
                description = description.translatedText,
                uses = uses.map { it.translatedText },
                facts = facts.map { it.translatedText },
            ),
            sourceLanguage = sourceLanguage,
            targetLanguage = targetLanguage,
            fromCache = allResults.all { it.fromCache },
        )
    }

    suspend fun translateTutorResponse(
        responseText: String,
        targetLanguage: String,
        sourceLanguage: String? = null,
    ): TranslationResult = translateText(responseText, targetLanguage, sourceLanguage)

    suspend fun translateQuizText(
        quizText: String,
        targetLanguage: String,
        sourceLanguage: String = TranslationService.DEFAULT_SOURCE_LANGUAGE,
    ): TranslationResult = translateText(quizText, targetLanguage, sourceLanguage)

    suspend fun translateQuiz(
        quiz: Quiz,
        targetLanguage: String,
        sourceLanguage: String = TranslationService.DEFAULT_SOURCE_LANGUAGE,
    ): Quiz {
        val normalizedTarget = languageManager.normalizeLanguageCode(targetLanguage)
        val normalizedSource = languageManager.normalizeLanguageCode(sourceLanguage)
        if (normalizedTarget == normalizedSource) return quiz

        val title = translateQuizText(quiz.title, normalizedTarget, normalizedSource).translatedText
        val questions = quiz.questions.map { question ->
            val translatedOptions = question.options.map { answer ->
                answer.copy(text = translateQuizText(answer.text, normalizedTarget, normalizedSource).translatedText)
            }
            val translatedCorrectAnswer = translatedOptions.firstOrNull {
                it.answerId == question.correctAnswer.answerId
            } ?: Answer(
                answerId = question.correctAnswer.answerId,
                text = translateQuizText(question.correctAnswer.text, normalizedTarget, normalizedSource).translatedText,
            )

            Question(
                questionId = question.questionId,
                questionText = translateQuizText(question.questionText, normalizedTarget, normalizedSource).translatedText,
                options = translatedOptions,
                correctAnswer = translatedCorrectAnswer,
                explanation = translateQuizText(question.explanation, normalizedTarget, normalizedSource).translatedText,
            )
        }

        return quiz.copy(title = title, questions = questions)
    }

    suspend fun pruneOldCache(maxAgeMillis: Long = CACHE_MAX_AGE_MILLIS) {
        translationDao.deleteOldTranslations(System.currentTimeMillis() - maxAgeMillis)
    }

    fun voiceLanguageFor(languageCode: String) = languageManager.toVoiceLanguage(languageCode)

    private fun sanitizeInput(text: String): String {
        val sanitized = text.replace(Regex("[\\u0000-\\u001F\\u007F]"), " ").trim()
        require(sanitized.isNotBlank()) { "Translation text is empty." }
        require(sanitized.length <= MAX_TRANSLATION_INPUT_LENGTH) {
            "Translation text is too long. Try a shorter explanation."
        }
        return sanitized
    }

    private suspend fun fetchCloudTranslation(cacheId: String): TranslationResult? = runCatching {
        firestore.collection(TRANSLATIONS_COLLECTION)
            .document(cacheId.substringBefore(':', missingDelimiterValue = "unknown"))
            .collection(TRANSLATED_CONTENT_COLLECTION)
            .document(cacheId.substringAfter(':', missingDelimiterValue = cacheId))
            .get()
            .await()
            .takeIf { it.exists() }
            ?.let { snapshot ->
                TranslationResult(
                    cacheId = cacheId,
                    sourceText = snapshot.getString("sourceText").orEmpty(),
                    sourceLanguage = snapshot.getString("sourceLanguage").orEmpty(),
                    targetLanguage = snapshot.getString("targetLanguage").orEmpty(),
                    translatedText = snapshot.getString("translatedText").orEmpty(),
                    timestamp = snapshot.getLong("timestamp") ?: System.currentTimeMillis(),
                    fromCache = true,
                )
            }
            ?.takeIf { it.translatedText.isNotBlank() }
    }.getOrNull()

    private suspend fun uploadCloudTranslation(result: TranslationResult) {
        runCatching {
            firestore.collection(TRANSLATIONS_COLLECTION)
                .document(result.targetLanguage)
                .collection(TRANSLATED_CONTENT_COLLECTION)
                .document(result.cacheId.substringAfter(':', missingDelimiterValue = result.cacheId))
                .set(
                    mapOf(
                        "sourceText" to result.sourceText,
                        "sourceLanguage" to result.sourceLanguage,
                        "targetLanguage" to result.targetLanguage,
                        "translatedText" to result.translatedText,
                        "timestamp" to result.timestamp,
                    ),
                )
                .await()
        }
    }

    companion object {
        const val TRANSLATIONS_COLLECTION = "translations"
        const val TRANSLATED_CONTENT_COLLECTION = "translatedContent"
        private const val CACHE_MAX_AGE_MILLIS = 1000L * 60L * 60L * 24L * 90L
        private const val MAX_TRANSLATION_INPUT_LENGTH = 5_000

        fun cacheKey(
            sourceText: String,
            sourceLanguage: String,
            targetLanguage: String,
        ): String {
            val raw = "${sourceLanguage.lowercase()}|${targetLanguage.lowercase()}|${sourceText.trim()}"
            val digest = MessageDigest.getInstance("SHA-256").digest(raw.toByteArray())
            return "${targetLanguage.lowercase()}:${digest.joinToString(separator = "") { "%02x".format(it) }}"
        }
    }
}
