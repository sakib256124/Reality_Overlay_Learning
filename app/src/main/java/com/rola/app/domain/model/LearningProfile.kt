package com.rola.app.domain.model

data class LearningProfile(
    val userId: String,
    val totalObjectsLearned: Int,
    val averageQuizScore: Int,
    val favoriteCategories: List<String>,
    val weakAreas: List<String>,
    val learningLevel: SkillLevel,
    val learningStreak: Int,
    val totalLearningTimeMillis: Long,
    val frequentlySearchedTopics: List<String>,
    val difficultConcepts: List<String>,
    val preferredLanguage: String,
    val learningSpeed: LearningSpeed,
    val personalizationEnabled: Boolean = true,
    val updatedAt: Long = System.currentTimeMillis(),
) {
    val progressPercent: Int
        get() = when {
            totalObjectsLearned <= 0 -> 0
            learningLevel == SkillLevel.Advanced -> 90
            learningLevel == SkillLevel.Intermediate -> 60
            else -> (totalObjectsLearned * 5).coerceIn(5, 45)
        }
}
