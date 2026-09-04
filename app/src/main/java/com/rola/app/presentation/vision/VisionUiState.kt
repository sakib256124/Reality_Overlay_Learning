package com.rola.app.presentation.vision

import com.rola.app.domain.model.DetectionResult
import com.rola.app.domain.model.DetectedObject
import com.rola.app.domain.model.ObjectRelationship
import com.rola.app.domain.model.SceneContext

enum class VisionStatus {
    Idle,
    Scanning,
    Processing,
    SceneDetected,
    LowConfidence,
    Error,
}

data class VisionUiState(
    val status: VisionStatus = VisionStatus.Idle,
    val detectionResult: DetectionResult? = null,
    val trackedObjects: List<DetectedObject> = emptyList(),
    val sceneContext: SceneContext? = null,
    val relationships: List<ObjectRelationship> = emptyList(),
    val statusMessage: String = "Point camera at a scene",
    val inferenceTimeMillis: Long = 0L,
    val frameCount: Int = 0,
    val lowPowerMode: Boolean = false,
    val errorMessage: String? = null,
) {
    val hasDetections: Boolean
        get() = trackedObjects.isNotEmpty()
}
