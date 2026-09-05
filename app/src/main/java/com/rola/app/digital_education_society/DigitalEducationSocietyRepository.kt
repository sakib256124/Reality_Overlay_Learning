package com.rola.app.digital_education_society

import com.rola.app.data.database.DigitalEducationSocietyDao
import com.rola.app.data.database.entities.DigitalAvatarEntity
import com.rola.app.data.database.entities.EducationInstitutionEntity
import com.rola.app.data.database.entities.GlobalEducationNetworkEntity
import com.rola.app.data.database.entities.GlobalLearningAnalyticsEntity
import com.rola.app.data.database.entities.GovernancePolicyEntity
import com.rola.app.data.database.entities.KnowledgeCommunityEntity
import com.rola.app.data.database.entities.KnowledgeExchangeHistoryEntity
import com.rola.app.data.database.entities.SocietyAIAgentEntity
import com.rola.app.data.database.entities.SocietyInnovationRecordEntity
import com.rola.app.digital_education_society.civilization_core.DigitalEducationCivilizationResult
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class DigitalEducationSocietyRepository @Inject constructor(
    private val dao: DigitalEducationSocietyDao,
) {
    fun observeDashboard(institutionId: String, learnerId: String): Flow<DigitalEducationSocietyDashboardState> =
        combine(
            dao.observeNetwork(institutionId),
            dao.observeCommunities(),
            dao.observeAnalytics(institutionId),
            dao.observeAvatar(learnerId),
            dao.observeGovernance(),
        ) { network, communities, analytics, avatar, governance ->
            DigitalEducationSocietyDashboardState(
                participantCount = network?.connectedParticipants?.size ?: 0,
                knowledgeTopics = network?.sharedKnowledgeTopics.orEmpty(),
                communityHighlights = communities.flatMap { it.collaborationGroups }.take(6),
                globalTrends = analytics?.worldwideTrends.orEmpty(),
                avatarSummary = avatar?.let { "${it.knowledgeLevel}: ${it.skills.joinToString()}" }.orEmpty(),
                governanceStatus = governance.map { "${it.decision}: ${it.auditSummary}" },
            )
        }.let { base ->
            combine(base, dao.observeInnovation()) { dashboard, innovation ->
                dashboard.copy(innovationIdeas = innovation.flatMap { it.contentIdeas }.take(6))
            }
        }

    suspend fun saveResult(result: DigitalEducationCivilizationResult) {
        dao.upsertNetwork(
            GlobalEducationNetworkEntity(
                networkId = result.knowledgeSociety.societyId,
                institutionId = result.challenge.institutionId,
                region = result.challenge.region,
                connectedParticipants = result.knowledgeSociety.connectedParticipants.map { it.name },
                sharedKnowledgeTopics = result.knowledgeSociety.sharedKnowledgeTopics,
                learningImprovementPlan = result.knowledgeSociety.learningImprovementPlan,
            ),
        )
        dao.upsertCommunity(
            KnowledgeCommunityEntity(
                communityId = result.communityPlan.communityId,
                collaborationGroups = result.communityPlan.collaborationGroups,
                sharedResources = result.communityPlan.sharedResources,
                aiRecommendations = result.communityPlan.aiRecommendations,
            ),
        )
        dao.upsertAgents(
            result.ecosystemState.coordinatedServices.map {
                SocietyAIAgentEntity(
                    agentId = "society-agent-${UUID.randomUUID()}",
                    agentType = it,
                    capabilities = listOf("coordination", "knowledge sharing", "human-reviewed support"),
                    trustLevel = result.governancePolicy.trustLevel.name,
                    authenticated = true,
                )
            },
        )
        dao.upsertInstitution(
            EducationInstitutionEntity(
                institutionId = result.challenge.institutionId,
                region = result.challenge.region,
                languages = result.challenge.languages,
                activeServices = result.ecosystemState.coordinatedServices,
                accessibilityImprovements = result.ecosystemState.accessibilityImprovements,
            ),
        )
        dao.upsertInnovation(
            SocietyInnovationRecordEntity(
                innovationId = result.innovationRecord.innovationId,
                topic = result.innovationRecord.topic,
                contentIdeas = result.innovationRecord.contentIdeas,
                researchCollaborations = result.innovationRecord.researchCollaborations,
                exchangeValue = result.innovationRecord.exchangeValue,
                humanApprovalRequired = result.innovationProposal.humanApprovalRequired,
            ),
        )
        dao.upsertAnalytics(
            GlobalLearningAnalyticsEntity(
                reportId = result.analyticsReport.reportId,
                institutionId = result.analyticsReport.institutionId,
                worldwideTrends = result.analyticsReport.worldwideTrends,
                knowledgeGaps = result.analyticsReport.knowledgeGaps,
                futureSkillNeeds = result.analyticsReport.futureSkillNeeds,
                improvementSummary = result.analyticsReport.improvementSummary,
            ),
        )
        dao.upsertAvatar(
            DigitalAvatarEntity(
                avatarId = result.avatar.avatarId,
                learnerId = result.avatar.learnerId,
                learningHistory = result.avatar.learningHistory,
                skills = result.avatar.skills,
                knowledgeLevel = result.avatar.knowledgeLevel,
                goals = result.avatar.goals,
                achievements = result.avatar.achievements,
            ),
        )
        dao.upsertGovernance(
            GovernancePolicyEntity(
                policyId = result.governancePolicy.policyId,
                decision = result.governancePolicy.decision.name,
                trustLevel = result.governancePolicy.trustLevel.name,
                accountabilityRules = result.governancePolicy.accountabilityRules,
                dataProtectionRules = result.governancePolicy.dataProtectionRules,
                auditSummary = result.governancePolicy.auditSummary,
            ),
        )
        dao.upsertExchange(
            KnowledgeExchangeHistoryEntity(
                exchangeId = "knowledge-exchange-${UUID.randomUUID()}",
                topic = result.challenge.topic,
                validationSteps = result.knowledgeSociety.validationSteps,
                distributionReason = result.resourceDistribution.distributionReason,
                createdAt = System.currentTimeMillis(),
            ),
        )
    }
}

data class DigitalEducationSocietyDashboardState(
    val participantCount: Int = 0,
    val knowledgeTopics: List<String> = emptyList(),
    val communityHighlights: List<String> = emptyList(),
    val globalTrends: List<String> = emptyList(),
    val avatarSummary: String = "",
    val governanceStatus: List<String> = emptyList(),
    val innovationIdeas: List<String> = emptyList(),
)
