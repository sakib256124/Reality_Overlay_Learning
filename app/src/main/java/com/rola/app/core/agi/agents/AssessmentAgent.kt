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
class AssessmentAgent @Inject constructor() : AutonomousLearningAgent {
    override val role: AGIAgentRole = AGIAgentRole.AssessmentAgent

    override fun canHandle(event: AGILearningEvent): Boolean =
        event.activityType == AGIActivityType.QuizAttempt || (event.score ?: 100) < 70

    override fun act(
        event: AGILearningEvent,
        memory: LearnerMemory,
    ): AGIAgentMessage = AGIAgentMessage(
        messageId = "agent-message-${UUID.randomUUID()}",
        agent = role,
        learnerId = memory.learnerId,
        topic = event.topic,
        intent = "Diagnose mastery and generate assessment follow-up.",
        evidence = listOf("Score ${event.score ?: "unknown"}", "Mistakes ${memory.previousMistakes.take(3).joinToString()}"),
        confidence = 0.9f,
    )
}
