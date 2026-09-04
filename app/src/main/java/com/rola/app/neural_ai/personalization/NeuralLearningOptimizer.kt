package com.rola.app.neural_ai.personalization

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NeuralLearningOptimizer @Inject constructor() {
    fun optimize(plan: NeuralPersonalizationPlan): NeuralLearningOptimization =
        NeuralLearningOptimization(
            optimizationId = "neural-optimization-${UUID.randomUUID()}",
            userId = plan.userId,
            lessonDifficulty = plan.difficultyPercent.coerceIn(10, 95),
            explanationSpeed = plan.explanationSpeedPercent.coerceIn(25, 95),
            contentFormat = plan.contentFormat,
            practiceFrequency = plan.practiceFrequencyPercent.coerceIn(10, 90),
            learningEnvironment = plan.learningEnvironment,
            explainableReason = plan.rationale,
        )
}
