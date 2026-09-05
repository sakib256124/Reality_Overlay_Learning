package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "universal_learning_models", indices = [Index(value = ["userId"])])
data class UniversalLearningModelEntity(@PrimaryKey val modelId: String, val userId: String, val strategy: String, val personalizedPath: List<String>, val skillRoadmap: List<String>)

@Entity(tableName = "knowledge_fusion_records", indices = [Index(value = ["recordId"])])
data class KnowledgeFusionRecordEntity(@PrimaryKey val recordId: String, val sources: List<String>, val hiddenRelationships: List<String>, val contentImprovements: List<String>)

@Entity(tableName = "intelligence_connections", indices = [Index(value = ["connectionId"])])
data class IntelligenceConnectionEntity(@PrimaryKey val connectionId: String, val systems: List<String>, val unifiedIntelligence: String, val coordinationMode: String)

@Entity(tableName = "learning_evolution_history", indices = [Index(value = ["evolutionId"])])
data class LearningEvolutionHistoryEntity(@PrimaryKey val evolutionId: String, val curriculumImprovements: List<String>, val teachingImprovements: List<String>, val assessmentImprovements: List<String>)

@Entity(tableName = "universal_education_profiles", indices = [Index(value = ["profileId"])])
data class UniversalEducationProfileEntity(@PrimaryKey val profileId: String, val participants: List<String>, val accessibilityPlan: String, val resourceOptimization: String)

@Entity(tableName = "ai_coordination_logs", indices = [Index(value = ["createdAt"])])
data class AICoordinationLogEntity(@PrimaryKey val logId: String, val message: String, val createdAt: Long)

@Entity(tableName = "singularity_analytics", indices = [Index(value = ["intelligenceScore"])])
data class SingularityAnalyticsEntity(@PrimaryKey val analyticsId: String, val intelligenceScore: Int, val knowledgeGrowthScore: Int, val coordinationScore: Int)

@Entity(tableName = "singularity_governance_records", indices = [Index(value = ["humanApprovalRequired"])])
data class SingularityGovernanceRecordEntity(@PrimaryKey val governanceId: String, val policies: List<String>, val auditTrail: List<String>, val humanApprovalRequired: Boolean)
