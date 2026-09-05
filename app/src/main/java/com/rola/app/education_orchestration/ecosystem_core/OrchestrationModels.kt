package com.rola.app.education_orchestration.ecosystem_core

enum class OrchestrationStatus { Planning, Coordinating, Optimized, NeedsHumanOverride }
enum class EducationAIService { TeacherAI, TutorAI, CompanionAI, AGI, ASI, ResearchAI, KnowledgeAI, EmotionalAI, PredictiveAI, MasteryAI, PlanningAI }

data class OrchestrationRequest(
    val userId: String,
    val requirement: String,
    val learningState: String,
    val activeSystems: List<EducationAIService>,
    val userFeedback: Int,
    val systemHealth: Int,
)

data class EducationOrchestrationPlan(val orchestrationId: String, val selectedCapabilities: List<EducationAIService>, val coordinationPlan: List<String>, val learningResult: String)
data class AIServiceState(val serviceId: String, val registeredAgents: List<String>, val activeServices: List<EducationAIService>, val communicationChannels: List<String>, val resourceAllocation: String, val performanceScore: Int)
data class LearningWorkflow(val workflowId: String, val lifecycleSteps: List<String>, val integratedSystems: List<EducationAIService>, val currentStage: String)
data class EcosystemOptimization(val optimizationId: String, val resourceUsageScore: Int, val learningQualityScore: Int, val performanceScore: Int, val userExperienceScore: Int, val outcomeScore: Int, val recommendations: List<String>)
data class EducationIntelligenceReport(val reportId: String, val monitoredServices: List<String>, val learningProgress: Int, val knowledgeGrowth: Int, val userEngagement: Int, val systemHealth: Int)
data class EducationDecision(val decisionId: String, val agentToUse: String, val learningStrategy: String, val resourceToProvide: String, val adaptationRequired: Boolean, val transparency: String)
data class AgentCoordinationPlan(val coordinationId: String, val teachingAgents: List<String>, val researchAgents: List<String>, val companionAgents: List<String>, val knowledgeAgents: List<String>, val assessmentAgents: List<String>, val conflictResolution: String)
data class EducationQualityScore(val qualityId: String, val learningEffectiveness: Int, val aiResponseQuality: Int, val contentAccuracy: Int, val userSatisfaction: Int, val overallScore: Int)
data class GovernanceRecord(val governanceId: String, val permissionControl: String, val serviceAuthentication: String, val humanOverride: Boolean, val auditSummary: String)
data class AutonomousEducationResult(
    val resultId: String,
    val orchestrationPlan: EducationOrchestrationPlan,
    val serviceState: AIServiceState,
    val workflow: LearningWorkflow,
    val optimization: EcosystemOptimization,
    val monitoringReport: EducationIntelligenceReport,
    val decision: EducationDecision,
    val agentCoordination: AgentCoordinationPlan,
    val qualityScore: EducationQualityScore,
    val governance: GovernanceRecord,
    val status: OrchestrationStatus,
)
