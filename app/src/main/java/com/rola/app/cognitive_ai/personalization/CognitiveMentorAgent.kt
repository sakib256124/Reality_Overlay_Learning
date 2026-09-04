package com.rola.app.cognitive_ai.personalization

import com.rola.app.domain.model.LearnerCognitiveProfile
import com.rola.app.domain.model.LearningPrediction
import com.rola.app.domain.model.PersonalLearningPlan
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CognitiveMentorAgent @Inject constructor(
    private val personalizationEngine: PersonalizationEngine,
) {
    fun mentor(profile: LearnerCognitiveProfile, prediction: LearningPrediction): PersonalLearningPlan {
        val base = personalizationEngine.learningPlan(profile)
        return base.copy(
            dailyGuidance = base.dailyGuidance + "Next predicted need: ${prediction.requiredLearningPath.firstOrNull().orEmpty()}",
            motivationalMessage = "${base.motivationalMessage} Predicted performance: ${prediction.futurePerformance}%.",
            weaknessExplanation = base.weaknessExplanation.ifEmpty {
                prediction.predictedDifficulties.map { "Watch for $it during the next session." }
            },
        )
    }
}
