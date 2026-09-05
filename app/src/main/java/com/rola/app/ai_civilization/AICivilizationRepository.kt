package com.rola.app.ai_civilization

import com.rola.app.ai_civilization.intelligence.CivilizationResult
import com.rola.app.data.database.AICivilizationDao
import com.rola.app.data.database.entities.AICivilizationEntity
import com.rola.app.data.database.entities.AIGovernanceLogEntity
import com.rola.app.data.database.entities.CivilizationAnalyticsEntity
import com.rola.app.data.database.entities.CivilizationInnovationRecordEntity
import com.rola.app.data.database.entities.CivilizationKnowledgeEvolutionEntity
import com.rola.app.data.database.entities.CivilizationLearningEvolutionEntity
import com.rola.app.data.database.entities.FutureEducationPlanEntity
import com.rola.app.data.database.entities.GlobalKnowledgeConnectionEntity
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class AICivilizationRepository @Inject constructor(private val dao: AICivilizationDao) {
    fun observeDashboard(): Flow<CivilizationDashboardState> =
        combine(dao.observeCivilization(), dao.observeKnowledge(), dao.observeLearning(), dao.observeInnovation(), dao.observeFuturePlan(), dao.observeAnalytics()) { values ->
            val civ = values[0] as AICivilizationEntity?
            val knowledge = values[1] as CivilizationKnowledgeEvolutionEntity?
            val learning = values[2] as CivilizationLearningEvolutionEntity?
            val innovation = values[3] as CivilizationInnovationRecordEntity?
            val plan = values[4] as FutureEducationPlanEntity?
            val analytics = values[5] as CivilizationAnalyticsEntity?
            CivilizationDashboardState(
                intelligence = civ?.globalEducationIntelligence.orEmpty(),
                knowledgeEvolution = knowledge?.graphExpansion.orEmpty(),
                learningImprovement = learning?.learningPathUpdates.orEmpty(),
                innovations = innovation?.technologies.orEmpty(),
                futurePredictions = plan?.trends.orEmpty(),
                roadmap = plan?.roadmap.orEmpty(),
                intelligenceScore = analytics?.intelligenceScore ?: 0,
                evolutionScore = analytics?.evolutionScore ?: 0,
            )
        }

    suspend fun save(result: CivilizationResult) {
        dao.upsertCivilization(AICivilizationEntity(result.civilization.civilizationId, result.civilization.globalEducationIntelligence, result.civilization.participants, result.civilization.coordinationModel))
        dao.upsertKnowledge(CivilizationKnowledgeEvolutionEntity(result.knowledgeEvolution.evolutionId, result.knowledgeEvolution.missingKnowledge, result.knowledgeEvolution.validationSummary, result.knowledgeEvolution.graphExpansion))
        dao.upsertLearning(CivilizationLearningEvolutionEntity(result.learningEvolution.evolutionId, result.learningEvolution.learningPathUpdates, result.learningEvolution.curriculumUpdates, result.learningEvolution.recommendationUpdates))
        dao.upsertInnovation(CivilizationInnovationRecordEntity(result.innovation.innovationId, result.innovation.technologies, result.innovation.methods, result.innovation.researchDirections, result.innovation.humanApproved))
        dao.upsertFuturePlan(FutureEducationPlanEntity(result.futurePlan.planId, result.futurePlan.futureSkills, result.futurePlan.futureSubjects, result.futurePlan.trends, result.futurePlan.roadmap))
        dao.upsertConnection(GlobalKnowledgeConnectionEntity(result.knowledgeNetwork.connectionId, result.knowledgeNetwork.connectedSources, result.knowledgeNetwork.collaborationSummary))
        dao.upsertGovernance(AIGovernanceLogEntity(result.governance.logId, result.governance.policies, result.governance.auditTrail, result.governance.approvalRequired))
        dao.upsertAnalytics(CivilizationAnalyticsEntity(result.analytics.analyticsId, result.analytics.intelligenceScore, result.analytics.evolutionScore, result.analytics.innovationScore))
    }
}

data class CivilizationDashboardState(
    val intelligence: String = "",
    val knowledgeEvolution: List<String> = emptyList(),
    val learningImprovement: List<String> = emptyList(),
    val innovations: List<String> = emptyList(),
    val futurePredictions: List<String> = emptyList(),
    val roadmap: List<String> = emptyList(),
    val intelligenceScore: Int = 0,
    val evolutionScore: Int = 0,
)
