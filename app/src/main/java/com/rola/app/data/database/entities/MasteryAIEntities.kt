package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "skill_mastery_profiles", indices = [Index(value = ["skillName"]), Index(value = ["masteryLevel"])])
data class SkillMasteryProfileEntity(@PrimaryKey val profileId: String, val skillName: String, val masteryLevel: String, val knowledgeLevel: Int, val practicalAbility: Int, val consistencyScore: Int, val explainability: String)
@Entity(tableName = "competency_scores", indices = [Index(value = ["level"])])
data class CompetencyScoreEntity(@PrimaryKey val scoreId: String, val conceptMastery: Int, val practicalApplication: Int, val criticalThinking: Int, val creativity: Int, val realWorldPerformance: Int, val level: String)
@Entity(tableName = "learning_gaps", indices = [Index(value = ["gapId"])])
data class LearningGapEntity(@PrimaryKey val gapId: String, val missingConcepts: List<String>, val weakSkills: List<String>, val misunderstoodTopics: List<String>, val incorrectPatterns: List<String>, val recommendation: String)
@Entity(tableName = "skill_progress", indices = [Index(value = ["masteryProgress"])])
data class SkillProgressEntity(@PrimaryKey val analyticsId: String, val masteryProgress: Int, val improvementAreas: List<String>, val futureRecommendations: List<String>, val biasCheck: String)
@Entity(tableName = "mastery_history", indices = [Index(value = ["decision"])])
data class MasteryHistoryEntity(@PrimaryKey val resultId: String, val profileId: String, val scoreId: String, val gapId: String, val decision: String, val transparentEvaluation: Boolean, val humanReviewSupported: Boolean)
@Entity(tableName = "assessment_results", indices = [Index(value = ["dailyProgress"])])
data class AssessmentResultEntity(@PrimaryKey val assessmentId: String, val dailyProgress: Int, val practicalPerformance: Int, val knowledgeRetention: Int, val skillGrowth: Int, val fairnessExplanation: String)
@Entity(tableName = "improvement_plans", indices = [Index(value = ["improvementId"])])
data class ImprovementPlanEntity(@PrimaryKey val improvementId: String, val practiceTasks: List<String>, val projectRecommendations: List<String>, val learningChallenges: List<String>, val longTermImprovement: String)
@Entity(tableName = "project_evaluations", indices = [Index(value = ["projectId"])])
data class ProjectEvaluationEntity(@PrimaryKey val projectId: String, val realWorldProjects: List<String>, val practicalAssessment: String, val portfolioEvidence: List<String>, val expertEvaluation: String)
