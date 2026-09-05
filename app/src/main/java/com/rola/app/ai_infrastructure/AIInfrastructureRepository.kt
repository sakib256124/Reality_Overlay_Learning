package com.rola.app.ai_infrastructure

import com.rola.app.ai_infrastructure.models.AIInfrastructureResult
import com.rola.app.data.database.AIInfrastructureDao
import com.rola.app.data.database.entities.AIClusterEntity
import com.rola.app.data.database.entities.AIInfrastructureEntity
import com.rola.app.data.database.entities.CloudServiceEntity
import com.rola.app.data.database.entities.DeploymentHistoryEntity
import com.rola.app.data.database.entities.EdgeDeviceEntity
import com.rola.app.data.database.entities.ModelRegistryEntity
import com.rola.app.data.database.entities.ResourceMetricEntity
import com.rola.app.data.database.entities.ScalingEventEntity
import com.rola.app.data.database.entities.SystemHealthEntity
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class AIInfrastructureRepository @Inject constructor(
    private val dao: AIInfrastructureDao,
) {
    fun observeDashboard(institutionId: String): Flow<AIInfrastructureDashboardState> =
        combine(
            dao.observeInfrastructure(institutionId),
            dao.observeCloudServices(),
            dao.observeLatestCluster(),
            dao.observeLatestResourceMetric(),
            dao.observeLatestHealth(),
        ) { infrastructure, services, cluster, resource, health ->
            AIInfrastructureDashboardState(
                activeUsers = infrastructure?.activeUsers ?: 0,
                requestedServices = infrastructure?.requestedServices.orEmpty(),
                reliabilitySummary = infrastructure?.reliabilitySummary.orEmpty(),
                cloudServices = services.map { "${it.serviceType}: autoscale=${it.autoScalingEnabled}" },
                clusterRegions = cluster?.regions.orEmpty(),
                predictedScale = resource?.predictedScale ?: 0,
                serviceHealth = health?.serviceHealth.orEmpty(),
                responseTimeMs = health?.responseTimeMs ?: 0,
            )
        }.let { base ->
            combine(base, dao.observeScalingEvents(institutionId)) { dashboard, events ->
                dashboard.copy(scalingEvents = events.map { "${it.scalingReason}: ${it.recoveryActions.joinToString()}" })
            }
        }

    suspend fun saveResult(result: AIInfrastructureResult) {
        dao.upsertInfrastructure(
            AIInfrastructureEntity(
                infrastructureId = result.resultId,
                institutionId = result.request.institutionId,
                region = result.request.learnerRegion.name,
                activeUsers = result.request.activeUsers,
                requestedServices = result.request.requestedServices.map { it.name },
                reliabilitySummary = result.healthReport.cloudHealth,
            ),
        )
        dao.upsertCloudServices(
            result.cloudPlan.services.map {
                CloudServiceEntity(
                    serviceId = "cloud-service-${UUID.randomUUID()}",
                    serviceType = it.name,
                    autoScalingEnabled = result.cloudPlan.autoScalingEnabled,
                    faultRecoveryPlan = result.cloudPlan.faultRecoveryPlan,
                )
            },
        )
        dao.upsertEdgeDevices(
            result.edgePlan.devices.map {
                EdgeDeviceEntity(
                    deviceId = it,
                    planId = result.edgePlan.planId,
                    offlineCapability = result.edgePlan.offlineCapability,
                    latencyTargetMs = result.edgePlan.latencyTargetMs,
                    batteryStrategy = result.edgePlan.batteryStrategy,
                )
            },
        )
        dao.upsertCluster(
            AIClusterEntity(
                clusterId = result.clusterPlan.clusterId,
                regions = result.clusterPlan.regions.map { it.name },
                primaryRegion = result.request.learnerRegion.name,
                dataLocality = result.clusterPlan.dataLocality,
                disasterRecoveryPlan = result.clusterPlan.disasterRecoveryPlan,
            ),
        )
        dao.upsertModels(
            result.modelDeploymentPlan.modelVersions.map {
                ModelRegistryEntity(
                    modelId = "model-registry-${UUID.randomUUID()}",
                    deploymentId = result.modelDeploymentPlan.deploymentId,
                    modelVersion = it,
                    targetTiers = result.modelDeploymentPlan.targetTiers.map { tier -> tier.name },
                    testingRequired = result.modelDeploymentPlan.testingRequired,
                )
            },
        )
        dao.upsertDeployment(
            DeploymentHistoryEntity(
                deploymentId = result.modelDeploymentPlan.deploymentId,
                rollbackPlan = result.modelDeploymentPlan.rollbackPlan,
                status = "TestingRequired",
                createdAt = System.currentTimeMillis(),
            ),
        )
        dao.upsertResourceMetric(
            ResourceMetricEntity(
                metricId = "resource-metric-${UUID.randomUUID()}",
                optimizationId = result.resourceOptimizationPlan.optimizationId,
                cpuStrategy = result.resourceOptimizationPlan.cpuStrategy,
                gpuStrategy = result.resourceOptimizationPlan.gpuStrategy,
                storageStrategy = result.resourceOptimizationPlan.storageStrategy,
                networkStrategy = result.resourceOptimizationPlan.networkStrategy,
                predictedScale = result.resourceOptimizationPlan.predictedScale,
            ),
        )
        dao.upsertHealth(
            SystemHealthEntity(
                reportId = result.healthReport.reportId,
                responseTimeMs = result.healthReport.responseTimeMs,
                modelAccuracyPercent = result.healthReport.modelAccuracyPercent,
                serviceHealth = result.healthReport.serviceHealth.name,
                cloudHealth = result.healthReport.cloudHealth,
                auditNotes = result.healthReport.auditNotes,
            ),
        )
        dao.upsertScalingEvent(
            ScalingEventEntity(
                eventId = result.selfHealingPlan.healingId,
                institutionId = result.request.institutionId,
                scalingReason = result.selfHealingPlan.failurePrediction,
                recoveryActions = result.selfHealingPlan.recoveryActions,
                createdAt = System.currentTimeMillis(),
            ),
        )
    }
}

data class AIInfrastructureDashboardState(
    val activeUsers: Int = 0,
    val requestedServices: List<String> = emptyList(),
    val reliabilitySummary: String = "",
    val cloudServices: List<String> = emptyList(),
    val clusterRegions: List<String> = emptyList(),
    val predictedScale: Int = 0,
    val serviceHealth: String = "",
    val responseTimeMs: Int = 0,
    val scalingEvents: List<String> = emptyList(),
)
