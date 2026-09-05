package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "predictive_learning_predictions", indices = [Index(value = ["userId"])])
data class PredictiveLearningPredictionEntity(@PrimaryKey val predictionId: String, val userId: String, val futurePerformance: String, val challenges: List<String>, val knowledgeGaps: List<String>, val confidenceScore: Int)

@Entity(tableName = "future_skill_models", indices = [Index(value = ["modelId"])])
data class FutureSkillModelEntity(@PrimaryKey val modelId: String, val futureSkills: List<String>, val technologyRequirements: List<String>, val emergingAreas: List<String>)

@Entity(tableName = "potential_profiles", indices = [Index(value = ["researchPotential"])])
data class PotentialProfileEntity(@PrimaryKey val profileId: String, val strengths: List<String>, val creativityScore: Int, val researchPotential: String, val suggestedPath: String)

@Entity(tableName = "growth_analytics", indices = [Index(value = ["growthScore"])])
data class GrowthAnalyticsEntity(@PrimaryKey val analyticsId: String, val predictionAccuracy: Int, val growthScore: Int, val privacyProtected: Boolean)

@Entity(tableName = "future_roadmaps", indices = [Index(value = ["roadmapId"])])
data class FutureRoadmapEntity(@PrimaryKey val roadmapId: String, val longTermPlan: List<String>, val skillRoadmap: List<String>, val researchRoadmap: List<String>, val careerStrategy: String)

@Entity(tableName = "trend_analysis", indices = [Index(value = ["reportId"])])
data class TrendAnalysisEntity(@PrimaryKey val reportId: String, val scientificTrends: List<String>, val technologyChanges: List<String>, val educationDemands: List<String>)

@Entity(tableName = "prediction_history", indices = [Index(value = ["simulationId"])])
data class PredictionHistoryEntity(@PrimaryKey val simulationId: String, val currentPathOutcome: String, val optimizedPathOutcome: String, val futureSuccessProbability: Int)

@Entity(tableName = "predictive_optimization_results", indices = [Index(value = ["optimizationId"])])
data class PredictiveOptimizationResultEntity(@PrimaryKey val optimizationId: String, val studyStrategy: String, val practiceFrequency: String, val resourceSelection: List<String>, val difficultyLevel: String)
