package com.rola.app.unit

import com.rola.app.ai_civilization.future.FutureEducationPlanner
import com.rola.app.ai_civilization.governance.CivilizationGovernanceManager
import com.rola.app.ai_civilization.innovation.InnovationDiscoveryEngine
import com.rola.app.ai_civilization.intelligence.CivilizationContext
import com.rola.app.ai_civilization.intelligence.CivilizationLearningLevel
import com.rola.app.ai_civilization.intelligence.UniversalAICivilizationEngine
import com.rola.app.ai_civilization.intelligence.UniversalAIEducator
import com.rola.app.ai_civilization.knowledge_evolution.KnowledgeCivilizationNetwork
import com.rola.app.ai_civilization.knowledge_evolution.KnowledgeEvolutionEngine
import com.rola.app.ai_civilization.learning_evolution.LearningEvolutionEngine
import com.rola.app.ai_civilization.society.AICivilizationManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AICivilizationPlatformTest {
    private val engine = UniversalAICivilizationEngine(
        AICivilizationManager(),
        KnowledgeEvolutionEngine(),
        LearningEvolutionEngine(),
        InnovationDiscoveryEngine(),
        FutureEducationPlanner(),
        CivilizationGovernanceManager(),
        UniversalAIEducator(),
        KnowledgeCivilizationNetwork(),
    )

    @Test
    fun civilizationCycle_evolvesKnowledgeLearningInnovationAndGovernance() {
        val result = engine.evolveCivilization(
            CivilizationContext("learner-civ", "Electric Circuits", CivilizationLearningLevel.Research, listOf("knowledge gap", "future skill")),
        )

        assertEquals("universal-ai-learning-civilization", result.civilization.civilizationId)
        assertTrue(result.knowledgeEvolution.graphExpansion.isNotEmpty())
        assertTrue(result.learningEvolution.curriculumUpdates.any { it.contains("Electric Circuits") })
        assertTrue(result.innovation.researchDirections.contains("AI-human curriculum co-design"))
        assertTrue(result.governance.approvalRequired)
        assertTrue(result.futurePlan.roadmap.isNotEmpty())
        assertTrue(result.analytics.evolutionScore >= 90)
    }
}
