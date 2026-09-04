package com.rola.app.presentation.neural_ai

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.neural_ai.NeuralAIDashboardState
import com.rola.app.neural_ai.NeuralAIRepository
import com.rola.app.neural_ai.cognitive_signal.NeuralSignalSample
import com.rola.app.neural_ai.cognitive_signal.SignalQuality
import com.rola.app.neural_ai.learning_state.LearningStateManager
import com.rola.app.neural_ai.neural_processing.NeuralAIEngine
import com.rola.app.neural_ai.privacy.NeuralConsentState
import com.rola.app.neural_ai.privacy.NeuralDataPrivacyManager
import com.rola.app.neural_ai.teaching.NeuralTeachingAgent
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class NeuralLearningDashboardViewModel @Inject constructor(
    private val neuralAIEngine: NeuralAIEngine,
    private val learningStateManager: LearningStateManager,
    private val neuralAIRepository: NeuralAIRepository,
    private val privacyManager: NeuralDataPrivacyManager,
    private val neuralTeachingAgent: NeuralTeachingAgent,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val userId: String = savedStateHandle["userId"] ?: "local-learner"
    private val topic: String = savedStateHandle["topic"] ?: "Neural Learning Basics"
    private val localState = MutableStateFlow(NeuralLearningDashboardUiState(topic = topic))

    val uiState = combine(
        localState,
        neuralAIRepository.observeDashboard(userId),
    ) { state, dashboard ->
        state.copy(dashboard = dashboard)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = NeuralLearningDashboardUiState(topic = topic),
    )

    fun runSampleNeuralAnalysis() {
        viewModelScope.launch {
            localState.update { it.copy(loading = true, errorMessage = null, message = "Processing neural learning signal...") }
            runCatching {
                val consent = NeuralConsentState(
                    userId = userId,
                    neuralAnalysisEnabled = true,
                    localProcessingOnly = true,
                    allowCloudSync = false,
                )
                privacyManager.requireConsent(consent)
                val profile = learningStateManager.createInitialProfile(userId)
                val sample = sampleSignal()
                val result = withContext(defaultDispatcher) {
                    neuralAIEngine.processSignalWithProfile(sample, topic, profile)
                }
                withContext(ioDispatcher) {
                    neuralAIRepository.saveSignal(sample)
                    neuralAIRepository.saveResponse(result.response, result.updatedProfile)
                }
                val teachingInstruction = neuralTeachingAgent.instructionFor(result.response)
                privacyManager.explainDecision(
                    userId = userId,
                    reason = result.response.explainableReason,
                    sourceSignals = listOf("attention", "engagement", "workload", "fatigue"),
                    localProcessingOnly = consent.localProcessingOnly,
                )
                teachingInstruction
            }.onSuccess { instruction ->
                localState.update {
                    it.copy(
                        loading = false,
                        message = "Neural analysis completed.",
                        latestTeachingInstruction = instruction,
                    )
                }
            }.onFailure { error ->
                localState.update {
                    it.copy(
                        loading = false,
                        message = null,
                        errorMessage = error.message ?: "Neural AI analysis failed.",
                    )
                }
            }
        }
    }

    private fun sampleSignal(): NeuralSignalSample =
        NeuralSignalSample(
            signalId = "dashboard-signal-${UUID.randomUUID()}",
            userId = userId,
            sessionId = "dashboard-simulated-session",
            attentionScore = 0.46f,
            engagementScore = 0.52f,
            mentalWorkloadScore = 0.82f,
            fatigueScore = 0.58f,
            signalQuality = SignalQuality.Good,
        )
}

data class NeuralLearningDashboardUiState(
    val topic: String,
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val latestTeachingInstruction: String = "",
    val dashboard: NeuralAIDashboardState = NeuralAIDashboardState(),
)
