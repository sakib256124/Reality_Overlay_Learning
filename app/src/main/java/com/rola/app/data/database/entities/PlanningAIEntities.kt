package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "planning_learning_goals", indices = [Index(value = ["goalId"])])
data class PlanningLearningGoalEntity(@PrimaryKey val goalId: String, val shortTermGoals: List<String>, val longTermGoals: List<String>, val academicGoals: List<String>, val careerGoals: List<String>, val researchGoals: List<String>, val skillGoals: List<String>)
@Entity(tableName = "learning_plans", indices = [Index(value = ["status"])])
data class LearningPlanEntity(@PrimaryKey val planId: String, val goalId: String, val strategyId: String, val scheduleId: String, val optimizationId: String, val status: String, val userEditable: Boolean, val humanApprovalRequired: Boolean)
@Entity(tableName = "strategy_records", indices = [Index(value = ["strategyId"])])
data class StrategyRecordEntity(@PrimaryKey val strategyId: String, val methods: List<String>, val studyTechniques: List<String>, val selectedResources: List<String>, val practiceStrategy: String, val assessmentStrategy: String)
@Entity(tableName = "schedules", indices = [Index(value = ["scheduleId"])])
data class ScheduleEntity(@PrimaryKey val scheduleId: String, val dailyActivities: List<String>, val weeklyPlan: List<String>, val monthlyGoals: List<String>, val roadmap: List<String>)
@Entity(tableName = "optimization_history", indices = [Index(value = ["efficiencyScore"])])
data class OptimizationHistoryEntity(@PrimaryKey val optimizationId: String, val efficiencyScore: Int, val resourceUsageScore: Int, val timeManagementScore: Int, val knowledgeGrowthScore: Int, val recommendations: List<String>)
@Entity(tableName = "adaptive_changes", indices = [Index(value = ["changeId"])])
data class AdaptiveChangeEntity(@PrimaryKey val changeId: String, val trigger: String, val scheduleAdjustment: String, val methodAdjustment: String, val roadmapUpdate: String)
@Entity(tableName = "task_execution", indices = [Index(value = ["progressPercent"])])
data class TaskExecutionEntity(@PrimaryKey val executionId: String, val completedTasks: List<String>, val activeTasks: List<String>, val progressPercent: Int, val goalAchievement: String)
@Entity(tableName = "career_roadmaps", indices = [Index(value = ["roadmapId"])])
data class CareerRoadmapEntity(@PrimaryKey val roadmapId: String, val requiredSkills: List<String>, val learningSequence: List<String>, val futureOpportunities: List<String>)
@Entity(tableName = "research_plans", indices = [Index(value = ["researchPlanId"])])
data class ResearchPlanEntity(@PrimaryKey val researchPlanId: String, val researchGoals: List<String>, val experimentPlan: List<String>, val literaturePlan: List<String>, val projectRoadmap: List<String>)
