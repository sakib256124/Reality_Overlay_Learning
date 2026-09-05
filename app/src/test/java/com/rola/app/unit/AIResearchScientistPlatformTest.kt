package com.rola.app.unit

import com.rola.app.ai_research.analysis.KnowledgeValidationEngine
import com.rola.app.ai_research.analysis.ResearchAnalysisEngine
import com.rola.app.ai_research.collaboration.ResearchCollaborationManager
import com.rola.app.ai_research.discovery.ResearchDiscoveryManager
import com.rola.app.ai_research.experiment.ExperimentDesigner
import com.rola.app.ai_research.hypothesis.HypothesisGenerator
import com.rola.app.ai_research.scientist.AIResearchScientistAgent
import com.rola.app.ai_research.scientist.AIResearchScientistEngine
import com.rola.app.ai_research.scientist.ResearchContext
import com.rola.app.ai_research.scientist.ResearchMentorAgent
import com.rola.app.ai_research.scientist.ScientificLearningEngine
import org.junit.Assert.assertTrue
import org.junit.Test

class AIResearchScientistPlatformTest {
    private val engine = AIResearchScientistEngine(
        AIResearchScientistAgent(),
        ResearchDiscoveryManager(),
        HypothesisGenerator(),
        ExperimentDesigner(),
        ResearchAnalysisEngine(),
        KnowledgeValidationEngine(),
        ResearchCollaborationManager(),
        ResearchMentorAgent(),
        ScientificLearningEngine(),
    )

    @Test
    fun researchCycle_generatesHypothesisExperimentValidationCollaborationAndLearning() {
        val result = engine.runResearchCycle(
            ResearchContext("learner-research", "Renewable Energy", "Can simulations improve retention?", listOf("energy basics"), listOf("simulation trend")),
        )

        assertTrue(result.discovery.knowledgeGaps.isNotEmpty())
        assertTrue(result.hypotheses.hypotheses.isNotEmpty())
        assertTrue(result.experiment.resources.contains("Digital Twin"))
        assertTrue(result.validation.approved)
        assertTrue(result.collaboration.participants.contains("Researchers"))
        assertTrue(result.learningPackage.lessons.isNotEmpty())
        assertTrue(result.analytics.validationScore >= 90)
    }
}
