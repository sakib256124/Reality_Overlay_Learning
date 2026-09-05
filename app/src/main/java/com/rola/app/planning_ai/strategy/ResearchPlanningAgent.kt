package com.rola.app.planning_ai.strategy

import com.rola.app.planning_ai.planning_core.PlanningRequest
import com.rola.app.planning_ai.planning_core.ResearchPlan
import javax.inject.Inject

class ResearchPlanningAgent @Inject constructor() {
    fun plan(request: PlanningRequest): ResearchPlan =
        ResearchPlan(
            researchPlanId = "research-plan-${request.learnerId}",
            researchGoals = listOf("Investigate a question inside ${request.desiredOutcome}"),
            experimentPlan = listOf("define hypothesis", "collect learning evidence", "test improvement strategy"),
            literaturePlan = listOf("review ROLA knowledge base", "compare AI research scientist findings", "summarize credible sources"),
            projectRoadmap = listOf("question", "prototype", "measure", "publish reflection"),
        )
}
