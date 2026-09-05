package com.rola.app.creative_ai.creativity_engine

enum class CreativeLevel { Beginner, Intermediate, Advanced, Research }

data class CreativeAIRequest(val userId: String, val subject: String, val topic: String, val level: CreativeLevel, val objective: String, val humanIdea: String?)
data class CreativeContentPackage(val contentId: String, val lessons: List<String>, val examples: List<String>, val activities: List<String>, val practiceMaterials: List<String>)
data class CreativeKnowledge(val knowledgeId: String, val explanations: List<String>, val analogies: List<String>, val conceptConnections: List<String>)
data class CreativeInnovation(val innovationId: String, val teachingMethods: List<String>, val technologies: List<String>, val classroomStrategies: List<String>, val humanValidationRequired: Boolean)
data class ResearchIdeaSet(val researchId: String, val topics: List<String>, val hypotheses: List<String>, val experiments: List<String>, val futureDirections: List<String>)
data class CreativeSimulation(val simulationId: String, val virtualExperiments: List<String>, val arActivities: List<String>, val digitalTwinScenarios: List<String>)
data class CreativePersonalization(val personalizationId: String, val examples: List<String>, val projects: List<String>, val challenges: List<String>)
data class HumanAICreativeProject(val projectId: String, val humanContribution: String, val aiEnhancement: String, val solution: String)
data class CreativeEvaluation(val evaluationId: String, val accuracyScore: Int, val creativityScore: Int, val learningEffectiveness: Int, val safeForLearners: Boolean, val explanation: String)

data class CreativeAIResult(
    val resultId: String,
    val content: CreativeContentPackage,
    val knowledge: CreativeKnowledge,
    val innovation: CreativeInnovation,
    val research: ResearchIdeaSet,
    val simulation: CreativeSimulation,
    val personalization: CreativePersonalization,
    val collaboration: HumanAICreativeProject,
    val evaluation: CreativeEvaluation,
)
