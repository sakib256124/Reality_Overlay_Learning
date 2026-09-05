package com.rola.app.collective_ai.communication

import com.rola.app.collective_ai.intelligence_network.AIAgentNode
import com.rola.app.collective_ai.intelligence_network.AgentCommunication
import javax.inject.Inject

class AICommunityManager @Inject constructor() {
    fun openCommunicationChannel(agents: List<AIAgentNode>): List<AgentCommunication> =
        agents.zipWithNext().map { pair ->
            AgentCommunication(
                communicationId = "message-${pair.first.agentId}-${pair.second.agentId}",
                senderAgentId = pair.first.agentId,
                receiverAgentId = pair.second.agentId,
                message = "${pair.first.name} requests review from ${pair.second.name}.",
            )
        }
}
