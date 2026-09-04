package com.rola.app.presentation.quantum_ai

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.quantum_ai.QuantumAIDashboardState
import com.rola.app.quantum_ai.QuantumAIRepository
import com.rola.app.quantum_ai.intelligence.QuantumLearningInput
import com.rola.app.quantum_ai.quantum_engine.QuantumAIEngine
import dagger.hilt.android.lifecycle.HiltViewModel
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
class QuantumAIDashboardViewModel @Inject constructor(
    private val quantumAIEngine: QuantumAIEngine,
    private val repository: QuantumAIRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val learnerId: String = savedStateHandle["learnerId"] ?: "local-learner"
    private val institutionId: String = savedStateHandle["institutionId"] ?: "local-institution"
    private val localState = MutableStateFlow(QuantumAIDashboardUiState())

    val uiState = combine(
        localState,
        repository.observeDashboard(learnerId, institutionId),
    ) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = QuantumAIDashboardUiState(),
        )

    fun runQuantumOptimization() {
        viewModelScope.launch {
            localState.update { it.copy(loading = true, message = "Running quantum-inspired learning optimization...", errorMessage = null) }
            runCatching {
                val input = QuantumLearningInput(
                    learnerId = learnerId,
                    institutionId = institutionId,
                    topic = "Electric Circuits",
                    behaviorSignals = listOf("visual: strong AR interaction", "research: circuit misconception evidence"),
                    cognitiveProfile = "visual learner with moderate confidence",
                    learningHistoryScores = listOf(62, 68, 55, 74),
                    knowledgeGaps = listOf("Voltage", "Current flow"),
                    learningGoals = listOf("Explain circuit behavior", "Build a simple AR circuit"),
                )
                val result = withContext(defaultDispatcher) { quantumAIEngine.runQuantumEducationCycle(input) }
                withContext(ioDispatcher) { repository.saveResult(result) }
                result.decision.educationalAction
            }.onSuccess { action ->
                localState.update { it.copy(loading = false, message = action) }
            }.onFailure { error ->
                localState.update {
                    it.copy(
                        loading = false,
                        message = null,
                        errorMessage = error.message ?: "Quantum AI optimization failed.",
                    )
                }
            }
        }
    }
}

data class QuantumAIDashboardUiState(
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val dashboard: QuantumAIDashboardState = QuantumAIDashboardState(),
)
