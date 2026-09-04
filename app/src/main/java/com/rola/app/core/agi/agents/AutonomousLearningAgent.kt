package com.rola.app.core.agi.agents

import com.rola.app.core.agi.memory.LearnerMemory
import com.rola.app.domain.model.AGIAgentMessage
import com.rola.app.domain.model.AGIAgentRole
import com.rola.app.domain.model.AGILearningEvent

interface AutonomousLearningAgent {
    val role: AGIAgentRole

    fun canHandle(event: AGILearningEvent): Boolean

    fun act(
        event: AGILearningEvent,
        memory: LearnerMemory,
    ): AGIAgentMessage
}
