package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rola.app.domain.model.Recommendation
import com.rola.app.domain.model.RecommendationPriority
import com.rola.app.domain.model.RecommendationType
import com.rola.app.domain.model.SkillLevel

@Entity(
    tableName = "recommendations",
    indices = [
        Index(value = ["userId"]),
        Index(value = ["priority"]),
        Index(value = ["completed"]),
    ],
)
data class RecommendationEntity(
    @PrimaryKey val recommendationId: String,
    val userId: String,
    val title: String,
    val description: String,
    val topic: String,
    val type: RecommendationType,
    val priority: RecommendationPriority,
    val targetSkillLevel: SkillLevel,
    val createdAt: Long,
    val completed: Boolean = false,
    val isSynced: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis(),
) {
    fun toDomain(): Recommendation = Recommendation(
        recommendationId = recommendationId,
        userId = userId,
        title = title,
        description = description,
        topic = topic,
        type = type,
        priority = priority,
        targetSkillLevel = targetSkillLevel,
        createdAt = createdAt,
        completed = completed,
    )
}

fun Recommendation.toEntity(isSynced: Boolean = false): RecommendationEntity = RecommendationEntity(
    recommendationId = recommendationId,
    userId = userId,
    title = title,
    description = description,
    topic = topic,
    type = type,
    priority = priority,
    targetSkillLevel = targetSkillLevel,
    createdAt = createdAt,
    completed = completed,
    isSynced = isSynced,
)
