package com.rola.app.domain.model

data class Language(
    val languageCode: String,
    val languageName: String,
    val nativeName: String,
    val supportedVoice: Boolean,
)

