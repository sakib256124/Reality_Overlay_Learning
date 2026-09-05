package com.rola.app.ai_os.ecosystem

import com.rola.app.ai_os.agents.AgentRuntimeManager
import com.rola.app.ai_os.api.EducationAPIManager
import com.rola.app.ai_os.extensions.AIExtensionManager
import com.rola.app.ai_os.intelligence.AIEducationOSResult
import com.rola.app.ai_os.intelligence.AIOSRequest
import com.rola.app.ai_os.intelligence.DecisionOrchestrator
import com.rola.app.ai_os.kernel.AIKernelManager
import com.rola.app.ai_os.memory.EducationMemoryCore
import com.rola.app.ai_os.monitoring.AIOSMonitoringEngine
import com.rola.app.ai_os.resources.AIResourceManager
import com.rola.app.ai_os.security.AIOSSecurityManager
import com.rola.app.ai_os.services.EducationServiceManager
import com.rola.app.ai_os.workflow.LearningWorkflowEngine
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIEducationOS @Inject constructor(
    private val kernelManager: AIKernelManager,
    private val serviceManager: EducationServiceManager,
    private val agentRuntimeManager: AgentRuntimeManager,
    private val decisionOrchestrator: DecisionOrchestrator,
    private val workflowEngine: LearningWorkflowEngine,
    private val memoryCore: EducationMemoryCore,
    private val resourceManager: AIResourceManager,
    private val apiManager: EducationAPIManager,
    private val extensionManager: AIExtensionManager,
    private val securityManager: AIOSSecurityManager,
    private val monitoringEngine: AIOSMonitoringEngine,
) {
    fun run(request: AIOSRequest): AIEducationOSResult {
        val decision = decisionOrchestrator.decide(request)
        return AIEducationOSResult(
            resultId = "ai-education-os-${UUID.randomUUID()}",
            request = request,
            kernelState = kernelManager.boot(request),
            serviceRegistry = serviceManager.registerServices(request),
            agentRuntime = agentRuntimeManager.startAgents(request),
            workflowState = workflowEngine.buildWorkflow(request, decision),
            memorySnapshot = memoryCore.snapshot(request),
            resourcePlan = resourceManager.allocate(request),
            decision = decision,
            apiPlan = apiManager.expose(request),
            extensionPlan = extensionManager.validateExtensions(),
            securityReport = securityManager.secure(request),
            monitoringReport = monitoringEngine.monitor(request),
        )
    }
}
