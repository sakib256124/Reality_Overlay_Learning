package com.rola.app.neural_ai.interaction

import com.rola.app.neural_ai.learning_state.CognitiveLoadLevel
import com.rola.app.neural_ai.learning_state.CognitiveState
import com.rola.app.neural_ai.learning_state.UnderstandingLevel
import com.rola.app.neural_ai.personalization.NeuralLearningOptimization
import com.rola.app.neural_ai.prediction.NeuralLearningPrediction
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NeuralInteractionController @Inject constructor() {
    fun createResponse(
        cognitiveState: CognitiveState,
        optimization: NeuralLearningOptimization,
        prediction: NeuralLearningPrediction,
    ): NeuralEducationResponse {
        val actions = buildList {
            when (cognitiveState.understandingLevel) {
                UnderstandingLevel.Low -> {
                    add(NeuralInteractionAction.SimplifyExplanation)
                    add(NeuralInteractionAction.AddVisualExample)
                    add(NeuralInteractionAction.StartPractice)
                }
                UnderstandingLevel.Developing -> {
                    add(NeuralInteractionAction.AddVisualExample)
                    add(NeuralInteractionAction.SlowDownLesson)
                }
                UnderstandingLevel.Strong -> add(NeuralInteractionAction.ContinueLearning)
            }
            if (cognitiveState.cognitiveLoadLevel == CognitiveLoadLevel.Overloaded || cognitiveState.mentalFatiguePercent > 70) {
                add(NeuralInteractionAction.OfferBreak)
            }
        }.distinct()

        return NeuralEducationResponse(
            responseId = "neural-response-${UUID.randomUUID()}",
            userId = cognitiveState.userId,
            topic = cognitiveState.topic,
            cognitiveState = cognitiveState,
            optimization = optimization,
            prediction = prediction,
            actions = actions,
            teacherPrompt = teacherPrompt(cognitiveState, optimization),
            explainableReason = "${cognitiveState.explanation} ${prediction.explanation}",
        )
    }

    private fun teacherPrompt(
        state: CognitiveState,
        optimization: NeuralLearningOptimization,
    ): String = when (state.understandingLevel) {
        UnderstandingLevel.Low ->
            "Explain ${state.topic} using simple language, one AR visual, and a short practice question."
        UnderstandingLevel.Developing ->
            "Teach ${state.topic} with a guided example at ${optimization.explanationSpeed}% pace."
        UnderstandingLevel.Strong ->
            "Extend ${state.topic} with an exploratory AR challenge."
    }
}
