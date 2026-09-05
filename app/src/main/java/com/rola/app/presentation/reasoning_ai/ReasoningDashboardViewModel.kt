package com.rola.app.presentation.reasoning_ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.reasoning_ai.ReasoningAIRepository
import com.rola.app.reasoning_ai.ReasoningDashboardState
import com.rola.app.reasoning_ai.reasoning_core.AIReasoningEngine
import com.rola.app.reasoning_ai.reasoning_core.ReasoningDomain
import com.rola.app.reasoning_ai.reasoning_core.ReasoningRequest
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
class ReasoningDashboardViewModel @Inject constructor(
    private val engine: AIReasoningEngine,
    private val repository: ReasoningAIRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val local = MutableStateFlow(ReasoningUiState())
    val uiState = combine(local, repository.observeDashboard()) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ReasoningUiState())

    fun solveProblem() = viewModelScope.launch {
        local.update { it.copy(loading = true, message = "Solving with reasoning AI...", errorMessage = null) }
        runCatching {
            val result = withContext(defaultDispatcher) {
                engine.solve(ReasoningRequest("local-learner", "Why does current change when resistance changes?", ReasoningDomain.Science, listOf("Ohm law", "electric circuit", "cause effect"), "current gets tired"))
            }
            withContext(ioDispatcher) { repository.save(result) }
            result.solution.confidence.name
        }.onSuccess { local.update { state -> state.copy(loading = false, message = "Reasoning complete: $it.") } }
            .onFailure { error -> local.update { it.copy(loading = false, errorMessage = error.message ?: "Reasoning failed.") } }
    }
}

data class ReasoningUiState(val loading: Boolean = false, val message: String? = null, val errorMessage: String? = null, val dashboard: ReasoningDashboardState = ReasoningDashboardState())
