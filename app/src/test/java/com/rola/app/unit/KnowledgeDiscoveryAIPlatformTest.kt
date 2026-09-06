package com.rola.app.unit

import com.rola.app.knowledge_discovery_ai.analysis.KnowledgeDiscoveryManager
import com.rola.app.knowledge_discovery_ai.discovery_core.AutonomousKnowledgeDiscoveryEngine
import com.rola.app.knowledge_discovery_ai.discovery_core.DiscoverySourceType
import com.rola.app.knowledge_discovery_ai.discovery_core.DiscoveryStatus
import com.rola.app.knowledge_discovery_ai.discovery_core.KnowledgeDiscoveryRequest
import com.rola.app.knowledge_discovery_ai.evolution.FutureKnowledgePredictionEngine
import com.rola.app.knowledge_discovery_ai.evolution.KnowledgeEvolutionTracker
import com.rola.app.knowledge_discovery_ai.global_search.GlobalKnowledgeScanner
import com.rola.app.knowledge_discovery_ai.intelligence.DiscoveryIntelligenceManager
import com.rola.app.knowledge_discovery_ai.intelligence.DiscoveryValidationEngine
import com.rola.app.knowledge_discovery_ai.intelligence.GlobalIntelligenceNetwork
import com.rola.app.knowledge_discovery_ai.relationship.KnowledgeRelationshipEngine
import com.rola.app.knowledge_discovery_ai.research.DiscoveryLearningManager
import com.rola.app.knowledge_discovery_ai.research.ResearchOpportunityAnalyzer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class KnowledgeDiscoveryAIPlatformTest {
    private val engine = AutonomousKnowledgeDiscoveryEngine(
        GlobalKnowledgeScanner(),
        KnowledgeDiscoveryManager(),
        KnowledgeRelationshipEngine(),
        ResearchOpportunityAnalyzer(),
        DiscoveryValidationEngine(),
        GlobalIntelligenceNetwork(),
        FutureKnowledgePredictionEngine(),
        DiscoveryLearningManager(),
        KnowledgeEvolutionTracker(),
        DiscoveryIntelligenceManager(),
    )

    @Test
    fun knowledgeDiscovery_scansRelatesValidatesPredictsAndPackagesLearning() {
        val result = engine.discover(
            KnowledgeDiscoveryRequest(
                learnerId = "discovery-learner",
                domain = "biology and machine learning",
                existingKnowledge = listOf("biology", "machine learning", "data analysis"),
                globalSources = listOf(DiscoverySourceType.ResearchPaper, DiscoverySourceType.ScientificDatabase, DiscoverySourceType.DigitalLibrary),
                researchQuestion = "How can students learn bioinformatics earlier?",
            ),
        )

        assertEquals(DiscoveryStatus.NeedsHumanApproval, result.status)
        assertTrue(result.scan.reliabilitySignals.contains("peer-reviewed source"))
        assertTrue(result.discovery.newConcepts.contains("bioinformatics learning path"))
        assertTrue(result.relationships.crossDomainRelationships.any { it.contains("Bioinformatics Learning Path") })
        assertTrue(result.opportunities.researchGaps.any { it.contains("bioinformatics") })
        assertTrue(result.validation.reliabilityScore >= 90)
        assertTrue(result.validation.humanApprovalRequired)
        assertTrue(result.network.aiSystems.contains("Knowledge Engineering"))
        assertTrue(result.prediction.futureSkills.contains("source evaluation"))
        assertTrue(result.learningPackage.lessons.isNotEmpty())
        assertTrue(result.intelligence.trustStatus.contains("Source verification"))
    }
}
