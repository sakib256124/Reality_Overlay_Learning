package com.rola.app.presentation.adaptive

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.data.adaptive.UserProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class AdaptiveLearningViewModel @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(AdaptiveUiState())
    val uiState: StateFlow<AdaptiveUiState> = _uiState.asStateFlow()

    init {
        observeAdaptiveLearning()
        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _uiState.update { it.copy(status = AdaptiveStatus.Loading, errorMessage = null) }
            runCatching {
                userProfileRepository.refreshLearningProfile()
                userProfileRepository.refreshRecommendations()
            }.onFailure { throwable ->
                showError(throwable.message ?: "Unable to refresh adaptive learning profile.")
            }
        }
    }

    fun markRecommendationCompleted(recommendationId: String) {
        viewModelScope.launch {
            runCatching { userProfileRepository.markRecommendationCompleted(recommendationId) }
                .onFailure { throwable -> showError(throwable.message ?: "Unable to update recommendation.") }
        }
    }

    fun setPersonalizationEnabled(enabled: Boolean) {
        viewModelScope.launch {
            runCatching { userProfileRepository.setPersonalizationEnabled(enabled) }
                .onFailure { throwable -> showError(throwable.message ?: "Unable to update personalization setting.") }
        }
    }

    private fun observeAdaptiveLearning() {
        viewModelScope.launch {
            userProfileRepository.observeAdaptiveSummary()
                .catch { throwable -> showError(throwable.message ?: "Unable to load adaptive dashboard.") }
                .collect { summary ->
                    _uiState.update {
                        it.copy(
                            status = AdaptiveStatus.Ready,
                            profile = summary.profile,
                            pattern = summary.pattern,
                            recommendations = summary.recommendations,
                            roadmap = summary.roadmap,
                            nextTopic = summary.nextTopic,
                            predictedTrend = summary.predictedTrend,
                            explanationComplexity = summary.explanationComplexity,
                            errorMessage = null,
                        )
                    }
                }
        }
    }

    private fun showError(message: String) {
        _uiState.update {
            it.copy(
                status = AdaptiveStatus.Error,
                errorMessage = message,
            )
        }
    }
}
