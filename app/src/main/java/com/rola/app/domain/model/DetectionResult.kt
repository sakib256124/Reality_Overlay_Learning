package com.rola.app.domain.model

data class BoundingBox(
    val left: Float = 0.12f,
    val top: Float = 0.18f,
    val right: Float = 0.88f,
    val bottom: Float = 0.78f,
) {
    val width: Float
        get() = (right - left).coerceAtLeast(0f)

    val height: Float
        get() = (bottom - top).coerceAtLeast(0f)

    val centerX: Float
        get() = left + width / 2f

    val centerY: Float
        get() = top + height / 2f
}

data class DetectionResult(
    val objects: List<DetectedObject>,
    val sceneContext: SceneContext,
    val relationships: List<ObjectRelationship>,
    val inferenceTimeMillis: Long,
    val frameTimestamp: Long = System.currentTimeMillis(),
) {
    val primaryObject: DetectedObject?
        get() = objects.maxByOrNull { it.confidence }

    val hasStableObjects: Boolean
        get() = objects.any { it.isStable }
}
