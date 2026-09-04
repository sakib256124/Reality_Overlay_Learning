package com.rola.app.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.data.quiz.QuizRepository
import com.rola.app.domain.model.LearningProgress
import com.rola.app.domain.model.ObjectModel
import com.rola.app.domain.model.ScanHistory
import com.rola.app.domain.repository.LearningHistoryRepository
import com.rola.app.domain.usecase.GetFeaturedObjectsUseCase
import com.rola.app.presentation.state.UIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeDashboardState(
    val recentHistory: List<ScanHistory> = emptyList(),
    val progress: LearningProgress = LearningProgress(
        totalQuizzesCompleted = 0,
        averageScore = 0,
        totalPoints = 0,
        currentLevel = 1,
        learningStreak = 0,
        strongTopics = emptyList(),
        weakTopics = emptyList(),
        badges = emptyList(),
    ),
    val errorMessage: String? = null,
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFeaturedObjectsUseCase: GetFeaturedObjectsUseCase,
    private val learningHistoryRepository: LearningHistoryRepository,
    private val quizRepository: QuizRepository,
) : ViewModel() {
    private val _featuredObjects =
        MutableStateFlow<UIState<List<ObjectModel>>>(UIState.Loading)
    val featuredObjects: StateFlow<UIState<List<ObjectModel>>> = _featuredObjects.asStateFlow()
    private val _dashboardState = MutableStateFlow(HomeDashboardState())
    val dashboardState: StateFlow<HomeDashboardState> = _dashboardState.asStateFlow()

    init {
        loadFeaturedObjects()
        observeDashboard()
    }

    private fun loadFeaturedObjects() {
        viewModelScope.launch {
            getFeaturedObjectsUseCase()
                .catch { throwable ->
                    _featuredObjects.value = UIState.Error(
                        message = throwable.message ?: "Unable to load learning objects",
                        throwable = throwable,
                    )
                }
                .collect { objects ->
                    _featuredObjects.value = UIState.Success(objects)
                }
        }
    }

    private fun observeDashboard() {
        viewModelScope.launch {
            combine(
                learningHistoryRepository.observeUserHistory(),
                quizRepository.observeProgress(),
            ) { history, progress ->
                HomeDashboardState(
                    recentHistory = history.take(3),
                    progress = progress,
                    errorMessage = null,
                )
            }
                .catch { throwable ->
                    _dashboardState.value = _dashboardState.value.copy(
                        errorMessage = throwable.message ?: "Unable to load dashboard.",
                    )
                }
                .collect { state -> _dashboardState.value = state }
        }
    }
}
