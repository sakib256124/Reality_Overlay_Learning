package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "engineered_knowledge_entities", indices = [Index(value = ["entityId"])])
data class EngineeredKnowledgeEntity(@PrimaryKey val entityId: String, val definitions: List<String>, val learningMaterials: List<String>, val examples: List<String>, val skills: List<String>)
@Entity(tableName = "engineered_knowledge_relationships", indices = [Index(value = ["mappingId"])])
data class EngineeredKnowledgeRelationshipEntity(@PrimaryKey val mappingId: String, val conceptMappings: List<String>, val learningPathways: List<String>)
@Entity(tableName = "engineered_knowledge_sources", indices = [Index(value = ["extractionId"])])
data class EngineeredKnowledgeSourceEntity(@PrimaryKey val extractionId: String, val concepts: List<String>, val importantFacts: List<String>, val classifications: List<String>)
@Entity(tableName = "engineered_knowledge_validation", indices = [Index(value = ["approved"])])
data class EngineeredKnowledgeValidationEntity(@PrimaryKey val validationId: String, val scientificCorrectness: Int, val sourceReliability: Int, val logicalConsistency: Int, val confidence: String, val approved: Boolean)
@Entity(tableName = "concept_mappings", indices = [Index(value = ["mappingId"])])
data class ConceptMappingEntity(@PrimaryKey val mappingId: String, val conceptMappings: List<String>, val learningPathways: List<String>)
@Entity(tableName = "engineered_learning_resources", indices = [Index(value = ["deliveryId"])])
data class EngineeredLearningResourceEntity(@PrimaryKey val deliveryId: String, val levelAdjustedExplanation: String, val resources: List<String>, val personalizationBasis: List<String>)
@Entity(tableName = "engineered_knowledge_evolution_history", indices = [Index(value = ["evolutionId"])])
data class EngineeredKnowledgeEvolutionHistoryEntity(@PrimaryKey val evolutionId: String, val outdatedUpdates: List<String>, val newRelationships: List<String>, val improvedExplanations: List<String>)
@Entity(tableName = "semantic_indexes", indices = [Index(value = ["indexId"])])
data class SemanticIndexEntity(@PrimaryKey val indexId: String, val indexes: List<String>)
@Entity(tableName = "engineered_knowledge_analytics", indices = [Index(value = ["validationScore"])])
data class EngineeredKnowledgeAnalyticsEntity(@PrimaryKey val analyticsId: String, val knowledgeGrowth: Int, val validationScore: Int, val learningImpact: Int)
