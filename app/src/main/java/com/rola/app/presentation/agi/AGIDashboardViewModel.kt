package com.rola.app.presentation.agi

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.core.agi.AGIOrchestrator
import com.rola.app.core.agi.AGIRepository
import com.rola.app.domain.model.AGIAccessContext
import com.rola.app.domain.model.AGIActivityType
import com.rola.app.domain.model.AGIDashboardState
import com.rola.app.domain.model.AGILearningEvent
import com.rola.app.domain.model.AGIOrchestrationRequest
import com.rola.app.domain.model.AGIPermission
import com.rola.app.domain.model.AGIPrivacyMode
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AGIDashboardViewModel @Inject constructor(
    private val agiOrchestrator: AGIOrchestrator,
    agiRepository: AGIRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val institutionId: String = savedStateHandle["institutionId"] ?: "local-institution"
    private val learnerId: String = savedStateHandle["learnerId"] ?: "local-learner"
    private val adminId: String = savedStateHandle["adminId"] ?: "local-admin"
    private val localState = MutableStateFlow(AGIDashboardUiState())

    val uiState = combine(
        localState,
        agiRepository.observeDashboard(institutionId),
    ) { state, dashboard ->
        state.copy(dashboard = dashboard)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AGIDashboardUiState(),
    )

    fun updateObjective(value: String) {
        localState.update { it.copy(objective = value.take(120), message = null, errorMessage = null) }
    }

    fun updateTopic(value: String) {
        localState.update { it.copy(topic = value.take(80), message = null, errorMessage = null) }
    }

    fun runAGICycle() {
        viewModelScope.launch {
            val state = uiState.value
            localState.update { it.copy(loading = true, message = null, errorMessage = null) }
            runCatching {
                agiOrchestrator.orchestrate(
                    request = AGIOrchestrationRequest(
                        learnerId = learnerId,
                        institutionId = institutionId,
                        objective = state.objective.ifBlank { "Improve conceptual mastery" },
                        event = AGILearningEvent(
                            eventId = "agi-event-${UUID.randomUUID()}",
                            learnerId = learnerId,
                            activityType = AGIActivityType.ARExperiment,
                            topic = state.topic.ifBlank { "Energy transfer" },
                            signal = "Learner completed an AR experiment and needs next-step guidance.",
                            score = 68,
                        ),
                        privacyMode = AGIPrivacyMode.Standard,
                    ),
                    accessContext = AGIAccessContext(
                        userId = adminId,
                        institutionId = institutionId,
                        permissions = setOf(
                            AGIPermission.RunAgents,
                            AGIPermission.ViewLearnerModels,
                            AGIPermission.EvolveCurriculum,
                            AGIPermission.ApproveAIDecisions,
                            AGIPermission.ViewKnowledgeNetwork,
                        ),
                    ),
                )
            }.onSuccess { result ->
                localState.update {
                    it.copy(
                        loading = false,
                        latestResult = result,
                        message = "AGI cycle completed with ${result.selectedAgents.size} agents.",
                    )
                }
            }.onFailure { error ->
                localState.update {
                    it.copy(loading = false, errorMessage = error.message ?: "Could not run AGI cycle.")
                }
            }
        }
    }
}

data class AGIDashboardUiState(
    val objective: String = "Improve conceptual mastery",
    val topic: String = "Energy transfer",
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val dashboard: AGIDashboardState? = null,
    val latestResult: com.rola.app.domain.model.AGIOrchestrationResult? = null,
)
