package com.rola.app.knowledge_discovery_ai.intelligence

import com.rola.app.knowledge_discovery_ai.discovery_core.DiscoveryIntelligenceSummary
import javax.inject.Inject

class DiscoveryIntelligenceManager @Inject constructor() {
    fun optimize(summary: DiscoveryIntelligenceSummary): DiscoveryIntelligenceSummary =
        summary.copy(analysis = summary.analysis + "indexed for fast knowledge retrieval")
}
