package com.rola.app.neural_ai.personalization

import com.rola.app.neural_ai.learning_state.TeachingMethod

enum class ContentFormat {
    ARVisual,
    TextSummary,
    AudioExplanation,
    InteractiveSimulation,
    GuidedPractice,
}

data class NeuralPersonalizationPlan(
    val planId: String,
    val userId: String,
    val teachingMethod: TeachingMethod,
    val contentFormat: ContentFormat,
    val difficultyPercent: Int,
    val explanationSpeedPercent: Int,
    val practiceFrequencyPercent: Int,
    val learningEnvironment: String,
    val rationale: String,
)

data class NeuralLearningOptimization(
    val optimizationId: String,
    val userId: String,
    val lessonDifficulty: Int,
    val explanationSpeed: Int,
    val contentFormat: ContentFormat,
    val practiceFrequency: Int,
    val learningEnvironment: String,
    val explainableReason: String,
)
