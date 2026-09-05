package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "digital_companions", indices = [Index(value = ["userId"])])
data class DigitalCompanionEntity(
    @PrimaryKey val companionId: String,
    val userId: String,
    val personalityProfile: String,
    val learningHistory: List<String>,
    val knowledgeUnderstanding: List<String>,
    val communicationStyle: String,
    val learningGoals: List<String>,
    val preferences: List<String>,
    val memoryControlEnabled: Boolean,
)

@Entity(tableName = "companion_memory", indices = [Index(value = ["userId"]), Index(value = ["memoryType"]), Index(value = ["topic"])])
data class CompanionMemoryEntity(
    @PrimaryKey val memoryId: String,
    val userId: String,
    val memoryType: String,
    val topic: String,
    val summary: String,
    val userControlled: Boolean,
    val updatedAt: Long,
)

@Entity(tableName = "companion_conversations", indices = [Index(value = ["userId"]), Index(value = ["createdAt"])])
data class CompanionConversationEntity(
    @PrimaryKey val conversationId: String,
    val userId: String,
    val message: String,
    val modalities: List<String>,
    val knowledgeSources: List<String>,
    val createdAt: Long,
)

@Entity(tableName = "companion_personality", indices = [Index(value = ["userId"])])
data class CompanionPersonalityEntity(
    @PrimaryKey val profileId: String,
    val userId: String,
    val tone: String,
    val motivationStyle: String,
    val explanationPreference: String,
)

@Entity(tableName = "companion_learning_goals", indices = [Index(value = ["userId"]), Index(value = ["completed"])])
data class CompanionLearningGoalEntity(
    @PrimaryKey val goalId: String,
    val userId: String,
    val title: String,
    val roadmap: List<String>,
    val completed: Boolean,
)

@Entity(tableName = "relationship_history", indices = [Index(value = ["userId"])])
data class RelationshipHistoryEntity(
    @PrimaryKey val relationshipId: String,
    val userId: String,
    val progressSummary: String,
    val interactionHistory: List<String>,
    val goalsAchieved: List<String>,
    val learnsBestBy: String,
)

@Entity(tableName = "companion_recommendations", indices = [Index(value = ["userId"])])
data class CompanionRecommendationEntity(
    @PrimaryKey val recommendationId: String,
    val userId: String,
    val recommendations: List<String>,
    val transparentReason: String,
)

@Entity(tableName = "companion_analytics", indices = [Index(value = ["userId"])])
data class CompanionAnalyticsEntity(
    @PrimaryKey val analyticsId: String,
    val userId: String,
    val motivationPercent: Int,
    val confidence: String,
    val frustrationPercent: Int,
    val engagementPercent: Int,
    val futureRoadmap: List<String>,
)

