package com.rola.app.presentation.virtual_campus_ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.virtual_campus_ai.VirtualCampusAIRepository
import com.rola.app.virtual_campus_ai.VirtualCampusDashboardState
import com.rola.app.virtual_campus_ai.campus_core.CampusSpaceType
import com.rola.app.virtual_campus_ai.campus_core.VirtualCampusAIEngine
import com.rola.app.virtual_campus_ai.campus_core.VirtualCampusRequest
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
class VirtualCampusDashboardViewModel @Inject constructor(
    private val engine: VirtualCampusAIEngine,
    private val repository: VirtualCampusAIRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val local = MutableStateFlow(VirtualCampusUiState())
    val uiState = combine(local, repository.observeDashboard()) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), VirtualCampusUiState())

    fun openCampus() = viewModelScope.launch {
        val accessLevel = "verified-student"
        local.update { it.copy(loading = true, message = "Opening autonomous virtual campus...", errorMessage = null) }
        runCatching {
            val result = withContext(defaultDispatcher) {
                engine.openCampus(
                    VirtualCampusRequest(
                        learnerId = "local-learner",
                        courseTopic = "robotics engineering",
                        learningGoal = "study robotics through virtual labs",
                        preferredSpace = CampusSpaceType.Laboratory,
                        collaborators = listOf("teacher-avatar", "research-peer"),
                        accessLevel = accessLevel,
                    ),
                )
            }
            withContext(ioDispatcher) { repository.save(result, accessLevel) }
            result.status.name
        }.onSuccess { status -> local.update { it.copy(loading = false, message = "Virtual campus active: $status.") } }
            .onFailure { error -> local.update { it.copy(loading = false, errorMessage = error.message ?: "Virtual campus launch failed.") } }
    }
}

data class VirtualCampusUiState(
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val dashboard: VirtualCampusDashboardState = VirtualCampusDashboardState(),
)
