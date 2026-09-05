package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "lifelong_memory", indices = [Index(value = ["userId"])])
data class LifelongMemoryEntity(@PrimaryKey val memoryId: String, val userId: String, val shortTermMemory: List<String>, val longTermMemory: List<String>, val userOwned: Boolean)

@Entity(tableName = "personal_knowledge_graph", indices = [Index(value = ["graphId"])])
data class PersonalKnowledgeGraphEntity(@PrimaryKey val graphId: String, val concepts: List<String>, val skills: List<String>, val experiences: List<String>, val achievements: List<String>, val expertise: String)

@Entity(tableName = "learning_experiences", indices = [Index(value = ["experienceId"])])
data class LearningExperienceEntity(@PrimaryKey val experienceId: String, val projects: List<String>, val experiments: List<String>, val researchWork: List<String>, val practicalSkills: List<String>)

@Entity(tableName = "skill_evolution", indices = [Index(value = ["evolutionId"])])
data class SkillEvolutionEntity(@PrimaryKey val evolutionId: String, val organizedKnowledge: List<String>, val outdatedInformationRemoved: List<String>, val newConnections: List<String>)

@Entity(tableName = "memory_history", indices = [Index(value = ["retrievalId"])])
data class MemoryHistoryEntity(@PrimaryKey val retrievalId: String, val recalledLessons: List<String>, val pastMistakes: List<String>, val learningPreferences: List<String>, val personalizedExplanation: String)

@Entity(tableName = "knowledge_connections", indices = [Index(value = ["connectionId"])])
data class KnowledgeConnectionEntity(@PrimaryKey val connectionId: String, val personalGraphId: String, val globalKnowledgeSummary: String, val relationships: List<String>)

@Entity(tableName = "learning_timeline", indices = [Index(value = ["userId"])])
data class LearningTimelineEntity(@PrimaryKey val timelineId: String, val userId: String, val milestones: List<String>, val futureGoals: List<String>)

@Entity(tableName = "expertise_profile", indices = [Index(value = ["expertise"])])
data class ExpertiseProfileEntity(@PrimaryKey val profileId: String, val expertise: String, val careerLearningPath: List<String>, val recommendedSkills: List<String>)

@Entity(tableName = "memory_analytics", indices = [Index(value = ["knowledgeGrowthScore"])])
data class MemoryAnalyticsEntity(@PrimaryKey val analyticsId: String, val knowledgeGrowthScore: Int, val retrievalQualityScore: Int, val personalizationScore: Int)
