package com.rola.app.presentation.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.domain.repository.LearningHistoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val learningHistoryRepository: LearningHistoryRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    private val searchQuery = MutableStateFlow("")
    private val selectedCategory = MutableStateFlow<String?>(null)
    private var historyJob: Job? = null

    init {
        observeHistory()
        syncNow()
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery.value = query
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun onCategorySelected(category: String?) {
        selectedCategory.value = category
        _uiState.update { it.copy(selectedCategory = category) }
    }

    fun deleteScan(scanId: String) {
        viewModelScope.launch {
            runCatching { learningHistoryRepository.deleteScan(scanId) }
                .onFailure { throwable -> showError(throwable.message ?: "Unable to delete scan.") }
        }
    }

    fun deleteHistory() {
        viewModelScope.launch {
            runCatching { learningHistoryRepository.deleteHistory() }
                .onFailure { throwable -> showError(throwable.message ?: "Unable to delete history.") }
        }
    }

    fun syncNow() {
        viewModelScope.launch {
            _uiState.update { it.copy(isSyncing = true, errorMessage = null) }
            runCatching { learningHistoryRepository.syncNow() }
                .onFailure { throwable ->
                    showError(throwable.message ?: "Sync failed. Local history is still available offline.")
                }
            _uiState.update { it.copy(isSyncing = false) }
        }
    }

    private fun observeHistory() {
        historyJob?.cancel()
        historyJob = viewModelScope.launch {
            combine(searchQuery, selectedCategory) { query, category -> query to category }
                .flatMapLatest { (query, category) ->
                    learningHistoryRepository.observeUserHistory(query = query, category = category)
                }
                .combine(learningHistoryRepository.observeHistoryCategories()) { history, categories ->
                    history to categories
                }
                .collect { (history, categories) ->
                    _uiState.update {
                        it.copy(
                            history = history,
                            categories = categories,
                            isLoading = false,
                            errorMessage = null,
                        )
                    }
                }
        }
    }

    private fun showError(message: String) {
        _uiState.update {
            it.copy(
                isLoading = false,
                errorMessage = message,
            )
        }
    }
}
