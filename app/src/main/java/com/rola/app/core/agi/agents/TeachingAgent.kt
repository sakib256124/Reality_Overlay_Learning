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
class TeachingAgent @Inject constructor() : AutonomousLearningAgent {
    override val role: AGIAgentRole = AGIAgentRole.TeachingAgent

    override fun canHandle(event: AGILearningEvent): Boolean =
        event.activityType in setOf(AGIActivityType.ObjectScan, AGIActivityType.TutorQuestion, AGIActivityType.LessonComplete)

    override fun act(
        event: AGILearningEvent,
        memory: LearnerMemory,
    ): AGIAgentMessage = AGIAgentMessage(
        messageId = "agent-message-${UUID.randomUUID()}",
        agent = role,
        learnerId = memory.learnerId,
        topic = event.topic,
        intent = "Generate an adaptive explanation and lesson action for ${event.topic}.",
        evidence = listOf("Level ${memory.knowledgeLevel.name}", "Retention ${memory.retentionScore}", event.signal),
        confidence = 0.86f,
    )
}
