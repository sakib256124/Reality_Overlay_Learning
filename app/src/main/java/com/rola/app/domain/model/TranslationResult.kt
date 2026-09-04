package com.rola.app.domain.model

data class TranslationResult(
    val cacheId: String,
    val sourceText: String,
    val sourceLanguage: String,
    val targetLanguage: String,
    val translatedText: String,
    val timestamp: Long = System.currentTimeMillis(),
    val fromCache: Boolean = false,
)

data class TranslatedObjectInformation(
    val original: ObjectInformation,
    val translated: ObjectInformation,
    val sourceLanguage: String,
    val targetLanguage: String,
    val fromCache: Boolean,
)

