package com.rola.app.data.adaptive

import com.rola.app.domain.model.LearningPattern
import com.rola.app.domain.model.LearningProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIRecommendationService @Inject constructor(
    private val recommendationEngine: RecommendationEngine,
) {
    fun learningRoadmap(
        profile: LearningProfile,
        pattern: LearningPattern,
    ): List<String> = listOf(
        "Current level: ${profile.learningLevel.name}",
        "Recommended topic: ${recommendationEngine.predictNextTopic(profile, pattern)}",
        "Practice: ${recommendationEngine.adaptiveQuizDifficulty(profile).name} quiz",
        "Assessment: review weak areas after the next quiz",
        "Next goal: ${recommendationEngine.predictImprovementTrend(pattern)}",
    )

    fun tutorPersonalizationPrefix(profile: LearningProfile): String =
        when (profile.learningLevel) {
            com.rola.app.domain.model.SkillLevel.Beginner ->
                "Since you are a beginner, use a simple example first."
            com.rola.app.domain.model.SkillLevel.Intermediate ->
                "Use a balanced explanation with one real-world connection."
            com.rola.app.domain.model.SkillLevel.Advanced ->
                "Use scientific vocabulary and compare related concepts."
        }
}
