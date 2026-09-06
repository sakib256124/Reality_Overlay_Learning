package com.rola.app.neural_learning_ai.adaptation

import com.rola.app.neural_learning_ai.neural_core.CognitiveLearningProfile
import com.rola.app.neural_learning_ai.neural_core.LearningAdaptationPlan
import com.rola.app.neural_learning_ai.neural_core.NeuralLearningRequest
import javax.inject.Inject

class LearningAdaptationEngine @Inject constructor() {
    fun adapt(request: NeuralLearningRequest, profile: CognitiveLearningProfile): LearningAdaptationPlan =
        LearningAdaptationPlan(
            adaptationId = "adapt-${request.learnerId}",
            contentDifficulty = if (request.masteryLevel.contains("advanced", true)) "advanced" else "guided intermediate",
            explanationStyle = if (profile.problemSolvingStyle.contains("visual")) "visual analogy and diagram" else "step-by-step text",
            learningSpeed = if (profile.understandingSpeed >= 85) "accelerated" else "steady",
            practiceFrequency = if (request.emotionalState.contains("confident", true)) "daily challenge" else "short reinforced practice",
            transparentReason = "Adapted from cognitive profile, emotional state, mastery level, and learning history.",
        )
}
