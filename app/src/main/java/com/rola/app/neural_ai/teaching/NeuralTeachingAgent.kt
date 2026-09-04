package com.rola.app.neural_ai.teaching

import com.rola.app.neural_ai.interaction.NeuralEducationResponse
import com.rola.app.neural_ai.interaction.NeuralInteractionAction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NeuralTeachingAgent @Inject constructor() {
    fun instructionFor(response: NeuralEducationResponse): String {
        val strategy = when {
            NeuralInteractionAction.OfferBreak in response.actions -> "pause-support"
            NeuralInteractionAction.SimplifyExplanation in response.actions -> "simplified-teaching"
            NeuralInteractionAction.ContinueLearning in response.actions -> "challenge-extension"
            else -> "guided-teaching"
        }

        return "$strategy: ${response.teacherPrompt}"
    }
}
