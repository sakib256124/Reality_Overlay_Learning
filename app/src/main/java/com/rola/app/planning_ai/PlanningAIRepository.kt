package com.rola.app.planning_ai

import com.rola.app.data.database.PlanningAIDao
import com.rola.app.data.database.entities.AdaptiveChangeEntity
import com.rola.app.data.database.entities.CareerRoadmapEntity
import com.rola.app.data.database.entities.LearningPlanEntity
import com.rola.app.data.database.entities.OptimizationHistoryEntity
import com.rola.app.data.database.entities.PlanningLearningGoalEntity
import com.rola.app.data.database.entities.ResearchPlanEntity
import com.rola.app.data.database.entities.ScheduleEntity
import com.rola.app.data.database.entities.StrategyRecordEntity
import com.rola.app.data.database.entities.TaskExecutionEntity
import com.rola.app.planning_ai.planning_core.AutonomousLearningPlan
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class PlanningAIRepository @Inject constructor(private val dao: PlanningAIDao) {
    fun observeDashboard(): Flow<PlanningDashboardState> =
        combine(
            dao.observeGoal(),
            dao.observePlan(),
            dao.observeStrategy(),
            dao.observeSchedule(),
            dao.observeOptimization(),
            dao.observeAdaptiveChange(),
            dao.observeExecution(),
            dao.observeCareerRoadmap(),
            dao.observeResearchPlan(),
        ) { values ->
            val goal = values[0] as PlanningLearningGoalEntity?
            val plan = values[1] as LearningPlanEntity?
            val strategy = values[2] as StrategyRecordEntity?
            val schedule = values[3] as ScheduleEntity?
            val optimization = values[4] as OptimizationHistoryEntity?
            val adaptive = values[5] as AdaptiveChangeEntity?
            val execution = values[6] as TaskExecutionEntity?
            val career = values[7] as CareerRoadmapEntity?
            val research = values[8] as ResearchPlanEntity?
            PlanningDashboardState(
                currentGoals = goal?.shortTermGoals.orEmpty() + goal?.longTermGoals.orEmpty(),
                learningRoadmap = schedule?.roadmap.orEmpty(),
                dailyTasks = schedule?.dailyActivities.orEmpty(),
                progress = execution?.progressPercent ?: 0,
                aiRecommendations = optimization?.recommendations.orEmpty() + listOfNotNull(adaptive?.scheduleAdjustment),
                futurePlans = career?.futureOpportunities.orEmpty() + research?.projectRoadmap.orEmpty(),
                status = plan?.status.orEmpty(),
                strategySummary = strategy?.methods.orEmpty().joinToString(),
            )
        }

    suspend fun save(plan: AutonomousLearningPlan) {
        dao.upsertGoal(PlanningLearningGoalEntity(plan.goal.goalId, plan.goal.shortTermGoals, plan.goal.longTermGoals, plan.goal.academicGoals, plan.goal.careerGoals, plan.goal.researchGoals, plan.goal.skillGoals))
        dao.upsertPlan(LearningPlanEntity(plan.planId, plan.goal.goalId, plan.strategy.strategyId, plan.schedule.scheduleId, plan.optimization.optimizationId, plan.status.name, userEditable = true, humanApprovalRequired = true))
        dao.upsertStrategy(StrategyRecordEntity(plan.strategy.strategyId, plan.strategy.methods, plan.strategy.studyTechniques, plan.strategy.selectedResources, plan.strategy.practiceStrategy, plan.strategy.assessmentStrategy))
        dao.upsertSchedule(ScheduleEntity(plan.schedule.scheduleId, plan.schedule.dailyActivities, plan.schedule.weeklyPlan, plan.schedule.monthlyGoals, plan.schedule.roadmap))
        dao.upsertOptimization(OptimizationHistoryEntity(plan.optimization.optimizationId, plan.optimization.efficiencyScore, plan.optimization.resourceUsageScore, plan.optimization.timeManagementScore, plan.optimization.knowledgeGrowthScore, plan.optimization.recommendations))
        dao.upsertAdaptiveChange(AdaptiveChangeEntity(plan.adaptiveChange.changeId, plan.adaptiveChange.trigger, plan.adaptiveChange.scheduleAdjustment, plan.adaptiveChange.methodAdjustment, plan.adaptiveChange.roadmapUpdate))
        dao.upsertExecution(TaskExecutionEntity(plan.execution.executionId, plan.execution.completedTasks, plan.execution.activeTasks, plan.execution.progressPercent, plan.execution.goalAchievement))
        dao.upsertCareerRoadmap(CareerRoadmapEntity(plan.careerRoadmap.roadmapId, plan.careerRoadmap.requiredSkills, plan.careerRoadmap.learningSequence, plan.careerRoadmap.futureOpportunities))
        dao.upsertResearchPlan(ResearchPlanEntity(plan.researchPlan.researchPlanId, plan.researchPlan.researchGoals, plan.researchPlan.experimentPlan, plan.researchPlan.literaturePlan, plan.researchPlan.projectRoadmap))
    }
}

data class PlanningDashboardState(
    val currentGoals: List<String> = emptyList(),
    val learningRoadmap: List<String> = emptyList(),
    val dailyTasks: List<String> = emptyList(),
    val progress: Int = 0,
    val aiRecommendations: List<String> = emptyList(),
    val futurePlans: List<String> = emptyList(),
    val status: String = "",
    val strategySummary: String = "",
)
