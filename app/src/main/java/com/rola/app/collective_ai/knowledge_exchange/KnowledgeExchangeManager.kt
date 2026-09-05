package com.rola.app.collective_ai.knowledge_exchange

import com.rola.app.collective_ai.intelligence_network.AIAgentNode
import com.rola.app.collective_ai.intelligence_network.CollectiveAIRequest
import com.rola.app.collective_ai.intelligence_network.KnowledgeExchange
import javax.inject.Inject

class KnowledgeExchangeManager @Inject constructor() {
    fun exchangeKnowledge(request: CollectiveAIRequest, agents: List<AIAgentNode>): List<KnowledgeExchange> =
        agents.zipWithNext().mapIndexed { index, pair ->
            KnowledgeExchange(
                exchangeId = "exchange-${request.topic.lowercase().replace(" ", "-")}-$index",
                sourceAgentId = pair.first.agentId,
                targetAgentId = pair.second.agentId,
                topic = request.topic,
                knowledgeSummary = "${pair.first.name} shares ${pair.first.capability} with ${pair.second.name}.",
                sources = listOf("Knowledge Graph", "AI Research System", "Global Education Network"),
            )
        }
}
