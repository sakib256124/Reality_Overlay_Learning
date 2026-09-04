package com.rola.app.global.network

import com.rola.app.data.translation.TranslationRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalTranslationBridge @Inject constructor(
    private val translationRepository: TranslationRepository,
) {
    suspend fun translateLearningExperience(
        text: String,
        teacherLanguage: String,
        studentLanguage: String,
    ): String {
        if (teacherLanguage == studentLanguage) return text
        return translationRepository.translateText(
            text = text,
            sourceLanguage = teacherLanguage,
            targetLanguage = studentLanguage,
        ).translatedText
    }
}
