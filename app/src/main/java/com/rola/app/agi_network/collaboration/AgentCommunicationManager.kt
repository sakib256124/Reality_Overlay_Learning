package com.rola.app.agi_network.collaboration

import com.rola.app.agi_network.intelligence.AgentCommunicationMessage
import com.rola.app.agi_network.intelligence.AGINetworkAgentRole
import com.rola.app.agi_network.intelligence.EducationNetworkSignal
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AgentCommunicationManager @Inject constructor() {
    fun messagesFor(
        signal: EducationNetworkSignal,
        agents: List<AGINetworkAgentRole>,
    ): List<AgentCommunicationMessage> {
        if (agents.size < 2) return emptyList()
        return agents.zipWithNext().map { (from, to) ->
            AgentCommunicationMessage(
                messageId = "agent-message-${UUID.randomUUID()}",
                fromAgent = from,
                toAgent = to,
                topic = signal.topic,
                content = "${from.name} shares evidence about ${signal.topic} with ${to.name}.",
                confidence = confidenceFor(signal),
            )
        }
    }

    fun resolveConflicts(messages: List<AgentCommunicationMessage>): String {
        val lowConfidence = messages.any { it.confidence < 0.7f }
        return if (lowConfidence) {
            "Route conflicting or low-confidence agent output to teacher governance review."
        } else {
            "Agents agree enough to generate a draft educational action for human-supervised review."
        }
    }

    private fun confidenceFor(signal: EducationNetworkSignal): Float =
        when {
            signal.researchEvidence.size >= 2 && (signal.contentQualityScore ?: 80) >= 75 -> 0.88f
            signal.researchEvidence.isNotEmpty() -> 0.76f
            else -> 0.68f
        }
}
