package com.rola.app.planning_ai.strategy

import com.rola.app.planning_ai.planning_core.CareerRoadmap
import com.rola.app.planning_ai.planning_core.LearningGoal
import com.rola.app.planning_ai.planning_core.PlanningRequest
import javax.inject.Inject

class CareerPlanningEngine @Inject constructor() {
    fun plan(request: PlanningRequest, goal: LearningGoal): CareerRoadmap =
        CareerRoadmap(
            roadmapId = "career-${goal.goalId}",
            requiredSkills = goal.skillGoals + listOf("portfolio evidence", "communication", "AI collaboration"),
            learningSequence = listOf("foundation", "guided practice", "project proof", "career specialization"),
            futureOpportunities = listOf("AI-assisted educator", "research builder", "domain specialist for ${request.desiredOutcome}"),
        )
}
