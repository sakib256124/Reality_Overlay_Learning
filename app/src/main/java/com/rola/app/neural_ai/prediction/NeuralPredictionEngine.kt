package com.rola.app.neural_ai.prediction

import com.rola.app.neural_ai.learning_state.CognitiveLoadLevel
import com.rola.app.neural_ai.learning_state.CognitiveState
import com.rola.app.neural_ai.learning_state.NeuralLearningProfile
import com.rola.app.neural_ai.learning_state.UnderstandingLevel
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NeuralPredictionEngine @Inject constructor() {
    fun predict(
        cognitiveState: CognitiveState,
        profile: NeuralLearningProfile,
    ): NeuralLearningPrediction {
        val success = when {
            cognitiveState.understandingLevel == UnderstandingLevel.Strong -> 86
            cognitiveState.cognitiveLoadLevel == CognitiveLoadLevel.Overloaded -> 42
            cognitiveState.focusLevel.name == "Low" -> 48
            else -> 68
        }
        val support = buildList {
            if (cognitiveState.understandingLevel == UnderstandingLevel.Low) add("Simpler explanation")
            if (cognitiveState.mentalFatiguePercent > 60) add("Short learning break")
            if (cognitiveState.cognitiveLoadLevel == CognitiveLoadLevel.High) add("Step-by-step example")
            if (isEmpty()) add("Continue adaptive challenge")
        }

        return NeuralLearningPrediction(
            predictionId = "neural-prediction-${UUID.randomUUID()}",
            userId = cognitiveState.userId,
            topic = cognitiveState.topic,
            learningSuccessPercent = success,
            requiredSupport = support,
            skillDevelopment = profile.knowledgeDevelopment.ifEmpty { listOf("Build baseline knowledge map") },
            knowledgeRetentionPercent = (success - cognitiveState.mentalFatiguePercent / 4).coerceIn(20, 96),
            longTermRoadmap = listOf(
                "Review ${cognitiveState.topic} with adaptive examples",
                "Practice with AR-based questions",
                "Re-check retention in the next session",
            ),
            explanation = "Prediction combines focus, workload, fatigue, and recent neural learning profile signals.",
        )
    }
}
