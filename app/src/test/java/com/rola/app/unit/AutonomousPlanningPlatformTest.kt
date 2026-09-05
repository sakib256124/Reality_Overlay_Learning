package com.rola.app.unit

import com.rola.app.planning_ai.adaptation.AdaptivePlanningManager
import com.rola.app.planning_ai.execution.LearningExecutionManager
import com.rola.app.planning_ai.goal_management.LearningGoalManager
import com.rola.app.planning_ai.optimization.PlanOptimizationEngine
import com.rola.app.planning_ai.planning_core.AutonomousPlanningEngine
import com.rola.app.planning_ai.planning_core.PlanningRequest
import com.rola.app.planning_ai.planning_core.PlanningStatus
import com.rola.app.planning_ai.scheduling.LearningScheduleManager
import com.rola.app.planning_ai.strategy.CareerPlanningEngine
import com.rola.app.planning_ai.strategy.ResearchPlanningAgent
import com.rola.app.planning_ai.strategy.StrategyGenerationEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AutonomousPlanningPlatformTest {
    private val engine = AutonomousPlanningEngine(
        LearningGoalManager(),
        StrategyGenerationEngine(),
        LearningScheduleManager(),
        PlanOptimizationEngine(),
        AdaptivePlanningManager(),
        LearningExecutionManager(),
        CareerPlanningEngine(),
        ResearchPlanningAgent(),
    )

    @Test
    fun autonomousPlan_generatesGoalsStrategyScheduleAdaptationAndExecution() {
        val plan = engine.createPlan(
            PlanningRequest(
                learnerId = "planner-learner",
                desiredOutcome = "AI learning strategy architect",
                currentAbility = 68,
                availableHoursPerWeek = 9,
                resources = listOf("Cognitive AI", "Emotional AI", "Predictive AI", "Lifelong Memory"),
                emotionalState = "focused but slightly stressed",
                knowledgeGaps = listOf("planning optimization", "research sequencing"),
            ),
        )

        assertEquals(PlanningStatus.NeedsHumanApproval, plan.status)
        assertTrue(plan.goal.shortTermGoals.isNotEmpty())
        assertTrue(plan.strategy.methods.contains("retrieval practice"))
        assertTrue(plan.schedule.dailyActivities.isNotEmpty())
        assertTrue(plan.optimization.efficiencyScore >= 60)
        assertTrue(plan.adaptiveChange.scheduleAdjustment.contains("reduce load"))
        assertTrue(plan.execution.progressPercent > 0)
        assertTrue(plan.careerRoadmap.requiredSkills.any { it.contains("planning optimization") })
        assertTrue(plan.researchPlan.experimentPlan.contains("define hypothesis"))
    }
}
