package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "asi_profiles", indices = [Index(value = ["learnerId"])])
data class ASIProfileEntity(
    @PrimaryKey val profileId: String,
    val learnerId: String,
    val intelligenceScope: List<String>,
    val personalizationDepthPercent: Int,
    val responsibleAIMode: String,
    val updatedAt: Long,
)

@Entity(tableName = "asi_models")
data class ASIModelEntity(
    @PrimaryKey val modelId: String,
    val name: String,
    val capabilities: List<String>,
    val safetyBoundary: String,
    val version: String,
)

@Entity(tableName = "reasoning_history", indices = [Index(value = ["topic"])])
data class ReasoningHistoryEntity(
    @PrimaryKey val traceId: String,
    val topic: String,
    val reasoningSteps: List<String>,
    val confidencePercent: Int,
    val explanation: String,
)

@Entity(tableName = "knowledge_evolution_records", indices = [Index(value = ["topic"])])
data class KnowledgeEvolutionRecordEntity(
    @PrimaryKey val mapId: String,
    val topic: String,
    val domainConnections: List<String>,
    val newKnowledgeLinks: List<String>,
    val contentImprovementIdeas: List<String>,
)

@Entity(tableName = "self_improvement_logs", indices = [Index(value = ["requiresOfflineValidation"])])
data class SelfImprovementLogEntity(
    @PrimaryKey val logId: String,
    val improvedAreas: List<String>,
    val evaluationSummary: String,
    val requiresOfflineValidation: Boolean,
)

@Entity(tableName = "ai_creative_outputs", indices = [Index(value = ["topic"])])
data class AICreativeOutputEntity(
    @PrimaryKey val outputId: String,
    val topic: String,
    val educationalApproaches: List<String>,
    val learningActivities: List<String>,
    val simulations: List<String>,
    val researchDirections: List<String>,
)

@Entity(tableName = "human_ai_interactions", indices = [Index(value = ["planId"])])
data class HumanAIInteractionEntity(
    @PrimaryKey val interactionId: String,
    val planId: String,
    val stakeholders: List<String>,
    val aiSuggestions: List<String>,
    val requiredApprovals: List<String>,
    val feedbackLoop: String,
)

@Entity(tableName = "asi_governance_records", indices = [Index(value = ["approvalStatus"]), Index(value = ["riskLevel"])])
data class ASIGovernanceRecordEntity(
    @PrimaryKey val recordId: String,
    val approvalStatus: String,
    val riskLevel: String,
    val transparencyNotes: List<String>,
    val ethicsChecks: List<String>,
    val humanOverrideAvailable: Boolean,
)

@Entity(tableName = "global_education_insights", indices = [Index(value = ["institutionId"])])
data class GlobalEducationInsightEntity(
    @PrimaryKey val insightId: String,
    val institutionId: String,
    val globalEducationPatterns: List<String>,
    val knowledgeSharingPlan: String,
    val innovationOpportunities: List<String>,
)
