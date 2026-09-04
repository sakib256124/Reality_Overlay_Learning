package com.rola.app.neural_ai.neural_processing

import com.rola.app.neural_ai.brain_interface.BrainInterfaceManager
import com.rola.app.neural_ai.cognitive_signal.NeuralSignalSample
import com.rola.app.neural_ai.interaction.NeuralEducationResponse
import com.rola.app.neural_ai.interaction.NeuralInteractionController
import com.rola.app.neural_ai.learning_state.CognitiveStateAnalyzer
import com.rola.app.neural_ai.learning_state.LearningStateManager
import com.rola.app.neural_ai.learning_state.NeuralLearningProfile
import com.rola.app.neural_ai.personalization.NeuralLearningOptimizer
import com.rola.app.neural_ai.personalization.NeuralPersonalizationEngine
import com.rola.app.neural_ai.prediction.NeuralPredictionEngine
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class NeuralAIEngine @Inject constructor(
    private val brainInterfaceManager: BrainInterfaceManager,
    private val signalProcessor: NeuralSignalProcessor,
    private val cognitiveStateAnalyzer: CognitiveStateAnalyzer,
    private val learningStateManager: LearningStateManager,
    private val predictionEngine: NeuralPredictionEngine,
    private val personalizationEngine: NeuralPersonalizationEngine,
    private val learningOptimizer: NeuralLearningOptimizer,
    private val interactionController: NeuralInteractionController,
) {
    suspend fun startNeuralLearningSession(
        userId: String,
        topic: String,
        profile: NeuralLearningProfile = learningStateManager.createInitialProfile(userId),
    ): Result<Flow<NeuralEducationResponse>> =
        brainInterfaceManager.connectToBestAvailableDevice().map { session ->
            brainInterfaceManager.observeSignals(session, userId)
                .map { processSignal(it, topic, profile) }
        }

    fun processSignal(
        sample: NeuralSignalSample,
        topic: String,
        profile: NeuralLearningProfile,
    ): NeuralEducationResponse = processSignalWithProfile(sample, topic, profile).response

    fun processSignalWithProfile(
        sample: NeuralSignalSample,
        topic: String,
        profile: NeuralLearningProfile,
    ): NeuralProcessingResult {
        require(profile.consentGranted) { "Neural learning analysis requires explicit learner consent." }
        val processedSignal = signalProcessor.process(sample)
        val cognitiveState = cognitiveStateAnalyzer.analyze(processedSignal, topic)
        val learningState = learningStateManager.updateLearningState(cognitiveState, profile)
        val updatedProfile = learningStateManager.updateProfile(profile, cognitiveState, learningState)
        val prediction = predictionEngine.predict(cognitiveState, updatedProfile)
        val plan = personalizationEngine.personalize(updatedProfile, learningState, prediction)
        val optimization = learningOptimizer.optimize(plan)
        return NeuralProcessingResult(
            response = interactionController.createResponse(cognitiveState, optimization, prediction),
            updatedProfile = updatedProfile,
        )
    }
}

data class NeuralProcessingResult(
    val response: NeuralEducationResponse,
    val updatedProfile: NeuralLearningProfile,
)
