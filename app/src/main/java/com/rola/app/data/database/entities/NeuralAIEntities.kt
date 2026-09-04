package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "neural_profiles", indices = [Index(value = ["userId"])])
data class NeuralProfileEntity(
    @PrimaryKey val profileId: String,
    val userId: String,
    val cognitivePatterns: List<String>,
    val attentionBehavior: String,
    val memoryResponse: String,
    val learningSpeed: String,
    val preferredTeachingMethod: String,
    val knowledgeDevelopment: List<String>,
    val consentGranted: Boolean,
    val updatedAt: Long,
)

@Entity(tableName = "brain_signals", indices = [Index(value = ["userId"]), Index(value = ["sessionId"]), Index(value = ["timestamp"])])
data class BrainSignalEntity(
    @PrimaryKey val signalId: String,
    val userId: String,
    val sessionId: String,
    val attentionScore: Float,
    val engagementScore: Float,
    val mentalWorkloadScore: Float,
    val fatigueScore: Float,
    val signalQuality: String,
    val timestamp: Long,
)

@Entity(tableName = "neural_cognitive_states", indices = [Index(value = ["userId"]), Index(value = ["topic"]), Index(value = ["timestamp"])])
data class NeuralCognitiveStateEntity(
    @PrimaryKey val stateId: String,
    val userId: String,
    val topic: String,
    val attentionPercent: Int,
    val engagementPercent: Int,
    val focusLevel: String,
    val cognitiveLoadLevel: String,
    val understandingLevel: String,
    val mentalFatiguePercent: Int,
    val explanation: String,
    val timestamp: Long,
)

@Entity(tableName = "neural_learning_states", indices = [Index(value = ["userId"]), Index(value = ["activeTopic"])])
data class NeuralLearningStateEntity(
    @PrimaryKey val stateId: String,
    val userId: String,
    val activeTopic: String,
    val recommendedDifficulty: Int,
    val recommendedMethod: String,
    val supportRequired: Boolean,
    val adaptationReason: String,
    val updatedAt: Long,
)

@Entity(tableName = "neural_interactions", indices = [Index(value = ["userId"]), Index(value = ["topic"]), Index(value = ["timestamp"])])
data class NeuralInteractionEntity(
    @PrimaryKey val responseId: String,
    val userId: String,
    val topic: String,
    val actions: List<String>,
    val teacherPrompt: String,
    val explainableReason: String,
    val timestamp: Long,
)

@Entity(tableName = "attention_records", indices = [Index(value = ["userId"]), Index(value = ["topic"]), Index(value = ["timestamp"])])
data class AttentionRecordEntity(
    @PrimaryKey val recordId: String,
    val userId: String,
    val topic: String,
    val attentionPercent: Int,
    val engagementPercent: Int,
    val focusLevel: String,
    val timestamp: Long,
)

@Entity(tableName = "neural_learning_predictions", indices = [Index(value = ["userId"]), Index(value = ["topic"])])
data class NeuralLearningPredictionEntity(
    @PrimaryKey val predictionId: String,
    val userId: String,
    val topic: String,
    val learningSuccessPercent: Int,
    val requiredSupport: List<String>,
    val skillDevelopment: List<String>,
    val knowledgeRetentionPercent: Int,
    val longTermRoadmap: List<String>,
    val explanation: String,
)

@Entity(tableName = "cognitive_reports", indices = [Index(value = ["userId"]), Index(value = ["createdAt"])])
data class CognitiveReportEntity(
    @PrimaryKey val reportId: String,
    val userId: String,
    val summary: String,
    val recommendations: List<String>,
    val privacyMode: String,
    val createdAt: Long,
)
