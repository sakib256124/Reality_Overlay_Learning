package com.rola.app.presentation.self_evolving_ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.self_evolving_ai.EvolutionDashboardState
import com.rola.app.self_evolving_ai.SelfEvolvingAIRepository
import com.rola.app.self_evolving_ai.evolution_core.SelfEvolutionEngine
import com.rola.app.self_evolving_ai.evolution_core.SelfEvolutionRequest
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
class EvolutionDashboardViewModel @Inject constructor(
    private val engine: SelfEvolutionEngine,
    private val repository: SelfEvolvingAIRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val local = MutableStateFlow(EvolutionUiState())
    val uiState = combine(local, repository.observeDashboard()) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EvolutionUiState())

    fun evolveSystem() = viewModelScope.launch {
        local.update { it.copy(loading = true, message = "Running self-evolution analysis...", errorMessage = null) }
        runCatching {
            val result = withContext(defaultDispatcher) {
                engine.evolve(
                    SelfEvolutionRequest(
                        systemId = "rola-self-evolution",
                        aiResponseQuality = 78,
                        teachingEffectiveness = 76,
                        recommendationAccuracy = 74,
                        learningOutcomes = 83,
                        userSatisfaction = 88,
                        systemPerformance = 91,
                        feedback = listOf("need clearer examples", "recommendations should match mastery gaps"),
                    ),
                )
            }
            withContext(ioDispatcher) { repository.save(result) }
            result.status.name
        }.onSuccess { status -> local.update { it.copy(loading = false, message = "Evolution proposal ready: $status.") } }
            .onFailure { error -> local.update { it.copy(loading = false, errorMessage = error.message ?: "Evolution failed.") } }
    }
}

data class EvolutionUiState(val loading: Boolean = false, val message: String? = null, val errorMessage: String? = null, val dashboard: EvolutionDashboardState = EvolutionDashboardState())
