package com.rola.app.data.translation

import android.content.Context
import com.google.mlkit.nl.translate.TranslateLanguage
import com.rola.app.data.voice.VoiceLanguage
import com.rola.app.domain.model.Language
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class LanguageManager @Inject constructor(
    @ApplicationContext context: Context,
) {
    val supportedLanguages: List<Language> = listOf(
        Language("en", "English", "English", supportedVoice = true),
        Language("bn", "Bengali", "বাংলা", supportedVoice = true),
        Language("es", "Spanish", "Español", supportedVoice = true),
        Language("fr", "French", "Français", supportedVoice = true),
        Language("de", "German", "Deutsch", supportedVoice = true),
        Language("zh", "Chinese", "中文", supportedVoice = true),
        Language("ar", "Arabic", "العربية", supportedVoice = true),
    )

    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)
    private val _selectedLanguageCode = MutableStateFlow(
        languageForCode(preferences.getString(KEY_SELECTED_LANGUAGE, DEFAULT_LANGUAGE_CODE).orEmpty()).languageCode,
    )
    val selectedLanguageCode: StateFlow<String> = _selectedLanguageCode.asStateFlow()

    fun languageForCode(languageCode: String): Language =
        supportedLanguages.firstOrNull { it.languageCode == normalizeLanguageCode(languageCode) }
            ?: supportedLanguages.first()

    fun setSelectedLanguage(languageCode: String): Language {
        val language = languageForCode(languageCode)
        preferences.edit()
            .putString(KEY_SELECTED_LANGUAGE, language.languageCode)
            .apply()
        _selectedLanguageCode.value = language.languageCode
        return language
    }

    fun toTranslateLanguage(languageCode: String): String? = when (normalizeLanguageCode(languageCode)) {
        "en" -> TranslateLanguage.ENGLISH
        "bn" -> TranslateLanguage.BENGALI
        "es" -> TranslateLanguage.SPANISH
        "fr" -> TranslateLanguage.FRENCH
        "de" -> TranslateLanguage.GERMAN
        "zh" -> TranslateLanguage.CHINESE
        "ar" -> TranslateLanguage.ARABIC
        else -> null
    }

    fun toVoiceLanguage(languageCode: String): VoiceLanguage = when (normalizeLanguageCode(languageCode)) {
        "bn" -> VoiceLanguage.Bengali
        "es" -> VoiceLanguage.Spanish
        "fr" -> VoiceLanguage.French
        "de" -> VoiceLanguage.German
        "zh" -> VoiceLanguage.Chinese
        "ar" -> VoiceLanguage.Arabic
        else -> VoiceLanguage.English
    }

    fun normalizeLanguageCode(languageCode: String): String = languageCode
        .lowercase()
        .substringBefore('-')
        .ifBlank { "en" }

    private companion object {
        const val PREFERENCES_NAME = "rola_language_preferences"
        const val KEY_SELECTED_LANGUAGE = "selected_learning_language"
        const val DEFAULT_LANGUAGE_CODE = "en"
    }
}
