package com.rola.app.cognitive_ai.personalization

import com.rola.app.domain.model.CognitiveRecommendation
import com.rola.app.domain.model.LearnerCognitiveProfile
import com.rola.app.domain.model.PersonalLearningPlan
import com.rola.app.domain.model.PreferredLearningMethod
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonalizationEngine @Inject constructor() {
    fun learningPlan(profile: LearnerCognitiveProfile): PersonalLearningPlan {
        val focus = profile.knowledgeWeaknesses.firstOrNull()
            ?: profile.learningGoals.firstOrNull()?.targetConcept
            ?: profile.knowledgeStrengths.firstOrNull()
            ?: "learning foundations"
        return PersonalLearningPlan(
            planId = "personal-learning-plan-${UUID.randomUUID()}",
            userId = profile.userId,
            dailyGuidance = listOf(
                "Start with a five-minute review of $focus.",
                "Use ${profile.preferredLearningMethod.name} for the main activity.",
                "Finish with one reflection about what changed.",
            ),
            studyPlan = listOf(
                "Warm-up: recall prior knowledge.",
                "Core: personalized explanation.",
                "Practice: adaptive activity.",
                "Check: short assessment.",
            ),
            motivationalMessage = motivationFor(profile),
            weaknessExplanation = profile.knowledgeWeaknesses.map { "$it needs simpler examples and spaced practice." },
        )
    }

    fun recommendation(profile: LearnerCognitiveProfile): CognitiveRecommendation {
        val next = profile.knowledgeWeaknesses.firstOrNull()
            ?: profile.learningGoals.firstOrNull()?.targetConcept
            ?: profile.knowledgeStrengths.firstOrNull()
            ?: "Next discovery topic"
        return CognitiveRecommendation(
            recommendationId = "cognitive-recommendation-${UUID.randomUUID()}",
            userId = profile.userId,
            nextLesson = next,
            practiceActivities = listOf("Guided example", "Adaptive practice", "Reflection prompt"),
            arExperience = when (profile.preferredLearningMethod) {
                PreferredLearningMethod.ARModel, PreferredLearningMethod.ThreeDExplanation, PreferredLearningMethod.Simulation -> "Use AR model and 3D explanation for $next."
                else -> "Use a short AR evidence check for $next."
            },
            researchTopics = listOf("$next applications", "$next misconceptions"),
            quizDifficulty = profile.learningLevel,
            rationale = "Selected from cognitive profile weaknesses, goals, preferred method, and skill map.",
        )
    }

    private fun motivationFor(profile: LearnerCognitiveProfile): String = when {
        profile.intelligenceScore >= 80 -> "Your learning pattern is strong; use a challenge to keep growing."
        profile.knowledgeWeaknesses.isNotEmpty() -> "Your next improvement is clear: focus on ${profile.knowledgeWeaknesses.first()} today."
        else -> "Keep the streak steady with one focused learning session."
    }
}
