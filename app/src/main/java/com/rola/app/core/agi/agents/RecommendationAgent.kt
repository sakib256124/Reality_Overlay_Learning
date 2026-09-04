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
class RecommendationAgent @Inject constructor() : AutonomousLearningAgent {
    override val role: AGIAgentRole = AGIAgentRole.RecommendationAgent

    override fun canHandle(event: AGILearningEvent): Boolean =
        event.activityType in setOf(AGIActivityType.LessonComplete, AGIActivityType.GoalReview, AGIActivityType.QuizAttempt)

    override fun act(
        event: AGILearningEvent,
        memory: LearnerMemory,
    ): AGIAgentMessage = AGIAgentMessage(
        messageId = "agent-message-${UUID.randomUUID()}",
        agent = role,
        learnerId = memory.learnerId,
        topic = event.topic,
        intent = "Recommend next learning path, practice, or extension.",
        evidence = listOf("Speed ${memory.learningSpeed.name}", "Goals ${memory.goals.size}", event.signal),
        confidence = 0.84f,
    )
}
