package com.rola.app.domain.model

data class Question(
    val questionId: String,
    val questionText: String,
    val options: List<Answer>,
    val correctAnswer: Answer,
    val explanation: String,
)
