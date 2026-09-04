package com.rola.app.neural_ai.learning_state

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LearningStateManager @Inject constructor() {
    fun createInitialProfile(userId: String): NeuralLearningProfile = NeuralLearningProfile(
        profileId = "neural-profile-$userId",
        userId = userId,
        cognitivePatterns = listOf("Baseline profile awaiting neural learning signals"),
        attentionBehavior = "Unknown",
        memoryResponse = "Not enough sessions yet",
        learningSpeed = "Balanced",
        preferredTeachingMethod = TeachingMethod.Visual,
        knowledgeDevelopment = emptyList(),
        consentGranted = true,
    )

    fun updateLearningState(
        cognitiveState: CognitiveState,
        profile: NeuralLearningProfile,
    ): LearningState {
        val method = when (cognitiveState.understandingLevel) {
            UnderstandingLevel.Low -> TeachingMethod.SimplifiedExplanation
            UnderstandingLevel.Developing -> TeachingMethod.Interactive
            UnderstandingLevel.Strong -> profile.preferredTeachingMethod
        }
        val difficulty = when (cognitiveState.cognitiveLoadLevel) {
            CognitiveLoadLevel.Low -> 70
            CognitiveLoadLevel.Balanced -> 60
            CognitiveLoadLevel.High -> 45
            CognitiveLoadLevel.Overloaded -> 30
        }

        return LearningState(
            stateId = "learning-state-${UUID.randomUUID()}",
            userId = cognitiveState.userId,
            activeTopic = cognitiveState.topic,
            recommendedDifficulty = difficulty,
            recommendedMethod = method,
            supportRequired = cognitiveState.understandingLevel != UnderstandingLevel.Strong,
            adaptationReason = cognitiveState.explanation,
            updatedAt = cognitiveState.timestamp,
        )
    }

    fun updateProfile(
        profile: NeuralLearningProfile,
        cognitiveState: CognitiveState,
        learningState: LearningState,
    ): NeuralLearningProfile {
        val pattern = "${cognitiveState.topic}: ${cognitiveState.focusLevel} focus, ${cognitiveState.cognitiveLoadLevel} load"
        val development = "${cognitiveState.topic}: ${cognitiveState.understandingLevel} understanding"
        return profile.copy(
            cognitivePatterns = (listOf(pattern) + profile.cognitivePatterns).distinct().take(12),
            attentionBehavior = "${cognitiveState.attentionPercent}% attention",
            memoryResponse = if (learningState.supportRequired) "Needs reinforcement" else "Stable recall expected",
            learningSpeed = if (cognitiveState.cognitiveLoadLevel == CognitiveLoadLevel.Overloaded) "Slow" else "Balanced",
            preferredTeachingMethod = learningState.recommendedMethod,
            knowledgeDevelopment = (listOf(development) + profile.knowledgeDevelopment).distinct().take(12),
            updatedAt = cognitiveState.timestamp,
        )
    }
}
