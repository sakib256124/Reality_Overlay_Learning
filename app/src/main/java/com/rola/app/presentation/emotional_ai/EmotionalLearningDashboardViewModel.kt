package com.rola.app.presentation.emotional_ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.emotional_ai.EmotionalAIRepository
import com.rola.app.emotional_ai.EmotionalDashboardState
import com.rola.app.emotional_ai.intelligence.EmotionalAIEngine
import com.rola.app.emotional_ai.learner_state.EmotionalLearningContext
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
class EmotionalLearningDashboardViewModel @Inject constructor(
    private val engine: EmotionalAIEngine,
    private val repository: EmotionalAIRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val local = MutableStateFlow(EmotionalUiState())
    val uiState = combine(local, repository.observeDashboard()) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EmotionalUiState())

    fun analyzeEmotion() = viewModelScope.launch {
        local.update { it.copy(loading = true, message = "Analyzing learner emotional state...", errorMessage = null) }
        runCatching {
            val context = EmotionalLearningContext("local-learner", "Fractions", "This is hard and I confuse the steps.", 35, 58, listOf("hesitation", "curious"), listOf("completed visual lesson"))
            val result = withContext(defaultDispatcher) { engine.supportLearning(context) }
            withContext(ioDispatcher) { repository.save(result) }
            result.state.tone.name
        }.onSuccess { local.update { state -> state.copy(loading = false, message = "Emotion support ready: $it.") } }
            .onFailure { error -> local.update { it.copy(loading = false, errorMessage = error.message ?: "Emotion analysis failed.") } }
    }
}

data class EmotionalUiState(val loading: Boolean = false, val message: String? = null, val errorMessage: String? = null, val dashboard: EmotionalDashboardState = EmotionalDashboardState())
