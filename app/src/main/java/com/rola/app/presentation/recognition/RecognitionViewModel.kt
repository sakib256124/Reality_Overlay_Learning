package com.rola.app.presentation.recognition

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ar.core.Frame
import com.rola.app.data.ml.RecognitionOutcome
import com.rola.app.data.ml.RecognitionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicBoolean
import javax.inject.Inject

@HiltViewModel
class RecognitionViewModel @Inject constructor(
    private val recognitionRepository: RecognitionRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(RecognitionUiState())
    val uiState: StateFlow<RecognitionUiState> = _uiState.asStateFlow()

    private val inferenceRunning = AtomicBoolean(false)
    private var frameCounter = 0
    private var inferenceJob: Job? = null

    fun onCameraFrame(frame: Frame) {
        frameCounter++
        if (frameCounter % FRAME_SKIP_COUNT != 0) return
        if (!inferenceRunning.compareAndSet(false, true)) return

        val bitmap = recognitionRepository.copyFrameToBitmap(frame)
        if (bitmap == null) {
            inferenceRunning.set(false)
            _uiState.update {
                it.copy(
                    status = RecognitionStatus.Scanning,
                    statusMessage = "Scanning...",
                    errorMessage = null,
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                status = RecognitionStatus.Processing,
                statusMessage = "Processing...",
                errorMessage = null,
            )
        }

        inferenceJob = viewModelScope.launch(Dispatchers.Default) {
            try {
                val outcome = recognitionRepository.recognizeBitmap(bitmap)
                when (outcome) {
                    is RecognitionOutcome.Success -> {
                        val bestResult = outcome.results.first()
                        _uiState.update {
                            it.copy(
                                status = RecognitionStatus.ObjectDetected,
                                statusMessage = "Object Detected",
                                latestResult = bestResult,
                                errorMessage = null,
                            )
                        }
                    }
                    is RecognitionOutcome.LowConfidence -> _uiState.update {
                        it.copy(
                            status = RecognitionStatus.LowConfidence,
                            statusMessage = "Scanning...",
                            latestResult = outcome.bestResult,
                            errorMessage = "Recognition confidence is too low.",
                        )
                    }
                    is RecognitionOutcome.Error -> _uiState.update {
                        it.copy(
                            status = RecognitionStatus.Error,
                            statusMessage = "Scanning...",
                            errorMessage = outcome.message,
                        )
                    }
                }
            } finally {
                bitmap.recycle()
                inferenceRunning.set(false)
            }
        }
    }

    fun reset() {
        _uiState.value = RecognitionUiState(status = RecognitionStatus.Scanning)
    }

    override fun onCleared() {
        inferenceJob?.cancel()
        recognitionRepository.release()
        super.onCleared()
    }

    private companion object {
        const val FRAME_SKIP_COUNT = 20
    }
}
