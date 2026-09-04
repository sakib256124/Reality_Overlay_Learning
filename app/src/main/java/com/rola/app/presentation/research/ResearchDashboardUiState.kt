package com.rola.app.presentation.research

import com.rola.app.domain.model.ResearchDashboardState

data class ResearchDashboardUiState(
    val dashboard: ResearchDashboardState = ResearchDashboardState(),
    val topicInput: String = "Plant",
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
)
