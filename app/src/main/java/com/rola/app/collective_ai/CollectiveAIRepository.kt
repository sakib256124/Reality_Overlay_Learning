package com.rola.app.collective_ai

import com.rola.app.collective_ai.intelligence_network.CollectiveAIResult
import com.rola.app.data.database.CollectiveAIDao
import com.rola.app.data.database.entities.AIConsensusRecordEntity
import com.rola.app.data.database.entities.AgentCommunicationHistoryEntity
import com.rola.app.data.database.entities.CollaborationAnalyticsEntity
import com.rola.app.data.database.entities.CollectiveAIAgentEntity
import com.rola.app.data.database.entities.CollectiveAgentRelationshipEntity
import com.rola.app.data.database.entities.CollectiveAgentTaskEntity
import com.rola.app.data.database.entities.CollectiveKnowledgeExchangeEntity
import com.rola.app.data.database.entities.CollectiveLearningResultEntity
import com.rola.app.data.database.entities.HumanFeedbackEntity
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class CollectiveAIRepository @Inject constructor(
    private val dao: CollectiveAIDao,
) {
    fun observeDashboard(): Flow<CollectiveAIDashboardState> =
        combine(
            dao.observeAgents(),
            dao.observeKnowledgeExchange(),
            dao.observeLatestConsensus(),
            dao.observeLatestResult(),
            dao.observeLatestAnalytics(),
        ) { agents, exchanges, consensus, result, analytics ->
            CollectiveAIDashboardState(
                activeAgents = agents.size,
                agentNames = agents.map { it.name },
                collaborationStatus = if (agents.isEmpty()) "Collective network idle" else "Agents collaborating",
                knowledgeExchange = exchanges.map { "${it.topic}: ${it.knowledgeSummary}" },
                consensus = consensus?.let { "${it.outcome}: ${it.selectedStrategy}" }.orEmpty(),
                decisions = consensus?.rankedStrategies.orEmpty(),
                improvements = result?.improvedStrategies.orEmpty(),
                curriculumUpdates = result?.curriculumUpdates.orEmpty(),
                consensusScore = analytics?.consensusScore ?: 0,
                improvementScore = analytics?.improvementScore ?: 0,
            )
        }

    suspend fun saveResult(result: CollectiveAIResult) {
        dao.upsertAgents(result.agents.map {
            CollectiveAIAgentEntity(
                agentId = it.agentId,
                name = it.name,
                agentType = it.agentType.name,
                capability = it.capability,
                status = it.status.name,
            )
        })
        dao.upsertRelationships(result.relationships.map {
            CollectiveAgentRelationshipEntity(
                relationshipId = it.relationshipId,
                fromAgentId = it.fromAgentId,
                toAgentId = it.toAgentId,
                relationshipType = it.relationshipType,
                trustScore = it.trustScore,
            )
        })
        dao.upsertTasks(result.tasks.map {
            CollectiveAgentTaskEntity(it.taskId, it.agentId, it.taskType, it.priority, it.output)
        })
        dao.upsertKnowledgeExchange(result.knowledgeExchanges.map {
            CollectiveKnowledgeExchangeEntity(
                exchangeId = it.exchangeId,
                sourceAgentId = it.sourceAgentId,
                targetAgentId = it.targetAgentId,
                topic = it.topic,
                knowledgeSummary = it.knowledgeSummary,
                sources = it.sources,
            )
        })
        dao.upsertConsensus(
            AIConsensusRecordEntity(
                consensusId = result.consensus.consensusId,
                outcome = result.consensus.outcome.name,
                selectedStrategy = result.consensus.selectedStrategy,
                rankedStrategies = result.consensus.rankedStrategies,
                accuracyScore = result.consensus.accuracyScore,
                explanation = result.consensus.explanation,
            ),
        )
        dao.upsertCommunications(result.communications.map {
            AgentCommunicationHistoryEntity(
                communicationId = it.communicationId,
                senderAgentId = it.senderAgentId,
                receiverAgentId = it.receiverAgentId,
                message = it.message,
                createdAt = it.createdAt,
            )
        })
        dao.upsertHumanFeedback(
            HumanFeedbackEntity(
                feedbackId = result.humanFeedback.feedbackId,
                userId = result.humanFeedback.userId,
                role = result.humanFeedback.role,
                feedback = result.humanFeedback.feedback,
                validationScore = result.humanFeedback.validationScore,
            ),
        )
        dao.upsertLearningResult(
            CollectiveLearningResultEntity(
                resultId = result.learningResult.resultId,
                improvedStrategies = result.learningResult.improvedStrategies,
                curriculumUpdates = result.learningResult.curriculumUpdates,
                assessmentImprovements = result.learningResult.assessmentImprovements,
                recommendationUpdates = result.learningResult.recommendationUpdates,
            ),
        )
        dao.upsertAnalytics(
            CollaborationAnalyticsEntity(
                analyticsId = result.analytics.analyticsId,
                activeAgents = result.analytics.activeAgents,
                communicationCount = result.analytics.communicationCount,
                consensusScore = result.analytics.consensusScore,
                improvementScore = result.analytics.improvementScore,
            ),
        )
    }
}

data class CollectiveAIDashboardState(
    val activeAgents: Int = 0,
    val agentNames: List<String> = emptyList(),
    val collaborationStatus: String = "",
    val knowledgeExchange: List<String> = emptyList(),
    val consensus: String = "",
    val decisions: List<String> = emptyList(),
    val improvements: List<String> = emptyList(),
    val curriculumUpdates: List<String> = emptyList(),
    val consensusScore: Int = 0,
    val improvementScore: Int = 0,
)
