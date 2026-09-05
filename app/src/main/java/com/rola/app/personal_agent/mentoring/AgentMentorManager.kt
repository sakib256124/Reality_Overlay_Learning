package com.rola.app.personal_agent.mentoring

import com.rola.app.personal_agent.agent_core.AgentMentorPlan
import com.rola.app.personal_agent.agent_core.AgentRequest
import javax.inject.Inject

class AgentMentorManager @Inject constructor() {
    fun mentor(request: AgentRequest): AgentMentorPlan =
        AgentMentorPlan(
            mentorId = "mentor-${request.userId}",
            careerGuidance = listOf("map ${request.currentGoal} toward ${request.careerObjective}", "build portfolio evidence"),
            researchGuidance = listOf("turn weak areas into research questions", "use research assistant for credible discovery"),
            skillDevelopment = listOf("master foundations", "practice real projects", "review progress weekly"),
            growthRoadmap = listOf("diagnose", "learn", "practice", "prove", "evolve"),
        )
}
