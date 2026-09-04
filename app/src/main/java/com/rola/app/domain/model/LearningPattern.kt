package com.rola.app.domain.model

data class LearningPattern(
    val strongTopics: List<String>,
    val weakTopics: List<String>,
    val knowledgeGaps: List<String>,
    val preferredCategories: List<String>,
    val dailyLearningTimeMillis: Long,
    val weeklyProgressPercent: Int,
    val quizImprovementTrend: Int,
    val consistencyScore: Int,
    val learningSpeed: LearningSpeed,
)
