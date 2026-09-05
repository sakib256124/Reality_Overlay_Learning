package com.rola.app.self_evolving_ai.evolution_core

enum class EvolutionStatus { Monitoring, Proposing, Validating, ApprovedForDeployment, NeedsHumanApproval }
enum class ModelDeploymentStage { Candidate, Testing, Evaluated, Deployed, RolledBack }

data class SelfEvolutionRequest(
    val systemId: String,
    val aiResponseQuality: Int,
    val teachingEffectiveness: Int,
    val recommendationAccuracy: Int,
    val learningOutcomes: Int,
    val userSatisfaction: Int,
    val systemPerformance: Int,
    val feedback: List<String>,
)

data class AIPerformanceReport(val reportId: String, val responseQuality: Int, val teachingEffectiveness: Int, val recommendationAccuracy: Int, val learningOutcomes: Int, val userSatisfaction: Int, val systemPerformance: Int, val weaknesses: List<String>)
data class ImprovementAction(val actionId: String, val teachingStrategyImprovement: String, val recommendationImprovement: String, val workflowImprovement: String, val knowledgeDeliveryImprovement: String, val personalizationAccuracy: Int, val validationRequired: Boolean)
data class LearningOptimizationResult(val optimizationId: String, val learningPathOptimization: String, val contentDeliveryOptimization: String, val assessmentOptimization: String, val difficultyAdjustment: String, val engagementStrategy: String, val qualityScore: Int)
data class ModelEvolutionRecord(val modelVersionId: String, val previousVersion: String, val newVersion: String, val performanceComparison: String, val deploymentStage: ModelDeploymentStage, val rollbackSupported: Boolean)
data class FeedbackLearningRecord(val feedbackId: String, val studentFeedback: List<String>, val teacherFeedback: List<String>, val aiPerformanceFeedback: List<String>, val learningResults: List<String>, val behaviorImprovement: String)
data class EvolutionExperimentResult(val experimentId: String, val strategyA: String, val strategyB: String, val winningStrategy: String, val improvementScore: Int)
data class AdaptiveEducationEvolution(val evolutionId: String, val workflowUpdate: String, val coordinationUpdate: String, val resourceSelectionUpdate: String, val studentExperienceUpdate: String)
data class EvolutionMemory(val memoryId: String, val previousImprovements: List<String>, val successfulStrategies: List<String>, val failedExperiments: List<String>, val evolutionHistory: List<String>)
data class EvolutionGovernanceRecord(val governanceId: String, val humanApprovalRequired: Boolean, val safetyLimits: List<String>, val rollbackCapability: Boolean, val auditHistory: List<String>)
data class SelfEvolutionResult(
    val resultId: String,
    val performanceReport: AIPerformanceReport,
    val improvementAction: ImprovementAction,
    val learningOptimization: LearningOptimizationResult,
    val modelEvolution: ModelEvolutionRecord,
    val feedbackLearning: FeedbackLearningRecord,
    val experiment: EvolutionExperimentResult,
    val adaptiveEducation: AdaptiveEducationEvolution,
    val memory: EvolutionMemory,
    val governance: EvolutionGovernanceRecord,
    val status: EvolutionStatus,
)
