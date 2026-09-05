package com.rola.app.unit

import com.rola.app.ai_os.agents.AgentRuntimeManager
import com.rola.app.ai_os.api.EducationAPIManager
import com.rola.app.ai_os.ecosystem.AIEducationOS
import com.rola.app.ai_os.extensions.AIExtensionManager
import com.rola.app.ai_os.intelligence.AIOSAgentType
import com.rola.app.ai_os.intelligence.AIOSPermission
import com.rola.app.ai_os.intelligence.AIOSRequest
import com.rola.app.ai_os.intelligence.AIOSServiceType
import com.rola.app.ai_os.intelligence.DecisionOrchestrator
import com.rola.app.ai_os.kernel.AIKernelManager
import com.rola.app.ai_os.memory.EducationMemoryCore
import com.rola.app.ai_os.monitoring.AIOSMonitoringEngine
import com.rola.app.ai_os.resources.AIResourceManager
import com.rola.app.ai_os.security.AIOSSecurityManager
import com.rola.app.ai_os.services.EducationServiceManager
import com.rola.app.ai_os.workflow.LearningWorkflowEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AIEducationOSPlatformTest {
    private val os = AIEducationOS(
        kernelManager = AIKernelManager(),
        serviceManager = EducationServiceManager(),
        agentRuntimeManager = AgentRuntimeManager(),
        decisionOrchestrator = DecisionOrchestrator(),
        workflowEngine = LearningWorkflowEngine(),
        memoryCore = EducationMemoryCore(),
        resourceManager = AIResourceManager(),
        apiManager = EducationAPIManager(),
        extensionManager = AIExtensionManager(),
        securityManager = AIOSSecurityManager(),
        monitoringEngine = AIOSMonitoringEngine(),
    )

    @Test
    fun aiEducationOS_bootsKernelServicesAndAgents() {
        val result = os.run(sampleRequest())

        assertTrue(result.kernelState.startedServices.contains(AIOSServiceType.AITeacher))
        assertTrue(result.serviceRegistry.discoveryEndpoints.any { it.contains("ai-os/services") })
        assertTrue(result.agentRuntime.activeAgents.contains(AIOSAgentType.Teacher))
    }

    @Test
    fun aiEducationOS_orchestratesDecisionWorkflowAndMemory() {
        val result = os.run(sampleRequest())

        assertEquals(AIOSAgentType.Teacher, result.decision.selectedAgent)
        assertTrue(result.workflowState.stages.contains("Knowledge retrieval"))
        assertTrue(result.memorySnapshot.shortTermMemory.any { it.contains("Energy Transfer") })
    }

    @Test
    fun aiEducationOS_securesApisExtensionsAndMonitoring() {
        val result = os.run(sampleRequest())

        assertTrue(result.apiPlan.authenticated)
        assertTrue(result.extensionPlan.validationSteps.contains("safety check"))
        assertTrue(result.securityReport.permissions.contains(AIOSPermission.HumanOverride))
        assertTrue(result.monitoringReport.responseQualityPercent >= 90)
    }

    private fun sampleRequest(): AIOSRequest =
        AIOSRequest(
            requestId = "ai-os-test",
            userId = "local-learner",
            institutionId = "local-institution",
            userNeed = "Explain and assess a science concept",
            activeTopic = "Energy Transfer",
            deviceContext = "Android AR device",
            requestedServices = listOf(
                AIOSServiceType.AITeacher,
                AIOSServiceType.AITutor,
                AIOSServiceType.Knowledge,
                AIOSServiceType.Analytics,
                AIOSServiceType.Translation,
                AIOSServiceType.Vision,
                AIOSServiceType.SpatialLearning,
                AIOSServiceType.RobotEducation,
            ),
        )
}

