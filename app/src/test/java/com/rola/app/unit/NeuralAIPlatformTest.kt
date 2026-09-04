package com.rola.app.unit

import com.rola.app.neural_ai.cognitive_signal.NeuralSignalSample
import com.rola.app.neural_ai.cognitive_signal.SignalQuality
import com.rola.app.neural_ai.interaction.NeuralInteractionAction
import com.rola.app.neural_ai.interaction.NeuralInteractionController
import com.rola.app.neural_ai.learning_state.CognitiveStateAnalyzer
import com.rola.app.neural_ai.learning_state.LearningStateManager
import com.rola.app.neural_ai.learning_state.TeachingMethod
import com.rola.app.neural_ai.learning_state.UnderstandingLevel
import com.rola.app.neural_ai.neural_processing.NeuralAIEngine
import com.rola.app.neural_ai.neural_processing.NeuralSignalProcessor
import com.rola.app.neural_ai.performance.NeuralPerformanceOptimizer
import com.rola.app.neural_ai.personalization.NeuralLearningOptimizer
import com.rola.app.neural_ai.personalization.NeuralPersonalizationEngine
import com.rola.app.neural_ai.prediction.NeuralPredictionEngine
import com.rola.app.neural_ai.privacy.NeuralConsentState
import com.rola.app.neural_ai.privacy.NeuralDataPrivacyManager
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test

class NeuralAIPlatformTest {
    private val learningStateManager = LearningStateManager()
    private val engine = NeuralAIEngine(
        brainInterfaceManager = FakeBrainInterfaceManagerFactory.create(),
        signalProcessor = NeuralSignalProcessor(),
        cognitiveStateAnalyzer = CognitiveStateAnalyzer(),
        learningStateManager = learningStateManager,
        predictionEngine = NeuralPredictionEngine(),
        personalizationEngine = NeuralPersonalizationEngine(),
        learningOptimizer = NeuralLearningOptimizer(),
        interactionController = NeuralInteractionController(),
    )

    @Test
    fun processSignal_detectsLowUnderstandingAndSimplifiesTeaching() {
        val profile = learningStateManager.createInitialProfile("learner-1")
        val result = engine.processSignalWithProfile(
            sample = NeuralSignalSample(
                signalId = "signal-1",
                userId = "learner-1",
                sessionId = "session-1",
                attentionScore = 0.32f,
                engagementScore = 0.38f,
                mentalWorkloadScore = 0.91f,
                fatigueScore = 0.74f,
                signalQuality = SignalQuality.Good,
            ),
            topic = "Chemistry",
            profile = profile,
        )

        assertEquals(UnderstandingLevel.Low, result.response.cognitiveState.understandingLevel)
        assertTrue(NeuralInteractionAction.SimplifyExplanation in result.response.actions)
        assertEquals(TeachingMethod.SimplifiedExplanation, result.updatedProfile.preferredTeachingMethod)
    }

    @Test
    fun privacyManager_blocksNeuralAnalysisWithoutConsent() {
        val privacyManager = NeuralDataPrivacyManager()

        assertThrows(IllegalArgumentException::class.java) {
            privacyManager.requireConsent(
                NeuralConsentState(
                    userId = "learner-1",
                    neuralAnalysisEnabled = false,
                    localProcessingOnly = true,
                    allowCloudSync = false,
                ),
            )
        }
    }

    @Test
    fun performanceOptimizer_skipsFramesForRealtimeEfficiency() = runTest {
        val optimizer = NeuralPerformanceOptimizer()
        val values = optimizer.throttleSignals(flowOf(1, 2, 3, 4, 5, 6), everyNthSignal = 2).toList()

        assertEquals(listOf(2, 4, 6), values)
    }
}
