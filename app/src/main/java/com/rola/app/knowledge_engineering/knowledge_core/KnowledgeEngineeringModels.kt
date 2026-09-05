package com.rola.app.knowledge_engineering.knowledge_core

enum class KnowledgeConfidence { Low, Medium, High, Verified }

data class KnowledgeEngineeringRequest(val userId: String, val topic: String, val rawKnowledge: List<String>, val studentLevel: String, val learningStyle: String)
data class ExtractedKnowledge(val extractionId: String, val concepts: List<String>, val importantFacts: List<String>, val relationships: List<String>, val classifications: List<String>)
data class StructuredKnowledge(val entityId: String, val definitions: List<String>, val learningMaterials: List<String>, val examples: List<String>, val skills: List<String>)
data class KnowledgeValidation(val validationId: String, val scientificCorrectness: Int, val sourceReliability: Int, val logicalConsistency: Int, val confidence: KnowledgeConfidence, val approved: Boolean)
data class KnowledgeReasoning(val reasoningId: String, val answer: String, val connectedConcepts: List<String>, val explanation: String)
data class KnowledgeOrganization(val mappingId: String, val conceptMappings: List<String>, val learningPathways: List<String>, val semanticIndexes: List<String>)
data class KnowledgeEvolution(val evolutionId: String, val outdatedUpdates: List<String>, val newRelationships: List<String>, val improvedExplanations: List<String>)
data class KnowledgeDelivery(val deliveryId: String, val levelAdjustedExplanation: String, val resources: List<String>, val personalizationBasis: List<String>)
data class KnowledgeAnalytics(val analyticsId: String, val knowledgeGrowth: Int, val validationScore: Int, val learningImpact: Int)

data class KnowledgeEngineeringResult(
    val resultId: String,
    val extracted: ExtractedKnowledge,
    val structured: StructuredKnowledge,
    val validation: KnowledgeValidation,
    val reasoning: KnowledgeReasoning,
    val organization: KnowledgeOrganization,
    val evolution: KnowledgeEvolution,
    val delivery: KnowledgeDelivery,
    val analytics: KnowledgeAnalytics,
)
