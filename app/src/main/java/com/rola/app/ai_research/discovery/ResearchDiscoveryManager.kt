package com.rola.app.ai_research.discovery

import com.rola.app.ai_research.scientist.ResearchContext
import com.rola.app.ai_research.scientist.ResearchDiscovery
import javax.inject.Inject

class ResearchDiscoveryManager @Inject constructor() {
    fun discover(context: ResearchContext): ResearchDiscovery =
        ResearchDiscovery(
            discoveryId = "discovery-${context.domain.lowercase().replace(" ", "-")}",
            knowledgeGaps = listOf("unanswered question: ${context.question}", "limited learner experiment evidence"),
            opportunities = listOf("connect Knowledge Graph with Creative AI outputs", "turn question into student research proposal"),
            emergingTopics = context.dataSignals + "AI-assisted scientific learning",
        )
}
