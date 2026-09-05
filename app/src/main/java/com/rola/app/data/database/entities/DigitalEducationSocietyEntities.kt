package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "global_education_network", indices = [Index(value = ["institutionId"]), Index(value = ["region"])])
data class GlobalEducationNetworkEntity(
    @PrimaryKey val networkId: String,
    val institutionId: String,
    val region: String,
    val connectedParticipants: List<String>,
    val sharedKnowledgeTopics: List<String>,
    val learningImprovementPlan: String,
)

@Entity(tableName = "knowledge_communities", indices = [Index(value = ["communityId"])])
data class KnowledgeCommunityEntity(
    @PrimaryKey val communityId: String,
    val collaborationGroups: List<String>,
    val sharedResources: List<String>,
    val aiRecommendations: List<String>,
)

@Entity(tableName = "ai_agents", indices = [Index(value = ["agentType"]), Index(value = ["trustLevel"])])
data class SocietyAIAgentEntity(
    @PrimaryKey val agentId: String,
    val agentType: String,
    val capabilities: List<String>,
    val trustLevel: String,
    val authenticated: Boolean,
)

@Entity(tableName = "education_institutions", indices = [Index(value = ["institutionId"]), Index(value = ["region"])])
data class EducationInstitutionEntity(
    @PrimaryKey val institutionId: String,
    val region: String,
    val languages: List<String>,
    val activeServices: List<String>,
    val accessibilityImprovements: List<String>,
)

@Entity(tableName = "innovation_records", indices = [Index(value = ["topic"])])
data class SocietyInnovationRecordEntity(
    @PrimaryKey val innovationId: String,
    val topic: String,
    val contentIdeas: List<String>,
    val researchCollaborations: List<String>,
    val exchangeValue: String,
    val humanApprovalRequired: Boolean,
)

@Entity(tableName = "global_learning_analytics", indices = [Index(value = ["institutionId"])])
data class GlobalLearningAnalyticsEntity(
    @PrimaryKey val reportId: String,
    val institutionId: String,
    val worldwideTrends: List<String>,
    val knowledgeGaps: List<String>,
    val futureSkillNeeds: List<String>,
    val improvementSummary: String,
)

@Entity(tableName = "digital_avatars", indices = [Index(value = ["learnerId"])])
data class DigitalAvatarEntity(
    @PrimaryKey val avatarId: String,
    val learnerId: String,
    val learningHistory: List<String>,
    val skills: List<String>,
    val knowledgeLevel: String,
    val goals: List<String>,
    val achievements: List<String>,
)

@Entity(tableName = "governance_policies", indices = [Index(value = ["decision"]), Index(value = ["trustLevel"])])
data class GovernancePolicyEntity(
    @PrimaryKey val policyId: String,
    val decision: String,
    val trustLevel: String,
    val accountabilityRules: List<String>,
    val dataProtectionRules: List<String>,
    val auditSummary: String,
)

@Entity(tableName = "knowledge_exchange_history", indices = [Index(value = ["topic"]), Index(value = ["createdAt"])])
data class KnowledgeExchangeHistoryEntity(
    @PrimaryKey val exchangeId: String,
    val topic: String,
    val validationSteps: List<String>,
    val distributionReason: String,
    val createdAt: Long,
)

