package com.rola.app.presentation.knowledge_engineering

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.knowledge_engineering.KnowledgeEngineeringDashboardState
import com.rola.app.knowledge_engineering.KnowledgeEngineeringRepository
import com.rola.app.knowledge_engineering.knowledge_core.AIKnowledgeEngine
import com.rola.app.knowledge_engineering.knowledge_core.KnowledgeEngineeringRequest
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
class KnowledgeIntelligenceDashboardViewModel @Inject constructor(
    private val engine: AIKnowledgeEngine,
    private val repository: KnowledgeEngineeringRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val local = MutableStateFlow(KnowledgeEngineeringUiState())
    val uiState = combine(local, repository.observeDashboard()) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), KnowledgeEngineeringUiState())

    fun engineerKnowledge() = viewModelScope.launch {
        local.update { it.copy(loading = true, message = "Engineering verified knowledge...", errorMessage = null) }
        runCatching {
            val result = withContext(defaultDispatcher) {
                engine.engineerKnowledge(KnowledgeEngineeringRequest("local-learner", "Photosynthesis", listOf("plants use light energy", "chlorophyll captures light"), "Beginner", "visual"))
            }
            withContext(ioDispatcher) { repository.save(result) }
            result.validation.confidence.name
        }.onSuccess { local.update { state -> state.copy(loading = false, message = "Knowledge approved: $it.") } }
            .onFailure { error -> local.update { it.copy(loading = false, errorMessage = error.message ?: "Knowledge engineering failed.") } }
    }
}

data class KnowledgeEngineeringUiState(val loading: Boolean = false, val message: String? = null, val errorMessage: String? = null, val dashboard: KnowledgeEngineeringDashboardState = KnowledgeEngineeringDashboardState())
