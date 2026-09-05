package com.rola.app.ai_infrastructure.models

enum class InfrastructureRegion {
    NorthAmerica,
    Europe,
    AsiaPacific,
    GlobalSouth,
    EdgeLocal,
}

enum class AIServiceType {
    AITeacher,
    AITutor,
    Knowledge,
    Research,
    Analytics,
    Translation,
    Vision,
    Metaverse,
}

enum class ComputeTier {
    MobileEdge,
    RegionalCloud,
    GlobalCluster,
}

enum class ServiceHealth {
    Healthy,
    Degraded,
    Recovering,
    Offline,
}

data class AIInfrastructureRequest(
    val requestId: String,
    val institutionId: String,
    val learnerRegion: InfrastructureRegion,
    val activeUsers: Int,
    val requestedServices: List<AIServiceType>,
    val edgeDevices: List<String>,
    val workloadSignals: List<String>,
)

data class DistributedAIWorkload(
    val workloadId: String,
    val services: List<AIServiceType>,
    val processingNodes: List<String>,
    val responsePlan: String,
)

data class CloudAIPlan(
    val planId: String,
    val models: List<String>,
    val agents: List<String>,
    val services: List<AIServiceType>,
    val autoScalingEnabled: Boolean,
    val faultRecoveryPlan: String,
)

data class EdgeAIPlan(
    val planId: String,
    val devices: List<String>,
    val offlineCapability: Boolean,
    val latencyTargetMs: Int,
    val batteryStrategy: String,
)

data class AIServiceMeshPlan(
    val meshId: String,
    val serviceDiscovery: List<String>,
    val secureRoutes: List<String>,
    val failureHandling: List<String>,
)

data class GlobalAIClusterPlan(
    val clusterId: String,
    val regions: List<InfrastructureRegion>,
    val dataLocality: String,
    val disasterRecoveryPlan: String,
)

data class AIModelDeploymentPlan(
    val deploymentId: String,
    val modelVersions: List<String>,
    val targetTiers: List<ComputeTier>,
    val rollbackPlan: String,
    val testingRequired: Boolean,
)

data class ResourceOptimizationPlan(
    val optimizationId: String,
    val cpuStrategy: String,
    val gpuStrategy: String,
    val storageStrategy: String,
    val networkStrategy: String,
    val predictedScale: Int,
)

data class InfrastructureHealthReport(
    val reportId: String,
    val responseTimeMs: Int,
    val modelAccuracyPercent: Int,
    val serviceHealth: ServiceHealth,
    val cloudHealth: String,
    val auditNotes: List<String>,
)

data class SelfHealingPlan(
    val healingId: String,
    val detectedProblems: List<String>,
    val recoveryActions: List<String>,
    val failurePrediction: String,
)

data class InfrastructureSecurityReport(
    val securityId: String,
    val zeroTrustEnabled: Boolean,
    val authenticatedServices: List<AIServiceType>,
    val encryptedCommunication: Boolean,
    val accessControlSummary: String,
    val auditLoggingEnabled: Boolean,
)

data class AIInfrastructureResult(
    val resultId: String,
    val request: AIInfrastructureRequest,
    val distributedWorkload: DistributedAIWorkload,
    val cloudPlan: CloudAIPlan,
    val edgePlan: EdgeAIPlan,
    val serviceMeshPlan: AIServiceMeshPlan,
    val clusterPlan: GlobalAIClusterPlan,
    val modelDeploymentPlan: AIModelDeploymentPlan,
    val resourceOptimizationPlan: ResourceOptimizationPlan,
    val healthReport: InfrastructureHealthReport,
    val selfHealingPlan: SelfHealingPlan,
    val securityReport: InfrastructureSecurityReport,
)

