package com.rola.app.presentation.ai_teacher

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.ai_teacher.teaching.AITeacherEngine
import com.rola.app.ai_teacher.teaching.AITeacherRepository
import com.rola.app.domain.model.AITeacherDashboard
import com.rola.app.domain.model.CurriculumRequest
import com.rola.app.domain.model.SkillLevel
import com.rola.app.domain.model.TeacherPermission
import com.rola.app.domain.model.TeacherSecurityContext
import com.rola.app.domain.model.TeachingPlanRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class AITeacherDashboardViewModel @Inject constructor(
    aiTeacherRepository: AITeacherRepository,
    private val aiTeacherEngine: AITeacherEngine,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val teacherId: String = savedStateHandle["teacherId"] ?: "local-teacher"
    private val classId: String = savedStateHandle["classId"] ?: "local-class"
    private val institutionId: String = savedStateHandle["institutionId"] ?: "local-institution"
    private val actionState = MutableStateFlow(AITeacherActionState())

    val uiState = combine(
        aiTeacherRepository.observeDashboard(teacherId, classId),
        actionState,
    ) { dashboard, action ->
        AITeacherDashboardUiState(
            dashboard = dashboard,
            isGenerating = action.isGenerating,
            message = action.message,
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = AITeacherDashboardUiState(),
        )

    fun generateStarterCurriculum(
        subject: String,
        topic: String,
        gradeLevel: String,
        objective: String,
        durationWeeks: Int,
        level: SkillLevel,
    ) {
        viewModelScope.launch {
            actionState.value = AITeacherActionState(isGenerating = true, message = "Generating curriculum draft...")
            runCatching {
                aiTeacherEngine.generateCurriculum(
                    teacherId = teacherId,
                    institutionId = institutionId,
                    request = CurriculumRequest(
                        subject = subject.ifBlank { "Science" },
                        topic = topic.ifBlank { "Energy" },
                        gradeLevel = gradeLevel.ifBlank { "Grade 7" },
                        learningObjective = objective.ifBlank { "Explain the topic using evidence from AR observations." },
                        durationWeeks = durationWeeks.coerceIn(1, 8),
                        studentLevel = level,
                    ),
                )
            }.onSuccess {
                actionState.value = AITeacherActionState(message = "Draft curriculum created for teacher review.")
            }.onFailure {
                actionState.value = AITeacherActionState(message = it.message ?: "Could not generate curriculum.")
            }
        }
    }

    fun generateAdaptivePlan() {
        viewModelScope.launch {
            actionState.value = AITeacherActionState(isGenerating = true, message = "Analyzing student signals...")
            runCatching { aiTeacherEngine.createAdaptiveTeachingPlan(teacherId) }
                .onSuccess { actionState.value = AITeacherActionState(message = "Adaptive teaching plan created.") }
                .onFailure { actionState.value = AITeacherActionState(message = it.message ?: "Could not create adaptive plan.") }
        }
    }

    fun approveFirstPending(distributeToStudents: Boolean) {
        viewModelScope.launch {
            val curriculum = uiState.value.dashboard?.pendingApprovals?.firstOrNull()
            if (curriculum == null) {
                actionState.value = AITeacherActionState(message = "No pending curriculum to approve.")
                return@launch
            }
            runCatching {
                aiTeacherEngine.approveGeneratedContent(
                    curriculumId = curriculum.curriculumId,
                    securityContext = securityContext(),
                    comments = "Reviewed and approved from AI Teacher dashboard.",
                    distributeToStudents = distributeToStudents,
                )
            }.onSuccess {
                actionState.value = AITeacherActionState(message = if (distributeToStudents) "Curriculum distributed." else "Curriculum approved.")
            }.onFailure {
                actionState.value = AITeacherActionState(message = it.message ?: "Could not approve curriculum.")
            }
        }
    }

    fun generateTeachingPlanPreview() {
        viewModelScope.launch {
            actionState.value = AITeacherActionState(isGenerating = true, message = "Creating teaching plan...")
            runCatching {
                aiTeacherEngine.generateTeachingPlan(
                    securityContext = securityContext(),
                    request = TeachingPlanRequest(
                        teacherId = teacherId,
                        institutionId = institutionId,
                        subject = "Science",
                        topic = "Energy",
                        gradeLevel = "Grade 7",
                        durationWeeks = 4,
                        classSize = 28,
                        studentLevel = SkillLevel.Intermediate,
                    ),
                )
            }.onSuccess {
                actionState.value = AITeacherActionState(message = "Teaching plan ready: ${it.weeklySequence.firstOrNull().orEmpty()}")
            }.onFailure {
                actionState.value = AITeacherActionState(message = it.message ?: "Could not create teaching plan.")
            }
        }
    }

    private fun securityContext(): TeacherSecurityContext = TeacherSecurityContext(
        teacherId = teacherId,
        institutionId = institutionId,
        permissions = setOf(
            TeacherPermission.GenerateCurriculum,
            TeacherPermission.ReviewContent,
            TeacherPermission.ApproveContent,
            TeacherPermission.DistributeContent,
            TeacherPermission.ViewStudentAnalytics,
        ),
    )
}

data class AITeacherDashboardUiState(
    val dashboard: AITeacherDashboard? = null,
    val isGenerating: Boolean = false,
    val message: String = "",
)

private data class AITeacherActionState(
    val isGenerating: Boolean = false,
    val message: String = "",
)
