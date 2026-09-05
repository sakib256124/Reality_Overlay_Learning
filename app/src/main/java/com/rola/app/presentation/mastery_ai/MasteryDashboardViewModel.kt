package com.rola.app.presentation.mastery_ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.mastery_ai.MasteryAIRepository
import com.rola.app.mastery_ai.MasteryDashboardState
import com.rola.app.mastery_ai.mastery_engine.AdaptiveMasteryEngine
import com.rola.app.mastery_ai.mastery_engine.MasteryRequest
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
class MasteryDashboardViewModel @Inject constructor(
    private val engine: AdaptiveMasteryEngine,
    private val repository: MasteryAIRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val local = MutableStateFlow(MasteryUiState())
    val uiState = combine(local, repository.observeDashboard()) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), MasteryUiState())

    fun evaluateMastery() = viewModelScope.launch {
        local.update { it.copy(loading = true, message = "Evaluating mastery...", errorMessage = null) }
        runCatching {
            val result = withContext(defaultDispatcher) {
                engine.evaluate(
                    MasteryRequest(
                        learnerId = "local-learner",
                        skillName = "Programming",
                        knowledgeLevel = 66,
                        practicalAbility = 58,
                        problemSolvingCapability = 63,
                        learningConsistency = 72,
                        previousPerformance = listOf(48, 55, 62, 67),
                        misunderstoodTopics = listOf("state management", "debugging strategy"),
                    ),
                )
            }
            withContext(ioDispatcher) { repository.save(result) }
            result.decision.name
        }.onSuccess { decision -> local.update { it.copy(loading = false, message = "Mastery cycle complete: $decision.") } }
            .onFailure { error -> local.update { it.copy(loading = false, errorMessage = error.message ?: "Mastery evaluation failed.") } }
    }
}

data class MasteryUiState(val loading: Boolean = false, val message: String? = null, val errorMessage: String? = null, val dashboard: MasteryDashboardState = MasteryDashboardState())
