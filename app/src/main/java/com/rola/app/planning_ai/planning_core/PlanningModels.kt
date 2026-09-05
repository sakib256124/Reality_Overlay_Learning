package com.rola.app.planning_ai.planning_core

enum class PlanningHorizon { Daily, Weekly, Monthly, LongTerm }
enum class PlanningStatus { Draft, Active, Optimized, NeedsHumanApproval, Completed }

data class PlanningRequest(
    val learnerId: String,
    val desiredOutcome: String,
    val currentAbility: Int,
    val availableHoursPerWeek: Int,
    val resources: List<String>,
    val emotionalState: String,
    val knowledgeGaps: List<String>,
)

data class LearningGoal(val goalId: String, val shortTermGoals: List<String>, val longTermGoals: List<String>, val academicGoals: List<String>, val careerGoals: List<String>, val researchGoals: List<String>, val skillGoals: List<String>)
data class LearningStrategy(val strategyId: String, val methods: List<String>, val studyTechniques: List<String>, val selectedResources: List<String>, val practiceStrategy: String, val assessmentStrategy: String)
data class LearningSchedule(val scheduleId: String, val dailyActivities: List<String>, val weeklyPlan: List<String>, val monthlyGoals: List<String>, val roadmap: List<String>)
data class OptimizedPlan(val optimizationId: String, val efficiencyScore: Int, val resourceUsageScore: Int, val timeManagementScore: Int, val knowledgeGrowthScore: Int, val recommendations: List<String>)
data class AdaptivePlanChange(val changeId: String, val trigger: String, val scheduleAdjustment: String, val methodAdjustment: String, val roadmapUpdate: String)
data class TaskExecutionRecord(val executionId: String, val completedTasks: List<String>, val activeTasks: List<String>, val progressPercent: Int, val goalAchievement: String)
data class CareerRoadmap(val roadmapId: String, val requiredSkills: List<String>, val learningSequence: List<String>, val futureOpportunities: List<String>)
data class ResearchPlan(val researchPlanId: String, val researchGoals: List<String>, val experimentPlan: List<String>, val literaturePlan: List<String>, val projectRoadmap: List<String>)
data class AutonomousLearningPlan(
    val planId: String,
    val goal: LearningGoal,
    val strategy: LearningStrategy,
    val schedule: LearningSchedule,
    val optimization: OptimizedPlan,
    val adaptiveChange: AdaptivePlanChange,
    val execution: TaskExecutionRecord,
    val careerRoadmap: CareerRoadmap,
    val researchPlan: ResearchPlan,
    val status: PlanningStatus,
)
