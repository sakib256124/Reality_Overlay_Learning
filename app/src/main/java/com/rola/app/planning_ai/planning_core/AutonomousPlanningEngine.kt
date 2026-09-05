package com.rola.app.planning_ai.planning_core

import com.rola.app.planning_ai.adaptation.AdaptivePlanningManager
import com.rola.app.planning_ai.execution.LearningExecutionManager
import com.rola.app.planning_ai.goal_management.LearningGoalManager
import com.rola.app.planning_ai.optimization.PlanOptimizationEngine
import com.rola.app.planning_ai.scheduling.LearningScheduleManager
import com.rola.app.planning_ai.strategy.CareerPlanningEngine
import com.rola.app.planning_ai.strategy.ResearchPlanningAgent
import com.rola.app.planning_ai.strategy.StrategyGenerationEngine
import javax.inject.Inject

class AutonomousPlanningEngine @Inject constructor(
    private val goalManager: LearningGoalManager,
    private val strategyGenerationEngine: StrategyGenerationEngine,
    private val scheduleManager: LearningScheduleManager,
    private val optimizationEngine: PlanOptimizationEngine,
    private val adaptivePlanningManager: AdaptivePlanningManager,
    private val executionManager: LearningExecutionManager,
    private val careerPlanningEngine: CareerPlanningEngine,
    private val researchPlanningAgent: ResearchPlanningAgent,
) {
    fun createPlan(request: PlanningRequest): AutonomousLearningPlan {
        val goal = goalManager.generate(request)
        val strategy = strategyGenerationEngine.generate(request, goal)
        val schedule = scheduleManager.schedule(request, strategy)
        val optimization = optimizationEngine.optimize(request, schedule)
        return AutonomousLearningPlan(
            planId = "plan-${request.learnerId}",
            goal = goal,
            strategy = strategy,
            schedule = schedule,
            optimization = optimization,
            adaptiveChange = adaptivePlanningManager.adapt(request, optimization),
            execution = executionManager.execute(schedule),
            careerRoadmap = careerPlanningEngine.plan(request, goal),
            researchPlan = researchPlanningAgent.plan(request),
            status = PlanningStatus.NeedsHumanApproval,
        )
    }
}
