package com.rola.app.neural_learning_ai.intelligence

import com.rola.app.neural_learning_ai.neural_core.CognitiveLearningProfile
import com.rola.app.neural_learning_ai.neural_core.LearningAdaptationPlan
import com.rola.app.neural_learning_ai.neural_core.NeuralAssistantGuidance
import javax.inject.Inject

class NeuralLearningAssistant @Inject constructor() {
    fun guide(profile: CognitiveLearningProfile, adaptation: LearningAdaptationPlan): NeuralAssistantGuidance =
        NeuralAssistantGuidance(
            assistantId = "assistant-${profile.profileId}",
            thinkingPattern = profile.problemSolvingStyle,
            personalizedGuidance = listOf("start with a visual map", "explain back the core relationship", "practice one transfer problem"),
            learningStrategies = listOf(adaptation.explanationStyle, adaptation.practiceFrequency),
            understandingImprovements = listOf("reduce cognitive load", "strengthen retrieval cues"),
        )
}
