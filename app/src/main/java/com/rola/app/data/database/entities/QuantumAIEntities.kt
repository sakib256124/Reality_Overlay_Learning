package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "quantum_profiles", indices = [Index(value = ["learnerId"])])
data class QuantumProfileEntity(
    @PrimaryKey val profileId: String,
    val learnerId: String,
    val computeMode: String,
    val optimizationReadinessPercent: Int,
    val preferredExplanationStyle: String,
    val activeGoals: List<String>,
    val updatedAt: Long,
)

@Entity(tableName = "quantum_models", indices = [Index(value = ["computeMode"])])
data class QuantumModelEntity(
    @PrimaryKey val modelId: String,
    val name: String,
    val computeMode: String,
    val version: String,
    val optimizationScope: List<String>,
)

@Entity(tableName = "optimization_results", indices = [Index(value = ["learnerId"]), Index(value = ["topic"])])
data class OptimizationResultEntity(
    @PrimaryKey val optimizationId: String,
    val learnerId: String,
    val topic: String,
    val learningOptimizationScore: Int,
    val optimizedPath: List<String>,
    val curriculumSequence: List<String>,
    val assessmentStrategy: String,
    val recommendationStrategy: String,
    val explanation: String,
)

@Entity(tableName = "quantum_decisions", indices = [Index(value = ["learnerId"]), Index(value = ["topic"]), Index(value = ["decisionType"])])
data class QuantumDecisionEntity(
    @PrimaryKey val decisionId: String,
    val learnerId: String,
    val topic: String,
    val decisionType: String,
    val educationalAction: String,
    val confidencePercent: Int,
    val explanation: String,
    val humanControlRequired: Boolean,
)

@Entity(tableName = "learning_optimization_history", indices = [Index(value = ["learnerId"]), Index(value = ["createdAt"])])
data class LearningOptimizationHistoryEntity(
    @PrimaryKey val historyId: String,
    val learnerId: String,
    val topic: String,
    val summary: String,
    val score: Int,
    val createdAt: Long,
)

@Entity(tableName = "quantum_predictions", indices = [Index(value = ["learnerId"]), Index(value = ["topic"])])
data class QuantumPredictionEntity(
    @PrimaryKey val predictionId: String,
    val learnerId: String,
    val topic: String,
    val futurePerformancePercent: Int,
    val skillDevelopment: List<String>,
    val learningChallenges: List<String>,
    val knowledgeRequirements: List<String>,
    val longTermRoadmap: List<String>,
)

@Entity(tableName = "knowledge_discovery_records", indices = [Index(value = ["topic"])])
data class KnowledgeDiscoveryRecordEntity(
    @PrimaryKey val discoveryId: String,
    val topic: String,
    val hiddenRelationships: List<String>,
    val discoveredConcepts: List<String>,
    val scientificSignals: List<String>,
    val expansionRecommendation: String,
)

@Entity(tableName = "quantum_analytics", indices = [Index(value = ["institutionId"])])
data class QuantumAnalyticsEntity(
    @PrimaryKey val reportId: String,
    val institutionId: String,
    val learningOptimizationScore: Int,
    val aiImprovementPercent: Int,
    val predictionAccuracyPercent: Int,
    val systemIntelligenceGrowth: List<String>,
    val auditNotes: List<String>,
)
