package com.rola.app.presentation.recognition

import com.rola.app.domain.model.RecognitionResult

enum class RecognitionStatus {
    Idle,
    Scanning,
    Processing,
    ObjectDetected,
    LowConfidence,
    Error,
}

data class RecognitionUiState(
    val status: RecognitionStatus = RecognitionStatus.Idle,
    val statusMessage: String = "Scanning...",
    val latestResult: RecognitionResult? = null,
    val errorMessage: String? = null,
)
