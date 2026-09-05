package com.rola.app.presentation.collective_ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.collective_ai.CollectiveAIDashboardState
import com.rola.app.collective_ai.CollectiveAIRepository
import com.rola.app.collective_ai.intelligence_network.CollectiveAIEngine
import com.rola.app.collective_ai.intelligence_network.CollectiveAIRequest
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
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
class CollectiveAIDashboardViewModel @Inject constructor(
    private val collectiveAIEngine: CollectiveAIEngine,
    private val repository: CollectiveAIRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val localState = MutableStateFlow(CollectiveAIDashboardUiState())

    val uiState = combine(
        localState,
        repository.observeDashboard(),
    ) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CollectiveAIDashboardUiState(),
        )

    fun runCollectiveCycle() {
        viewModelScope.launch {
            localState.update { it.copy(loading = true, message = "Coordinating multi-agent education society...", errorMessage = null) }
            runCatching {
                val request = CollectiveAIRequest(
                    userId = "local-learner",
                    problem = "Student understands circuit parts but cannot explain current flow.",
                    topic = "Electric Circuits",
                    learnerSignals = listOf("low confidence", "concept gap: voltage vs current", "needs visual example"),
                    humanFeedback = "Teacher asks for a simpler explanation and quick practice.",
                )
                val result = withContext(defaultDispatcher) { collectiveAIEngine.solveEducationalProblem(request) }
                withContext(ioDispatcher) { repository.saveResult(result) }
                result.consensus.outcome.name
            }.onSuccess { outcome ->
                localState.update { it.copy(loading = false, message = "Collective decision ready: $outcome.") }
            }.onFailure { error ->
                localState.update {
                    it.copy(
                        loading = false,
                        message = null,
                        errorMessage = error.message ?: "Collective AI cycle failed.",
                    )
                }
            }
        }
    }
}

data class CollectiveAIDashboardUiState(
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val dashboard: CollectiveAIDashboardState = CollectiveAIDashboardState(),
)
