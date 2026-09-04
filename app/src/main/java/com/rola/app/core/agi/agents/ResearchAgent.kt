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
class ResearchAgent @Inject constructor() : AutonomousLearningAgent {
    override val role: AGIAgentRole = AGIAgentRole.ResearchAgent

    override fun canHandle(event: AGILearningEvent): Boolean =
        event.activityType == AGIActivityType.ResearchRead || event.signal.contains("trend", ignoreCase = true)

    override fun act(
        event: AGILearningEvent,
        memory: LearnerMemory,
    ): AGIAgentMessage = AGIAgentMessage(
        messageId = "agent-message-${UUID.randomUUID()}",
        agent = role,
        learnerId = memory.learnerId,
        topic = event.topic,
        intent = "Analyze new research signals and identify curriculum implications.",
        evidence = listOf(event.signal, "Interests ${memory.interests.take(3).joinToString()}"),
        confidence = 0.82f,
    )
}
