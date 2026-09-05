package com.rola.app.asi_core.intelligence

enum class ASIStakeholder {
    Teacher,
    Researcher,
    Student,
    InstitutionLeader,
}

enum class ASIRiskLevel {
    Low,
    Medium,
    High,
    Critical,
}

enum class ASIApprovalStatus {
    DraftOnly,
    NeedsHumanReview,
    ApprovedForPilot,
    Rejected,
}

enum class ASIDecisionImpact {
    PersonalLesson,
    Assessment,
    Curriculum,
    InstitutionPolicy,
    GlobalKnowledge,
}

data class ASIEducationChallenge(
    val challengeId: String,
    val learnerId: String,
    val institutionId: String,
    val topic: String,
    val problemStatement: String,
    val cognitiveSignals: List<String>,
    val neuralSignals: List<String>,
    val quantumInsights: List<String>,
    val learningHistory: List<Int>,
    val stakeholders: List<ASIStakeholder>,
)

data class ASIProfile(
    val profileId: String,
    val learnerId: String,
    val intelligenceScope: List<String>,
    val personalizationDepthPercent: Int,
    val responsibleAIMode: String,
    val updatedAt: Long = System.currentTimeMillis(),
)

data class ASIModelState(
    val modelId: String,
    val name: String,
    val capabilities: List<String>,
    val safetyBoundary: String,
    val version: String,
)

data class AdvancedReasoningTrace(
    val traceId: String,
    val topic: String,
    val reasoningSteps: List<String>,
    val confidencePercent: Int,
    val explanation: String,
)

data class UniversalKnowledgeMap(
    val mapId: String,
    val topic: String,
    val domainConnections: List<String>,
    val newKnowledgeLinks: List<String>,
    val contentImprovementIdeas: List<String>,
)

data class SelfImprovementLog(
    val logId: String,
    val improvedAreas: List<String>,
    val evaluationSummary: String,
    val requiresOfflineValidation: Boolean,
)

data class CreativeKnowledgeOutput(
    val outputId: String,
    val topic: String,
    val educationalApproaches: List<String>,
    val learningActivities: List<String>,
    val simulations: List<String>,
    val researchDirections: List<String>,
)

data class HumanAICollaborationPlan(
    val planId: String,
    val stakeholders: List<ASIStakeholder>,
    val aiSuggestions: List<String>,
    val requiredApprovals: List<String>,
    val feedbackLoop: String,
)

data class LearningStrategyPlan(
    val strategyId: String,
    val learnerId: String,
    val learningSequence: List<String>,
    val teachingMethod: String,
    val assessmentStyle: String,
    val resourceSelection: List<String>,
    val learningEnvironment: String,
)

data class ASIEducationDecision(
    val decisionId: String,
    val learnerId: String,
    val topic: String,
    val impact: ASIDecisionImpact,
    val action: String,
    val explanation: String,
    val riskLevel: ASIRiskLevel,
)

data class ASIGovernanceRecord(
    val recordId: String,
    val approvalStatus: ASIApprovalStatus,
    val riskLevel: ASIRiskLevel,
    val transparencyNotes: List<String>,
    val ethicsChecks: List<String>,
    val humanOverrideAvailable: Boolean,
)

data class ASIWorldEducationInsight(
    val insightId: String,
    val institutionId: String,
    val globalEducationPatterns: List<String>,
    val knowledgeSharingPlan: String,
    val innovationOpportunities: List<String>,
)

data class ASIResult(
    val resultId: String,
    val profile: ASIProfile,
    val modelState: ASIModelState,
    val reasoningTrace: AdvancedReasoningTrace,
    val knowledgeMap: UniversalKnowledgeMap,
    val selfImprovementLog: SelfImprovementLog,
    val creativeOutput: CreativeKnowledgeOutput,
    val collaborationPlan: HumanAICollaborationPlan,
    val learningStrategy: LearningStrategyPlan,
    val professorResponse: ASIProfessorResponse,
    val decision: ASIEducationDecision,
    val governanceRecord: ASIGovernanceRecord,
    val worldInsight: ASIWorldEducationInsight,
)
