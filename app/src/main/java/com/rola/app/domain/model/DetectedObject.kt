package com.rola.app.domain.model

data class DetectedObject(
    val id: String,
    val label: String,
    val confidence: Float,
    val anchorId: String? = null,
    val boundingBox: BoundingBox = BoundingBox(),
    val trackingId: String = id,
    val firstSeenAt: Long = System.currentTimeMillis(),
    val lastSeenAt: Long = System.currentTimeMillis(),
    val frameCount: Int = 1,
    val classification: String = label,
    val isStable: Boolean = frameCount >= 3,
)
