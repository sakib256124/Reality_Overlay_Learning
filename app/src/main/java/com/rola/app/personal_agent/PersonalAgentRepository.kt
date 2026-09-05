package com.rola.app.personal_agent

import com.rola.app.data.database.PersonalAgentDao
import com.rola.app.data.database.entities.AgentAnalyticsEntity
import com.rola.app.data.database.entities.AgentEvolutionHistoryEntity
import com.rola.app.data.database.entities.AgentInteractionEntity
import com.rola.app.data.database.entities.AgentLearningHistoryEntity
import com.rola.app.data.database.entities.AgentMemoryEntity
import com.rola.app.data.database.entities.AgentProfileEntity
import com.rola.app.data.database.entities.AgentRecommendationEntity
import com.rola.app.data.database.entities.PersonalAgentEntity
import com.rola.app.personal_agent.agent_core.UniversalAgentResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class PersonalAgentRepository @Inject constructor(private val dao: PersonalAgentDao) {
    fun observeDashboard(): Flow<PersonalAgentDashboardState> =
        combine(
            dao.observeAgent(),
            dao.observeProfile(),
            dao.observeMemory(),
            dao.observeInteraction(),
            dao.observeLearningHistory(),
            dao.observeRecommendation(),
            dao.observeEvolution(),
            dao.observeAnalytics(),
        ) { values ->
            val agent = values[0] as PersonalAgentEntity?
            val profile = values[1] as AgentProfileEntity?
            val memory = values[2] as AgentMemoryEntity?
            val interaction = values[3] as AgentInteractionEntity?
            val history = values[4] as AgentLearningHistoryEntity?
            val recommendation = values[5] as AgentRecommendationEntity?
            val evolution = values[6] as AgentEvolutionHistoryEntity?
            val analytics = values[7] as AgentAnalyticsEntity?
            PersonalAgentDashboardState(
                agentStatus = agent?.status.orEmpty(),
                learningProgress = analytics?.learningProgress ?: 0,
                currentGoals = profile?.goals.orEmpty(),
                recommendations = listOfNotNull(recommendation?.strategy, recommendation?.learningActivity) + interaction?.selectedCapabilities.orEmpty(),
                skills = profile?.skills.orEmpty(),
                futureRoadmap = history?.mentorRoadmap.orEmpty(),
                memorySummary = memory?.longTermMemory.orEmpty(),
                evolutionSummary = listOfNotNull(evolution?.teachingImprovement, evolution?.communicationStyle),
                securityStatus = analytics?.securityStatus.orEmpty(),
            )
        }

    suspend fun save(result: UniversalAgentResult) {
        val agentId = result.agent.agentId
        dao.upsertAgent(PersonalAgentEntity(agentId, result.agent.userId, result.agent.personality, result.agent.learningStyle, result.agent.status.name))
        dao.upsertProfile(AgentProfileEntity("profile-$agentId", agentId, result.agent.knowledgeProfile, result.agent.skills, result.agent.goals, result.agent.preferences, result.agent.careerObjectives))
        dao.upsertMemory(AgentMemoryEntity(result.memory.memoryId, agentId, result.memory.shortTermMemory, result.memory.longTermMemory, result.memory.userControlled, result.memory.privacyProtected))
        dao.upsertInteraction(AgentInteractionEntity(result.resultId, agentId, result.teaching.explanation, result.intelligence.selectedCapabilities.map { it.name }, result.intelligence.transparentDecision))
        dao.upsertLearningHistory(AgentLearningHistoryEntity("history-$agentId", agentId, result.agent.learningHistory, result.teaching.explanation, result.mentor.growthRoadmap))
        dao.upsertRecommendation(AgentRecommendationEntity(result.decision.decisionId, agentId, result.decision.moduleToUse, result.decision.explanationStyle, result.decision.learningActivity, result.decision.strategy, result.decision.humanControl))
        dao.upsertEvolution(AgentEvolutionHistoryEntity(result.evolution.evolutionId, agentId, result.evolution.teachingImprovement, result.evolution.communicationStyle, result.evolution.recommendationImprovement, result.evolution.planningAccuracy, result.evolution.personalUnderstanding))
        dao.upsertAnalytics(AgentAnalyticsEntity("analytics-$agentId", agentId, learningProgress = 76, satisfactionScore = 90, memoryAccuracy = 94, securityStatus = "User-controlled memory, privacy protection, transparent decisions, secure communication.", personalUnderstanding = result.evolution.personalUnderstanding))
    }
}

data class PersonalAgentDashboardState(
    val agentStatus: String = "",
    val learningProgress: Int = 0,
    val currentGoals: List<String> = emptyList(),
    val recommendations: List<String> = emptyList(),
    val skills: List<String> = emptyList(),
    val futureRoadmap: List<String> = emptyList(),
    val memorySummary: List<String> = emptyList(),
    val evolutionSummary: List<String> = emptyList(),
    val securityStatus: String = "",
)
