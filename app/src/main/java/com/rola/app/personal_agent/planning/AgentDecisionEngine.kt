package com.rola.app.personal_agent.planning

import com.rola.app.personal_agent.agent_core.AgentCapability
import com.rola.app.personal_agent.agent_core.AgentDecision
import com.rola.app.personal_agent.agent_core.AgentIntelligencePlan
import com.rola.app.personal_agent.agent_core.AgentRequest
import javax.inject.Inject

class AgentDecisionEngine @Inject constructor() {
    fun decide(request: AgentRequest, intelligence: AgentIntelligencePlan): AgentDecision {
        val module = when {
            AgentCapability.EmotionalSupport in intelligence.selectedCapabilities -> "Emotional AI + Tutor"
            request.userNeed.contains("plan", ignoreCase = true) -> "Planning AI"
            request.userNeed.contains("master", ignoreCase = true) -> "Mastery AI"
            request.userNeed.contains("research", ignoreCase = true) -> "Research AI"
            else -> "AI Teacher"
        }
        return AgentDecision(
            decisionId = "decision-${request.userId}",
            moduleToUse = module,
            explanationStyle = if (request.skillLevel.contains("beginner", ignoreCase = true)) "simple guided explanation" else "concise expert explanation",
            learningActivity = "personalized lesson, practice, and progress review",
            strategy = "Use AI reasoning to select the smallest helpful action for ${request.currentGoal}.",
            humanControl = true,
        )
    }
}
