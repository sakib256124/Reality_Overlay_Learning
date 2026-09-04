package com.rola.app.agi_network.intelligence

enum class AGINetworkAgentRole {
    AITeacherAgent,
    AITutorAgent,
    ResearchAgent,
    KnowledgeAgent,
    AssessmentAgent,
    AnalyticsAgent,
    RobotTeachingAgent,
    CognitiveLearningAgent,
}

enum class AgentTaskStatus {
    Queued,
    Running,
    NeedsHumanApproval,
    Completed,
    Blocked,
}

enum class AGINetworkPermission {
    RunAgents,
    ImproveModels,
    ProposeCurriculum,
    ApprovePublication,
    ViewAnalytics,
    OverrideAgents,
}

enum class GovernanceDecision {
    ApprovedForDraft,
    NeedsHumanReview,
    Rejected,
}

data class AGINetworkAccessContext(
    val userId: String,
    val institutionId: String,
    val permissions: Set<AGINetworkPermission>,
    val humanSupervisorId: String?,
)

data class EducationNetworkSignal(
    val signalId: String,
    val learnerId: String,
    val institutionId: String,
    val topic: String,
    val activityType: String,
    val learningOutcomeScore: Int?,
    val contentQualityScore: Int?,
    val researchEvidence: List<String>,
    val timestamp: Long = System.currentTimeMillis(),
)

data class AGIAgentProfile(
    val agentId: String,
    val role: AGINetworkAgentRole,
    val name: String,
    val capabilities: List<String>,
    val active: Boolean = true,
)

data class AGIAgentTask(
    val taskId: String,
    val agentRole: AGINetworkAgentRole,
    val title: String,
    val priority: Int,
    val status: AgentTaskStatus,
    val evidence: List<String>,
)

data class AgentCommunicationMessage(
    val messageId: String,
    val fromAgent: AGINetworkAgentRole,
    val toAgent: AGINetworkAgentRole,
    val topic: String,
    val content: String,
    val confidence: Float,
)

data class AgentCollaborationPlan(
    val planId: String,
    val selectedAgents: List<AGINetworkAgentRole>,
    val tasks: List<AGIAgentTask>,
    val messages: List<AgentCommunicationMessage>,
    val conflictResolution: String,
)

data class SelfLearningEvaluation(
    val evaluationId: String,
    val teachingImprovement: String,
    val recommendationAccuracyPercent: Int,
    val questionQualityPercent: Int,
    val contentQualityPercent: Int,
    val improvementTargets: List<String>,
)

data class AIImprovementPlan(
    val planId: String,
    val modelAreas: List<String>,
    val strategyUpdates: List<String>,
    val requiresOfflineEvaluation: Boolean,
    val explanation: String,
)

data class AGIKnowledgeEvolutionProposal(
    val proposalId: String,
    val topic: String,
    val missingConcepts: List<String>,
    val improvedRelationships: List<String>,
    val materialUpdates: List<String>,
    val verificationEvidence: List<String>,
)

data class AGIEducationalDecision(
    val decisionId: String,
    val learnerId: String,
    val topic: String,
    val teachingApproach: String,
    val requiredContent: List<String>,
    val difficultyAdjustment: String,
    val learningEnvironment: String,
    val assessmentStrategy: String,
    val explanation: String,
)

data class CurriculumEvolutionPlan(
    val planId: String,
    val topic: String,
    val missingSkills: List<String>,
    val generatedCourses: List<String>,
    val updateRecommendations: List<String>,
    val approvalRequired: Boolean,
)

data class AGIEducationIntelligenceReport(
    val reportId: String,
    val institutionId: String,
    val globalPatterns: List<String>,
    val aiPerformancePercent: Int,
    val studentSuccessPercent: Int,
    val knowledgeGrowth: List<String>,
    val recommendations: List<String>,
)

data class AGIGovernanceRecord(
    val recordId: String,
    val decision: GovernanceDecision,
    val humanApprovalRequired: Boolean,
    val transparencyNotes: List<String>,
    val safetyRules: List<String>,
    val auditSummary: String,
)

data class AGINetworkResult(
    val resultId: String,
    val collaborationPlan: AgentCollaborationPlan,
    val selfLearningEvaluation: SelfLearningEvaluation,
    val improvementPlan: AIImprovementPlan,
    val knowledgeEvolution: AGIKnowledgeEvolutionProposal,
    val educationalDecision: AGIEducationalDecision,
    val curriculumEvolution: CurriculumEvolutionPlan,
    val analyticsReport: AGIEducationIntelligenceReport,
    val governanceRecord: AGIGovernanceRecord,
)
