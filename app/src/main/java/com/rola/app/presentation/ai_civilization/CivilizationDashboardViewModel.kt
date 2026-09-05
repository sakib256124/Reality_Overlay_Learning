package com.rola.app.presentation.ai_civilization

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.ai_civilization.AICivilizationRepository
import com.rola.app.ai_civilization.CivilizationDashboardState
import com.rola.app.ai_civilization.intelligence.CivilizationContext
import com.rola.app.ai_civilization.intelligence.CivilizationLearningLevel
import com.rola.app.ai_civilization.intelligence.UniversalAICivilizationEngine
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
class CivilizationDashboardViewModel @Inject constructor(
    private val engine: UniversalAICivilizationEngine,
    private val repository: AICivilizationRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val local = MutableStateFlow(CivilizationUiState())
    val uiState = combine(local, repository.observeDashboard()) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), CivilizationUiState())

    fun runCivilizationCycle() = viewModelScope.launch {
        local.update { it.copy(loading = true, message = "Evolving universal AI learning civilization...", errorMessage = null) }
        runCatching {
            val context = CivilizationContext("local-learner", "Electric Circuits", CivilizationLearningLevel.Advanced, listOf("needs future skills", "knowledge gap"))
            val result = withContext(defaultDispatcher) { engine.evolveCivilization(context) }
            withContext(ioDispatcher) { repository.save(result) }
            result.civilization.globalEducationIntelligence
        }.onSuccess { local.update { state -> state.copy(loading = false, message = it) } }
            .onFailure { error -> local.update { it.copy(loading = false, errorMessage = error.message ?: "Civilization cycle failed.") } }
    }
}

data class CivilizationUiState(val loading: Boolean = false, val message: String? = null, val errorMessage: String? = null, val dashboard: CivilizationDashboardState = CivilizationDashboardState())
