package com.rola.app.presentation.ecosystem

import com.rola.app.domain.model.EducationDashboardState
import com.rola.app.domain.model.LearningSessionState

data class EcosystemDashboardUiState(
    val dashboard: EducationDashboardState? = null,
    val activeSession: LearningSessionState? = null,
    val objectiveInput: String = "Explore nearby objects",
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
)
