package com.rola.app.presentation.enterprise

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.enterprise.teacher.TeacherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class TeacherDashboardViewModel @Inject constructor(
    teacherRepository: TeacherRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val institutionId: String = savedStateHandle["institutionId"] ?: "local-institution"
    private val teacherId: String = savedStateHandle["teacherId"] ?: "local-teacher"

    val uiState = teacherRepository.observeTeacherDashboard(institutionId, teacherId)
        .map { TeacherDashboardUiState(dashboard = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = TeacherDashboardUiState(),
        )
}

data class TeacherDashboardUiState(
    val dashboard: com.rola.app.domain.model.TeacherDashboard? = null,
)
