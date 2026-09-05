package com.rola.app.presentation.education_orchestration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.education_orchestration.EducationOrchestrationDashboardState
import com.rola.app.education_orchestration.EducationOrchestrationRepository
import com.rola.app.education_orchestration.ecosystem_core.AutonomousEducationManager
import com.rola.app.education_orchestration.ecosystem_core.EducationAIService
import com.rola.app.education_orchestration.ecosystem_core.OrchestrationRequest
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
class EducationOrchestrationDashboardViewModel @Inject constructor(
    private val manager: AutonomousEducationManager,
    private val repository: EducationOrchestrationRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val local = MutableStateFlow(EducationOrchestrationUiState())
    val uiState = combine(local, repository.observeDashboard()) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EducationOrchestrationUiState())

    fun orchestrate() = viewModelScope.launch {
        local.update { it.copy(loading = true, message = "Coordinating ROLA AI ecosystem...", errorMessage = null) }
        runCatching {
            val result = withContext(defaultDispatcher) {
                manager.manage(
                    OrchestrationRequest(
                        userId = "local-learner",
                        requirement = "Help me learn machine learning",
                        learningState = "needs goal, planning, knowledge, assessment, and mastery support",
                        activeSystems = listOf(EducationAIService.CompanionAI, EducationAIService.MasteryAI),
                        userFeedback = 88,
                        systemHealth = 93,
                    ),
                )
            }
            withContext(ioDispatcher) { repository.save(result) }
            result.status.name
        }.onSuccess { status -> local.update { it.copy(loading = false, message = "Orchestration complete: $status.") } }
            .onFailure { error -> local.update { it.copy(loading = false, errorMessage = error.message ?: "Orchestration failed.") } }
    }
}

data class EducationOrchestrationUiState(val loading: Boolean = false, val message: String? = null, val errorMessage: String? = null, val dashboard: EducationOrchestrationDashboardState = EducationOrchestrationDashboardState())
