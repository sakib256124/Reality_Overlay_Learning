package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rola.app.domain.model.LearningStatus
import com.rola.app.domain.model.ScanHistory

@Entity(
    tableName = "scan_history",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE,
        ),
        ForeignKey(
            entity = ObjectEntity::class,
            parentColumns = ["objectId"],
            childColumns = ["objectId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["userId"]),
        Index(value = ["objectId"]),
        Index(value = ["timestamp"]),
        Index(value = ["isSynced"]),
    ],
)
data class ScanHistoryEntity(
    @PrimaryKey val scanId: String,
    val userId: String,
    val objectId: String,
    val timestamp: Long,
    val confidenceScore: Float,
    val learningStatus: LearningStatus,
    val isSynced: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis(),
)

data class ScanHistoryWithObject(
    val scanId: String,
    val userId: String,
    val objectId: String,
    val objectName: String,
    val category: String,
    val timestamp: Long,
    val confidenceScore: Float,
    val learningStatus: LearningStatus,
    val imageUrl: String,
) {
    fun toDomain(): ScanHistory = ScanHistory(
        scanId = scanId,
        userId = userId,
        objectId = objectId,
        objectName = objectName,
        category = category,
        timestamp = timestamp,
        confidenceScore = confidenceScore,
        learningStatus = learningStatus,
        imageUrl = imageUrl,
    )
}

fun ScanHistory.toEntity(isSynced: Boolean = false): ScanHistoryEntity = ScanHistoryEntity(
    scanId = scanId,
    userId = userId,
    objectId = objectId,
    timestamp = timestamp,
    confidenceScore = confidenceScore,
    learningStatus = learningStatus,
    isSynced = isSynced,
)
