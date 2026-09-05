package com.rola.app.ai_research

import com.rola.app.ai_research.scientist.AIResearchResult
import com.rola.app.data.database.AIResearchDao
import com.rola.app.data.database.entities.AIResearchAnalyticsEntity
import com.rola.app.data.database.entities.AIResearchIdeaEntity
import com.rola.app.data.database.entities.AIResearchProjectEntity
import com.rola.app.data.database.entities.ExperimentEntity
import com.rola.app.data.database.entities.HypothesisEntity
import com.rola.app.data.database.entities.ResearchCollaborationEntity
import com.rola.app.data.database.entities.ResearchResultEntity
import com.rola.app.data.database.entities.ScientificKnowledgeEntity
import com.rola.app.data.database.entities.ValidationRecordEntity
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class AIResearchRepository @Inject constructor(private val dao: AIResearchDao) {
    fun observeDashboard(): Flow<AIResearchDashboardState> =
        combine(dao.observeProject(), dao.observeDiscovery(), dao.observeHypotheses(), dao.observeExperiment(), dao.observeAnalytics()) { project, discovery, hypotheses, experiment, analytics ->
            AIResearchDashboardState(
                projects = listOfNotNull(project?.title),
                discoveries = discovery?.opportunities.orEmpty(),
                hypotheses = hypotheses?.hypotheses.orEmpty(),
                experiments = experiment?.procedures.orEmpty(),
                knowledgeGrowth = analytics?.knowledgeGrowth ?: 0,
                validationScore = analytics?.validationScore ?: 0,
            )
        }

    suspend fun save(result: AIResearchResult) {
        dao.upsertProject(AIResearchProjectEntity(result.project.projectId, result.project.title, result.project.domain, result.project.roadmap))
        dao.upsertDiscovery(AIResearchIdeaEntity(result.discovery.discoveryId, result.discovery.knowledgeGaps, result.discovery.opportunities, result.discovery.emergingTopics))
        dao.upsertHypotheses(HypothesisEntity(result.hypotheses.hypothesisId, result.hypotheses.questions, result.hypotheses.hypotheses, result.hypotheses.possibleSolutions))
        dao.upsertExperiment(ExperimentEntity(result.experiment.experimentId, result.experiment.procedures, result.experiment.resources, result.experiment.simulationPlans, result.experiment.expectedResults))
        dao.upsertResult(ResearchResultEntity(result.analysis.resultId, result.analysis.patterns, result.analysis.relationships, result.analysis.interpretation))
        dao.upsertValidation(ValidationRecordEntity(result.validation.validationId, result.validation.accuracyScore, result.validation.sourceReliability, result.validation.approved, result.validation.reasoning))
        dao.upsertKnowledge(ScientificKnowledgeEntity(result.learningPackage.packageId, result.learningPackage.lessons, result.learningPackage.tutorials, result.learningPackage.simulations, result.learningPackage.projects))
        dao.upsertCollaboration(ResearchCollaborationEntity(result.collaboration.collaborationId, result.collaboration.participants, result.collaboration.sharedProjects, result.collaboration.discussionSummary))
        dao.upsertAnalytics(AIResearchAnalyticsEntity(result.analytics.analyticsId, result.analytics.discoveryScore, result.analytics.validationScore, result.analytics.knowledgeGrowth))
    }
}

data class AIResearchDashboardState(
    val projects: List<String> = emptyList(),
    val discoveries: List<String> = emptyList(),
    val hypotheses: List<String> = emptyList(),
    val experiments: List<String> = emptyList(),
    val knowledgeGrowth: Int = 0,
    val validationScore: Int = 0,
)
