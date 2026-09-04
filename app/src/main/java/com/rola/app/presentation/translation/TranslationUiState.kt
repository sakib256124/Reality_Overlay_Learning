package com.rola.app.presentation.translation

import com.rola.app.domain.model.Language
import com.rola.app.domain.model.ObjectInformation

enum class TranslationStatus {
    Idle,
    Loading,
    Ready,
    Speaking,
    Error,
}

data class TranslationUiState(
    val status: TranslationStatus = TranslationStatus.Idle,
    val objectInformation: ObjectInformation? = null,
    val translatedInformation: ObjectInformation? = null,
    val sourceText: String = "",
    val translatedText: String = "",
    val detectedLanguage: String = "en",
    val selectedLanguageCode: String = "en",
    val languages: List<Language> = emptyList(),
    val fromCache: Boolean = false,
    val errorMessage: String? = null,
) {
    val selectedLanguage: Language?
        get() = languages.firstOrNull { it.languageCode == selectedLanguageCode }

    val canTranslate: Boolean
        get() = objectInformation != null || sourceText.isNotBlank()
}

