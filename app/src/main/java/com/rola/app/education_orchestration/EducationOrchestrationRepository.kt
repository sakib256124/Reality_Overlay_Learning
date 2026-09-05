package com.rola.app.education_orchestration

import com.rola.app.data.database.EducationOrchestrationDao
import com.rola.app.data.database.entities.EcosystemAnalyticsEntity
import com.rola.app.data.database.entities.EcosystemOptimizationHistoryEntity
import com.rola.app.data.database.entities.EducationOrchestrationEntity
import com.rola.app.data.database.entities.OrchestrationAIServiceEntity
import com.rola.app.data.database.entities.OrchestrationAgentCoordinationEntity
import com.rola.app.data.database.entities.QualityMetricEntity
import com.rola.app.data.database.entities.SystemDecisionEntity
import com.rola.app.data.database.entities.WorkflowProcessEntity
import com.rola.app.education_orchestration.ecosystem_core.AutonomousEducationResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class EducationOrchestrationRepository @Inject constructor(private val dao: EducationOrchestrationDao) {
    fun observeDashboard(): Flow<EducationOrchestrationDashboardState> =
        combine(
            dao.observeOrchestration(),
            dao.observeServices(),
            dao.observeWorkflow(),
            dao.observeAgentCoordination(),
            dao.observeDecision(),
            dao.observeOptimization(),
            dao.observeQuality(),
            dao.observeAnalytics(),
        ) { values ->
            val orchestration = values[0] as EducationOrchestrationEntity?
            val services = values[1] as OrchestrationAIServiceEntity?
            val workflow = values[2] as WorkflowProcessEntity?
            val coordination = values[3] as OrchestrationAgentCoordinationEntity?
            val decision = values[4] as SystemDecisionEntity?
            val optimization = values[5] as EcosystemOptimizationHistoryEntity?
            val quality = values[6] as QualityMetricEntity?
            val analytics = values[7] as EcosystemAnalyticsEntity?
            EducationOrchestrationDashboardState(
                activeAISystems = services?.activeServices.orEmpty(),
                learningWorkflows = workflow?.lifecycleSteps.orEmpty(),
                aiDecisions = listOfNotNull(decision?.agentToUse, decision?.learningStrategy, decision?.transparency),
                ecosystemPerformance = optimization?.performanceScore ?: services?.performanceScore ?: 0,
                educationQuality = quality?.overallScore ?: 0,
                coordinationSummary = orchestration?.coordinationPlan.orEmpty() + coordination?.conflictResolution.orEmpty(),
                analyticsSummary = listOfNotNull(analytics?.governanceAudit, analytics?.systemHealth?.let { "system health: $it%" }),
                status = orchestration?.status.orEmpty(),
            )
        }

    suspend fun save(result: AutonomousEducationResult) {
        dao.upsertOrchestration(EducationOrchestrationEntity(result.orchestrationPlan.orchestrationId, result.orchestrationPlan.selectedCapabilities.map { it.name }, result.orchestrationPlan.coordinationPlan, result.orchestrationPlan.learningResult, result.status.name))
        dao.upsertServices(OrchestrationAIServiceEntity(result.serviceState.serviceId, result.serviceState.registeredAgents, result.serviceState.activeServices.map { it.name }, result.serviceState.communicationChannels, result.serviceState.resourceAllocation, result.serviceState.performanceScore))
        dao.upsertWorkflow(WorkflowProcessEntity(result.workflow.workflowId, result.workflow.lifecycleSteps, result.workflow.integratedSystems.map { it.name }, result.workflow.currentStage))
        dao.upsertAgentCoordination(OrchestrationAgentCoordinationEntity(result.agentCoordination.coordinationId, result.agentCoordination.teachingAgents, result.agentCoordination.researchAgents, result.agentCoordination.companionAgents, result.agentCoordination.knowledgeAgents, result.agentCoordination.assessmentAgents, result.agentCoordination.conflictResolution))
        dao.upsertDecision(SystemDecisionEntity(result.decision.decisionId, result.decision.agentToUse, result.decision.learningStrategy, result.decision.resourceToProvide, result.decision.adaptationRequired, result.decision.transparency))
        dao.upsertOptimization(EcosystemOptimizationHistoryEntity(result.optimization.optimizationId, result.optimization.resourceUsageScore, result.optimization.learningQualityScore, result.optimization.performanceScore, result.optimization.userExperienceScore, result.optimization.outcomeScore, result.optimization.recommendations))
        dao.upsertQuality(QualityMetricEntity(result.qualityScore.qualityId, result.qualityScore.learningEffectiveness, result.qualityScore.aiResponseQuality, result.qualityScore.contentAccuracy, result.qualityScore.userSatisfaction, result.qualityScore.overallScore))
        dao.upsertAnalytics(EcosystemAnalyticsEntity(result.monitoringReport.reportId, result.monitoringReport.monitoredServices, result.monitoringReport.learningProgress, result.monitoringReport.knowledgeGrowth, result.monitoringReport.userEngagement, result.monitoringReport.systemHealth, result.governance.auditSummary))
    }
}

data class EducationOrchestrationDashboardState(
    val activeAISystems: List<String> = emptyList(),
    val learningWorkflows: List<String> = emptyList(),
    val aiDecisions: List<String> = emptyList(),
    val ecosystemPerformance: Int = 0,
    val educationQuality: Int = 0,
    val coordinationSummary: List<String> = emptyList(),
    val analyticsSummary: List<String> = emptyList(),
    val status: String = "",
)
