package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "self_ai_evolution_history", indices = [Index(value = ["status"])])
data class SelfEvolutionHistoryEntity(@PrimaryKey val resultId: String, val reportId: String, val actionId: String, val modelVersionId: String, val status: String, val humanApprovalRequired: Boolean, val rollbackCapability: Boolean)
@Entity(tableName = "performance_metrics", indices = [Index(value = ["learningOutcomes"])])
data class PerformanceMetricEntity(@PrimaryKey val reportId: String, val responseQuality: Int, val teachingEffectiveness: Int, val recommendationAccuracy: Int, val learningOutcomes: Int, val userSatisfaction: Int, val systemPerformance: Int, val weaknesses: List<String>)
@Entity(tableName = "improvement_actions", indices = [Index(value = ["personalizationAccuracy"])])
data class ImprovementActionEntity(@PrimaryKey val actionId: String, val teachingStrategyImprovement: String, val recommendationImprovement: String, val workflowImprovement: String, val knowledgeDeliveryImprovement: String, val personalizationAccuracy: Int, val validationRequired: Boolean)
@Entity(tableName = "model_versions", indices = [Index(value = ["deploymentStage"])])
data class ModelVersionEntity(@PrimaryKey val modelVersionId: String, val previousVersion: String, val newVersion: String, val performanceComparison: String, val deploymentStage: String, val rollbackSupported: Boolean)
@Entity(tableName = "feedback_records", indices = [Index(value = ["feedbackId"])])
data class FeedbackRecordEntity(@PrimaryKey val feedbackId: String, val studentFeedback: List<String>, val teacherFeedback: List<String>, val aiPerformanceFeedback: List<String>, val learningResults: List<String>, val behaviorImprovement: String)
@Entity(tableName = "self_optimization_results", indices = [Index(value = ["qualityScore"])])
data class SelfOptimizationResultEntity(@PrimaryKey val optimizationId: String, val learningPathOptimization: String, val contentDeliveryOptimization: String, val assessmentOptimization: String, val difficultyAdjustment: String, val engagementStrategy: String, val qualityScore: Int)
@Entity(tableName = "evolution_experiments", indices = [Index(value = ["improvementScore"])])
data class EvolutionExperimentEntity(@PrimaryKey val experimentId: String, val strategyA: String, val strategyB: String, val winningStrategy: String, val improvementScore: Int)
@Entity(tableName = "system_growth_analytics", indices = [Index(value = ["improvementScore"])])
data class SystemGrowthAnalyticsEntity(@PrimaryKey val analyticsId: String, val previousImprovements: List<String>, val successfulStrategies: List<String>, val failedExperiments: List<String>, val evolutionHistory: List<String>, val workflowUpdate: String, val improvementScore: Int)
