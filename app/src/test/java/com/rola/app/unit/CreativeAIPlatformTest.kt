package com.rola.app.unit

import com.rola.app.creative_ai.collaboration.HumanAICreativityManager
import com.rola.app.creative_ai.content_generation.CreativePersonalizationEngine
import com.rola.app.creative_ai.content_generation.LearningContentCreator
import com.rola.app.creative_ai.creativity_engine.CreativeAIEngine
import com.rola.app.creative_ai.creativity_engine.CreativeAIRequest
import com.rola.app.creative_ai.creativity_engine.CreativeKnowledgeGenerator
import com.rola.app.creative_ai.creativity_engine.CreativeLevel
import com.rola.app.creative_ai.evaluation.CreativeEvaluationEngine
import com.rola.app.creative_ai.innovation.InnovationDiscoveryEngine
import com.rola.app.creative_ai.research_creation.ResearchIdeaGenerator
import com.rola.app.creative_ai.simulation_creation.CreativeSimulationEngine
import org.junit.Assert.assertTrue
import org.junit.Test

class CreativeAIPlatformTest {
    private val engine = CreativeAIEngine(
        LearningContentCreator(),
        CreativeKnowledgeGenerator(),
        InnovationDiscoveryEngine(),
        ResearchIdeaGenerator(),
        CreativeSimulationEngine(),
        CreativePersonalizationEngine(),
        HumanAICreativityManager(),
        CreativeEvaluationEngine(),
    )

    @Test
    fun creativeCycle_generatesContentResearchSimulationCollaborationAndSafetyEvaluation() {
        val result = engine.createEducationPackage(
            CreativeAIRequest("learner-creative", "Science", "Electric Circuits", CreativeLevel.Beginner, "Understand current flow", "Make circuits less abstract"),
        )

        assertTrue(result.content.lessons.isNotEmpty())
        assertTrue(result.knowledge.analogies.isNotEmpty())
        assertTrue(result.innovation.humanValidationRequired)
        assertTrue(result.research.hypotheses.isNotEmpty())
        assertTrue(result.simulation.arActivities.isNotEmpty())
        assertTrue(result.personalization.projects.isNotEmpty())
        assertTrue(result.collaboration.solution.contains("human-approved"))
        assertTrue(result.evaluation.safeForLearners)
    }
}
