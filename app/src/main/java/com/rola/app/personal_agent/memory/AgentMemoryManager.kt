package com.rola.app.personal_agent.memory

import com.rola.app.personal_agent.agent_core.AgentMemorySnapshot
import com.rola.app.personal_agent.agent_core.AgentRequest
import javax.inject.Inject

class AgentMemoryManager @Inject constructor() {
    fun remember(request: AgentRequest): AgentMemorySnapshot =
        AgentMemorySnapshot(
            memoryId = "memory-${request.userId}",
            shortTermMemory = listOf(request.userNeed, request.currentGoal, "current task support"),
            longTermMemory = listOf(request.careerObjective, request.skillLevel, request.learningSpeed) + request.previousMistakes,
            userControlled = true,
            privacyProtected = true,
        )
}
