package com.rola.app.domain.model

enum class LearningStatus {
    Scanned,
    Viewed,
    Explained,
    Completed,
}

data class ScanHistory(
    val scanId: String,
    val userId: String,
    val objectId: String,
    val objectName: String,
    val category: String,
    val timestamp: Long,
    val confidenceScore: Float,
    val learningStatus: LearningStatus,
    val imageUrl: String,
)
