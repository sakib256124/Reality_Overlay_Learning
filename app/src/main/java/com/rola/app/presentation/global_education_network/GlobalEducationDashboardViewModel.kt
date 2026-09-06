package com.rola.app.presentation.global_education_network

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.global_education_network.GlobalEducationDashboardState
import com.rola.app.global_education_network.GlobalEducationNetworkRepository
import com.rola.app.global_education_network.network_core.GlobalEducationNetworkEngine
import com.rola.app.global_education_network.network_core.GlobalEducationNetworkRequest
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
class GlobalEducationDashboardViewModel @Inject constructor(
    private val engine: GlobalEducationNetworkEngine,
    private val repository: GlobalEducationNetworkRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val local = MutableStateFlow(GlobalEducationUiState())
    val uiState = combine(local, repository.observeDashboard()) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), GlobalEducationUiState())

    fun connectGlobalNetwork() = viewModelScope.launch {
        local.update { it.copy(loading = true, message = "Connecting global education network...", errorMessage = null) }
        runCatching {
            val result = withContext(defaultDispatcher) {
                engine.connectWorld(
                    GlobalEducationNetworkRequest(
                        userId = "local-learner",
                        region = "South Asia",
                        goals = listOf("AI collaboration", "robotics research"),
                        skills = listOf("programming", "research methods"),
                        interests = listOf("global projects", "translated learning"),
                        learningHistory = listOf("knowledge discovery", "education marketplace"),
                        preferredLanguage = "Bangla",
                    ),
                )
            }
            withContext(ioDispatcher) { repository.save(result) }
            result.status.name
        }.onSuccess { status -> local.update { it.copy(loading = false, message = "Global network ready: $status.") } }
            .onFailure { error -> local.update { it.copy(loading = false, errorMessage = error.message ?: "Global network failed.") } }
    }
}

data class GlobalEducationUiState(
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val dashboard: GlobalEducationDashboardState = GlobalEducationDashboardState(),
)
