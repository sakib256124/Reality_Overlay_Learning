package com.rola.app.ai_civilization.intelligence

enum class CivilizationLearningLevel { Beginner, Intermediate, Advanced, Expert, Research }

data class CivilizationContext(val userId: String, val topic: String, val level: CivilizationLearningLevel, val signals: List<String>)
data class AICivilizationState(val civilizationId: String, val globalEducationIntelligence: String, val participants: List<String>, val coordinationModel: String)
data class KnowledgeEvolutionState(val evolutionId: String, val missingKnowledge: List<String>, val validationSummary: String, val graphExpansion: List<String>)
data class LearningEvolutionState(val evolutionId: String, val learningPathUpdates: List<String>, val curriculumUpdates: List<String>, val recommendationUpdates: List<String>)
data class InnovationRecord(val innovationId: String, val technologies: List<String>, val methods: List<String>, val researchDirections: List<String>, val humanApproved: Boolean)
data class FutureEducationPlan(val planId: String, val futureSkills: List<String>, val futureSubjects: List<String>, val trends: List<String>, val roadmap: List<String>)
data class KnowledgeCivilizationConnection(val connectionId: String, val connectedSources: List<String>, val collaborationSummary: String)
data class CivilizationGovernanceState(val logId: String, val policies: List<String>, val auditTrail: List<String>, val approvalRequired: Boolean)
data class CivilizationAnalytics(val analyticsId: String, val intelligenceScore: Int, val evolutionScore: Int, val innovationScore: Int)

data class CivilizationResult(
    val resultId: String,
    val civilization: AICivilizationState,
    val knowledgeEvolution: KnowledgeEvolutionState,
    val learningEvolution: LearningEvolutionState,
    val innovation: InnovationRecord,
    val futurePlan: FutureEducationPlan,
    val knowledgeNetwork: KnowledgeCivilizationConnection,
    val governance: CivilizationGovernanceState,
    val educatorResponse: String,
    val analytics: CivilizationAnalytics,
)
