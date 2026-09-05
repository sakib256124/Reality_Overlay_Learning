package com.rola.app.presentation.ai_research

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.ai_research.AIResearchDashboardState
import com.rola.app.ai_research.AIResearchRepository
import com.rola.app.ai_research.scientist.AIResearchScientistEngine
import com.rola.app.ai_research.scientist.ResearchContext
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
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
class ResearchIntelligenceDashboardViewModel @Inject constructor(
    private val engine: AIResearchScientistEngine,
    private val repository: AIResearchRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val local = MutableStateFlow(ResearchUiState())
    val uiState = combine(local, repository.observeDashboard()) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ResearchUiState())

    fun runResearch() = viewModelScope.launch {
        local.update { it.copy(loading = true, message = "Running AI research scientist cycle...", errorMessage = null) }
        runCatching {
            val result = withContext(defaultDispatcher) {
                engine.runResearchCycle(ResearchContext("local-learner", "Renewable Energy", "Can classroom simulations improve retention?", listOf("energy basics"), listOf("simulation trend")))
            }
            withContext(ioDispatcher) { repository.save(result) }
            result.project.title
        }.onSuccess { local.update { state -> state.copy(loading = false, message = it) } }
            .onFailure { error -> local.update { it.copy(loading = false, errorMessage = error.message ?: "Research cycle failed.") } }
    }
}

data class ResearchUiState(val loading: Boolean = false, val message: String? = null, val errorMessage: String? = null, val dashboard: AIResearchDashboardState = AIResearchDashboardState())
