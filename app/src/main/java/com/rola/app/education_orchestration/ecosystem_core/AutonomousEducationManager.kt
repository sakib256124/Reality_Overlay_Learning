package com.rola.app.education_orchestration.ecosystem_core

import com.rola.app.education_orchestration.ai_coordination.AIServiceCoordinator
import com.rola.app.education_orchestration.ai_coordination.AgentCoordinationManager
import com.rola.app.education_orchestration.governance.EducationQualityManager
import com.rola.app.education_orchestration.governance.OrchestrationGovernanceManager
import com.rola.app.education_orchestration.monitoring.EducationMonitoringManager
import com.rola.app.education_orchestration.optimization.EcosystemOptimizationEngine
import com.rola.app.education_orchestration.workflow.EducationDecisionEngine
import com.rola.app.education_orchestration.workflow.LearningWorkflowCoordinator
import javax.inject.Inject

class AutonomousEducationManager @Inject constructor(
    private val orchestrationEngine: EducationOrchestrationEngine,
    private val serviceCoordinator: AIServiceCoordinator,
    private val workflowCoordinator: LearningWorkflowCoordinator,
    private val optimizationEngine: EcosystemOptimizationEngine,
    private val monitoringManager: EducationMonitoringManager,
    private val decisionEngine: EducationDecisionEngine,
    private val agentCoordinationManager: AgentCoordinationManager,
    private val qualityManager: EducationQualityManager,
    private val governanceManager: OrchestrationGovernanceManager,
) {
    fun manage(request: OrchestrationRequest): AutonomousEducationResult {
        val plan = orchestrationEngine.orchestrate(request)
        val serviceState = serviceCoordinator.coordinate(plan)
        val workflow = workflowCoordinator.createWorkflow(request)
        val optimization = optimizationEngine.optimize(request, serviceState)
        val report = monitoringManager.monitor(request, serviceState)
        val decision = decisionEngine.decide(request, report)
        return AutonomousEducationResult(
            resultId = "autonomous-education-${request.userId}",
            orchestrationPlan = plan,
            serviceState = serviceState,
            workflow = workflow,
            optimization = optimization,
            monitoringReport = report,
            decision = decision,
            agentCoordination = agentCoordinationManager.coordinate(decision),
            qualityScore = qualityManager.evaluate(optimization),
            governance = governanceManager.govern(decision),
            status = if (request.systemHealth < 60) OrchestrationStatus.NeedsHumanOverride else OrchestrationStatus.Optimized,
        )
    }
}
