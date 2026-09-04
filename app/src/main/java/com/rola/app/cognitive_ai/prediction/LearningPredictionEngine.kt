package com.rola.app.cognitive_ai.prediction

import com.rola.app.domain.model.LearnerCognitiveProfile
import com.rola.app.domain.model.LearningPrediction
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LearningPredictionEngine @Inject constructor() {
    fun predict(profile: LearnerCognitiveProfile): LearningPrediction {
        val weak = profile.knowledgeWeaknesses.take(5)
        val futurePerformance = (profile.intelligenceScore + profile.skillDevelopment.count { it.growthTrend.name.contains("Improving") } * 4)
            .coerceIn(0, 100)
        return LearningPrediction(
            predictionId = "learning-prediction-${UUID.randomUUID()}",
            userId = profile.userId,
            futurePerformance = futurePerformance,
            predictedDifficulties = weak.ifEmpty { listOf("Future difficulty depends on next topic complexity.") },
            predictedKnowledgeGaps = weak.map { "Prerequisite gap near $it" },
            skillImprovement = when {
                futurePerformance >= 85 -> "Likely rapid improvement with advanced challenges."
                futurePerformance >= 65 -> "Likely steady improvement with adaptive practice."
                else -> "Improvement requires reteaching, repetition, and confidence rebuilding."
            },
            requiredLearningPath = weak.map { "Reteach $it" }
                .ifEmpty { profile.knowledgeStrengths.take(3).map { "Extend $it" } }
                .ifEmpty { listOf("Diagnostic lesson", "Practice activity", "Mastery check") },
        )
    }
}
