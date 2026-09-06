package com.rola.app.knowledge_discovery_ai.discovery_core

enum class DiscoverySourceType { ResearchPaper, EducationalResource, ScientificDatabase, DigitalLibrary, KnowledgeNetwork }
enum class DiscoveryStatus { Scanning, Validating, Expanded, NeedsHumanApproval }

data class KnowledgeDiscoveryRequest(
    val learnerId: String,
    val domain: String,
    val existingKnowledge: List<String>,
    val globalSources: List<DiscoverySourceType>,
    val researchQuestion: String,
)

data class GlobalKnowledgeScan(val scanId: String, val sources: List<String>, val extractedConcepts: List<String>, val reliabilitySignals: List<String>, val educationalIntegration: List<String>)
data class KnowledgeDiscovery(val discoveryId: String, val newConcepts: List<String>, val emergingTechnologies: List<String>, val researchTrends: List<String>, val knowledgeGaps: List<String>, val learningOpportunities: List<String>)
data class KnowledgeRelationshipMap(val mapId: String, val conceptConnections: List<String>, val crossDomainRelationships: List<String>, val hiddenPatterns: List<String>, val scientificRelationships: List<String>)
data class ResearchOpportunityReport(val opportunityId: String, val unsolvedProblems: List<String>, val researchGaps: List<String>, val futureTopics: List<String>, val innovationOpportunities: List<String>)
data class DiscoveryValidationReport(val validationId: String, val accuracyScore: Int, val reliabilityScore: Int, val evidenceQuality: Int, val educationalUsefulness: Int, val humanApprovalRequired: Boolean)
data class GlobalIntelligenceNetworkState(val networkId: String, val universities: List<String>, val researchers: List<String>, val aiSystems: List<String>, val knowledgeDatabases: List<String>, val learningCommunities: List<String>)
data class FutureKnowledgeModel(val modelId: String, val futureTechnologies: List<String>, val futureSkills: List<String>, val futureResearchAreas: List<String>, val futureEducationNeeds: List<String>)
data class DiscoveryLearningPackage(val packageId: String, val lessons: List<String>, val courses: List<String>, val researchProjects: List<String>, val learningActivities: List<String>)
data class DiscoveryIntelligenceSummary(val summaryId: String, val knowledgeGrowth: Int, val analysis: List<String>, val auditTrail: List<String>, val trustStatus: String)
data class KnowledgeDiscoveryResult(
    val resultId: String,
    val scan: GlobalKnowledgeScan,
    val discovery: KnowledgeDiscovery,
    val relationships: KnowledgeRelationshipMap,
    val opportunities: ResearchOpportunityReport,
    val validation: DiscoveryValidationReport,
    val network: GlobalIntelligenceNetworkState,
    val prediction: FutureKnowledgeModel,
    val learningPackage: DiscoveryLearningPackage,
    val intelligence: DiscoveryIntelligenceSummary,
    val status: DiscoveryStatus,
)
