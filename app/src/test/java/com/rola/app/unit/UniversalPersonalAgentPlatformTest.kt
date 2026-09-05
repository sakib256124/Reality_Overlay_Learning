package com.rola.app.unit

import com.rola.app.personal_agent.agent_core.AgentCapability
import com.rola.app.personal_agent.agent_core.AgentRequest
import com.rola.app.personal_agent.agent_core.PersonalAgentCoreManager
import com.rola.app.personal_agent.agent_core.UniversalEducationAgent
import com.rola.app.personal_agent.evolution.AgentEvolutionEngine
import com.rola.app.personal_agent.intelligence.AgentIntelligenceEngine
import com.rola.app.personal_agent.memory.AgentMemoryManager
import com.rola.app.personal_agent.mentoring.AgentMentorManager
import com.rola.app.personal_agent.planning.AgentDecisionEngine
import com.rola.app.personal_agent.teaching.AgentTeachingManager
import org.junit.Assert.assertTrue
import org.junit.Test

class UniversalPersonalAgentPlatformTest {
    private val agent = UniversalEducationAgent(
        PersonalAgentCoreManager(),
        AgentIntelligenceEngine(),
        AgentMemoryManager(),
        AgentTeachingManager(),
        AgentMentorManager(),
        AgentDecisionEngine(),
        AgentEvolutionEngine(),
    )

    @Test
    fun personalAgent_fusesIntelligenceMemoryTeachingMentoringDecisionAndEvolution() {
        val result = agent.respond(
            AgentRequest(
                userId = "personal-agent-learner",
                userNeed = "plan a mastery-based research learning path",
                currentGoal = "AI education architecture",
                skillLevel = "intermediate",
                emotionState = "focused but stressed",
                learningSpeed = "steady",
                previousMistakes = listOf("skipped review", "weak project evidence"),
                careerObjective = "future AI education architect",
            ),
        )

        assertTrue(result.agent.preferences.contains("user-controlled memory"))
        assertTrue(result.intelligence.selectedCapabilities.contains(AgentCapability.Planning))
        assertTrue(result.intelligence.selectedCapabilities.contains(AgentCapability.Research))
        assertTrue(result.intelligence.selectedCapabilities.contains(AgentCapability.EmotionalSupport))
        assertTrue(result.memory.userControlled)
        assertTrue(result.memory.privacyProtected)
        assertTrue(result.teaching.exercises.any { it.contains("skipped review") })
        assertTrue(result.mentor.growthRoadmap.contains("evolve"))
        assertTrue(result.decision.humanControl)
        assertTrue(result.evolution.personalUnderstanding >= 90)
    }
}
