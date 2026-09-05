package com.rola.app.unit

import com.rola.app.education_orchestration.ai_coordination.AIServiceCoordinator
import com.rola.app.education_orchestration.ai_coordination.AgentCoordinationManager
import com.rola.app.education_orchestration.ecosystem_core.AutonomousEducationManager
import com.rola.app.education_orchestration.ecosystem_core.EducationAIService
import com.rola.app.education_orchestration.ecosystem_core.EducationOrchestrationEngine
import com.rola.app.education_orchestration.ecosystem_core.OrchestrationRequest
import com.rola.app.education_orchestration.ecosystem_core.OrchestrationStatus
import com.rola.app.education_orchestration.governance.EducationQualityManager
import com.rola.app.education_orchestration.governance.OrchestrationGovernanceManager
import com.rola.app.education_orchestration.monitoring.EducationMonitoringManager
import com.rola.app.education_orchestration.optimization.EcosystemOptimizationEngine
import com.rola.app.education_orchestration.workflow.EducationDecisionEngine
import com.rola.app.education_orchestration.workflow.LearningWorkflowCoordinator
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EducationOrchestrationPlatformTest {
    private val manager = AutonomousEducationManager(
        EducationOrchestrationEngine(),
        AIServiceCoordinator(),
        LearningWorkflowCoordinator(),
        EcosystemOptimizationEngine(),
        EducationMonitoringManager(),
        EducationDecisionEngine(),
        AgentCoordinationManager(),
        EducationQualityManager(),
        OrchestrationGovernanceManager(),
    )

    @Test
    fun orchestration_selectsServicesCoordinatesWorkflowAndAppliesGovernance() {
        val result = manager.manage(
            OrchestrationRequest(
                userId = "orchestration-learner",
                requirement = "Help me learn machine learning",
                learningState = "needs goal, planning, knowledge, assessment, and mastery support",
                activeSystems = listOf(EducationAIService.CompanionAI, EducationAIService.MasteryAI),
                userFeedback = 88,
                systemHealth = 93,
            ),
        )

        assertEquals(OrchestrationStatus.Optimized, result.status)
        assertTrue(result.orchestrationPlan.selectedCapabilities.contains(EducationAIService.PlanningAI))
        assertTrue(result.orchestrationPlan.selectedCapabilities.contains(EducationAIService.ResearchAI))
        assertTrue(result.serviceState.registeredAgents.isNotEmpty())
        assertTrue(result.workflow.lifecycleSteps.contains("mastery analysis"))
        assertTrue(result.optimization.recommendations.contains("balance AI workload"))
        assertTrue(result.monitoringReport.systemHealth >= 90)
        assertTrue(result.decision.transparency.contains("learner state"))
        assertTrue(result.agentCoordination.conflictResolution.contains("human override"))
        assertTrue(result.qualityScore.overallScore > 80)
        assertTrue(result.governance.humanOverride)
    }
}
