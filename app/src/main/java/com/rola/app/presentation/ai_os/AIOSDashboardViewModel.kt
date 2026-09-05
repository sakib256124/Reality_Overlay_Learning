package com.rola.app.presentation.ai_os

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.ai_os.AIOSDashboardState
import com.rola.app.ai_os.AIOSRepository
import com.rola.app.ai_os.ecosystem.AIEducationOS
import com.rola.app.ai_os.intelligence.AIOSRequest
import com.rola.app.ai_os.intelligence.AIOSServiceType
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
class AIOSDashboardViewModel @Inject constructor(
    private val aiEducationOS: AIEducationOS,
    private val repository: AIOSRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val userId: String = savedStateHandle["userId"] ?: "local-learner"
    private val institutionId: String = savedStateHandle["institutionId"] ?: "local-institution"
    private val localState = MutableStateFlow(AIOSDashboardUiState())

    val uiState = combine(
        localState,
        repository.observeDashboard(institutionId, userId),
    ) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AIOSDashboardUiState(),
        )

    fun runAIOS() {
        viewModelScope.launch {
            localState.update { it.copy(loading = true, message = "Running AI Education OS workflow...", errorMessage = null) }
            runCatching {
                val request = AIOSRequest(
                    requestId = "ai-os-request-${UUID.randomUUID()}",
                    userId = userId,
                    institutionId = institutionId,
                    userNeed = "Explain and assess a science concept",
                    activeTopic = "Energy Transfer",
                    deviceContext = "Android AR device",
                    requestedServices = listOf(
                        AIOSServiceType.AITeacher,
                        AIOSServiceType.AITutor,
                        AIOSServiceType.Knowledge,
                        AIOSServiceType.Analytics,
                        AIOSServiceType.Translation,
                        AIOSServiceType.Vision,
                        AIOSServiceType.SpatialLearning,
                        AIOSServiceType.RobotEducation,
                    ),
                )
                val result = withContext(defaultDispatcher) { aiEducationOS.run(request) }
                withContext(ioDispatcher) { repository.saveResult(result) }
                result.monitoringReport.health.name
            }.onSuccess { health ->
                localState.update { it.copy(loading = false, message = "AI OS workflow saved with $health health.") }
            }.onFailure { error ->
                localState.update {
                    it.copy(
                        loading = false,
                        message = null,
                        errorMessage = error.message ?: "AI OS workflow failed.",
                    )
                }
            }
        }
    }
}

data class AIOSDashboardUiState(
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val dashboard: AIOSDashboardState = AIOSDashboardState(),
)

