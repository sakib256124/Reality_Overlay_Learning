package com.rola.app.presentation.lifelong_memory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.lifelong_memory.LifelongMemoryDashboardState
import com.rola.app.lifelong_memory.LifelongMemoryRepository
import com.rola.app.lifelong_memory.memory_core.LifelongMemoryContext
import com.rola.app.lifelong_memory.memory_core.LifelongMemoryEngine
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
class LifelongLearningDashboardViewModel @Inject constructor(
    private val engine: LifelongMemoryEngine,
    private val repository: LifelongMemoryRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val userId = "local-learner"
    private val local = MutableStateFlow(LifelongMemoryUiState())
    val uiState = combine(local, repository.observeDashboard(userId)) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), LifelongMemoryUiState())

    fun updateMemory() = viewModelScope.launch {
        local.update { it.copy(loading = true, message = "Updating lifelong memory...", errorMessage = null) }
        runCatching {
            val context = LifelongMemoryContext(userId, "Electric Circuits", "Master current flow", listOf("I confuse voltage and current"), listOf("AR circuit lab"), listOf("Completed basics badge"))
            val result = withContext(defaultDispatcher) { engine.updateMemory(context) }
            withContext(ioDispatcher) { repository.save(result) }
            result.graph.expertise.name
        }.onSuccess { local.update { state -> state.copy(loading = false, message = "Memory updated: $it expertise.") } }
            .onFailure { error -> local.update { it.copy(loading = false, errorMessage = error.message ?: "Memory update failed.") } }
    }
}

data class LifelongMemoryUiState(val loading: Boolean = false, val message: String? = null, val errorMessage: String? = null, val dashboard: LifelongMemoryDashboardState = LifelongMemoryDashboardState())
