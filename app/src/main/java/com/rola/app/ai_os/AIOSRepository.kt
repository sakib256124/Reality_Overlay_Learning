package com.rola.app.ai_os

import com.rola.app.ai_os.intelligence.AIEducationOSResult
import com.rola.app.data.database.AIOSCoreDao
import com.rola.app.data.database.entities.AIOSConfigEntity
import com.rola.app.data.database.entities.AIOSServiceEntity
import com.rola.app.data.database.entities.AgentRegistryEntity
import com.rola.app.data.database.entities.ExtensionRegistryEntity
import com.rola.app.data.database.entities.MemoryCoreEntity
import com.rola.app.data.database.entities.ResourceRegistryEntity
import com.rola.app.data.database.entities.SecurityLogEntity
import com.rola.app.data.database.entities.SystemEventEntity
import com.rola.app.data.database.entities.WorkflowHistoryEntity
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class AIOSRepository @Inject constructor(
    private val dao: AIOSCoreDao,
) {
    fun observeDashboard(institutionId: String, userId: String): Flow<AIOSDashboardState> =
        combine(
            dao.observeConfig(institutionId),
            dao.observeServices(),
            dao.observeAgents(),
            dao.observeWorkflow(userId),
            dao.observeMemory(userId),
        ) { config, services, agents, workflow, memory ->
            AIOSDashboardState(
                startedServices = config?.startedServices.orEmpty(),
                executionPlan = config?.executionPlan.orEmpty(),
                services = services.map { "${it.serviceType}: ${it.discoveryEndpoint}" },
                agents = agents.map { "${it.agentType}: ${it.assignedTask}" },
                workflowStages = workflow?.stages.orEmpty(),
                memorySummary = memory?.shortTermMemory.orEmpty() + memory?.achievementMemory.orEmpty(),
            )
        }.let { base ->
            combine(base, dao.observeSecurity(userId), dao.observeEvents()) { dashboard, security, events ->
                dashboard.copy(
                    securitySummary = security?.auditSummary.orEmpty(),
                    systemEvents = events.map { "${it.eventType}: ${it.summary}" },
                )
            }
        }

    suspend fun saveResult(result: AIEducationOSResult) {
        dao.upsertConfig(
            AIOSConfigEntity(
                configId = result.kernelState.kernelId,
                institutionId = result.request.institutionId,
                startedServices = result.kernelState.startedServices.map { it.name },
                executionPlan = result.kernelState.executionPlan,
                systemIntelligence = result.kernelState.systemIntelligence,
            ),
        )
        dao.upsertServices(
            result.serviceRegistry.activeServices.zip(result.serviceRegistry.discoveryEndpoints).map { (service, endpoint) ->
                AIOSServiceEntity(
                    serviceId = "ai-os-service-${UUID.randomUUID()}",
                    serviceType = service.name,
                    discoveryEndpoint = endpoint,
                    healthSummary = result.serviceRegistry.healthSummary,
                )
            },
        )
        dao.upsertAgents(
            result.agentRuntime.activeAgents.zip(result.agentRuntime.assignedTasks).map { (agent, task) ->
                AgentRegistryEntity(
                    agentId = "agent-registry-${UUID.randomUUID()}",
                    agentType = agent.name,
                    assignedTask = task,
                    communicationPlan = result.agentRuntime.communicationPlan,
                )
            },
        )
        dao.upsertWorkflow(
            WorkflowHistoryEntity(
                workflowId = result.workflowState.workflowId,
                userId = result.request.userId,
                topic = result.request.activeTopic,
                stages = result.workflowState.stages,
                dynamicLearningPath = result.workflowState.dynamicLearningPath,
                progressUpdate = result.workflowState.progressUpdate,
            ),
        )
        dao.upsertMemory(
            MemoryCoreEntity(
                memoryId = result.memorySnapshot.memoryId,
                userId = result.request.userId,
                shortTermMemory = result.memorySnapshot.shortTermMemory,
                longTermMemory = result.memorySnapshot.longTermMemory,
                achievementMemory = result.memorySnapshot.achievementMemory,
            ),
        )
        dao.upsertResource(
            ResourceRegistryEntity(
                resourceId = result.resourcePlan.resourceId,
                institutionId = result.request.institutionId,
                aiModels = result.resourcePlan.aiModels,
                cloudResources = result.resourcePlan.cloudResources,
                edgeResources = result.resourcePlan.edgeResources,
                virtualAssets = result.resourcePlan.virtualAssets,
                optimizationSummary = result.resourcePlan.optimizationSummary,
            ),
        )
        dao.upsertSecurity(
            SecurityLogEntity(
                securityId = result.securityReport.securityId,
                userId = result.request.userId,
                permissions = result.securityReport.permissions.map { it.name },
                dataProtected = result.securityReport.dataProtected,
                humanOverrideEnabled = result.securityReport.humanOverrideEnabled,
                auditSummary = result.securityReport.auditSummary,
                createdAt = System.currentTimeMillis(),
            ),
        )
        dao.upsertExtension(
            ExtensionRegistryEntity(
                extensionId = result.extensionPlan.extensionId,
                registeredExtensions = result.extensionPlan.registeredExtensions,
                validationSteps = result.extensionPlan.validationSteps,
                integrationSummary = result.extensionPlan.integrationSummary,
            ),
        )
        dao.upsertEvent(
            SystemEventEntity(
                eventId = result.monitoringReport.reportId,
                eventType = result.monitoringReport.health.name,
                summary = result.monitoringReport.learningImprovement,
                createdAt = System.currentTimeMillis(),
            ),
        )
    }
}

data class AIOSDashboardState(
    val startedServices: List<String> = emptyList(),
    val executionPlan: String = "",
    val services: List<String> = emptyList(),
    val agents: List<String> = emptyList(),
    val workflowStages: List<String> = emptyList(),
    val memorySummary: List<String> = emptyList(),
    val securitySummary: String = "",
    val systemEvents: List<String> = emptyList(),
)
