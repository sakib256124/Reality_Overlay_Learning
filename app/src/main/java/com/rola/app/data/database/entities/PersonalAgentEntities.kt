package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "personal_agents", indices = [Index(value = ["userId"]), Index(value = ["status"])])
data class PersonalAgentEntity(@PrimaryKey val agentId: String, val userId: String, val personality: String, val learningStyle: String, val status: String)
@Entity(tableName = "agent_profiles", indices = [Index(value = ["agentId"])])
data class AgentProfileEntity(@PrimaryKey val profileId: String, val agentId: String, val knowledgeProfile: List<String>, val skills: List<String>, val goals: List<String>, val preferences: List<String>, val careerObjectives: List<String>)
@Entity(tableName = "agent_memory", indices = [Index(value = ["agentId"])])
data class AgentMemoryEntity(@PrimaryKey val memoryId: String, val agentId: String, val shortTermMemory: List<String>, val longTermMemory: List<String>, val userControlled: Boolean, val privacyProtected: Boolean)
@Entity(tableName = "agent_interactions", indices = [Index(value = ["agentId"])])
data class AgentInteractionEntity(@PrimaryKey val interactionId: String, val agentId: String, val userNeed: String, val selectedCapabilities: List<String>, val transparentDecision: String)
@Entity(tableName = "agent_learning_history", indices = [Index(value = ["agentId"])])
data class AgentLearningHistoryEntity(@PrimaryKey val historyId: String, val agentId: String, val learningHistory: List<String>, val teachingResponse: String, val mentorRoadmap: List<String>)
@Entity(tableName = "agent_recommendations", indices = [Index(value = ["agentId"])])
data class AgentRecommendationEntity(@PrimaryKey val recommendationId: String, val agentId: String, val moduleToUse: String, val explanationStyle: String, val learningActivity: String, val strategy: String, val humanControl: Boolean)
@Entity(tableName = "agent_evolution_history", indices = [Index(value = ["agentId"])])
data class AgentEvolutionHistoryEntity(@PrimaryKey val evolutionId: String, val agentId: String, val teachingImprovement: String, val communicationStyle: String, val recommendationImprovement: String, val planningAccuracy: Int, val personalUnderstanding: Int)
@Entity(tableName = "agent_analytics", indices = [Index(value = ["personalUnderstanding"])])
data class AgentAnalyticsEntity(@PrimaryKey val analyticsId: String, val agentId: String, val learningProgress: Int, val satisfactionScore: Int, val memoryAccuracy: Int, val securityStatus: String, val personalUnderstanding: Int)
