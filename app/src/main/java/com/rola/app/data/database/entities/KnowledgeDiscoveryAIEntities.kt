package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "knowledge_discovery_discoveries", indices = [Index(value = ["status"])])
data class KnowledgeDiscoveryEntity(@PrimaryKey val discoveryId: String, val newConcepts: List<String>, val emergingTechnologies: List<String>, val researchTrends: List<String>, val knowledgeGaps: List<String>, val learningOpportunities: List<String>, val status: String)
@Entity(tableName = "knowledge_discovery_global_sources", indices = [Index(value = ["scanId"])])
data class GlobalKnowledgeSourceEntity(@PrimaryKey val scanId: String, val sources: List<String>, val extractedConcepts: List<String>, val reliabilitySignals: List<String>, val educationalIntegration: List<String>)
@Entity(tableName = "knowledge_discovery_research_opportunities", indices = [Index(value = ["opportunityId"])])
data class KnowledgeResearchOpportunityEntity(@PrimaryKey val opportunityId: String, val unsolvedProblems: List<String>, val researchGaps: List<String>, val futureTopics: List<String>, val innovationOpportunities: List<String>)
@Entity(tableName = "knowledge_discovery_relationship_maps", indices = [Index(value = ["mapId"])])
data class KnowledgeDiscoveryRelationshipMapEntity(@PrimaryKey val mapId: String, val conceptConnections: List<String>, val crossDomainRelationships: List<String>, val hiddenPatterns: List<String>, val scientificRelationships: List<String>)
@Entity(tableName = "knowledge_discovery_validation", indices = [Index(value = ["confidenceScore"]), Index(value = ["humanApprovalRequired"])])
data class KnowledgeDiscoveryValidationEntity(@PrimaryKey val validationId: String, val accuracyScore: Int, val reliabilityScore: Int, val evidenceQuality: Int, val educationalUsefulness: Int, val confidenceScore: Int, val humanApprovalRequired: Boolean)
@Entity(tableName = "knowledge_discovery_future_models", indices = [Index(value = ["modelId"])])
data class FutureKnowledgeDiscoveryModelEntity(@PrimaryKey val modelId: String, val futureTechnologies: List<String>, val futureSkills: List<String>, val futureResearchAreas: List<String>, val futureEducationNeeds: List<String>)
@Entity(tableName = "knowledge_discovery_intelligence_network", indices = [Index(value = ["networkId"])])
data class KnowledgeDiscoveryIntelligenceNetworkEntity(@PrimaryKey val networkId: String, val universities: List<String>, val researchers: List<String>, val aiSystems: List<String>, val knowledgeDatabases: List<String>, val learningCommunities: List<String>)
