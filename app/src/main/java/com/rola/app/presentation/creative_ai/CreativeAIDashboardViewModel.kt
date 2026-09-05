package com.rola.app.presentation.creative_ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.creative_ai.CreativeAIRepository
import com.rola.app.creative_ai.CreativeDashboardState
import com.rola.app.creative_ai.creativity_engine.CreativeAIEngine
import com.rola.app.creative_ai.creativity_engine.CreativeAIRequest
import com.rola.app.creative_ai.creativity_engine.CreativeLevel
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
class CreativeAIDashboardViewModel @Inject constructor(
    private val engine: CreativeAIEngine,
    private val repository: CreativeAIRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val local = MutableStateFlow(CreativeUiState())
    val uiState = combine(local, repository.observeDashboard()) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CreativeUiState())

    fun generateCreativePackage() = viewModelScope.launch {
        local.update { it.copy(loading = true, message = "Generating creative education package...", errorMessage = null) }
        runCatching {
            val request = CreativeAIRequest("local-learner", "Science", "Electric Circuits", CreativeLevel.Beginner, "Understand current flow", "Make circuits feel less abstract")
            val result = withContext(defaultDispatcher) { engine.createEducationPackage(request) }
            withContext(ioDispatcher) { repository.save(request, result) }
            result.collaboration.solution
        }.onSuccess { local.update { state -> state.copy(loading = false, message = it) } }
            .onFailure { error -> local.update { it.copy(loading = false, errorMessage = error.message ?: "Creative generation failed.") } }
    }
}

data class CreativeUiState(val loading: Boolean = false, val message: String? = null, val errorMessage: String? = null, val dashboard: CreativeDashboardState = CreativeDashboardState())
