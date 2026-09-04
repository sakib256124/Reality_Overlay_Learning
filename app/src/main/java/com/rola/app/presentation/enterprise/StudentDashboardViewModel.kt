package com.rola.app.presentation.enterprise

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.enterprise.student.StudentRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

@HiltViewModel
class StudentDashboardViewModel @Inject constructor(
    studentRepository: StudentRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val studentId: String = savedStateHandle["studentId"] ?: "local_user"

    val uiState = studentRepository.observeStudentDashboard(studentId)
        .map { StudentDashboardUiState(dashboard = it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = StudentDashboardUiState(),
        )
}

data class StudentDashboardUiState(
    val dashboard: com.rola.app.domain.model.StudentDashboard? = null,
)
