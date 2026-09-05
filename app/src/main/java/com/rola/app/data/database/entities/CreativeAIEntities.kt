package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "creative_contents", indices = [Index(value = ["contentId"])])
data class CreativeContentEntity(@PrimaryKey val contentId: String, val lessons: List<String>, val examples: List<String>, val activities: List<String>, val practiceMaterials: List<String>)

@Entity(tableName = "creative_generated_lessons", indices = [Index(value = ["lessonId"])])
data class CreativeGeneratedLessonEntity(@PrimaryKey val lessonId: String, val subject: String, val topic: String, val level: String, val objective: String)

@Entity(tableName = "creative_innovation_records", indices = [Index(value = ["humanValidationRequired"])])
data class CreativeInnovationRecordEntity(@PrimaryKey val innovationId: String, val teachingMethods: List<String>, val technologies: List<String>, val classroomStrategies: List<String>, val humanValidationRequired: Boolean)

@Entity(tableName = "research_ideas", indices = [Index(value = ["researchId"])])
data class ResearchIdeaEntity(@PrimaryKey val researchId: String, val topics: List<String>, val hypotheses: List<String>, val experiments: List<String>, val futureDirections: List<String>)

@Entity(tableName = "creative_projects", indices = [Index(value = ["projectId"])])
data class CreativeProjectEntity(@PrimaryKey val projectId: String, val humanContribution: String, val aiEnhancement: String, val solution: String)

@Entity(tableName = "simulation_templates", indices = [Index(value = ["simulationId"])])
data class SimulationTemplateEntity(@PrimaryKey val simulationId: String, val virtualExperiments: List<String>, val arActivities: List<String>, val digitalTwinScenarios: List<String>)

@Entity(tableName = "creative_evaluations", indices = [Index(value = ["safeForLearners"])])
data class CreativeEvaluationEntity(@PrimaryKey val evaluationId: String, val accuracyScore: Int, val creativityScore: Int, val learningEffectiveness: Int, val safeForLearners: Boolean, val explanation: String)

@Entity(tableName = "human_ai_projects", indices = [Index(value = ["projectId"])])
data class HumanAIProjectEntity(@PrimaryKey val projectId: String, val humanContribution: String, val aiEnhancement: String, val solution: String)
