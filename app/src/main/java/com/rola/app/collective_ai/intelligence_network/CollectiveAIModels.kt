package com.rola.app.collective_ai.intelligence_network

enum class CollectiveAgentType {
    AITeacher,
    AITutor,
    Research,
    Knowledge,
    Assessment,
    Cognitive,
    RobotTeaching,
    SpatialLearning,
    CompanionAI,
}

enum class CollectiveAgentStatus {
    Active,
    Collaborating,
    Reviewing,
}

enum class ConsensusOutcome {
    StrongConsensus,
    PartialConsensus,
    NeedsHumanReview,
}

data class CollectiveAIRequest(
    val userId: String,
    val problem: String,
    val topic: String,
    val learnerSignals: List<String>,
    val humanFeedback: String? = null,
)

data class AIAgentNode(
    val agentId: String,
    val name: String,
    val agentType: CollectiveAgentType,
    val capability: String,
    val status: CollectiveAgentStatus,
)

data class AgentRelationship(
    val relationshipId: String,
    val fromAgentId: String,
    val toAgentId: String,
    val relationshipType: String,
    val trustScore: Int,
)

data class AgentTask(
    val taskId: String,
    val agentId: String,
    val taskType: String,
    val priority: Int,
    val output: String,
)

data class KnowledgeExchange(
    val exchangeId: String,
    val sourceAgentId: String,
    val targetAgentId: String,
    val topic: String,
    val knowledgeSummary: String,
    val sources: List<String>,
)

data class AgentCommunication(
    val communicationId: String,
    val senderAgentId: String,
    val receiverAgentId: String,
    val message: String,
    val createdAt: Long = System.currentTimeMillis(),
)

data class AgentAnalysis(
    val agentId: String,
    val perspective: String,
    val evidence: List<String>,
    val recommendation: String,
    val confidencePercent: Int,
)

data class DebateRound(
    val debateId: String,
    val perspectives: List<String>,
    val verifiedFacts: List<String>,
    val comparisonSummary: String,
)

data class AIConsensusRecord(
    val consensusId: String,
    val outcome: ConsensusOutcome,
    val selectedStrategy: String,
    val rankedStrategies: List<String>,
    val accuracyScore: Int,
    val explanation: String,
)

data class CollectiveDecision(
    val decisionId: String,
    val finalEducationalAction: String,
    val reasoningTrace: List<String>,
    val humanReviewRequired: Boolean,
)

data class HumanFeedbackSignal(
    val feedbackId: String,
    val userId: String,
    val role: String,
    val feedback: String,
    val validationScore: Int,
)

data class CollectiveLearningResult(
    val resultId: String,
    val improvedStrategies: List<String>,
    val curriculumUpdates: List<String>,
    val assessmentImprovements: List<String>,
    val recommendationUpdates: List<String>,
)

data class CollaborationAnalytics(
    val analyticsId: String,
    val activeAgents: Int,
    val communicationCount: Int,
    val consensusScore: Int,
    val improvementScore: Int,
)

data class CollectiveGovernanceState(
    val governanceId: String,
    val authenticatedAgents: Int,
    val permissions: List<String>,
    val auditEntries: List<String>,
    val transparencyNotes: List<String>,
)

data class GlobalResearchNetworkState(
    val networkId: String,
    val participants: List<String>,
    val discoveries: List<String>,
    val collaborationFocus: String,
)

data class CollectiveAIResult(
    val resultId: String,
    val agents: List<AIAgentNode>,
    val relationships: List<AgentRelationship>,
    val tasks: List<AgentTask>,
    val knowledgeExchanges: List<KnowledgeExchange>,
    val communications: List<AgentCommunication>,
    val analyses: List<AgentAnalysis>,
    val debate: DebateRound,
    val consensus: AIConsensusRecord,
    val decision: CollectiveDecision,
    val humanFeedback: HumanFeedbackSignal,
    val learningResult: CollectiveLearningResult,
    val governance: CollectiveGovernanceState,
    val researchNetwork: GlobalResearchNetworkState,
    val analytics: CollaborationAnalytics,
)
