package com.rola.app.presentation.agi_network

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.agi_network.AGINetworkDashboardState
import com.rola.app.agi_network.AGINetworkRepository
import com.rola.app.agi_network.intelligence.AGINetworkAccessContext
import com.rola.app.agi_network.intelligence.AGINetworkEngine
import com.rola.app.agi_network.intelligence.AGINetworkPermission
import com.rola.app.agi_network.intelligence.EducationNetworkSignal
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
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
class AGINetworkDashboardViewModel @Inject constructor(
    private val agiNetworkEngine: AGINetworkEngine,
    private val repository: AGINetworkRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val institutionId: String = savedStateHandle["institutionId"] ?: "local-institution"
    private val learnerId: String = savedStateHandle["learnerId"] ?: "local-learner"
    private val localState = MutableStateFlow(AGINetworkDashboardUiState())

    val uiState = combine(
        localState,
        repository.observeDashboard(institutionId),
    ) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AGINetworkDashboardUiState(),
        )

    fun runNetworkCycle() {
        viewModelScope.launch {
            localState.update { it.copy(loading = true, message = "Coordinating AGI education agents...", errorMessage = null) }
            runCatching {
                val signal = EducationNetworkSignal(
                    signalId = "network-signal-${UUID.randomUUID()}",
                    learnerId = learnerId,
                    institutionId = institutionId,
                    topic = "Mathematics Foundations",
                    activityType = "quiz-and-research",
                    learningOutcomeScore = 58,
                    contentQualityScore = 72,
                    researchEvidence = listOf(
                        "Fractions misconception: learners confuse part-whole and ratio models",
                        "Visual model update: area diagrams improve early fraction understanding",
                    ),
                )
                val access = AGINetworkAccessContext(
                    userId = "teacher-supervisor",
                    institutionId = institutionId,
                    permissions = setOf(
                        AGINetworkPermission.RunAgents,
                        AGINetworkPermission.ImproveModels,
                        AGINetworkPermission.ProposeCurriculum,
                        AGINetworkPermission.ViewAnalytics,
                    ),
                    humanSupervisorId = "teacher-supervisor",
                )
                val result = withContext(defaultDispatcher) {
                    agiNetworkEngine.runEducationNetworkCycle(signal, access)
                }
                withContext(ioDispatcher) {
                    repository.saveResult(result)
                }
                result.governanceRecord.auditSummary
            }.onSuccess { audit ->
                localState.update { it.copy(loading = false, message = audit) }
            }.onFailure { error ->
                localState.update {
                    it.copy(
                        loading = false,
                        message = null,
                        errorMessage = error.message ?: "AGI network cycle failed.",
                    )
                }
            }
        }
    }
}

data class AGINetworkDashboardUiState(
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val dashboard: AGINetworkDashboardState = AGINetworkDashboardState(),
)
