package com.rola.app.domain.model

enum class QuizDifficulty {
    Easy,
    Medium,
    Hard,
}

data class Quiz(
    val quizId: String,
    val objectId: String,
    val title: String,
    val difficulty: QuizDifficulty,
    val questions: List<Question>,
    val createdDate: Long,
)
