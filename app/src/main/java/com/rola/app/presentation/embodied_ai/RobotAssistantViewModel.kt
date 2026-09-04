package com.rola.app.presentation.embodied_ai

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.domain.model.EducationalRobot
import com.rola.app.domain.model.RobotDashboardState
import com.rola.app.domain.model.RobotPermission
import com.rola.app.domain.model.RobotSecurityContext
import com.rola.app.domain.model.RobotSession
import com.rola.app.domain.model.RobotSessionStatus
import com.rola.app.embodied_ai.EmbodiedAIEngine
import com.rola.app.embodied_ai.EmbodiedAIRepository
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
class RobotAssistantViewModel @Inject constructor(
    private val embodiedAIEngine: EmbodiedAIEngine,
    private val embodiedAIRepository: EmbodiedAIRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val institutionId: String = savedStateHandle["institutionId"] ?: "local-institution"
    private val teacherId: String = savedStateHandle["teacherId"] ?: "local-teacher"
    private val studentId: String = savedStateHandle["studentId"] ?: "local-student"
    private val localState = MutableStateFlow(RobotAssistantUiState())

    val uiState = combine(
        localState,
        embodiedAIRepository.observeDashboard(),
    ) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = RobotAssistantUiState(),
        )

    fun updateQuestion(value: String) {
        localState.update { it.copy(question = value.take(160), message = null) }
    }

    fun registerRobot() {
        viewModelScope.launch {
            runCatching {
                embodiedAIEngine.createDefaultRobot(
                    name = "ROLA Classroom Robot",
                    environment = "STEM classroom",
                    context = securityContext(),
                )
            }.onSuccess { robot ->
                embodiedAIRepository.saveRobot(robot)
                localState.update { it.copy(activeRobot = robot, message = "Educational robot registered.") }
            }.onFailure { error ->
                localState.update { it.copy(errorMessage = error.message ?: "Could not register robot.") }
            }
        }
    }

    fun runRobotInteraction() {
        viewModelScope.launch {
            val robot = uiState.value.activeRobot ?: uiState.value.dashboard?.robots?.firstOrNull()
            if (robot == null) {
                localState.update { it.copy(message = "Register a robot before running interaction.") }
                return@launch
            }
            localState.update { it.copy(loading = true, message = null, errorMessage = null) }
            runCatching {
                embodiedAIEngine.runTeachingInteraction(
                    robot = robot,
                    studentId = studentId,
                    topic = "Plant growth",
                    question = uiState.value.question.ifBlank { "Why does this plant grow?" },
                    context = securityContext(),
                )
            }.onSuccess { decision ->
                embodiedAIRepository.saveInteraction(
                    com.rola.app.domain.model.RobotInteraction(
                        interactionId = "robot-ui-interaction-${UUID.randomUUID()}",
                        robotId = robot.robotId,
                        studentId = studentId,
                        inputMode = com.rola.app.domain.model.RobotInputMode.Voice,
                        inputText = uiState.value.question,
                        responseText = decision.teachingAction.message,
                        emotionResponse = decision.rationale,
                    ),
                )
                embodiedAIRepository.saveSession(
                    RobotSession(
                        sessionId = "robot-session-${UUID.randomUUID()}",
                        robotId = robot.robotId,
                        classroomId = "classroom-1",
                        teacherId = teacherId,
                        status = RobotSessionStatus.Teaching,
                        activeTopic = decision.teachingAction.topic,
                    ),
                )
                localState.update {
                    it.copy(
                        loading = false,
                        latestDecision = decision.rationale,
                        latestRobotSpeech = embodiedAIEngine.speak(decision.teachingAction.message, "en"),
                        message = "Robot teaching action completed.",
                    )
                }
            }.onFailure { error ->
                localState.update { it.copy(loading = false, errorMessage = error.message ?: "Robot interaction failed.") }
            }
        }
    }

    private fun securityContext(): RobotSecurityContext =
        RobotSecurityContext(
            userId = teacherId,
            institutionId = institutionId,
            permissions = setOf(
                RobotPermission.RegisterRobot,
                RobotPermission.StartRobotSession,
                RobotPermission.ControlRobot,
                RobotPermission.ViewRobotAnalytics,
                RobotPermission.ManageRobotNetwork,
            ),
        )
}

data class RobotAssistantUiState(
    val question: String = "Why does this plant grow?",
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val activeRobot: EducationalRobot? = null,
    val latestDecision: String = "",
    val latestRobotSpeech: String = "",
    val dashboard: RobotDashboardState? = null,
)
