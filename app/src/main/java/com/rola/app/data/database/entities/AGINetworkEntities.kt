package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "agi_network_agents", indices = [Index(value = ["role"]), Index(value = ["active"])])
data class AGINetworkAgentEntity(
    @PrimaryKey val agentId: String,
    val role: String,
    val name: String,
    val capabilities: List<String>,
    val active: Boolean,
)

@Entity(tableName = "agi_network_agent_tasks", indices = [Index(value = ["agentRole"]), Index(value = ["status"])])
data class AGINetworkAgentTaskEntity(
    @PrimaryKey val taskId: String,
    val agentRole: String,
    val title: String,
    val priority: Int,
    val status: String,
    val evidence: List<String>,
)

@Entity(tableName = "agi_network_agent_communication", indices = [Index(value = ["topic"]), Index(value = ["fromAgent"]), Index(value = ["toAgent"])])
data class AGINetworkAgentCommunicationEntity(
    @PrimaryKey val messageId: String,
    val fromAgent: String,
    val toAgent: String,
    val topic: String,
    val content: String,
    val confidence: Float,
)

@Entity(tableName = "agi_network_ai_evolution_history", indices = [Index(value = ["createdAt"])])
data class AIEvolutionHistoryEntity(
    @PrimaryKey val evaluationId: String,
    val teachingImprovement: String,
    val recommendationAccuracyPercent: Int,
    val questionQualityPercent: Int,
    val contentQualityPercent: Int,
    val improvementTargets: List<String>,
    val createdAt: Long,
)

@Entity(tableName = "agi_network_knowledge_evolution", indices = [Index(value = ["topic"])])
data class AGINetworkKnowledgeEvolutionEntity(
    @PrimaryKey val proposalId: String,
    val topic: String,
    val missingConcepts: List<String>,
    val improvedRelationships: List<String>,
    val materialUpdates: List<String>,
    val verificationEvidence: List<String>,
)

@Entity(tableName = "agi_network_ai_decisions", indices = [Index(value = ["learnerId"]), Index(value = ["topic"])])
data class AGINetworkDecisionEntity(
    @PrimaryKey val decisionId: String,
    val learnerId: String,
    val topic: String,
    val teachingApproach: String,
    val requiredContent: List<String>,
    val difficultyAdjustment: String,
    val learningEnvironment: String,
    val assessmentStrategy: String,
    val explanation: String,
)

@Entity(tableName = "agi_network_curriculum_evolution", indices = [Index(value = ["topic"]), Index(value = ["approvalRequired"])])
data class AGINetworkCurriculumEvolutionEntity(
    @PrimaryKey val planId: String,
    val topic: String,
    val missingSkills: List<String>,
    val generatedCourses: List<String>,
    val updateRecommendations: List<String>,
    val approvalRequired: Boolean,
)

@Entity(tableName = "agi_network_analytics", indices = [Index(value = ["institutionId"])])
data class AGINetworkAnalyticsEntity(
    @PrimaryKey val reportId: String,
    val institutionId: String,
    val globalPatterns: List<String>,
    val aiPerformancePercent: Int,
    val studentSuccessPercent: Int,
    val knowledgeGrowth: List<String>,
    val recommendations: List<String>,
)

@Entity(tableName = "agi_network_governance_records", indices = [Index(value = ["decision"]), Index(value = ["humanApprovalRequired"])])
data class AGINetworkGovernanceRecordEntity(
    @PrimaryKey val recordId: String,
    val decision: String,
    val humanApprovalRequired: Boolean,
    val transparencyNotes: List<String>,
    val safetyRules: List<String>,
    val auditSummary: String,
)
