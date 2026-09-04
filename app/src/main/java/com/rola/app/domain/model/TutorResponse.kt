package com.rola.app.domain.model

data class TutorResponse(
    val message: ChatMessage,
    val sources: List<String>,
    val confidence: Float,
    val suggestedQuestions: List<String>,
)
