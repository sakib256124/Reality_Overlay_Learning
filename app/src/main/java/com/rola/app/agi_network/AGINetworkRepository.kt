package com.rola.app.agi_network

import com.rola.app.agi_network.intelligence.AGINetworkResult
import com.rola.app.data.database.AGINetworkDao
import com.rola.app.data.database.entities.AGIEvolutionHistoryEntity
import com.rola.app.data.database.entities.AGINetworkAgentCommunicationEntity
import com.rola.app.data.database.entities.AGINetworkAgentTaskEntity
import com.rola.app.data.database.entities.AGINetworkAnalyticsEntity
import com.rola.app.data.database.entities.AGINetworkCurriculumEvolutionEntity
import com.rola.app.data.database.entities.AGINetworkDecisionEntity
import com.rola.app.data.database.entities.AGINetworkGovernanceRecordEntity
import com.rola.app.data.database.entities.AGINetworkKnowledgeEvolutionEntity
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class AGINetworkRepository @Inject constructor(
    private val agiNetworkDao: AGINetworkDao,
) {
    fun observeDashboard(institutionId: String): Flow<AGINetworkDashboardState> =
        combine(
            agiNetworkDao.observeRecentDecisions(),
            agiNetworkDao.observeAgentTasks(),
            agiNetworkDao.observeGovernance(),
            agiNetworkDao.observeLatestAnalytics(institutionId),
            agiNetworkDao.observeCurriculumEvolution(),
        ) { decisions, tasks, governance, analytics, curriculum ->
            AGINetworkDashboardState(
                recentDecisions = decisions.map { "${it.topic}: ${it.teachingApproach}" },
                activeTasks = tasks.map { "${it.agentRole}: ${it.title} (${it.status})" },
                governanceAlerts = governance.map { it.auditSummary },
                aiPerformancePercent = analytics?.aiPerformancePercent ?: 0,
                studentSuccessPercent = analytics?.studentSuccessPercent ?: 0,
                curriculumDrafts = curriculum.map { "${it.topic}: ${it.generatedCourses.joinToString()}" },
            )
        }

    suspend fun saveResult(result: AGINetworkResult) {
        agiNetworkDao.upsertTasks(result.collaborationPlan.tasks.map { task ->
            AGINetworkAgentTaskEntity(
                taskId = task.taskId,
                agentRole = task.agentRole.name,
                title = task.title,
                priority = task.priority,
                status = task.status.name,
                evidence = task.evidence,
            )
        })
        agiNetworkDao.upsertMessages(result.collaborationPlan.messages.map { message ->
            AGINetworkAgentCommunicationEntity(
                messageId = message.messageId,
                fromAgent = message.fromAgent.name,
                toAgent = message.toAgent.name,
                topic = message.topic,
                content = message.content,
                confidence = message.confidence,
            )
        })
        agiNetworkDao.upsertEvolutionHistory(
            AGIEvolutionHistoryEntity(
                evaluationId = result.selfLearningEvaluation.evaluationId,
                teachingImprovement = result.selfLearningEvaluation.teachingImprovement,
                recommendationAccuracyPercent = result.selfLearningEvaluation.recommendationAccuracyPercent,
                questionQualityPercent = result.selfLearningEvaluation.questionQualityPercent,
                contentQualityPercent = result.selfLearningEvaluation.contentQualityPercent,
                improvementTargets = result.selfLearningEvaluation.improvementTargets,
                createdAt = System.currentTimeMillis(),
            ),
        )
        agiNetworkDao.upsertKnowledgeEvolution(
            AGINetworkKnowledgeEvolutionEntity(
                proposalId = result.knowledgeEvolution.proposalId,
                topic = result.knowledgeEvolution.topic,
                missingConcepts = result.knowledgeEvolution.missingConcepts,
                improvedRelationships = result.knowledgeEvolution.improvedRelationships,
                materialUpdates = result.knowledgeEvolution.materialUpdates,
                verificationEvidence = result.knowledgeEvolution.verificationEvidence,
            ),
        )
        agiNetworkDao.upsertDecision(
            AGINetworkDecisionEntity(
                decisionId = result.educationalDecision.decisionId,
                learnerId = result.educationalDecision.learnerId,
                topic = result.educationalDecision.topic,
                teachingApproach = result.educationalDecision.teachingApproach,
                requiredContent = result.educationalDecision.requiredContent,
                difficultyAdjustment = result.educationalDecision.difficultyAdjustment,
                learningEnvironment = result.educationalDecision.learningEnvironment,
                assessmentStrategy = result.educationalDecision.assessmentStrategy,
                explanation = result.educationalDecision.explanation,
            ),
        )
        agiNetworkDao.upsertCurriculumEvolution(
            AGINetworkCurriculumEvolutionEntity(
                planId = result.curriculumEvolution.planId,
                topic = result.curriculumEvolution.topic,
                missingSkills = result.curriculumEvolution.missingSkills,
                generatedCourses = result.curriculumEvolution.generatedCourses,
                updateRecommendations = result.curriculumEvolution.updateRecommendations,
                approvalRequired = result.curriculumEvolution.approvalRequired,
            ),
        )
        agiNetworkDao.upsertAnalytics(
            AGINetworkAnalyticsEntity(
                reportId = result.analyticsReport.reportId,
                institutionId = result.analyticsReport.institutionId,
                globalPatterns = result.analyticsReport.globalPatterns,
                aiPerformancePercent = result.analyticsReport.aiPerformancePercent,
                studentSuccessPercent = result.analyticsReport.studentSuccessPercent,
                knowledgeGrowth = result.analyticsReport.knowledgeGrowth,
                recommendations = result.analyticsReport.recommendations,
            ),
        )
        agiNetworkDao.upsertGovernance(
            AGINetworkGovernanceRecordEntity(
                recordId = result.governanceRecord.recordId,
                decision = result.governanceRecord.decision.name,
                humanApprovalRequired = result.governanceRecord.humanApprovalRequired,
                transparencyNotes = result.governanceRecord.transparencyNotes,
                safetyRules = result.governanceRecord.safetyRules,
                auditSummary = result.governanceRecord.auditSummary,
            ),
        )
    }
}

data class AGINetworkDashboardState(
    val recentDecisions: List<String> = emptyList(),
    val activeTasks: List<String> = emptyList(),
    val governanceAlerts: List<String> = emptyList(),
    val aiPerformancePercent: Int = 0,
    val studentSuccessPercent: Int = 0,
    val curriculumDrafts: List<String> = emptyList(),
)
