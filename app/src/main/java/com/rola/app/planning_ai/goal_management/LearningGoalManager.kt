package com.rola.app.planning_ai.goal_management

import com.rola.app.planning_ai.planning_core.LearningGoal
import com.rola.app.planning_ai.planning_core.PlanningRequest
import javax.inject.Inject

class LearningGoalManager @Inject constructor() {
    fun generate(request: PlanningRequest): LearningGoal =
        LearningGoal(
            goalId = "goal-${request.learnerId}",
            shortTermGoals = listOf("Close ${request.knowledgeGaps.firstOrNull() ?: "core"} gap", "Complete daily focused practice"),
            longTermGoals = listOf("Reach ${request.desiredOutcome}", "Build a reusable lifelong learning roadmap"),
            academicGoals = listOf("Improve mastery from ${request.currentAbility}% with measurable assessments"),
            careerGoals = listOf("Map skills toward future roles connected to ${request.desiredOutcome}"),
            researchGoals = listOf("Create one inquiry project around ${request.desiredOutcome}"),
            skillGoals = request.knowledgeGaps.map { "Strengthen $it" }.ifEmpty { listOf("Strengthen reasoning, practice, and reflection") },
        )
}
