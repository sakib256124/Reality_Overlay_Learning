package com.rola.app.knowledge_discovery_ai.research

import com.rola.app.knowledge_discovery_ai.discovery_core.KnowledgeDiscovery
import com.rola.app.knowledge_discovery_ai.discovery_core.ResearchOpportunityReport
import javax.inject.Inject

class ResearchOpportunityAnalyzer @Inject constructor() {
    fun identify(discovery: KnowledgeDiscovery): ResearchOpportunityReport =
        ResearchOpportunityReport(
            opportunityId = "opportunity-${discovery.discoveryId}",
            unsolvedProblems = listOf("personalized source reliability explanation", "low-latency global discovery ranking"),
            researchGaps = discovery.knowledgeGaps,
            futureTopics = listOf("autonomous research studio", "AI-assisted scientific literacy"),
            innovationOpportunities = listOf("convert validated discovery into micro-course", "teacher-reviewed research challenge"),
        )
}
