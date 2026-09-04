package com.rola.app.presentation.vision

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ar.core.Frame
import com.rola.app.data.vision.VisionProcessingOutcome
import com.rola.app.data.vision.VisionRepository
import com.rola.app.domain.model.DetectionResult
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class VisionViewModel @Inject constructor(
    private val visionRepository: VisionRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(VisionUiState())
    val uiState: StateFlow<VisionUiState> = _uiState.asStateFlow()

    private val processing = AtomicBoolean(false)
    private var frameCounter = 0
    private var processingJob: Job? = null

    fun onCameraFrame(frame: Frame) {
        frameCounter++
        val skipCount = if (_uiState.value.lowPowerMode) LOW_POWER_FRAME_SKIP else DEFAULT_FRAME_SKIP
        if (frameCounter % skipCount != 0) return
        if (!processing.compareAndSet(false, true)) return

        _uiState.update {
            it.copy(
                status = VisionStatus.Processing,
                statusMessage = "Understanding scene...",
                frameCount = frameCounter,
                errorMessage = null,
            )
        }

        processingJob = viewModelScope.launch(Dispatchers.Default) {
            try {
                when (val outcome = visionRepository.processFrame(frame)) {
                    is VisionProcessingOutcome.Success -> handleResult(outcome.result)
                    VisionProcessingOutcome.NoFrame -> _uiState.update {
                        it.copy(
                            status = VisionStatus.Scanning,
                            statusMessage = "Scanning...",
                            errorMessage = null,
                        )
                    }
                    is VisionProcessingOutcome.Error -> showError(outcome.message)
                }
            } finally {
                processing.set(false)
            }
        }
    }

    fun toggleLowPowerMode() {
        _uiState.update { it.copy(lowPowerMode = !it.lowPowerMode) }
    }

    fun reset() {
        processingJob?.cancel()
        visionRepository.resetTracking()
        frameCounter = 0
        processing.set(false)
        _uiState.value = VisionUiState(status = VisionStatus.Scanning)
    }

    override fun onCleared() {
        processingJob?.cancel()
        visionRepository.resetTracking()
        super.onCleared()
    }

    private fun handleResult(result: DetectionResult) {
        val stableObjects = result.objects.filter { it.confidence >= MIN_CONFIDENCE }
        _uiState.update {
            it.copy(
                status = if (stableObjects.isEmpty()) VisionStatus.LowConfidence else VisionStatus.SceneDetected,
                detectionResult = result,
                trackedObjects = stableObjects,
                sceneContext = result.sceneContext,
                relationships = result.relationships,
                inferenceTimeMillis = result.inferenceTimeMillis,
                statusMessage = if (stableObjects.isEmpty()) "Keep scanning" else "${stableObjects.size} objects detected",
                errorMessage = null,
            )
        }
    }

    private fun showError(message: String) {
        _uiState.update {
            it.copy(
                status = VisionStatus.Error,
                statusMessage = "Vision unavailable",
                errorMessage = message,
            )
        }
    }

    private companion object {
        const val DEFAULT_FRAME_SKIP = 12
        const val LOW_POWER_FRAME_SKIP = 30
        const val MIN_CONFIDENCE = 0.45f
    }
}
