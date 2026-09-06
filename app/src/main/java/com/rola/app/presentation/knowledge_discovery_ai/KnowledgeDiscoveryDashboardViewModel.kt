package com.rola.app.presentation.knowledge_discovery_ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.knowledge_discovery_ai.KnowledgeDiscoveryAIRepository
import com.rola.app.knowledge_discovery_ai.KnowledgeDiscoveryDashboardState
import com.rola.app.knowledge_discovery_ai.discovery_core.AutonomousKnowledgeDiscoveryEngine
import com.rola.app.knowledge_discovery_ai.discovery_core.DiscoverySourceType
import com.rola.app.knowledge_discovery_ai.discovery_core.KnowledgeDiscoveryRequest
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
class KnowledgeDiscoveryDashboardViewModel @Inject constructor(
    private val engine: AutonomousKnowledgeDiscoveryEngine,
    private val repository: KnowledgeDiscoveryAIRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val local = MutableStateFlow(KnowledgeDiscoveryUiState())
    val uiState = combine(local, repository.observeDashboard()) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), KnowledgeDiscoveryUiState())

    fun discoverKnowledge() = viewModelScope.launch {
        local.update { it.copy(loading = true, message = "Scanning global knowledge networks...", errorMessage = null) }
        runCatching {
            val result = withContext(defaultDispatcher) {
                engine.discover(
                    KnowledgeDiscoveryRequest(
                        learnerId = "local-learner",
                        domain = "biology and machine learning",
                        existingKnowledge = listOf("biology", "machine learning", "data analysis"),
                        globalSources = listOf(DiscoverySourceType.ResearchPaper, DiscoverySourceType.ScientificDatabase, DiscoverySourceType.DigitalLibrary),
                        researchQuestion = "How can students learn bioinformatics earlier?",
                    ),
                )
            }
            withContext(ioDispatcher) { repository.save(result) }
            result.status.name
        }.onSuccess { status -> local.update { it.copy(loading = false, message = "Discovery ready: $status.") } }
            .onFailure { error -> local.update { it.copy(loading = false, errorMessage = error.message ?: "Knowledge discovery failed.") } }
    }
}

data class KnowledgeDiscoveryUiState(
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val dashboard: KnowledgeDiscoveryDashboardState = KnowledgeDiscoveryDashboardState(),
)
