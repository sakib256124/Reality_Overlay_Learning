package com.rola.app.presentation.ai_metaverse

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.ai_metaverse.MetaverseDashboardState
import com.rola.app.ai_metaverse.MetaverseRepository
import com.rola.app.ai_metaverse.intelligence.MetaverseEducationEngine
import com.rola.app.ai_metaverse.intelligence.MetaverseLearningRequest
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
class MetaverseDashboardViewModel @Inject constructor(
    private val engine: MetaverseEducationEngine,
    private val repository: MetaverseRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val learnerId: String = savedStateHandle["learnerId"] ?: "local-learner"
    private val institutionId: String = savedStateHandle["institutionId"] ?: "local-institution"
    private val localState = MutableStateFlow(MetaverseDashboardUiState())

    val uiState = combine(
        localState,
        repository.observeDashboard(institutionId, learnerId),
    ) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = MetaverseDashboardUiState(),
        )

    fun createLearningUniverse() {
        viewModelScope.launch {
            localState.update { it.copy(loading = true, message = "Building AI metaverse learning universe...", errorMessage = null) }
            runCatching {
                val request = MetaverseLearningRequest(
                    requestId = "metaverse-request-${UUID.randomUUID()}",
                    learnerId = learnerId,
                    institutionId = institutionId,
                    subject = "Science",
                    topic = "Solar System",
                    studentLevel = "Beginner",
                    learningObjective = "Explain orbit, gravity, and scale through a virtual space lab",
                    collaborationMode = "global classroom",
                )
                val result = withContext(defaultDispatcher) { engine.createLearningUniverse(request) }
                withContext(ioDispatcher) { repository.saveResult(result) }
                result.governanceRecord.decision.name
            }.onSuccess { decision ->
                localState.update { it.copy(loading = false, message = "Metaverse universe saved as $decision.") }
            }.onFailure { error ->
                localState.update {
                    it.copy(
                        loading = false,
                        message = null,
                        errorMessage = error.message ?: "Metaverse universe creation failed.",
                    )
                }
            }
        }
    }
}

data class MetaverseDashboardUiState(
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val dashboard: MetaverseDashboardState = MetaverseDashboardState(),
)

