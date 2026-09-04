package com.rola.app.embodied_ai.interaction

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceInteractionManager @Inject constructor() {
    fun normalizeUtterance(utterance: String): String =
        utterance.trim().replace(Regex("\\s+"), " ")

    fun detectIntent(utterance: String): RobotVoiceIntent = when {
        utterance.contains("why", ignoreCase = true) || utterance.contains("how", ignoreCase = true) -> RobotVoiceIntent.Question
        utterance.contains("show", ignoreCase = true) || utterance.contains("demonstrate", ignoreCase = true) -> RobotVoiceIntent.DemonstrationRequest
        utterance.contains("help", ignoreCase = true) -> RobotVoiceIntent.HelpRequest
        utterance.contains("translate", ignoreCase = true) -> RobotVoiceIntent.Translate
        else -> RobotVoiceIntent.Conversation
    }

    fun speak(text: String, languageCode: String): String =
        "[$languageCode] ${text.take(240)}"
}

enum class RobotVoiceIntent {
    Question,
    DemonstrationRequest,
    HelpRequest,
    Translate,
    Conversation,
}
