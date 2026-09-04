package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rola.app.domain.model.AGIAgentRole
import com.rola.app.domain.model.AGIActivityType
import com.rola.app.domain.model.AGILearningEvent
import com.rola.app.domain.model.LearningGoal
import com.rola.app.domain.model.RecommendationPriority
import com.rola.app.domain.model.SkillGraphNode
import com.rola.app.domain.model.SkillLevel

@Entity(tableName = "agi_memory", indices = [Index(value = ["learnerId"]), Index(value = ["timestamp"])])
data class AGIMemoryEntity(
    @PrimaryKey val eventId: String,
    val learnerId: String,
    val activityType: AGIActivityType,
    val topic: String,
    val signal: String,
    val score: Int?,
    val timestamp: Long,
) {
    fun toDomain(): AGILearningEvent = AGILearningEvent(eventId, learnerId, activityType, topic, signal, score, timestamp)
}

@Entity(tableName = "learner_models", indices = [Index(value = ["learnerId"])])
data class LearnerModelEntity(
    @PrimaryKey val learnerId: String,
    val knowledgeLevel: SkillLevel,
    val learningSpeed: String,
    val retentionScore: Int,
    val interests: List<String>,
    val previousMistakes: List<String>,
    val learningPatterns: List<String>,
    val updatedAt: Long,
)

@Entity(tableName = "skill_graphs", indices = [Index(value = ["learnerId"]), Index(value = ["name"])])
data class SkillGraphEntity(
    @PrimaryKey val skillId: String,
    val learnerId: String,
    val name: String,
    val mastery: Int,
    val prerequisites: List<String>,
) {
    fun toDomain(): SkillGraphNode = SkillGraphNode(skillId, name, mastery, prerequisites)
}

@Entity(tableName = "ai_decisions", indices = [Index(value = ["learnerId"]), Index(value = ["institutionId"]), Index(value = ["createdAt"])])
data class AIDecisionEntity(
    @PrimaryKey val decisionId: String,
    val learnerId: String,
    val institutionId: String,
    val selectedAgents: List<String>,
    val actionTitle: String,
    val actionDescription: String,
    val verified: Boolean,
    val requiresHumanApproval: Boolean,
    val createdAt: Long,
)

@Entity(tableName = "learning_goals", indices = [Index(value = ["learnerId"]), Index(value = ["targetSkill"])])
data class LearningGoalEntity(
    @PrimaryKey val goalId: String,
    val learnerId: String,
    val title: String,
    val targetSkill: String,
    val targetMastery: Int,
    val progress: Int,
    val dueAt: Long?,
) {
    fun toDomain(): LearningGoal = LearningGoal(goalId, learnerId, title, targetSkill, targetMastery, progress, dueAt)
}

@Entity(tableName = "future_recommendations", indices = [Index(value = ["learnerId"]), Index(value = ["priority"])])
data class FutureRecommendationEntity(
    @PrimaryKey val recommendationId: String,
    val learnerId: String,
    val title: String,
    val rationale: String,
    val priority: RecommendationPriority,
    val generatedAt: Long,
)

@Entity(tableName = "knowledge_evolution", indices = [Index(value = ["institutionId"]), Index(value = ["topic"]), Index(value = ["status"])])
data class KnowledgeEvolutionEntity(
    @PrimaryKey val proposalId: String,
    val institutionId: String,
    val topic: String,
    val missingConcepts: List<String>,
    val curriculumUpdates: List<String>,
    val requiredApprovalRole: String,
    val status: String,
)

fun AGILearningEvent.toEntity(): AGIMemoryEntity = AGIMemoryEntity(
    eventId,
    learnerId,
    activityType,
    topic,
    signal,
    score,
    timestamp,
)

fun SkillGraphNode.toEntity(learnerId: String): SkillGraphEntity = SkillGraphEntity(
    skillId,
    learnerId,
    name,
    mastery,
    prerequisites,
)

fun LearningGoal.toEntity(): LearningGoalEntity = LearningGoalEntity(
    goalId,
    learnerId,
    title,
    targetSkill,
    targetMastery,
    progress,
    dueAt,
)

fun List<AGIAgentRole>.toAgentNames(): List<String> = map { it.name }
