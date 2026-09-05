package com.rola.app.knowledge_engineering

import com.rola.app.data.database.KnowledgeEngineeringDao
import com.rola.app.data.database.entities.ConceptMappingEntity
import com.rola.app.data.database.entities.EngineeredKnowledgeAnalyticsEntity
import com.rola.app.data.database.entities.EngineeredKnowledgeEntity
import com.rola.app.data.database.entities.EngineeredKnowledgeEvolutionHistoryEntity
import com.rola.app.data.database.entities.EngineeredKnowledgeRelationshipEntity
import com.rola.app.data.database.entities.EngineeredKnowledgeSourceEntity
import com.rola.app.data.database.entities.EngineeredKnowledgeValidationEntity
import com.rola.app.data.database.entities.EngineeredLearningResourceEntity
import com.rola.app.data.database.entities.SemanticIndexEntity
import com.rola.app.knowledge_engineering.knowledge_core.KnowledgeEngineeringResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class KnowledgeEngineeringRepository @Inject constructor(private val dao: KnowledgeEngineeringDao) {
    fun observeDashboard(): Flow<KnowledgeEngineeringDashboardState> =
        combine(dao.observeEntity(), dao.observeSource(), dao.observeValidation(), dao.observeEvolution(), dao.observeAnalytics()) { entity, source, validation, evolution, analytics ->
            KnowledgeEngineeringDashboardState(
                knowledgeGrowth = source?.concepts.orEmpty() + entity?.skills.orEmpty(),
                discoveries = evolution?.newRelationships.orEmpty(),
                relationships = source?.importantFacts.orEmpty(),
                validationStatus = validation?.confidence.orEmpty(),
                learningImpact = analytics?.learningImpact ?: 0,
                validationScore = analytics?.validationScore ?: 0,
            )
        }

    suspend fun save(result: KnowledgeEngineeringResult) {
        dao.upsertEntity(EngineeredKnowledgeEntity(result.structured.entityId, result.structured.definitions, result.structured.learningMaterials, result.structured.examples, result.structured.skills))
        dao.upsertRelationship(EngineeredKnowledgeRelationshipEntity(result.organization.mappingId, result.organization.conceptMappings, result.organization.learningPathways))
        dao.upsertSource(EngineeredKnowledgeSourceEntity(result.extracted.extractionId, result.extracted.concepts, result.extracted.importantFacts, result.extracted.classifications))
        dao.upsertValidation(EngineeredKnowledgeValidationEntity(result.validation.validationId, result.validation.scientificCorrectness, result.validation.sourceReliability, result.validation.logicalConsistency, result.validation.confidence.name, result.validation.approved))
        dao.upsertMapping(ConceptMappingEntity(result.organization.mappingId, result.organization.conceptMappings, result.organization.learningPathways))
        dao.upsertResource(EngineeredLearningResourceEntity(result.delivery.deliveryId, result.delivery.levelAdjustedExplanation, result.delivery.resources, result.delivery.personalizationBasis))
        dao.upsertEvolution(EngineeredKnowledgeEvolutionHistoryEntity(result.evolution.evolutionId, result.evolution.outdatedUpdates, result.evolution.newRelationships, result.evolution.improvedExplanations))
        dao.upsertSemanticIndex(SemanticIndexEntity("semantic-${result.organization.mappingId}", result.organization.semanticIndexes))
        dao.upsertAnalytics(EngineeredKnowledgeAnalyticsEntity(result.analytics.analyticsId, result.analytics.knowledgeGrowth, result.analytics.validationScore, result.analytics.learningImpact))
    }
}

data class KnowledgeEngineeringDashboardState(
    val knowledgeGrowth: List<String> = emptyList(),
    val discoveries: List<String> = emptyList(),
    val relationships: List<String> = emptyList(),
    val validationStatus: String = "",
    val learningImpact: Int = 0,
    val validationScore: Int = 0,
)
