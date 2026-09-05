package com.rola.app.ai_research.scientist

enum class ResearchReadiness { Student, Guided, Advanced, PublicationReady }

data class ResearchContext(val userId: String, val domain: String, val question: String, val priorKnowledge: List<String>, val dataSignals: List<String>)
data class ResearchProject(val projectId: String, val title: String, val domain: String, val roadmap: List<String>)
data class ResearchDiscovery(val discoveryId: String, val knowledgeGaps: List<String>, val opportunities: List<String>, val emergingTopics: List<String>)
data class ResearchHypothesisSet(val hypothesisId: String, val questions: List<String>, val hypotheses: List<String>, val possibleSolutions: List<String>)
data class ExperimentPlan(val experimentId: String, val procedures: List<String>, val resources: List<String>, val simulationPlans: List<String>, val expectedResults: List<String>)
data class ResearchAnalysis(val resultId: String, val patterns: List<String>, val relationships: List<String>, val interpretation: String)
data class KnowledgeValidation(val validationId: String, val accuracyScore: Int, val sourceReliability: Int, val approved: Boolean, val reasoning: String)
data class ResearchCollaboration(val collaborationId: String, val participants: List<String>, val sharedProjects: List<String>, val discussionSummary: String)
data class ScientificLearningPackage(val packageId: String, val lessons: List<String>, val tutorials: List<String>, val simulations: List<String>, val projects: List<String>)
data class ResearchMentorPlan(val mentorId: String, val improvements: List<String>, val methodGuidance: List<String>, val researchRoadmap: List<String>)
data class ResearchAnalytics(val analyticsId: String, val discoveryScore: Int, val validationScore: Int, val knowledgeGrowth: Int)

data class AIResearchResult(
    val resultId: String,
    val project: ResearchProject,
    val discovery: ResearchDiscovery,
    val hypotheses: ResearchHypothesisSet,
    val experiment: ExperimentPlan,
    val analysis: ResearchAnalysis,
    val validation: KnowledgeValidation,
    val collaboration: ResearchCollaboration,
    val learningPackage: ScientificLearningPackage,
    val mentorPlan: ResearchMentorPlan,
    val analytics: ResearchAnalytics,
)
