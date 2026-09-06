package com.rola.app.presentation.education_economy_ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.education_economy_ai.EducationEconomyAIRepository
import com.rola.app.education_economy_ai.EducationEconomyDashboardState
import com.rola.app.education_economy_ai.economy_core.EducationEconomyEngine
import com.rola.app.education_economy_ai.economy_core.EducationEconomyRequest
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
class EducationEconomyDashboardViewModel @Inject constructor(
    private val engine: EducationEconomyEngine,
    private val repository: EducationEconomyAIRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val local = MutableStateFlow(EducationEconomyUiState())
    val uiState = combine(local, repository.observeDashboard()) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EducationEconomyUiState())

    fun buildEconomy() = viewModelScope.launch {
        val learnerId = "local-learner"
        local.update { it.copy(loading = true, message = "Building education digital economy...", errorMessage = null) }
        runCatching {
            val result = withContext(defaultDispatcher) {
                engine.buildEconomy(
                    EducationEconomyRequest(
                        learnerId = learnerId,
                        creatorGoal = "publish trusted AI learning resources",
                        skillArea = "applied AI",
                        learningEvidence = listOf("simulation project", "adaptive assessment", "portfolio artifact"),
                        marketSignals = listOf("global AI course demand", "verified certificate demand"),
                    ),
                )
            }
            withContext(ioDispatcher) { repository.save(result, learnerId) }
            result.status.name
        }.onSuccess { status -> local.update { it.copy(loading = false, message = "Education economy ready: $status.") } }
            .onFailure { error -> local.update { it.copy(loading = false, errorMessage = error.message ?: "Education economy failed.") } }
    }
}

data class EducationEconomyUiState(
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val dashboard: EducationEconomyDashboardState = EducationEconomyDashboardState(),
)
