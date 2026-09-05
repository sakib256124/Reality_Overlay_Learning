package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "ai_infrastructure", indices = [Index(value = ["institutionId"]), Index(value = ["region"])])
data class AIInfrastructureEntity(
    @PrimaryKey val infrastructureId: String,
    val institutionId: String,
    val region: String,
    val activeUsers: Int,
    val requestedServices: List<String>,
    val reliabilitySummary: String,
)

@Entity(tableName = "cloud_services", indices = [Index(value = ["serviceType"])])
data class CloudServiceEntity(
    @PrimaryKey val serviceId: String,
    val serviceType: String,
    val autoScalingEnabled: Boolean,
    val faultRecoveryPlan: String,
)

@Entity(tableName = "edge_devices", indices = [Index(value = ["deviceId"])])
data class EdgeDeviceEntity(
    @PrimaryKey val deviceId: String,
    val planId: String,
    val offlineCapability: Boolean,
    val latencyTargetMs: Int,
    val batteryStrategy: String,
)

@Entity(tableName = "ai_clusters", indices = [Index(value = ["primaryRegion"])])
data class AIClusterEntity(
    @PrimaryKey val clusterId: String,
    val regions: List<String>,
    val primaryRegion: String,
    val dataLocality: String,
    val disasterRecoveryPlan: String,
)

@Entity(tableName = "model_registry", indices = [Index(value = ["deploymentId"])])
data class ModelRegistryEntity(
    @PrimaryKey val modelId: String,
    val deploymentId: String,
    val modelVersion: String,
    val targetTiers: List<String>,
    val testingRequired: Boolean,
)

@Entity(tableName = "deployment_history", indices = [Index(value = ["createdAt"])])
data class DeploymentHistoryEntity(
    @PrimaryKey val deploymentId: String,
    val rollbackPlan: String,
    val status: String,
    val createdAt: Long,
)

@Entity(tableName = "resource_metrics", indices = [Index(value = ["optimizationId"])])
data class ResourceMetricEntity(
    @PrimaryKey val metricId: String,
    val optimizationId: String,
    val cpuStrategy: String,
    val gpuStrategy: String,
    val storageStrategy: String,
    val networkStrategy: String,
    val predictedScale: Int,
)

@Entity(tableName = "system_health", indices = [Index(value = ["serviceHealth"])])
data class SystemHealthEntity(
    @PrimaryKey val reportId: String,
    val responseTimeMs: Int,
    val modelAccuracyPercent: Int,
    val serviceHealth: String,
    val cloudHealth: String,
    val auditNotes: List<String>,
)

@Entity(tableName = "scaling_events", indices = [Index(value = ["institutionId"]), Index(value = ["createdAt"])])
data class ScalingEventEntity(
    @PrimaryKey val eventId: String,
    val institutionId: String,
    val scalingReason: String,
    val recoveryActions: List<String>,
    val createdAt: Long,
)

