package com.rola.app.presentation.education_singularity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.education_singularity.EducationSingularityRepository
import com.rola.app.education_singularity.SingularityDashboardState
import com.rola.app.education_singularity.universal_intelligence.EducationalSingularityEngine
import com.rola.app.education_singularity.universal_intelligence.SingularityLearningContext
import com.rola.app.education_singularity.universal_intelligence.SingularityLevel
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
class SingularityDashboardViewModel @Inject constructor(
    private val engine: EducationalSingularityEngine,
    private val repository: EducationSingularityRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val local = MutableStateFlow(SingularityUiState())
    val uiState = combine(local, repository.observeDashboard()) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), SingularityUiState())

    fun runSingularityCycle() = viewModelScope.launch {
        local.update { it.copy(loading = true, message = "Fusing universal education intelligence...", errorMessage = null) }
        runCatching {
            val context = SingularityLearningContext("local-learner", "Electric Circuits", SingularityLevel.Intermediate, listOf("needs visual analogy", "quiz recovery"), listOf("global strategy: misconception repair"))
            val result = withContext(defaultDispatcher) { engine.createUniversalLearningSystem(context) }
            withContext(ioDispatcher) { repository.save(result) }
            result.learningModel.strategy
        }.onSuccess { local.update { state -> state.copy(loading = false, message = "Singularity model ready: $it.") } }
            .onFailure { error -> local.update { it.copy(loading = false, errorMessage = error.message ?: "Singularity cycle failed.") } }
    }
}

data class SingularityUiState(val loading: Boolean = false, val message: String? = null, val errorMessage: String? = null, val dashboard: SingularityDashboardState = SingularityDashboardState())
