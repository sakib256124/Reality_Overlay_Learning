package com.rola.app.presentation.spatial_computing_ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.spatial_computing_ai.SpatialComputingAIRepository
import com.rola.app.spatial_computing_ai.SpatialComputingDashboardState
import com.rola.app.spatial_computing_ai.spatial_core.AISpatialIntelligenceEngine
import com.rola.app.spatial_computing_ai.spatial_core.SpatialLearningRequest
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
class SpatialLearningDashboardViewModel @Inject constructor(
    private val engine: AISpatialIntelligenceEngine,
    private val repository: SpatialComputingAIRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val local = MutableStateFlow(SpatialLearningUiState())
    val uiState = combine(local, repository.observeDashboard()) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SpatialLearningUiState())

    fun createSpatialExperience() = viewModelScope.launch {
        local.update { it.copy(loading = true, message = "Mapping immersive spatial learning...", errorMessage = null) }
        runCatching {
            val result = withContext(defaultDispatcher) {
                engine.createExperience(
                    SpatialLearningRequest(
                        learnerId = "local-learner",
                        topic = "engineering physics",
                        studentLevel = "intermediate",
                        roomStructure = "lab room with open floor",
                        detectedObjects = listOf("human heart model", "gravity ramp"),
                        depthSignals = listOf("depth plane stable", "object anchor locked"),
                        voiceCommand = "start gravity experiment",
                    ),
                )
            }
            withContext(ioDispatcher) { repository.save(result) }
            result.status.name
        }.onSuccess { status -> local.update { it.copy(loading = false, message = "Spatial experience ready: $status.") } }
            .onFailure { error -> local.update { it.copy(loading = false, errorMessage = error.message ?: "Spatial experience failed.") } }
    }
}

data class SpatialLearningUiState(
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val dashboard: SpatialComputingDashboardState = SpatialComputingDashboardState(),
)
