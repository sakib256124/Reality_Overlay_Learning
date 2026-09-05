package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "collective_ai_agents", indices = [Index(value = ["agentType"]), Index(value = ["status"])])
data class CollectiveAIAgentEntity(
    @PrimaryKey val agentId: String,
    val name: String,
    val agentType: String,
    val capability: String,
    val status: String,
)

@Entity(tableName = "agent_relationships", indices = [Index(value = ["fromAgentId"]), Index(value = ["toAgentId"])])
data class CollectiveAgentRelationshipEntity(
    @PrimaryKey val relationshipId: String,
    val fromAgentId: String,
    val toAgentId: String,
    val relationshipType: String,
    val trustScore: Int,
)

@Entity(tableName = "agent_tasks", indices = [Index(value = ["agentId"]), Index(value = ["priority"])])
data class CollectiveAgentTaskEntity(
    @PrimaryKey val taskId: String,
    val agentId: String,
    val taskType: String,
    val priority: Int,
    val output: String,
)

@Entity(tableName = "collective_knowledge_exchange", indices = [Index(value = ["topic"]), Index(value = ["sourceAgentId"])])
data class CollectiveKnowledgeExchangeEntity(
    @PrimaryKey val exchangeId: String,
    val sourceAgentId: String,
    val targetAgentId: String,
    val topic: String,
    val knowledgeSummary: String,
    val sources: List<String>,
)

@Entity(tableName = "ai_consensus_records", indices = [Index(value = ["outcome"])])
data class AIConsensusRecordEntity(
    @PrimaryKey val consensusId: String,
    val outcome: String,
    val selectedStrategy: String,
    val rankedStrategies: List<String>,
    val accuracyScore: Int,
    val explanation: String,
)

@Entity(tableName = "agent_communication_history", indices = [Index(value = ["senderAgentId"]), Index(value = ["createdAt"])])
data class AgentCommunicationHistoryEntity(
    @PrimaryKey val communicationId: String,
    val senderAgentId: String,
    val receiverAgentId: String,
    val message: String,
    val createdAt: Long,
)

@Entity(tableName = "human_feedback", indices = [Index(value = ["userId"]), Index(value = ["role"])])
data class HumanFeedbackEntity(
    @PrimaryKey val feedbackId: String,
    val userId: String,
    val role: String,
    val feedback: String,
    val validationScore: Int,
)

@Entity(tableName = "collective_learning_results", indices = [Index(value = ["resultId"])])
data class CollectiveLearningResultEntity(
    @PrimaryKey val resultId: String,
    val improvedStrategies: List<String>,
    val curriculumUpdates: List<String>,
    val assessmentImprovements: List<String>,
    val recommendationUpdates: List<String>,
)

@Entity(tableName = "collaboration_analytics", indices = [Index(value = ["consensusScore"]), Index(value = ["improvementScore"])])
data class CollaborationAnalyticsEntity(
    @PrimaryKey val analyticsId: String,
    val activeAgents: Int,
    val communicationCount: Int,
    val consensusScore: Int,
    val improvementScore: Int,
)
