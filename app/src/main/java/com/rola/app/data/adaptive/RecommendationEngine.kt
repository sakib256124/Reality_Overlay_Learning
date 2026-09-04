package com.rola.app.data.adaptive

import com.rola.app.domain.model.LearningPattern
import com.rola.app.domain.model.LearningProfile
import com.rola.app.domain.model.Recommendation
import com.rola.app.domain.model.RecommendationPriority
import com.rola.app.domain.model.RecommendationType
import com.rola.app.domain.model.SkillLevel
import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RecommendationEngine @Inject constructor() {
    fun generateRecommendations(
        profile: LearningProfile,
        pattern: LearningPattern,
    ): List<Recommendation> = buildList {
        val favorite = profile.favoriteCategories.firstOrNull()
        if (favorite != null) {
            add(
                recommendation(
                    profile = profile,
                    title = "Explore ${nextTopicFor(favorite)} next",
                    description = "You often learn about $favorite, so the next useful step is ${nextTopicFor(favorite)}.",
                    topic = nextTopicFor(favorite),
                    type = RecommendationType.ExploreTopic,
                    priority = RecommendationPriority.Medium,
                ),
            )
        }

        profile.weakAreas.firstOrNull()?.let { weakArea ->
            add(
                recommendation(
                    profile = profile,
                    title = "Review $weakArea",
                    description = "Your recent activity suggests $weakArea needs a short refresher before harder lessons.",
                    topic = weakArea,
                    type = RecommendationType.ReviewWeakArea,
                    priority = RecommendationPriority.High,
                    targetSkillLevel = SkillLevel.Beginner,
                ),
            )
        }

        if (profile.averageQuizScore < 65) {
            add(
                recommendation(
                    profile = profile,
                    title = "Practice easier questions",
                    description = "Review basic object category, material, and use questions to rebuild confidence.",
                    topic = "Foundations",
                    type = RecommendationType.PracticeQuiz,
                    priority = RecommendationPriority.High,
                    targetSkillLevel = SkillLevel.Beginner,
                ),
            )
        } else if (profile.averageQuizScore >= 85) {
            add(
                recommendation(
                    profile = profile,
                    title = "Try advanced science prompts",
                    description = "Your quiz results are strong. Ask the tutor for scientific explanations and comparisons.",
                    topic = "Advanced reasoning",
                    type = RecommendationType.AskTutor,
                    priority = RecommendationPriority.Medium,
                    targetSkillLevel = SkillLevel.Advanced,
                ),
            )
        }

        if (profile.learningStreak == 0) {
            add(
                recommendation(
                    profile = profile,
                    title = "Start today's learning streak",
                    description = "Scan one familiar object, listen to the explanation, then answer one practice question.",
                    topic = "Daily habit",
                    type = RecommendationType.ContinueStreak,
                    priority = RecommendationPriority.Medium,
                ),
            )
        }

        pattern.knowledgeGaps.firstOrNull()?.let { gap ->
            add(
                recommendation(
                    profile = profile,
                    title = "Close the $gap gap",
                    description = "A focused lesson on $gap will make future object explanations easier to understand.",
                    topic = gap,
                    type = RecommendationType.ExploreTopic,
                    priority = RecommendationPriority.Medium,
                ),
            )
        }
    }
        .distinctBy { "${it.type}:${it.topic}" }
        .take(MAX_RECOMMENDATIONS)

    fun adaptiveQuizDifficulty(profile: LearningProfile): SkillLevel =
        when {
            profile.averageQuizScore >= 85 && profile.totalObjectsLearned >= 10 -> SkillLevel.Advanced
            profile.averageQuizScore >= 60 || profile.totalObjectsLearned >= 5 -> SkillLevel.Intermediate
            else -> SkillLevel.Beginner
        }

    fun explanationComplexity(profile: LearningProfile): String =
        when (profile.learningLevel) {
            SkillLevel.Beginner -> "simple example-first explanation"
            SkillLevel.Intermediate -> "balanced explanation with everyday examples and science terms"
            SkillLevel.Advanced -> "scientific explanation with comparisons and mechanisms"
        }

    fun predictNextTopic(profile: LearningProfile, pattern: LearningPattern): String =
        profile.weakAreas.firstOrNull()
            ?: pattern.knowledgeGaps.firstOrNull()
            ?: profile.favoriteCategories.firstOrNull()?.let(::nextTopicFor)
            ?: "Scientific classification"

    fun predictImprovementTrend(pattern: LearningPattern): String =
        when {
            pattern.quizImprovementTrend >= 10 -> "Improving quickly"
            pattern.quizImprovementTrend <= -10 -> "Needs review"
            pattern.consistencyScore >= 70 -> "Stable progress"
            else -> "Building consistency"
        }

    private fun recommendation(
        profile: LearningProfile,
        title: String,
        description: String,
        topic: String,
        type: RecommendationType,
        priority: RecommendationPriority,
        targetSkillLevel: SkillLevel = profile.learningLevel,
    ): Recommendation = Recommendation(
        recommendationId = stableId(profile.userId, type.name, topic, title),
        userId = profile.userId,
        title = title,
        description = description,
        topic = topic,
        type = type,
        priority = priority,
        targetSkillLevel = targetSkillLevel,
    )

    private fun nextTopicFor(category: String): String =
        when {
            category.contains("plant", ignoreCase = true) -> "plant anatomy"
            category.contains("material", ignoreCase = true) || category.contains("plastic", ignoreCase = true) -> "material properties"
            category.contains("learning", ignoreCase = true) -> "knowledge organization"
            category.contains("household", ignoreCase = true) -> "daily object science"
            else -> "$category relationships"
        }

    private fun stableId(vararg parts: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
            .digest(parts.joinToString("|").lowercase().toByteArray())
        return digest.joinToString(separator = "") { "%02x".format(it) }.take(24)
    }

    private companion object {
        const val MAX_RECOMMENDATIONS = 5
    }
}
