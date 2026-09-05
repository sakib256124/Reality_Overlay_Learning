package com.rola.app.personal_agent.agent_core

import javax.inject.Inject

class PersonalAgentCoreManager @Inject constructor() {
    fun createAgent(request: AgentRequest): PersonalEducationAgent =
        PersonalEducationAgent(
            agentId = "personal-agent-${request.userId}",
            userId = request.userId,
            learningHistory = listOf("current goal: ${request.currentGoal}", "need: ${request.userNeed}"),
            knowledgeProfile = listOf("skill level: ${request.skillLevel}", "learning speed: ${request.learningSpeed}"),
            skills = listOf(request.currentGoal, "self-directed learning", "AI collaboration"),
            goals = listOf(request.currentGoal, request.careerObjective),
            preferences = listOf("emotion-aware support", "transparent recommendations", "user-controlled memory"),
            personality = "supportive mentor with concise explanations",
            learningStyle = "adaptive mixed teaching",
            careerObjectives = listOf(request.careerObjective),
            status = AgentStatus.Active,
        )
}
