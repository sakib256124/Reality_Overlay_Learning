package com.rola.app.personal_agent.agent_core

import com.rola.app.personal_agent.evolution.AgentEvolutionEngine
import com.rola.app.personal_agent.intelligence.AgentIntelligenceEngine
import com.rola.app.personal_agent.memory.AgentMemoryManager
import com.rola.app.personal_agent.mentoring.AgentMentorManager
import com.rola.app.personal_agent.planning.AgentDecisionEngine
import com.rola.app.personal_agent.teaching.AgentTeachingManager
import javax.inject.Inject

class UniversalEducationAgent @Inject constructor(
    private val coreManager: PersonalAgentCoreManager,
    private val intelligenceEngine: AgentIntelligenceEngine,
    private val memoryManager: AgentMemoryManager,
    private val teachingManager: AgentTeachingManager,
    private val mentorManager: AgentMentorManager,
    private val decisionEngine: AgentDecisionEngine,
    private val evolutionEngine: AgentEvolutionEngine,
) {
    fun respond(request: AgentRequest): UniversalAgentResult {
        val intelligence = intelligenceEngine.fuse(request)
        return UniversalAgentResult(
            resultId = "universal-agent-${request.userId}",
            agent = coreManager.createAgent(request),
            intelligence = intelligence,
            memory = memoryManager.remember(request),
            teaching = teachingManager.teach(request),
            mentor = mentorManager.mentor(request),
            decision = decisionEngine.decide(request, intelligence),
            evolution = evolutionEngine.evolve(request),
        )
    }
}
