package com.rola.app.asi_core

import com.rola.app.asi_core.intelligence.ASIResult
import com.rola.app.data.database.ASICoreDao
import com.rola.app.data.database.entities.AICreativeOutputEntity
import com.rola.app.data.database.entities.ASIModelEntity
import com.rola.app.data.database.entities.ASIProfileEntity
import com.rola.app.data.database.entities.ASIGovernanceRecordEntity
import com.rola.app.data.database.entities.GlobalEducationInsightEntity
import com.rola.app.data.database.entities.HumanAIInteractionEntity
import com.rola.app.data.database.entities.KnowledgeEvolutionRecordEntity
import com.rola.app.data.database.entities.ReasoningHistoryEntity
import com.rola.app.data.database.entities.SelfImprovementLogEntity
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class ASIRepository @Inject constructor(
    private val asiCoreDao: ASICoreDao,
) {
    fun observeDashboard(learnerId: String, institutionId: String): Flow<ASIDashboardState> =
        combine(
            asiCoreDao.observeProfile(learnerId),
            asiCoreDao.observeReasoningHistory(),
            asiCoreDao.observeCreativeOutputs(),
            asiCoreDao.observeGovernance(),
            asiCoreDao.observeLatestGlobalInsight(institutionId),
        ) { profile, reasoning, creative, governance, insight ->
            ASIDashboardState(
                personalizationDepthPercent = profile?.personalizationDepthPercent ?: 0,
                reasoningSummaries = reasoning.map { "${it.topic}: ${it.confidencePercent}% - ${it.explanation}" },
                creativeOutputs = creative.flatMap { it.educationalApproaches }.take(6),
                governanceStatus = governance.map { "${it.approvalStatus}: ${it.riskLevel}" },
                globalInsights = insight?.globalEducationPatterns.orEmpty(),
            )
        }

    suspend fun saveResult(result: ASIResult) {
        asiCoreDao.upsertProfile(
            ASIProfileEntity(
                profileId = result.profile.profileId,
                learnerId = result.profile.learnerId,
                intelligenceScope = result.profile.intelligenceScope,
                personalizationDepthPercent = result.profile.personalizationDepthPercent,
                responsibleAIMode = result.profile.responsibleAIMode,
                updatedAt = result.profile.updatedAt,
            ),
        )
        asiCoreDao.upsertModel(
            ASIModelEntity(
                modelId = result.modelState.modelId,
                name = result.modelState.name,
                capabilities = result.modelState.capabilities,
                safetyBoundary = result.modelState.safetyBoundary,
                version = result.modelState.version,
            ),
        )
        asiCoreDao.upsertReasoning(
            ReasoningHistoryEntity(
                traceId = result.reasoningTrace.traceId,
                topic = result.reasoningTrace.topic,
                reasoningSteps = result.reasoningTrace.reasoningSteps,
                confidencePercent = result.reasoningTrace.confidencePercent,
                explanation = result.reasoningTrace.explanation,
            ),
        )
        asiCoreDao.upsertKnowledge(
            KnowledgeEvolutionRecordEntity(
                mapId = result.knowledgeMap.mapId,
                topic = result.knowledgeMap.topic,
                domainConnections = result.knowledgeMap.domainConnections,
                newKnowledgeLinks = result.knowledgeMap.newKnowledgeLinks,
                contentImprovementIdeas = result.knowledgeMap.contentImprovementIdeas,
            ),
        )
        asiCoreDao.upsertSelfImprovement(
            SelfImprovementLogEntity(
                logId = result.selfImprovementLog.logId,
                improvedAreas = result.selfImprovementLog.improvedAreas,
                evaluationSummary = result.selfImprovementLog.evaluationSummary,
                requiresOfflineValidation = result.selfImprovementLog.requiresOfflineValidation,
            ),
        )
        asiCoreDao.upsertCreativeOutput(
            AICreativeOutputEntity(
                outputId = result.creativeOutput.outputId,
                topic = result.creativeOutput.topic,
                educationalApproaches = result.creativeOutput.educationalApproaches,
                learningActivities = result.creativeOutput.learningActivities,
                simulations = result.creativeOutput.simulations,
                researchDirections = result.creativeOutput.researchDirections,
            ),
        )
        asiCoreDao.upsertHumanAIInteraction(
            HumanAIInteractionEntity(
                interactionId = "human-ai-${UUID.randomUUID()}",
                planId = result.collaborationPlan.planId,
                stakeholders = result.collaborationPlan.stakeholders.map { it.name },
                aiSuggestions = result.collaborationPlan.aiSuggestions,
                requiredApprovals = result.collaborationPlan.requiredApprovals,
                feedbackLoop = result.collaborationPlan.feedbackLoop,
            ),
        )
        asiCoreDao.upsertGovernance(
            ASIGovernanceRecordEntity(
                recordId = result.governanceRecord.recordId,
                approvalStatus = result.governanceRecord.approvalStatus.name,
                riskLevel = result.governanceRecord.riskLevel.name,
                transparencyNotes = result.governanceRecord.transparencyNotes,
                ethicsChecks = result.governanceRecord.ethicsChecks,
                humanOverrideAvailable = result.governanceRecord.humanOverrideAvailable,
            ),
        )
        asiCoreDao.upsertGlobalInsight(
            GlobalEducationInsightEntity(
                insightId = result.worldInsight.insightId,
                institutionId = result.worldInsight.institutionId,
                globalEducationPatterns = result.worldInsight.globalEducationPatterns,
                knowledgeSharingPlan = result.worldInsight.knowledgeSharingPlan,
                innovationOpportunities = result.worldInsight.innovationOpportunities,
            ),
        )
    }
}

data class ASIDashboardState(
    val personalizationDepthPercent: Int = 0,
    val reasoningSummaries: List<String> = emptyList(),
    val creativeOutputs: List<String> = emptyList(),
    val governanceStatus: List<String> = emptyList(),
    val globalInsights: List<String> = emptyList(),
)
