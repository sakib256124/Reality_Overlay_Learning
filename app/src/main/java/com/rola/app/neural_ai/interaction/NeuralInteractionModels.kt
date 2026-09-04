package com.rola.app.neural_ai.interaction

import com.rola.app.neural_ai.learning_state.CognitiveState
import com.rola.app.neural_ai.personalization.NeuralLearningOptimization
import com.rola.app.neural_ai.prediction.NeuralLearningPrediction

enum class NeuralInteractionAction {
    SimplifyExplanation,
    SlowDownLesson,
    AddVisualExample,
    StartPractice,
    OfferBreak,
    ContinueLearning,
}

data class NeuralEducationResponse(
    val responseId: String,
    val userId: String,
    val topic: String,
    val cognitiveState: CognitiveState,
    val optimization: NeuralLearningOptimization,
    val prediction: NeuralLearningPrediction,
    val actions: List<NeuralInteractionAction>,
    val teacherPrompt: String,
    val explainableReason: String,
    val timestamp: Long = System.currentTimeMillis(),
)
