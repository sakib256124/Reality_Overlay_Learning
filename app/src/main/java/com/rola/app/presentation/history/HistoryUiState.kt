package com.rola.app.presentation.history

import com.rola.app.domain.model.ScanHistory

data class HistoryUiState(
    val history: List<ScanHistory> = emptyList(),
    val categories: List<String> = emptyList(),
    val searchQuery: String = "",
    val selectedCategory: String? = null,
    val isLoading: Boolean = true,
    val isSyncing: Boolean = false,
    val errorMessage: String? = null,
)
