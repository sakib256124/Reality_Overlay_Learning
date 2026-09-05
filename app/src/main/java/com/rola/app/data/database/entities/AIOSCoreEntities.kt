package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "ai_os_config", indices = [Index(value = ["institutionId"])])
data class AIOSConfigEntity(
    @PrimaryKey val configId: String,
    val institutionId: String,
    val startedServices: List<String>,
    val executionPlan: String,
    val systemIntelligence: String,
)

@Entity(tableName = "ai_services", indices = [Index(value = ["serviceType"])])
data class AIOSServiceEntity(
    @PrimaryKey val serviceId: String,
    val serviceType: String,
    val discoveryEndpoint: String,
    val healthSummary: String,
)

@Entity(tableName = "agent_registry", indices = [Index(value = ["agentType"])])
data class AgentRegistryEntity(
    @PrimaryKey val agentId: String,
    val agentType: String,
    val assignedTask: String,
    val communicationPlan: String,
)

@Entity(tableName = "workflow_history", indices = [Index(value = ["userId"]), Index(value = ["topic"])])
data class WorkflowHistoryEntity(
    @PrimaryKey val workflowId: String,
    val userId: String,
    val topic: String,
    val stages: List<String>,
    val dynamicLearningPath: List<String>,
    val progressUpdate: String,
)

@Entity(tableName = "memory_core", indices = [Index(value = ["userId"])])
data class MemoryCoreEntity(
    @PrimaryKey val memoryId: String,
    val userId: String,
    val shortTermMemory: List<String>,
    val longTermMemory: List<String>,
    val achievementMemory: List<String>,
)

@Entity(tableName = "resource_registry", indices = [Index(value = ["institutionId"])])
data class ResourceRegistryEntity(
    @PrimaryKey val resourceId: String,
    val institutionId: String,
    val aiModels: List<String>,
    val cloudResources: List<String>,
    val edgeResources: List<String>,
    val virtualAssets: List<String>,
    val optimizationSummary: String,
)

@Entity(tableName = "system_events", indices = [Index(value = ["eventType"]), Index(value = ["createdAt"])])
data class SystemEventEntity(
    @PrimaryKey val eventId: String,
    val eventType: String,
    val summary: String,
    val createdAt: Long,
)

@Entity(tableName = "security_logs", indices = [Index(value = ["userId"]), Index(value = ["createdAt"])])
data class SecurityLogEntity(
    @PrimaryKey val securityId: String,
    val userId: String,
    val permissions: List<String>,
    val dataProtected: Boolean,
    val humanOverrideEnabled: Boolean,
    val auditSummary: String,
    val createdAt: Long,
)

@Entity(tableName = "extension_registry", indices = [Index(value = ["extensionId"])])
data class ExtensionRegistryEntity(
    @PrimaryKey val extensionId: String,
    val registeredExtensions: List<String>,
    val validationSteps: List<String>,
    val integrationSummary: String,
)

