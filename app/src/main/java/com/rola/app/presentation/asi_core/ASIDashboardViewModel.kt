package com.rola.app.presentation.asi_core

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.asi_core.ASIDashboardState
import com.rola.app.asi_core.ASIRepository
import com.rola.app.asi_core.intelligence.ASIEducationChallenge
import com.rola.app.asi_core.intelligence.ASIEngine
import com.rola.app.asi_core.intelligence.ASIStakeholder
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
class ASIDashboardViewModel @Inject constructor(
    private val asiEngine: ASIEngine,
    private val repository: ASIRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val learnerId: String = savedStateHandle["learnerId"] ?: "local-learner"
    private val institutionId: String = savedStateHandle["institutionId"] ?: "local-institution"
    private val localState = MutableStateFlow(ASIDashboardUiState())

    val uiState = combine(
        localState,
        repository.observeDashboard(learnerId, institutionId),
    ) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ASIDashboardUiState(),
        )

    fun runASIChallenge() {
        viewModelScope.launch {
            localState.update { it.copy(loading = true, message = "Solving supervised ASI education challenge...", errorMessage = null) }
            runCatching {
                val challenge = ASIEducationChallenge(
                    challengeId = "asi-challenge-${UUID.randomUUID()}",
                    learnerId = learnerId,
                    institutionId = institutionId,
                    topic = "Scientific Reasoning",
                    problemStatement = "Learner struggles to connect evidence, claim, and explanation in curriculum.",
                    cognitiveSignals = listOf("concept gap: evidence selection", "attention: moderate"),
                    neuralSignals = listOf("workload: high"),
                    quantumInsights = listOf("optimized route: prerequisite repair before simulation"),
                    learningHistory = listOf(52, 60, 66, 58),
                    stakeholders = listOf(ASIStakeholder.Teacher, ASIStakeholder.Student, ASIStakeholder.Researcher),
                )
                val result = withContext(defaultDispatcher) { asiEngine.solveEducationChallenge(challenge) }
                withContext(ioDispatcher) { repository.saveResult(result) }
                "${result.governanceRecord.approvalStatus.name}: ${result.professorResponse.explanation}"
            }.onSuccess { status ->
                localState.update { it.copy(loading = false, message = "ASI output saved as $status.") }
            }.onFailure { error ->
                localState.update {
                    it.copy(
                        loading = false,
                        message = null,
                        errorMessage = error.message ?: "ASI challenge failed.",
                    )
                }
            }
        }
    }
}

data class ASIDashboardUiState(
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val dashboard: ASIDashboardState = ASIDashboardState(),
)
