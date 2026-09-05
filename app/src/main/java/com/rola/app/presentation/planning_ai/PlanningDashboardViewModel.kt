package com.rola.app.presentation.planning_ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.planning_ai.PlanningAIRepository
import com.rola.app.planning_ai.PlanningDashboardState
import com.rola.app.planning_ai.planning_core.AutonomousPlanningEngine
import com.rola.app.planning_ai.planning_core.PlanningRequest
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
class PlanningDashboardViewModel @Inject constructor(
    private val engine: AutonomousPlanningEngine,
    private val repository: PlanningAIRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val local = MutableStateFlow(PlanningUiState())
    val uiState = combine(local, repository.observeDashboard()) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PlanningUiState())

    fun createPlan() = viewModelScope.launch {
        local.update { it.copy(loading = true, message = "Creating autonomous learning plan...", errorMessage = null) }
        runCatching {
            val plan = withContext(defaultDispatcher) {
                engine.createPlan(
                    PlanningRequest(
                        learnerId = "local-learner",
                        desiredOutcome = "AI learning strategy architect",
                        currentAbility = 68,
                        availableHoursPerWeek = 9,
                        resources = listOf("Cognitive AI", "Emotional AI", "Predictive AI", "Lifelong Memory"),
                        emotionalState = "focused but slightly stressed",
                        knowledgeGaps = listOf("planning optimization", "research sequencing"),
                    ),
                )
            }
            withContext(ioDispatcher) { repository.save(plan) }
            plan.status.name
        }.onSuccess { status -> local.update { it.copy(loading = false, message = "Planning complete: $status.") } }
            .onFailure { error -> local.update { it.copy(loading = false, errorMessage = error.message ?: "Planning failed.") } }
    }
}

data class PlanningUiState(val loading: Boolean = false, val message: String? = null, val errorMessage: String? = null, val dashboard: PlanningDashboardState = PlanningDashboardState())
