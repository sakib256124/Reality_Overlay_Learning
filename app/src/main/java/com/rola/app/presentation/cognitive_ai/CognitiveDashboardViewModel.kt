package com.rola.app.presentation.cognitive_ai

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.cognitive_ai.CognitiveAIRepository
import com.rola.app.cognitive_ai.CognitiveEngine
import com.rola.app.domain.model.CognitiveConsent
import com.rola.app.domain.model.CognitiveDashboardState
import com.rola.app.domain.model.CognitivePermission
import com.rola.app.domain.model.CognitiveSecurityContext
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class CognitiveDashboardViewModel @Inject constructor(
    private val cognitiveEngine: CognitiveEngine,
    private val cognitiveAIRepository: CognitiveAIRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val userId: String = savedStateHandle["userId"] ?: "local-learner"
    private val institutionId: String = savedStateHandle["institutionId"] ?: "local-institution"
    private val localState = MutableStateFlow(CognitiveDashboardUiState())

    val uiState = combine(
        localState,
        cognitiveAIRepository.observeDashboard(userId),
    ) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CognitiveDashboardUiState(),
        )

    fun runCognitiveAnalysis() {
        viewModelScope.launch {
            localState.update { it.copy(loading = true, message = null, errorMessage = null) }
            val securityContext = securityContext()
            runCatching {
                val activities = cognitiveAIRepository.recentActivities(userId)
                val seededActivities = activities.ifEmpty { sampleActivities(userId) }
                cognitiveAIRepository.saveActivities(seededActivities)
                val result = cognitiveEngine.analyzeStudent(userId, seededActivities, securityContext)
                cognitiveAIRepository.saveResult(result)
                result
            }.onSuccess { result ->
                localState.update {
                    it.copy(
                        loading = false,
                        latestDecision = result.decision.explanation,
                        message = "Cognitive analysis completed with ${result.profile.intelligenceScore}% intelligence score.",
                    )
                }
            }.onFailure { error ->
                localState.update { it.copy(loading = false, errorMessage = error.message ?: "Could not run cognitive analysis.") }
            }
        }
    }

    private fun sampleActivities(userId: String): List<com.rola.app.domain.model.CognitiveLearningActivity> =
        listOf(
            com.rola.app.domain.model.CognitiveLearningActivity("cog-activity-1", userId, com.rola.app.domain.model.CognitiveActivityType.ARObjectExploration, "Electricity", 420_000, 82),
            com.rola.app.domain.model.CognitiveLearningActivity("cog-activity-2", userId, com.rola.app.domain.model.CognitiveActivityType.Quiz, "Voltage", 180_000, 48, "Confused voltage with current"),
            com.rola.app.domain.model.CognitiveLearningActivity("cog-activity-3", userId, com.rola.app.domain.model.CognitiveActivityType.TutorConversation, "Voltage", 240_000, 64),
            com.rola.app.domain.model.CognitiveLearningActivity("cog-activity-4", userId, com.rola.app.domain.model.CognitiveActivityType.Simulation, "Circuit", 360_000, 76),
        )

    private fun securityContext(): CognitiveSecurityContext =
        CognitiveSecurityContext(
            userId = userId,
            institutionId = institutionId,
            permissions = setOf(
                CognitivePermission.AnalyzeLearner,
                CognitivePermission.ViewCognitiveProfile,
                CognitivePermission.GeneratePersonalPlan,
                CognitivePermission.MakeEducationalDecision,
                CognitivePermission.ViewEmotionAnalytics,
            ),
            consent = CognitiveConsent(
                cognitiveAnalysisEnabled = true,
                emotionAnalysisEnabled = true,
                cloudProcessingEnabled = false,
            ),
        )
}

data class CognitiveDashboardUiState(
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val latestDecision: String = "",
    val dashboard: CognitiveDashboardState? = null,
)
