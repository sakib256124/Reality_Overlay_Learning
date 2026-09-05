package com.rola.app.ai_os.agents

import com.rola.app.ai_os.intelligence.AIOSAgentType
import com.rola.app.ai_os.intelligence.AIOSRequest
import com.rola.app.ai_os.intelligence.AgentRuntimeState
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AgentRuntimeManager @Inject constructor() {
    fun startAgents(request: AIOSRequest): AgentRuntimeState {
        val agents = AIOSAgentType.values().toList()
        return AgentRuntimeState(
            runtimeId = "agent-runtime-${UUID.randomUUID()}",
            activeAgents = agents,
            assignedTasks = agents.map { "${it.name} handles ${request.activeTopic}" },
            communicationPlan = "Agents exchange task state through AI OS runtime messages with lifecycle tracking.",
        )
    }
}
