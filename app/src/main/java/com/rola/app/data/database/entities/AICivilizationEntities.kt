package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "ai_civilization", indices = [Index(value = ["civilizationId"])])
data class AICivilizationEntity(@PrimaryKey val civilizationId: String, val globalEducationIntelligence: String, val participants: List<String>, val coordinationModel: String)

@Entity(tableName = "civilization_knowledge_evolution", indices = [Index(value = ["evolutionId"])])
data class CivilizationKnowledgeEvolutionEntity(@PrimaryKey val evolutionId: String, val missingKnowledge: List<String>, val validationSummary: String, val graphExpansion: List<String>)

@Entity(tableName = "learning_evolution", indices = [Index(value = ["evolutionId"])])
data class CivilizationLearningEvolutionEntity(@PrimaryKey val evolutionId: String, val learningPathUpdates: List<String>, val curriculumUpdates: List<String>, val recommendationUpdates: List<String>)

@Entity(tableName = "civilization_innovation_records", indices = [Index(value = ["humanApproved"])])
data class CivilizationInnovationRecordEntity(@PrimaryKey val innovationId: String, val technologies: List<String>, val methods: List<String>, val researchDirections: List<String>, val humanApproved: Boolean)

@Entity(tableName = "future_education_plans", indices = [Index(value = ["planId"])])
data class FutureEducationPlanEntity(@PrimaryKey val planId: String, val futureSkills: List<String>, val futureSubjects: List<String>, val trends: List<String>, val roadmap: List<String>)

@Entity(tableName = "global_knowledge_connections", indices = [Index(value = ["connectionId"])])
data class GlobalKnowledgeConnectionEntity(@PrimaryKey val connectionId: String, val connectedSources: List<String>, val collaborationSummary: String)

@Entity(tableName = "ai_governance_logs", indices = [Index(value = ["approvalRequired"])])
data class AIGovernanceLogEntity(@PrimaryKey val logId: String, val policies: List<String>, val auditTrail: List<String>, val approvalRequired: Boolean)

@Entity(tableName = "civilization_analytics", indices = [Index(value = ["intelligenceScore"])])
data class CivilizationAnalyticsEntity(@PrimaryKey val analyticsId: String, val intelligenceScore: Int, val evolutionScore: Int, val innovationScore: Int)
