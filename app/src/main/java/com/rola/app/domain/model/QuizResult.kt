package com.rola.app.domain.model

data class QuizResult(
    val resultId: String,
    val userId: String,
    val quizId: String,
    val score: Int,
    val totalQuestions: Int,
    val percentage: Int,
    val completionTime: Long,
    val timestamp: Long,
)

data class LearningProgress(
    val totalQuizzesCompleted: Int,
    val averageScore: Int,
    val totalPoints: Int,
    val currentLevel: Int,
    val learningStreak: Int,
    val strongTopics: List<String>,
    val weakTopics: List<String>,
    val badges: List<String>,
)
