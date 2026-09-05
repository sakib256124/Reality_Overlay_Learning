package com.rola.app.presentation.predictive_ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.predictive_ai.PredictiveAIRepository
import com.rola.app.predictive_ai.PredictiveDashboardState
import com.rola.app.predictive_ai.intelligence.FutureMentorAgent
import com.rola.app.predictive_ai.intelligence.PredictionContext
import com.rola.app.predictive_ai.optimization.GrowthOptimizationManager
import com.rola.app.predictive_ai.optimization.PredictionAnalyticsManager
import com.rola.app.predictive_ai.prediction_engine.PredictiveAIEngine
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
class PredictiveDashboardViewModel @Inject constructor(
    private val engine: PredictiveAIEngine,
    private val repository: PredictiveAIRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val userId = "local-learner"
    private val local = MutableStateFlow(PredictiveUiState())
    val uiState = combine(local, repository.observeDashboard(userId)) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), PredictiveUiState())

    fun runPrediction() = viewModelScope.launch {
        local.update { it.copy(loading = true, message = "Predicting future learning potential...", errorMessage = null) }
        runCatching {
            val context = PredictionContext(userId, "AI Programming", listOf("math gap", "completed circuits"), listOf(78, 84, 88), listOf("strong focus", "visual learner"), listOf("research", "programming"))
            val result = withContext(defaultDispatcher) { engine.predictFuture(context) }
            withContext(ioDispatcher) { repository.save(userId, result) }
            result.potential.suggestedPath
        }.onSuccess { local.update { state -> state.copy(loading = false, message = "Prediction ready: $it.") } }
            .onFailure { error -> local.update { it.copy(loading = false, errorMessage = error.message ?: "Prediction failed.") } }
    }
}

data class PredictiveUiState(val loading: Boolean = false, val message: String? = null, val errorMessage: String? = null, val dashboard: PredictiveDashboardState = PredictiveDashboardState())
