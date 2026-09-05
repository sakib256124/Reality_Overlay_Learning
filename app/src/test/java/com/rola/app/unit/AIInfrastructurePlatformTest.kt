package com.rola.app.unit

import com.rola.app.ai_infrastructure.cloud_ai.AIModelInfrastructureManager
import com.rola.app.ai_infrastructure.cloud_ai.CloudAIOrchestrator
import com.rola.app.ai_infrastructure.cloud_ai.GlobalAIClusterManager
import com.rola.app.ai_infrastructure.distributed_ai.DistributedAIEngine
import com.rola.app.ai_infrastructure.distributed_ai.DistributedAIManager
import com.rola.app.ai_infrastructure.edge_ai.EdgeAIController
import com.rola.app.ai_infrastructure.models.AIInfrastructureRequest
import com.rola.app.ai_infrastructure.models.AIServiceType
import com.rola.app.ai_infrastructure.models.InfrastructureRegion
import com.rola.app.ai_infrastructure.models.ServiceHealth
import com.rola.app.ai_infrastructure.monitoring.InfrastructureMonitoringManager
import com.rola.app.ai_infrastructure.orchestration.AIInfrastructureEngine
import com.rola.app.ai_infrastructure.scaling.ResourceOptimizationEngine
import com.rola.app.ai_infrastructure.security.InfrastructureSecurityManager
import com.rola.app.ai_infrastructure.self_healing.SelfHealingInfrastructureEngine
import com.rola.app.ai_infrastructure.service_mesh.AIServiceManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AIInfrastructurePlatformTest {
    private val engine = AIInfrastructureEngine(
        distributedAIManager = DistributedAIManager(DistributedAIEngine()),
        cloudAIOrchestrator = CloudAIOrchestrator(),
        edgeAIController = EdgeAIController(),
        serviceManager = AIServiceManager(),
        clusterManager = GlobalAIClusterManager(),
        modelInfrastructureManager = AIModelInfrastructureManager(),
        resourceOptimizationEngine = ResourceOptimizationEngine(),
        monitoringManager = InfrastructureMonitoringManager(),
        selfHealingEngine = SelfHealingInfrastructureEngine(),
        securityManager = InfrastructureSecurityManager(),
    )

    @Test
    fun infrastructureCycle_distributesWorkloadsAcrossCloudAndEdge() {
        val result = engine.optimizeInfrastructure(sampleRequest())

        assertTrue(result.distributedWorkload.processingNodes.any { it.contains("vision") })
        assertTrue(result.cloudPlan.autoScalingEnabled)
        assertTrue(result.edgePlan.offlineCapability)
        assertTrue(result.edgePlan.latencyTargetMs <= 100)
    }

    @Test
    fun infrastructureCycle_buildsServiceMeshClustersAndModelDeployment() {
        val result = engine.optimizeInfrastructure(sampleRequest())

        assertTrue(result.serviceMeshPlan.secureRoutes.any { it.contains("mTLS") })
        assertTrue(result.clusterPlan.regions.contains(InfrastructureRegion.AsiaPacific))
        assertTrue(result.modelDeploymentPlan.testingRequired)
        assertTrue(result.modelDeploymentPlan.rollbackPlan.contains("rollback", ignoreCase = true))
    }

    @Test
    fun infrastructureCycle_monitorsHealsAndSecuresHighLoadSystem() {
        val result = engine.optimizeInfrastructure(sampleRequest())

        assertEquals(ServiceHealth.Degraded, result.healthReport.serviceHealth)
        assertTrue(result.selfHealingPlan.recoveryActions.any { it.contains("scale", ignoreCase = true) })
        assertTrue(result.resourceOptimizationPlan.predictedScale > sampleRequest().activeUsers)
        assertTrue(result.securityReport.zeroTrustEnabled)
        assertTrue(result.securityReport.encryptedCommunication)
    }

    private fun sampleRequest(): AIInfrastructureRequest =
        AIInfrastructureRequest(
            requestId = "infra-test",
            institutionId = "institution-1",
            learnerRegion = InfrastructureRegion.AsiaPacific,
            activeUsers = 125_000,
            requestedServices = listOf(
                AIServiceType.AITeacher,
                AIServiceType.AITutor,
                AIServiceType.Knowledge,
                AIServiceType.Vision,
                AIServiceType.Translation,
                AIServiceType.Metaverse,
            ),
            edgeDevices = listOf("android-phone", "ar-glasses", "education-robot"),
            workloadSignals = listOf("peak classroom traffic", "vision inference load", "regional metaverse sessions"),
        )
}

