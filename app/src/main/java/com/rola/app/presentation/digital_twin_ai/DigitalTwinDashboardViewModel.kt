package com.rola.app.presentation.digital_twin_ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.digital_twin_ai.DigitalTwinAIRepository
import com.rola.app.digital_twin_ai.DigitalTwinDashboardState
import com.rola.app.digital_twin_ai.twin_core.AIDigitalTwinEngine
import com.rola.app.digital_twin_ai.twin_core.DigitalTwinRequest
import com.rola.app.digital_twin_ai.twin_core.TwinDomain
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
class DigitalTwinDashboardViewModel @Inject constructor(
    private val engine: AIDigitalTwinEngine,
    private val repository: DigitalTwinAIRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val local = MutableStateFlow(DigitalTwinUiState())
    val uiState = combine(local, repository.observeDashboard()) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), DigitalTwinUiState())

    fun createTwin() = viewModelScope.launch {
        local.update { it.copy(loading = true, message = "Creating AI digital twin...", errorMessage = null) }
        runCatching {
            val result = withContext(defaultDispatcher) {
                engine.createLearningTwin(
                    DigitalTwinRequest(
                        learnerId = "local-learner",
                        realWorldObject = "electric motor",
                        domain = TwinDomain.Machine,
                        sensorData = listOf("temperature: normal", "vibration: mild", "speed: stable"),
                        arScanSignals = listOf("camera scan: motor body", "object recognition: rotating shaft"),
                        learningGoal = "understand electromechanical systems",
                    ),
                )
            }
            withContext(ioDispatcher) { repository.save(result) }
            result.twin.status.name
        }.onSuccess { status -> local.update { it.copy(loading = false, message = "Digital twin ready: $status.") } }
            .onFailure { error -> local.update { it.copy(loading = false, errorMessage = error.message ?: "Digital twin creation failed.") } }
    }
}

data class DigitalTwinUiState(val loading: Boolean = false, val message: String? = null, val errorMessage: String? = null, val dashboard: DigitalTwinDashboardState = DigitalTwinDashboardState())
