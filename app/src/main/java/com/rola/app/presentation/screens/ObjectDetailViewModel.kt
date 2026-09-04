package com.rola.app.presentation.screens

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.domain.model.ObjectModel
import com.rola.app.domain.repository.LearningRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ObjectDetailUiState(
    val objectId: String = "",
    val objectModel: ObjectModel? = null,
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
)

@HiltViewModel
class ObjectDetailViewModel @Inject constructor(
    private val learningRepository: LearningRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val objectId: String = savedStateHandle["objectId"] ?: ""
    private val _uiState = MutableStateFlow(ObjectDetailUiState(objectId = objectId))
    val uiState: StateFlow<ObjectDetailUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            runCatching { learningRepository.getObjectById(objectId) }
                .onSuccess { objectModel ->
                    _uiState.update {
                        it.copy(
                            objectModel = objectModel,
                            isLoading = false,
                            errorMessage = if (objectModel == null) "Object details are not available yet." else null,
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = throwable.message ?: "Unable to load object details.",
                        )
                    }
                }
        }
    }
}
