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
class PersonalMentorAgent @Inject constructor() : AutonomousLearningAgent {
    override val role: AGIAgentRole = AGIAgentRole.PersonalMentorAgent

    override fun canHandle(event: AGILearningEvent): Boolean =
        event.activityType == AGIActivityType.GoalReview || event.signal.contains("motivation", ignoreCase = true)

    override fun act(
        event: AGILearningEvent,
        memory: LearnerMemory,
    ): AGIAgentMessage = AGIAgentMessage(
        messageId = "agent-message-${UUID.randomUUID()}",
        agent = role,
        learnerId = memory.learnerId,
        topic = event.topic,
        intent = "Provide daily guidance, goal tracking, motivation, and weakness improvement.",
        evidence = listOf("Retention ${memory.retentionScore}", "Patterns ${memory.learningPatterns.take(2).joinToString()}"),
        confidence = 0.88f,
    )
}
