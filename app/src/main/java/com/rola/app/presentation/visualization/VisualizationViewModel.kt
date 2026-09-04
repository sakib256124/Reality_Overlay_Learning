package com.rola.app.presentation.visualization

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.data.model3d.ModelRepository
import com.rola.app.data.voice.VoiceRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class VisualizationViewModel @Inject constructor(
    private val modelRepository: ModelRepository,
    private val voiceRepository: VoiceRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val objectId: String = savedStateHandle["objectId"] ?: "bottle"
    private val _uiState = MutableStateFlow(VisualizationUiState(objectId = objectId))
    val uiState: StateFlow<VisualizationUiState> = _uiState.asStateFlow()

    init {
        loadModel()
    }

    fun loadModel() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(status = VisualizationStatus.Loading, errorMessage = null)
            }
            modelRepository.getModelForObject(objectId)
                .onSuccess { asset ->
                    _uiState.update {
                        it.copy(
                            modelAsset = asset,
                            status = VisualizationStatus.ReadyToPlace,
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            status = VisualizationStatus.Error,
                            errorMessage = throwable.message ?: "Unable to load 3D model.",
                        )
                    }
                }
        }
    }

    fun requestPlacement() {
        _uiState.update { it.copy(isPlacementRequested = true, errorMessage = null) }
    }

    fun onModelPlaced() {
        _uiState.update {
            it.copy(
                status = VisualizationStatus.Placed,
                isPlacementRequested = false,
                errorMessage = null,
            )
        }
    }

    fun onPlacementFailed(message: String) {
        _uiState.update {
            it.copy(
                isPlacementRequested = false,
                errorMessage = message,
            )
        }
    }

    fun onArSessionFailure(message: String) {
        _uiState.update {
            it.copy(
                status = VisualizationStatus.Error,
                isPlacementRequested = false,
                isTracking = false,
                errorMessage = message,
            )
        }
    }

    fun onTrackingChanged(isTracking: Boolean) {
        _uiState.update { it.copy(isTracking = isTracking) }
    }

    fun rotateBy(deltaDegrees: Float) {
        _uiState.update {
            it.copy(
                interaction = it.interaction.copy(
                    rotationYDegrees = it.interaction.rotationYDegrees + deltaDegrees,
                ),
            )
        }
    }

    fun zoomBy(multiplier: Float) {
        _uiState.update {
            it.copy(
                interaction = it.interaction.copy(
                    scaleMultiplier = (it.interaction.scaleMultiplier * multiplier)
                        .coerceIn(
                            com.rola.app.domain.model.ModelInteraction.MIN_SCALE_MULTIPLIER,
                            com.rola.app.domain.model.ModelInteraction.MAX_SCALE_MULTIPLIER,
                        ),
                ),
            )
        }
    }

    fun moveBy(deltaX: Float, deltaZ: Float) {
        _uiState.update {
            it.copy(
                interaction = it.interaction.copy(
                    offsetX = (it.interaction.offsetX + deltaX).coerceIn(-0.8f, 0.8f),
                    offsetZ = (it.interaction.offsetZ + deltaZ).coerceIn(-0.8f, 0.8f),
                ),
            )
        }
    }

    fun resetInteraction() {
        _uiState.update {
            it.copy(
                interaction = com.rola.app.domain.model.ModelInteraction(),
                status = if (it.modelAsset == null) VisualizationStatus.Loading else VisualizationStatus.ReadyToPlace,
                isPlacementRequested = false,
                errorMessage = null,
            )
        }
    }

    fun toggleAnimation() {
        _uiState.update {
            it.copy(interaction = it.interaction.copy(isAnimationEnabled = !it.interaction.isAnimationEnabled))
        }
    }

    fun toggleExplodedView() {
        _uiState.update {
            it.copy(interaction = it.interaction.copy(isExplodedView = !it.interaction.isExplodedView))
        }
    }

    fun toggleHighlightMode() {
        _uiState.update {
            it.copy(interaction = it.interaction.copy(isHighlightMode = !it.interaction.isHighlightMode))
        }
    }

    fun speakModelDescription() {
        val model = _uiState.value.modelAsset?.model ?: return
        viewModelScope.launch {
            voiceRepository.speak("${model.modelName}. ${model.description}")
        }
    }

    override fun onCleared() {
        voiceRepository.shutdown()
        super.onCleared()
    }
}
