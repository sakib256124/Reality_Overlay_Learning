package com.rola.app.presentation.neural_learning_ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.neural_learning_ai.NeuralLearningAIRepository
import com.rola.app.neural_learning_ai.NeuralLearningDashboardState
import com.rola.app.neural_learning_ai.neural_core.NeuralLearningEngine
import com.rola.app.neural_learning_ai.neural_core.NeuralLearningRequest
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
class NeuralLearningDashboardViewModel @Inject constructor(
    private val engine: NeuralLearningEngine,
    private val repository: NeuralLearningAIRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val local = MutableStateFlow(NeuralLearningUiState())
    val uiState = combine(local, repository.observeDashboard()) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), NeuralLearningUiState())

    fun personalizeLearning() = viewModelScope.launch {
        val learnerId = "local-learner"
        local.update { it.copy(loading = true, message = "Processing brain-like knowledge pathways...", errorMessage = null) }
        runCatching {
            val result = withContext(defaultDispatcher) {
                engine.personalize(
                    NeuralLearningRequest(
                        learnerId = learnerId,
                        concept = "AI development",
                        priorKnowledge = listOf("programming basics", "algorithms", "data structures"),
                        learningPatterns = listOf("visual learner", "project learning"),
                        attentionSignals = listOf("focused", "prefers diagrams"),
                        emotionalState = "confident",
                        masteryLevel = "intermediate",
                    ),
                )
            }
            withContext(ioDispatcher) { repository.save(result, learnerId) }
            result.status.name
        }.onSuccess { status -> local.update { it.copy(loading = false, message = "Neural learning adapted: $status.") } }
            .onFailure { error -> local.update { it.copy(loading = false, errorMessage = error.message ?: "Neural learning failed.") } }
    }
}

data class NeuralLearningUiState(
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val dashboard: NeuralLearningDashboardState = NeuralLearningDashboardState(),
)
