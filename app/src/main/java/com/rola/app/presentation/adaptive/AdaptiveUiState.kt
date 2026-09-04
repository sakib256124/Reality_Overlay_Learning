package com.rola.app.presentation.adaptive

import com.rola.app.domain.model.LearningPattern
import com.rola.app.domain.model.LearningProfile
import com.rola.app.domain.model.LearningSpeed
import com.rola.app.domain.model.Recommendation
import com.rola.app.domain.model.SkillLevel

enum class AdaptiveStatus {
    Loading,
    Ready,
    Error,
}

data class AdaptiveUiState(
    val status: AdaptiveStatus = AdaptiveStatus.Loading,
    val profile: LearningProfile = emptyProfile,
    val pattern: LearningPattern = emptyPattern,
    val recommendations: List<Recommendation> = emptyList(),
    val roadmap: List<String> = emptyList(),
    val nextTopic: String = "Scientific classification",
    val predictedTrend: String = "Building consistency",
    val explanationComplexity: String = "simple example-first explanation",
    val errorMessage: String? = null,
) {
    val progressPercent: Int
        get() = profile.progressPercent

    companion object {
        val emptyProfile = LearningProfile(
            userId = "local_user",
            totalObjectsLearned = 0,
            averageQuizScore = 0,
            favoriteCategories = emptyList(),
            weakAreas = emptyList(),
            learningLevel = SkillLevel.Beginner,
            learningStreak = 0,
            totalLearningTimeMillis = 0L,
            frequentlySearchedTopics = emptyList(),
            difficultConcepts = emptyList(),
            preferredLanguage = "en",
            learningSpeed = LearningSpeed.Balanced,
        )

        val emptyPattern = LearningPattern(
            strongTopics = emptyList(),
            weakTopics = emptyList(),
            knowledgeGaps = emptyList(),
            preferredCategories = emptyList(),
            dailyLearningTimeMillis = 0L,
            weeklyProgressPercent = 0,
            quizImprovementTrend = 0,
            consistencyScore = 0,
            learningSpeed = LearningSpeed.Balanced,
        )
    }
}
