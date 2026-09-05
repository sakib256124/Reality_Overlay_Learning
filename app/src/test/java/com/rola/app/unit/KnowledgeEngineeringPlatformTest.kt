package com.rola.app.unit

import com.rola.app.knowledge_engineering.evolution.KnowledgeEvolutionManager
import com.rola.app.knowledge_engineering.extraction.KnowledgeExtractionEngine
import com.rola.app.knowledge_engineering.knowledge_core.AIKnowledgeEngine
import com.rola.app.knowledge_engineering.knowledge_core.KnowledgeConfidence
import com.rola.app.knowledge_engineering.knowledge_core.KnowledgeCoreManager
import com.rola.app.knowledge_engineering.knowledge_core.KnowledgeEngineeringRequest
import com.rola.app.knowledge_engineering.knowledge_core.UniversalKnowledgeDatabase
import com.rola.app.knowledge_engineering.organization.AdvancedKnowledgeGraphManager
import com.rola.app.knowledge_engineering.organization.KnowledgeOrganizationManager
import com.rola.app.knowledge_engineering.processing.KnowledgeProcessingManager
import com.rola.app.knowledge_engineering.reasoning.IntelligentKnowledgeSearch
import com.rola.app.knowledge_engineering.reasoning.KnowledgeDeliveryEngine
import com.rola.app.knowledge_engineering.reasoning.KnowledgeReasoningEngine
import com.rola.app.knowledge_engineering.validation.KnowledgeValidationEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class KnowledgeEngineeringPlatformTest {
    private val engine = AIKnowledgeEngine(
        KnowledgeExtractionEngine(),
        KnowledgeProcessingManager(),
        KnowledgeValidationEngine(),
        KnowledgeReasoningEngine(),
        KnowledgeOrganizationManager(),
        KnowledgeEvolutionManager(),
        KnowledgeCoreManager(),
        KnowledgeDeliveryEngine(),
        AdvancedKnowledgeGraphManager(),
        IntelligentKnowledgeSearch(),
        UniversalKnowledgeDatabase(),
    )

    @Test
    fun knowledgeCycle_extractsValidatesReasonsOrganizesAndDeliversKnowledge() {
        val result = engine.engineerKnowledge(
            KnowledgeEngineeringRequest("learner-knowledge", "Photosynthesis", listOf("plants use light energy", "chlorophyll captures light"), "Beginner", "visual"),
        )

        assertTrue(result.extracted.concepts.contains("Photosynthesis"))
        assertTrue(result.structured.definitions.isNotEmpty())
        assertEquals(KnowledgeConfidence.Verified, result.validation.confidence)
        assertTrue(result.reasoning.explanation.contains("explainable"))
        assertTrue(result.organization.semanticIndexes.isNotEmpty())
        assertTrue(result.evolution.improvedExplanations.isNotEmpty())
        assertTrue(result.delivery.personalizationBasis.contains("emotional state"))
        assertTrue(result.analytics.validationScore >= 90)
    }
}
