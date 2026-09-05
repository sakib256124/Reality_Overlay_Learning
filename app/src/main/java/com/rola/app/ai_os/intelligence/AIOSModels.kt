package com.rola.app.ai_os.intelligence

enum class AIOSServiceType {
    AITeacher,
    AITutor,
    Knowledge,
    Research,
    Analytics,
    Translation,
    Vision,
    SpatialLearning,
    RobotEducation,
}

enum class AIOSAgentType {
    Teacher,
    Research,
    Knowledge,
    Cognitive,
    Robot,
    Spatial,
    Assessment,
}

enum class AIOSPermission {
    UseAIService,
    RunAgent,
    AccessMemory,
    GenerateContent,
    ViewAnalytics,
    HumanOverride,
}

enum class AIOSHealth {
    Operational,
    Degraded,
    RecoveryRequired,
}

data class AIOSRequest(
    val requestId: String,
    val userId: String,
    val institutionId: String,
    val userNeed: String,
    val activeTopic: String,
    val deviceContext: String,
    val requestedServices: List<AIOSServiceType>,
)

data class AIKernelState(
    val kernelId: String,
    val startedServices: List<AIOSServiceType>,
    val executionPlan: String,
    val systemIntelligence: String,
)

data class EducationServiceRegistry(
    val registryId: String,
    val activeServices: List<AIOSServiceType>,
    val discoveryEndpoints: List<String>,
    val healthSummary: String,
)

data class AgentRuntimeState(
    val runtimeId: String,
    val activeAgents: List<AIOSAgentType>,
    val assignedTasks: List<String>,
    val communicationPlan: String,
)

data class LearningWorkflowState(
    val workflowId: String,
    val stages: List<String>,
    val dynamicLearningPath: List<String>,
    val progressUpdate: String,
)

data class EducationMemorySnapshot(
    val memoryId: String,
    val shortTermMemory: List<String>,
    val longTermMemory: List<String>,
    val achievementMemory: List<String>,
)

data class AIResourcePlan(
    val resourceId: String,
    val aiModels: List<String>,
    val cloudResources: List<String>,
    val edgeResources: List<String>,
    val virtualAssets: List<String>,
    val optimizationSummary: String,
)

data class AIOSDecision(
    val decisionId: String,
    val selectedAgent: AIOSAgentType,
    val knowledgeSource: String,
    val teachingMethod: String,
    val generatedActivity: String,
    val explanation: String,
)

data class EducationAPIPlan(
    val apiId: String,
    val supportedClients: List<String>,
    val authenticated: Boolean,
    val responseContract: String,
)

data class AIExtensionPlan(
    val extensionId: String,
    val registeredExtensions: List<String>,
    val validationSteps: List<String>,
    val integrationSummary: String,
)

data class AIOSSecurityReport(
    val securityId: String,
    val identityManaged: Boolean,
    val permissions: Set<AIOSPermission>,
    val dataProtected: Boolean,
    val humanOverrideEnabled: Boolean,
    val auditSummary: String,
)

data class AIOSMonitoringReport(
    val reportId: String,
    val agentPerformancePercent: Int,
    val responseQualityPercent: Int,
    val health: AIOSHealth,
    val learningImprovement: String,
    val resourceUsageSummary: String,
)

data class AIEducationOSResult(
    val resultId: String,
    val request: AIOSRequest,
    val kernelState: AIKernelState,
    val serviceRegistry: EducationServiceRegistry,
    val agentRuntime: AgentRuntimeState,
    val workflowState: LearningWorkflowState,
    val memorySnapshot: EducationMemorySnapshot,
    val resourcePlan: AIResourcePlan,
    val decision: AIOSDecision,
    val apiPlan: EducationAPIPlan,
    val extensionPlan: AIExtensionPlan,
    val securityReport: AIOSSecurityReport,
    val monitoringReport: AIOSMonitoringReport,
)

