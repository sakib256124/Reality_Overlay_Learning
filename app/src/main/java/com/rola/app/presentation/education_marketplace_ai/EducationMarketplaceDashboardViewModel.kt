package com.rola.app.presentation.education_marketplace_ai

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.education_marketplace_ai.EducationMarketplaceAIRepository
import com.rola.app.education_marketplace_ai.EducationMarketplaceDashboardState
import com.rola.app.education_marketplace_ai.marketplace_core.EducationMarketplaceEngine
import com.rola.app.education_marketplace_ai.marketplace_core.EducationMarketplaceRequest
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
class EducationMarketplaceDashboardViewModel @Inject constructor(
    private val engine: EducationMarketplaceEngine,
    private val repository: EducationMarketplaceAIRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    private val local = MutableStateFlow(EducationMarketplaceUiState())
    val uiState = combine(local, repository.observeDashboard()) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), EducationMarketplaceUiState())

    fun buildMarketplace() = viewModelScope.launch {
        val learnerId = "local-learner"
        local.update { it.copy(loading = true, message = "Building intelligent education marketplace...", errorMessage = null) }
        runCatching {
            val result = withContext(defaultDispatcher) {
                engine.buildMarketplace(
                    EducationMarketplaceRequest(
                        learnerId = learnerId,
                        learningGoal = "learn applied AI with simulations",
                        skillLevel = "intermediate",
                        cognitiveProfile = "visual project learner",
                        emotionalState = "confident",
                        learningHistory = listOf("AI basics", "robotics lab", "knowledge graph lesson"),
                    ),
                )
            }
            withContext(ioDispatcher) { repository.save(result, learnerId) }
            result.status.name
        }.onSuccess { status -> local.update { it.copy(loading = false, message = "Marketplace ready: $status.") } }
            .onFailure { error -> local.update { it.copy(loading = false, errorMessage = error.message ?: "Marketplace setup failed.") } }
    }
}

data class EducationMarketplaceUiState(
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val dashboard: EducationMarketplaceDashboardState = EducationMarketplaceDashboardState(),
)
