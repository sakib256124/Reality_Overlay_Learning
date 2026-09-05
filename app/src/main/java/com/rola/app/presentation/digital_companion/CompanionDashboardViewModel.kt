package com.rola.app.presentation.digital_companion

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.digital_companion.DigitalCompanionDashboardState
import com.rola.app.digital_companion.DigitalCompanionRepository
import com.rola.app.digital_companion.companion_core.AIDigitalCompanionEngine
import com.rola.app.digital_companion.companion_core.CompanionLearningContext
import com.rola.app.digital_companion.companion_core.CompanionModality
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
class CompanionDashboardViewModel @Inject constructor(
    private val companionEngine: AIDigitalCompanionEngine,
    private val repository: DigitalCompanionRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val userId: String = savedStateHandle["userId"] ?: "local-learner"
    private val localState = MutableStateFlow(CompanionDashboardUiState())

    val uiState = combine(
        localState,
        repository.observeDashboard(userId),
    ) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CompanionDashboardUiState(),
        )

    fun runCompanionCycle() {
        viewModelScope.launch {
            localState.update { it.copy(loading = true, message = "Updating personal AI companion...", errorMessage = null) }
            runCatching {
                val context = CompanionLearningContext(
                    userId = userId,
                    topic = "Electric Circuits",
                    currentGoal = "Explain voltage and current with a real example",
                    recentMessage = "I still confuse voltage with current.",
                    skillLevel = "Beginner",
                    recentScores = listOf(52, 64, 68),
                    preferredModalities = listOf(CompanionModality.Text, CompanionModality.ARGuidance, CompanionModality.Voice),
                )
                val result = withContext(defaultDispatcher) { companionEngine.supportLearner(context) }
                withContext(ioDispatcher) { repository.saveResult(result) }
                result.decision.teachingApproach
            }.onSuccess { approach ->
                localState.update { it.copy(loading = false, message = "Companion updated: $approach.") }
            }.onFailure { error ->
                localState.update {
                    it.copy(
                        loading = false,
                        message = null,
                        errorMessage = error.message ?: "Companion update failed.",
                    )
                }
            }
        }
    }
}

data class CompanionDashboardUiState(
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val dashboard: DigitalCompanionDashboardState = DigitalCompanionDashboardState(),
)

