package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "education_orchestration", indices = [Index(value = ["status"])])
data class EducationOrchestrationEntity(@PrimaryKey val orchestrationId: String, val selectedCapabilities: List<String>, val coordinationPlan: List<String>, val learningResult: String, val status: String)
@Entity(tableName = "orchestration_ai_services", indices = [Index(value = ["performanceScore"])])
data class OrchestrationAIServiceEntity(@PrimaryKey val serviceId: String, val registeredAgents: List<String>, val activeServices: List<String>, val communicationChannels: List<String>, val resourceAllocation: String, val performanceScore: Int)
@Entity(tableName = "workflow_processes", indices = [Index(value = ["currentStage"])])
data class WorkflowProcessEntity(@PrimaryKey val workflowId: String, val lifecycleSteps: List<String>, val integratedSystems: List<String>, val currentStage: String)
@Entity(tableName = "agent_coordination", indices = [Index(value = ["coordinationId"])])
data class OrchestrationAgentCoordinationEntity(@PrimaryKey val coordinationId: String, val teachingAgents: List<String>, val researchAgents: List<String>, val companionAgents: List<String>, val knowledgeAgents: List<String>, val assessmentAgents: List<String>, val conflictResolution: String)
@Entity(tableName = "system_decisions", indices = [Index(value = ["adaptationRequired"])])
data class SystemDecisionEntity(@PrimaryKey val decisionId: String, val agentToUse: String, val learningStrategy: String, val resourceToProvide: String, val adaptationRequired: Boolean, val transparency: String)
@Entity(tableName = "ecosystem_optimization_history", indices = [Index(value = ["outcomeScore"])])
data class EcosystemOptimizationHistoryEntity(@PrimaryKey val optimizationId: String, val resourceUsageScore: Int, val learningQualityScore: Int, val performanceScore: Int, val userExperienceScore: Int, val outcomeScore: Int, val recommendations: List<String>)
@Entity(tableName = "quality_metrics", indices = [Index(value = ["overallScore"])])
data class QualityMetricEntity(@PrimaryKey val qualityId: String, val learningEffectiveness: Int, val aiResponseQuality: Int, val contentAccuracy: Int, val userSatisfaction: Int, val overallScore: Int)
@Entity(tableName = "ecosystem_analytics", indices = [Index(value = ["systemHealth"])])
data class EcosystemAnalyticsEntity(@PrimaryKey val reportId: String, val monitoredServices: List<String>, val learningProgress: Int, val knowledgeGrowth: Int, val userEngagement: Int, val systemHealth: Int, val governanceAudit: String)
