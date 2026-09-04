package com.rola.app.domain.model

data class RecognitionResult(
    val name: String,
    val confidence: Float,
    val timestamp: Long,
) {
    val confidencePercent: Int
        get() = (confidence * 100f).toInt().coerceIn(0, 100)
}
