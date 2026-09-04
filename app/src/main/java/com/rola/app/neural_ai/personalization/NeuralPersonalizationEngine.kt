package com.rola.app.neural_ai.personalization

import com.rola.app.neural_ai.learning_state.LearningState
import com.rola.app.neural_ai.learning_state.NeuralLearningProfile
import com.rola.app.neural_ai.learning_state.TeachingMethod
import com.rola.app.neural_ai.prediction.NeuralLearningPrediction
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NeuralPersonalizationEngine @Inject constructor() {
    fun personalize(
        profile: NeuralLearningProfile,
        learningState: LearningState,
        prediction: NeuralLearningPrediction,
    ): NeuralPersonalizationPlan {
        val format = when (learningState.recommendedMethod) {
            TeachingMethod.Visual -> ContentFormat.ARVisual
            TeachingMethod.Conversational -> ContentFormat.AudioExplanation
            TeachingMethod.Interactive -> ContentFormat.InteractiveSimulation
            TeachingMethod.PracticeBased -> ContentFormat.GuidedPractice
            TeachingMethod.SimplifiedExplanation -> ContentFormat.TextSummary
        }
        val speed = if (prediction.learningSuccessPercent < 55) 45 else 70
        val practice = if (learningState.supportRequired) 80 else 45

        return NeuralPersonalizationPlan(
            planId = "neural-plan-${UUID.randomUUID()}",
            userId = profile.userId,
            teachingMethod = learningState.recommendedMethod,
            contentFormat = format,
            difficultyPercent = learningState.recommendedDifficulty,
            explanationSpeedPercent = speed,
            practiceFrequencyPercent = practice,
            learningEnvironment = if (learningState.supportRequired) "Guided AR micro-lesson" else "Exploratory AR challenge",
            rationale = learningState.adaptationReason,
        )
    }
}
