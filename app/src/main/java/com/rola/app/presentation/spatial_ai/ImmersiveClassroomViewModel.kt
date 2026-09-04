package com.rola.app.presentation.spatial_ai

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.domain.model.SkillLevel
import com.rola.app.domain.model.SpatialLearningStyle
import com.rola.app.domain.model.SpatialLearningWorld
import com.rola.app.domain.model.SpatialLearningWorldRequest
import com.rola.app.domain.model.SpatialPermission
import com.rola.app.domain.model.SpatialSecurityContext
import com.rola.app.domain.model.SpatialSession
import com.rola.app.spatial_ai.SpatialAIEngine
import com.rola.app.spatial_ai.SpatialAIRepository
import com.rola.app.spatial_ai.SpatialAISummary
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ImmersiveClassroomViewModel @Inject constructor(
    private val spatialAIEngine: SpatialAIEngine,
    private val spatialAIRepository: SpatialAIRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val learnerId: String = savedStateHandle["learnerId"] ?: "local-learner"
    private val institutionId: String = savedStateHandle["institutionId"] ?: "local-institution"
    private val localState = MutableStateFlow(ImmersiveClassroomUiState())

    val uiState = combine(
        localState,
        spatialAIRepository.observeSummary(learnerId),
    ) { state, summary -> state.copy(summary = summary) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ImmersiveClassroomUiState(),
        )

    fun updateTopic(value: String) {
        localState.update { it.copy(topic = value.take(80), message = null) }
    }

    fun updateObjective(value: String) {
        localState.update { it.copy(objective = value.take(140), message = null) }
    }

    fun generateWorld() {
        viewModelScope.launch {
            val state = uiState.value
            localState.update { it.copy(loading = true, message = null, errorMessage = null) }
            runCatching {
                spatialAIEngine.generateWorld(
                    request = SpatialLearningWorldRequest(
                        subject = state.subject,
                        topic = state.topic.ifBlank { "Electricity" },
                        studentLevel = SkillLevel.Intermediate,
                        learningObjective = state.objective.ifBlank { "Explore the concept through interactive 3D evidence." },
                        learningStyle = SpatialLearningStyle.Kinesthetic,
                        gradeLevel = "Grade 7",
                    ),
                    securityContext = securityContext(),
                )
            }.onSuccess { world ->
                spatialAIRepository.saveWorld(world)
                localState.update {
                    it.copy(
                        loading = false,
                        activeWorld = world,
                        xrReadiness = spatialAIEngine.currentXRReadiness(),
                        message = "Virtual learning world generated.",
                    )
                }
            }.onFailure { error ->
                localState.update { it.copy(loading = false, errorMessage = error.message ?: "Could not generate spatial world.") }
            }
        }
    }

    fun enterClassroom() {
        viewModelScope.launch {
            val world = uiState.value.activeWorld
            if (world == null) {
                localState.update { it.copy(message = "Generate a world before entering the classroom.") }
                return@launch
            }
            val session = spatialAIEngine.startLearningSession(learnerId, world.worldId, securityContext())
            spatialAIRepository.saveSession(session)
            localState.update { it.copy(activeSession = session, message = "Entered immersive classroom.") }
        }
    }

    private fun securityContext(): SpatialSecurityContext = SpatialSecurityContext(
        userId = learnerId,
        institutionId = institutionId,
        permissions = setOf(
            SpatialPermission.CreateWorld,
            SpatialPermission.ManageDigitalTwin,
            SpatialPermission.StartSpatialSession,
            SpatialPermission.JoinCollaborativeRoom,
            SpatialPermission.ViewSpatialAnalytics,
        ),
    )
}

data class ImmersiveClassroomUiState(
    val subject: String = "Physics",
    val topic: String = "Electricity",
    val objective: String = "Build and explain a circuit using spatial evidence.",
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val xrReadiness: String = "",
    val activeWorld: SpatialLearningWorld? = null,
    val activeSession: SpatialSession? = null,
    val summary: SpatialAISummary? = null,
)
