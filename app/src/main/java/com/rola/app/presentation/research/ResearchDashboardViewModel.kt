package com.rola.app.presentation.research

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.data.research.ResearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ResearchDashboardViewModel @Inject constructor(
    private val researchRepository: ResearchRepository,
) : ViewModel() {
    private val localState = MutableStateFlow(ResearchDashboardUiState())

    val uiState = combine(
        localState,
        researchRepository.observeDashboard(),
    ) { state, dashboard ->
        state.copy(dashboard = dashboard)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = ResearchDashboardUiState(),
    )

    init {
        viewModelScope.launch {
            runCatching { researchRepository.ensureSeedSources() }
        }
    }

    fun updateTopic(value: String) {
        localState.update { it.copy(topicInput = value.take(80), errorMessage = null) }
    }

    fun analyzeGaps() {
        runResearchAction("Knowledge gaps queued for review.") {
            researchRepository.analyzeGaps(uiState.value.topicInput)
        }
    }

    fun createResearchTask() {
        runResearchAction("Research task created.") {
            researchRepository.createTaskForTopic(uiState.value.topicInput)
        }
    }

    fun runNextPendingTask() {
        val task = uiState.value.dashboard.pendingTasks.firstOrNull()
        if (task == null) {
            localState.update { it.copy(message = "No pending research task is available.") }
            return
        }
        runResearchAction("Research update and learning materials generated.") {
            researchRepository.runResearchTask(task)
        }
    }

    fun approveFirstUpdate() {
        val update = uiState.value.dashboard.pendingUpdates.firstOrNull()
        if (update == null) {
            localState.update { it.copy(message = "No pending update is awaiting approval.") }
            return
        }
        runResearchAction("Knowledge update approved and applied.") {
            researchRepository.approveUpdate(update)
        }
    }

    private fun runResearchAction(
        successMessage: String,
        action: suspend () -> Any,
    ) {
        viewModelScope.launch {
            localState.update { it.copy(loading = true, message = null, errorMessage = null) }
            runCatching { action() }
                .onSuccess {
                    localState.update { it.copy(loading = false, message = successMessage) }
                }
                .onFailure { error ->
                    localState.update {
                        it.copy(loading = false, errorMessage = error.message ?: "Research action failed.")
                    }
                }
        }
    }
}
