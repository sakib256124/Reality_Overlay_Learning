package com.rola.app.presentation.personal_agent

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.personal_agent.PersonalAgentDashboardState
import com.rola.app.personal_agent.PersonalAgentRepository
import com.rola.app.personal_agent.agent_core.AgentRequest
import com.rola.app.personal_agent.agent_core.UniversalEducationAgent
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
class PersonalAgentDashboardViewModel @Inject constructor(
    private val agent: UniversalEducationAgent,
    private val repository: PersonalAgentRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val local = MutableStateFlow(PersonalAgentUiState())
    val uiState = combine(local, repository.observeDashboard()) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PersonalAgentUiState())

    fun activateAgent() = viewModelScope.launch {
        local.update { it.copy(loading = true, message = "Activating universal personal education agent...", errorMessage = null) }
        runCatching {
            val result = withContext(defaultDispatcher) {
                agent.respond(
                    AgentRequest(
                        userId = "local-learner",
                        userNeed = "plan a mastery-based research learning path",
                        currentGoal = "AI education architecture",
                        skillLevel = "intermediate",
                        emotionState = "focused but stressed",
                        learningSpeed = "steady",
                        previousMistakes = listOf("skipped review", "weak project evidence"),
                        careerObjective = "future AI education architect",
                    ),
                )
            }
            withContext(ioDispatcher) { repository.save(result) }
            result.decision.moduleToUse
        }.onSuccess { module -> local.update { it.copy(loading = false, message = "Agent ready: $module selected.") } }
            .onFailure { error -> local.update { it.copy(loading = false, errorMessage = error.message ?: "Agent activation failed.") } }
    }
}

data class PersonalAgentUiState(val loading: Boolean = false, val message: String? = null, val errorMessage: String? = null, val dashboard: PersonalAgentDashboardState = PersonalAgentDashboardState())
