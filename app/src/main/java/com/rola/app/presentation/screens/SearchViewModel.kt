package com.rola.app.presentation.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.data.adaptive.UserProfileRepository
import com.rola.app.domain.model.ObjectModel
import com.rola.app.domain.repository.LearningRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchUiState(
    val query: String = "",
    val results: List<ObjectModel> = emptyList(),
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val learningRepository: LearningRepository,
    private val userProfileRepository: UserProfileRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    private var searchJob: Job? = null

    init {
        search("")
    }

    fun onQueryChanged(query: String) {
        _uiState.update { it.copy(query = query, isLoading = true) }
        if (query.trim().length >= MIN_TRACKED_QUERY_LENGTH) {
            viewModelScope.launch {
                runCatching { userProfileRepository.recordSearchTopic(query) }
            }
        }
        search(query)
    }

    private fun search(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            learningRepository.searchObjects(query)
                .catch { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Unable to search objects.",
                        )
                    }
                }
                .collect { objects ->
                    _uiState.update {
                        it.copy(results = objects, isLoading = false, errorMessage = null)
                    }
                }
        }
    }

    private companion object {
        const val MIN_TRACKED_QUERY_LENGTH = 3
    }
}
