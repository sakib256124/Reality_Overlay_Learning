package com.rola.app.domain.model

data class AGILearningEvent(
    val eventId: String,
    val learnerId: String,
    val activityType: AGIActivityType,
    val topic: String,
    val signal: String,
    val score: Int? = null,
    val timestamp: Long = System.currentTimeMillis(),
)

enum class AGIActivityType {
    ObjectScan,
    TutorQuestion,
    QuizAttempt,
    ResearchRead,
    ARExperiment,
    LessonComplete,
    GoalReview,
}

data class AGIOrchestrationRequest(
    val learnerId: String,
    val institutionId: String,
    val objective: String,
    val event: AGILearningEvent,
    val privacyMode: AGIPrivacyMode = AGIPrivacyMode.Standard,
)

data class AGIOrchestrationResult(
    val decisionId: String,
    val selectedAgents: List<AGIAgentRole>,
    val reasoningTrace: List<String>,
    val learningAction: AGILearningAction,
    val safetyReport: AISafetyReport,
    val cognitiveReport: CognitiveLearningReport,
    val recommendations: List<FutureRecommendation>,
)

enum class AGIAgentRole {
    TeachingAgent,
    ResearchAgent,
    AssessmentAgent,
    RecommendationAgent,
    KnowledgeExpansionAgent,
    PersonalMentorAgent,
    AnalyticsAgent,
}

data class AGIAgentMessage(
    val messageId: String,
    val agent: AGIAgentRole,
    val learnerId: String,
    val topic: String,
    val intent: String,
    val evidence: List<String>,
    val confidence: Float,
)

data class AGILearningAction(
    val actionId: String,
    val title: String,
    val description: String,
    val agentRole: AGIAgentRole,
    val actionType: AGILearningActionType,
    val priority: RecommendationPriority,
)

enum class AGILearningActionType {
    TeachConcept,
    GenerateAssessment,
    RecommendPractice,
    ExpandKnowledge,
    UpdateCurriculum,
    MentorCheckIn,
    RunSimulation,
}

data class CognitiveLearningReport(
    val reportId: String,
    val learnerId: String,
    val learningSpeed: LearningSpeed,
    val memoryRetentionScore: Int,
    val conceptDifficulty: SkillLevel,
    val engagementPattern: String,
    val skillMap: List<SkillGraphNode>,
    val futurePrediction: String,
    val generatedAt: Long = System.currentTimeMillis(),
)

data class SkillGraphNode(
    val skillId: String,
    val name: String,
    val mastery: Int,
    val prerequisites: List<String> = emptyList(),
)

data class LearningGoal(
    val goalId: String,
    val learnerId: String,
    val title: String,
    val targetSkill: String,
    val targetMastery: Int,
    val progress: Int,
    val dueAt: Long? = null,
)

data class FutureRecommendation(
    val recommendationId: String,
    val learnerId: String,
    val title: String,
    val rationale: String,
    val priority: RecommendationPriority,
    val generatedAt: Long = System.currentTimeMillis(),
)

data class KnowledgeEvolutionProposal(
    val proposalId: String,
    val institutionId: String,
    val topic: String,
    val missingConcepts: List<String>,
    val curriculumUpdates: List<String>,
    val requiredApprovalRole: TeacherPermission = TeacherPermission.ApproveContent,
    val status: TeacherApprovalStatus = TeacherApprovalStatus.PendingReview,
)

data class SimulationLearningScenario(
    val scenarioId: String,
    val title: String,
    val scenarioType: SimulationScenarioType,
    val topic: String,
    val instructions: List<String>,
    val expectedLearningSignals: List<String>,
)

enum class SimulationScenarioType {
    VirtualExperiment,
    ARLaboratory,
    ScientificSimulation,
    InteractiveScenario,
}

data class AISafetyReport(
    val reportId: String,
    val verified: Boolean,
    val educationalAccuracy: Float,
    val biasRisk: Float,
    val privacyRisk: Float,
    val requiresHumanApproval: Boolean,
    val notes: List<String>,
)

enum class AGIPrivacyMode {
    Strict,
    Standard,
    ResearchAllowed,
}

data class AGIAccessContext(
    val userId: String,
    val institutionId: String,
    val permissions: Set<AGIPermission>,
    val privacyMode: AGIPrivacyMode = AGIPrivacyMode.Standard,
)

enum class AGIPermission {
    RunAgents,
    ViewLearnerModels,
    EvolveCurriculum,
    ApproveAIDecisions,
    ViewKnowledgeNetwork,
}

data class AGIDashboardState(
    val activeAgents: List<AGIAgentRole>,
    val recentDecisions: List<AGIOrchestrationResult>,
    val cognitiveReports: List<CognitiveLearningReport>,
    val curriculumEvolution: List<KnowledgeEvolutionProposal>,
    val knowledgeGrowth: List<String>,
    val safetyAlerts: List<String>,
)
