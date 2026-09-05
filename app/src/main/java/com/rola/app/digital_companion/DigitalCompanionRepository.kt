package com.rola.app.digital_companion

import com.rola.app.data.database.DigitalCompanionDao
import com.rola.app.data.database.entities.CompanionAnalyticsEntity
import com.rola.app.data.database.entities.CompanionConversationEntity
import com.rola.app.data.database.entities.CompanionLearningGoalEntity
import com.rola.app.data.database.entities.CompanionMemoryEntity
import com.rola.app.data.database.entities.CompanionPersonalityEntity
import com.rola.app.data.database.entities.CompanionRecommendationEntity
import com.rola.app.data.database.entities.DigitalCompanionEntity
import com.rola.app.data.database.entities.RelationshipHistoryEntity
import com.rola.app.digital_companion.companion_core.DigitalCompanionResult
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class DigitalCompanionRepository @Inject constructor(
    private val dao: DigitalCompanionDao,
) {
    fun observeDashboard(userId: String): Flow<DigitalCompanionDashboardState> =
        combine(
            dao.observeCompanion(userId),
            dao.observeMemory(userId),
            dao.observeRelationship(userId),
            dao.observeRecommendations(userId),
            dao.observeAnalytics(userId),
        ) { companion, memories, relationship, recommendations, analytics ->
            DigitalCompanionDashboardState(
                companionStatus = companion?.let { "${it.personalityProfile}: ${it.communicationStyle}" }.orEmpty(),
                goals = companion?.learningGoals.orEmpty(),
                memories = memories.map { "${it.memoryType}: ${it.summary}" },
                learnsBestBy = relationship?.learnsBestBy.orEmpty(),
                recommendations = recommendations?.recommendations.orEmpty(),
                achievements = relationship?.goalsAchieved.orEmpty(),
                roadmap = analytics?.futureRoadmap.orEmpty(),
                engagementPercent = analytics?.engagementPercent ?: 0,
            )
        }

    suspend fun saveResult(result: DigitalCompanionResult) {
        val companion = result.companion
        dao.upsertCompanion(
            DigitalCompanionEntity(
                companionId = companion.companionId,
                userId = companion.userId,
                personalityProfile = companion.personalityProfile.name,
                learningHistory = companion.learningHistory,
                knowledgeUnderstanding = companion.knowledgeUnderstanding,
                communicationStyle = companion.communicationStyle,
                learningGoals = companion.learningGoals,
                preferences = companion.preferences,
                memoryControlEnabled = companion.memoryControlEnabled,
            ),
        )
        dao.upsertMemories(result.memories.map {
            CompanionMemoryEntity(it.memoryId, it.userId, it.memoryType.name, it.topic, it.summary, it.userControlled, it.updatedAt)
        })
        dao.upsertConversation(
            CompanionConversationEntity(
                conversationId = result.conversationResponse.responseId,
                userId = companion.userId,
                message = result.conversationResponse.message,
                modalities = result.conversationResponse.modalities.map { it.name },
                knowledgeSources = result.conversationResponse.knowledgeSources,
                createdAt = System.currentTimeMillis(),
            ),
        )
        dao.upsertPersonality(
            CompanionPersonalityEntity(
                profileId = result.personality.profileId,
                userId = companion.userId,
                tone = result.personality.tone.name,
                motivationStyle = result.personality.motivationStyle,
                explanationPreference = result.personality.explanationPreference,
            ),
        )
        dao.upsertGoals(result.learningPlan.weeklyGoals.map {
            CompanionLearningGoalEntity(
                goalId = "companion-goal-${UUID.randomUUID()}",
                userId = companion.userId,
                title = it,
                roadmap = result.learningPlan.skillRoadmap,
                completed = result.relationshipState.goalsAchieved.contains(it),
            )
        })
        dao.upsertRelationship(
            RelationshipHistoryEntity(
                relationshipId = result.relationshipState.relationshipId,
                userId = companion.userId,
                progressSummary = result.relationshipState.progressSummary,
                interactionHistory = result.relationshipState.interactionHistory,
                goalsAchieved = result.relationshipState.goalsAchieved,
                learnsBestBy = result.relationshipState.learnsBestBy,
            ),
        )
        dao.upsertRecommendation(
            CompanionRecommendationEntity(
                recommendationId = result.decision.decisionId,
                userId = companion.userId,
                recommendations = result.learningPlan.recommendations,
                transparentReason = result.decision.transparentReason,
            ),
        )
        dao.upsertAnalytics(
            CompanionAnalyticsEntity(
                analyticsId = result.emotionState.stateId,
                userId = companion.userId,
                motivationPercent = result.emotionState.motivationPercent,
                confidence = result.emotionState.confidence.name,
                frustrationPercent = result.emotionState.frustrationPercent,
                engagementPercent = result.emotionState.engagementPercent,
                futureRoadmap = result.evolutionState.futureRoadmap,
            ),
        )
    }
}

data class DigitalCompanionDashboardState(
    val companionStatus: String = "",
    val goals: List<String> = emptyList(),
    val memories: List<String> = emptyList(),
    val learnsBestBy: String = "",
    val recommendations: List<String> = emptyList(),
    val achievements: List<String> = emptyList(),
    val roadmap: List<String> = emptyList(),
    val engagementPercent: Int = 0,
)
