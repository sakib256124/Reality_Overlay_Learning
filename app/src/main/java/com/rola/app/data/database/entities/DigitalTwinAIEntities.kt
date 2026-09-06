package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "ai_digital_twins", indices = [Index(value = ["domain"]), Index(value = ["status"])])
data class AIDigitalTwinEntity(@PrimaryKey val twinId: String, val objectName: String, val domain: String, val status: String, val safetyNotes: List<String>)
@Entity(tableName = "twin_models", indices = [Index(value = ["modelType"])])
data class TwinModelEntity(@PrimaryKey val modelId: String, val modelType: String, val components: List<String>, val interactiveFeatures: List<String>, val visualizationPlan: String)
@Entity(tableName = "twin_simulation_history", indices = [Index(value = ["simulationId"])])
data class TwinSimulationHistoryEntity(@PrimaryKey val simulationId: String, val scenarios: List<String>, val predictions: List<String>, val visualizations: List<String>, val interactiveLearningTasks: List<String>)
@Entity(tableName = "twin_real_world_data", indices = [Index(value = ["privacyProtected"])])
data class TwinRealWorldDataEntity(@PrimaryKey val syncId: String, val sensorInputs: List<String>, val iotDevices: List<String>, val arScanUpdates: List<String>, val externalDataSources: List<String>, val privacyProtected: Boolean)
@Entity(tableName = "twin_analytics", indices = [Index(value = ["confidenceScore"])])
data class TwinAnalyticsEntity(@PrimaryKey val reportId: String, val behaviorPatterns: List<String>, val performanceInsights: List<String>, val possibleOutcomes: List<String>, val confidenceScore: Int)
@Entity(tableName = "twin_experiment_results", indices = [Index(value = ["sessionId"])])
data class TwinExperimentResultEntity(@PrimaryKey val experimentId: String, val sessionId: String, val guidedExperiments: List<String>, val sharedExperiments: List<String>, val safetyApproved: Boolean)
@Entity(tableName = "twin_learning_sessions", indices = [Index(value = ["sessionId"])])
data class TwinLearningSessionEntity(@PrimaryKey val sessionId: String, val explanations: List<String>, val guidedExperiments: List<String>, val conceptDemonstrations: List<String>, val skillDevelopmentTasks: List<String>)
@Entity(tableName = "twin_prediction_records", indices = [Index(value = ["predictionId"])])
data class TwinPredictionRecordEntity(@PrimaryKey val predictionId: String, val failurePredictions: List<String>, val futureBehavior: List<String>, val performanceChanges: List<String>, val experimentalOutcomes: List<String>)
