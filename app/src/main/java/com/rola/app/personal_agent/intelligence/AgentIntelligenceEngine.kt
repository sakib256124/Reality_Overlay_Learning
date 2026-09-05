package com.rola.app.personal_agent.intelligence

import com.rola.app.personal_agent.agent_core.AgentCapability
import com.rola.app.personal_agent.agent_core.AgentIntelligencePlan
import com.rola.app.personal_agent.agent_core.AgentRequest
import javax.inject.Inject

class AgentIntelligenceEngine @Inject constructor() {
    fun fuse(request: AgentRequest): AgentIntelligencePlan {
        val capabilities = mutableListOf(AgentCapability.Teaching, AgentCapability.Tutoring, AgentCapability.Planning)
        if (request.userNeed.contains("research", ignoreCase = true)) capabilities += AgentCapability.Research
        if (request.emotionState.contains("stress", ignoreCase = true)) capabilities += AgentCapability.EmotionalSupport
        capabilities += listOf(AgentCapability.Prediction, AgentCapability.Mastery, AgentCapability.Mentoring)
        return AgentIntelligencePlan(
            planId = "fusion-${request.userId}",
            selectedCapabilities = capabilities.distinct(),
            responseStrategy = "Select the best AI capability for ${request.userNeed} and personalize with mastery, emotion, planning, and memory signals.",
            transparentDecision = "Capability choice is explainable and can be overridden by the learner.",
        )
    }
}
