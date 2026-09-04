package com.rola.app.neural_ai.learning_state

enum class FocusLevel {
    Low,
    Moderate,
    High,
}

enum class UnderstandingLevel {
    Low,
    Developing,
    Strong,
}

enum class CognitiveLoadLevel {
    Low,
    Balanced,
    High,
    Overloaded,
}

enum class TeachingMethod {
    Visual,
    Conversational,
    Interactive,
    PracticeBased,
    SimplifiedExplanation,
}

data class CognitiveState(
    val stateId: String,
    val userId: String,
    val topic: String,
    val attentionPercent: Int,
    val engagementPercent: Int,
    val focusLevel: FocusLevel,
    val cognitiveLoadLevel: CognitiveLoadLevel,
    val understandingLevel: UnderstandingLevel,
    val mentalFatiguePercent: Int,
    val explanation: String,
    val timestamp: Long = System.currentTimeMillis(),
)

data class LearningState(
    val stateId: String,
    val userId: String,
    val activeTopic: String,
    val recommendedDifficulty: Int,
    val recommendedMethod: TeachingMethod,
    val supportRequired: Boolean,
    val adaptationReason: String,
    val updatedAt: Long = System.currentTimeMillis(),
)

data class NeuralLearningProfile(
    val profileId: String,
    val userId: String,
    val cognitivePatterns: List<String>,
    val attentionBehavior: String,
    val memoryResponse: String,
    val learningSpeed: String,
    val preferredTeachingMethod: TeachingMethod,
    val knowledgeDevelopment: List<String>,
    val consentGranted: Boolean,
    val updatedAt: Long = System.currentTimeMillis(),
)
