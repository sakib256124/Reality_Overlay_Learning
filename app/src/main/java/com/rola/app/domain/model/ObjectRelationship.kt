package com.rola.app.domain.model

data class ObjectRelationship(
    val relationshipId: String,
    val subjectObjectId: String,
    val objectObjectId: String,
    val relationshipType: String,
    val description: String,
    val confidence: Float,
) {
    val confidencePercent: Int
        get() = (confidence * 100f).toInt().coerceIn(0, 100)
}
