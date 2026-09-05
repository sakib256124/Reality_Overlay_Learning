package com.rola.app.presentation.ai_infrastructure

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.ai_infrastructure.AIInfrastructureDashboardState
import com.rola.app.ai_infrastructure.AIInfrastructureRepository
import com.rola.app.ai_infrastructure.models.AIInfrastructureRequest
import com.rola.app.ai_infrastructure.models.AIServiceType
import com.rola.app.ai_infrastructure.models.InfrastructureRegion
import com.rola.app.ai_infrastructure.orchestration.AIInfrastructureEngine
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
class AIInfrastructureDashboardViewModel @Inject constructor(
    private val engine: AIInfrastructureEngine,
    private val repository: AIInfrastructureRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val institutionId: String = savedStateHandle["institutionId"] ?: "local-institution"
    private val localState = MutableStateFlow(AIInfrastructureDashboardUiState())

    val uiState = combine(
        localState,
        repository.observeDashboard(institutionId),
    ) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AIInfrastructureDashboardUiState(),
        )

    fun optimizeInfrastructure() {
        viewModelScope.launch {
            localState.update { it.copy(loading = true, message = "Optimizing distributed AI infrastructure...", errorMessage = null) }
            runCatching {
                val request = AIInfrastructureRequest(
                    requestId = "infra-request-${UUID.randomUUID()}",
                    institutionId = institutionId,
                    learnerRegion = InfrastructureRegion.AsiaPacific,
                    activeUsers = 125_000,
                    requestedServices = listOf(
                        AIServiceType.AITeacher,
                        AIServiceType.AITutor,
                        AIServiceType.Knowledge,
                        AIServiceType.Vision,
                        AIServiceType.Translation,
                        AIServiceType.Metaverse,
                    ),
                    edgeDevices = listOf("android-phone", "ar-glasses", "education-robot"),
                    workloadSignals = listOf("peak classroom traffic", "vision inference load", "regional metaverse sessions"),
                )
                val result = withContext(defaultDispatcher) { engine.optimizeInfrastructure(request) }
                withContext(ioDispatcher) { repository.saveResult(result) }
                result.healthReport.serviceHealth.name
            }.onSuccess { health ->
                localState.update { it.copy(loading = false, message = "Infrastructure plan saved with $health health.") }
            }.onFailure { error ->
                localState.update {
                    it.copy(
                        loading = false,
                        message = null,
                        errorMessage = error.message ?: "Infrastructure optimization failed.",
                    )
                }
            }
        }
    }
}

data class AIInfrastructureDashboardUiState(
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val dashboard: AIInfrastructureDashboardState = AIInfrastructureDashboardState(),
)

