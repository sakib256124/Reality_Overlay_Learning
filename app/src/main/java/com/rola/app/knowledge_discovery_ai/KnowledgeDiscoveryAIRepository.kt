package com.rola.app.knowledge_discovery_ai

import com.rola.app.data.database.KnowledgeDiscoveryAIDao
import com.rola.app.data.database.entities.FutureKnowledgeDiscoveryModelEntity
import com.rola.app.data.database.entities.GlobalKnowledgeSourceEntity
import com.rola.app.data.database.entities.KnowledgeDiscoveryEntity
import com.rola.app.data.database.entities.KnowledgeDiscoveryIntelligenceNetworkEntity
import com.rola.app.data.database.entities.KnowledgeDiscoveryRelationshipMapEntity
import com.rola.app.data.database.entities.KnowledgeDiscoveryValidationEntity
import com.rola.app.data.database.entities.KnowledgeResearchOpportunityEntity
import com.rola.app.knowledge_discovery_ai.discovery_core.KnowledgeDiscoveryResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class KnowledgeDiscoveryAIRepository @Inject constructor(private val dao: KnowledgeDiscoveryAIDao) {
    fun observeDashboard(): Flow<KnowledgeDiscoveryDashboardState> =
        combine(
            dao.observeDiscovery(),
            dao.observeSources(),
            dao.observeOpportunities(),
            dao.observeRelationships(),
            dao.observeValidation(),
            dao.observeFutureModel(),
            dao.observeNetwork(),
        ) { values ->
            val discovery = values[0] as KnowledgeDiscoveryEntity?
            val sources = values[1] as GlobalKnowledgeSourceEntity?
            val opportunities = values[2] as KnowledgeResearchOpportunityEntity?
            val relationships = values[3] as KnowledgeDiscoveryRelationshipMapEntity?
            val validation = values[4] as KnowledgeDiscoveryValidationEntity?
            val future = values[5] as FutureKnowledgeDiscoveryModelEntity?
            val network = values[6] as KnowledgeDiscoveryIntelligenceNetworkEntity?
            KnowledgeDiscoveryDashboardState(
                newDiscoveries = discovery?.newConcepts.orEmpty(),
                researchOpportunities = opportunities?.researchGaps.orEmpty() + opportunities?.innovationOpportunities.orEmpty(),
                emergingKnowledge = discovery?.emergingTechnologies.orEmpty() + future?.futureTechnologies.orEmpty(),
                knowledgeGrowth = validation?.educationalUsefulness ?: 0,
                aiAnalysis = discovery?.researchTrends.orEmpty() + relationships?.hiddenPatterns.orEmpty(),
                sourceTrust = sources?.reliabilitySignals.orEmpty(),
                networkConnections = network?.universities.orEmpty() + network?.aiSystems.orEmpty(),
                futurePredictions = future?.futureSkills.orEmpty() + future?.futureEducationNeeds.orEmpty(),
                securityStatus = if (validation?.humanApprovalRequired == true) "Source verification, reliability score ${validation.confidenceScore}%, human approval, and audit tracking active." else "",
            )
        }

    suspend fun save(result: KnowledgeDiscoveryResult) {
        val confidence = (result.validation.accuracyScore + result.validation.reliabilityScore + result.validation.evidenceQuality + result.validation.educationalUsefulness) / 4
        dao.upsertDiscovery(KnowledgeDiscoveryEntity(result.discovery.discoveryId, result.discovery.newConcepts, result.discovery.emergingTechnologies, result.discovery.researchTrends, result.discovery.knowledgeGaps, result.discovery.learningOpportunities, result.status.name))
        dao.upsertSources(GlobalKnowledgeSourceEntity(result.scan.scanId, result.scan.sources, result.scan.extractedConcepts, result.scan.reliabilitySignals, result.scan.educationalIntegration))
        dao.upsertOpportunities(KnowledgeResearchOpportunityEntity(result.opportunities.opportunityId, result.opportunities.unsolvedProblems, result.opportunities.researchGaps, result.opportunities.futureTopics, result.opportunities.innovationOpportunities))
        dao.upsertRelationships(KnowledgeDiscoveryRelationshipMapEntity(result.relationships.mapId, result.relationships.conceptConnections, result.relationships.crossDomainRelationships, result.relationships.hiddenPatterns, result.relationships.scientificRelationships))
        dao.upsertValidation(KnowledgeDiscoveryValidationEntity(result.validation.validationId, result.validation.accuracyScore, result.validation.reliabilityScore, result.validation.evidenceQuality, result.validation.educationalUsefulness, confidence, result.validation.humanApprovalRequired))
        dao.upsertFutureModel(FutureKnowledgeDiscoveryModelEntity(result.prediction.modelId, result.prediction.futureTechnologies, result.prediction.futureSkills, result.prediction.futureResearchAreas, result.prediction.futureEducationNeeds))
        dao.upsertNetwork(KnowledgeDiscoveryIntelligenceNetworkEntity(result.network.networkId, result.network.universities, result.network.researchers, result.network.aiSystems, result.network.knowledgeDatabases, result.network.learningCommunities))
    }
}

data class KnowledgeDiscoveryDashboardState(
    val newDiscoveries: List<String> = emptyList(),
    val researchOpportunities: List<String> = emptyList(),
    val emergingKnowledge: List<String> = emptyList(),
    val knowledgeGrowth: Int = 0,
    val aiAnalysis: List<String> = emptyList(),
    val sourceTrust: List<String> = emptyList(),
    val networkConnections: List<String> = emptyList(),
    val futurePredictions: List<String> = emptyList(),
    val securityStatus: String = "",
)
