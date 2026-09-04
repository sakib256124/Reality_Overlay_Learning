package com.rola.app.core.agi.agents

import com.rola.app.core.agi.memory.LearnerMemory
import com.rola.app.domain.model.AGIAgentMessage
import com.rola.app.domain.model.AGIAgentRole
import com.rola.app.domain.model.AGIActivityType
import com.rola.app.domain.model.AGILearningEvent
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KnowledgeExpansionAgent @Inject constructor() : AutonomousLearningAgent {
    override val role: AGIAgentRole = AGIAgentRole.KnowledgeExpansionAgent

    override fun canHandle(event: AGILearningEvent): Boolean =
        event.activityType in setOf(AGIActivityType.ResearchRead, AGIActivityType.ARExperiment) ||
            event.signal.contains("missing", ignoreCase = true)

    override fun act(
        event: AGILearningEvent,
        memory: LearnerMemory,
    ): AGIAgentMessage = AGIAgentMessage(
        messageId = "agent-message-${UUID.randomUUID()}",
        agent = role,
        learnerId = memory.learnerId,
        topic = event.topic,
        intent = "Identify missing concepts and propose knowledge graph expansion.",
        evidence = listOf(event.signal, "Known skills ${memory.skills.take(3).joinToString { it.name }}"),
        confidence = 0.8f,
    )
}
