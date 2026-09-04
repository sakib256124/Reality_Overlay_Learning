package com.rola.app.presentation.ecosystem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.core.ai.EcosystemAnalyticsManager
import com.rola.app.core.ai.LearningExperienceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class EcosystemDashboardViewModel @Inject constructor(
    private val analyticsManager: EcosystemAnalyticsManager,
    private val learningExperienceManager: LearningExperienceManager,
) : ViewModel() {
    private val localState = MutableStateFlow(EcosystemDashboardUiState())

    val uiState = combine(
        localState,
        analyticsManager.observeEducationDashboard(),
    ) { state, dashboard ->
        state.copy(dashboard = dashboard)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = EcosystemDashboardUiState(),
    )

    fun updateObjective(value: String) {
        localState.update { it.copy(objectiveInput = value.take(100), errorMessage = null) }
    }

    fun runIntegratedSession() {
        viewModelScope.launch {
            val objective = uiState.value.objectiveInput.ifBlank { "Explore nearby objects" }
            localState.update { it.copy(loading = true, message = null, errorMessage = null) }
            runCatching { learningExperienceManager.runFullLearningJourney(objective) }
                .onSuccess { session ->
                    localState.update {
                        it.copy(
                            loading = false,
                            activeSession = session,
                            message = "Integrated learning session completed.",
                        )
                    }
                }
                .onFailure { error ->
                    localState.update {
                        it.copy(loading = false, errorMessage = error.message ?: "Could not run integrated session.")
                    }
                }
        }
    }
}
