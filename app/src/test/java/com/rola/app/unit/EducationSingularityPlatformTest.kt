package com.rola.app.unit

import com.rola.app.education_singularity.evolution.LearningEvolutionManager
import com.rola.app.education_singularity.governance.SingularityGovernanceManager
import com.rola.app.education_singularity.intelligence_fusion.IntelligenceFusionManager
import com.rola.app.education_singularity.knowledge_universe.KnowledgeFusionEngine
import com.rola.app.education_singularity.knowledge_universe.UniversalKnowledgeNetwork
import com.rola.app.education_singularity.learning_engine.UniversalTeacherIntelligence
import com.rola.app.education_singularity.universal_intelligence.EducationalSingularityEngine
import com.rola.app.education_singularity.universal_intelligence.SingularityLearningContext
import com.rola.app.education_singularity.universal_intelligence.SingularityLevel
import com.rola.app.education_singularity.universal_intelligence.UniversalEducationCoordinator
import com.rola.app.education_singularity.universal_intelligence.UniversalLearningIntelligence
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EducationSingularityPlatformTest {
    private val engine = EducationalSingularityEngine(
        UniversalLearningIntelligence(),
        KnowledgeFusionEngine(),
        IntelligenceFusionManager(),
        UniversalTeacherIntelligence(),
        LearningEvolutionManager(),
        UniversalEducationCoordinator(),
        UniversalKnowledgeNetwork(),
        SingularityGovernanceManager(),
    )

    @Test
    fun singularityCycle_fusesKnowledgeIntelligenceGovernanceAndRoadmap() {
        val result = engine.createUniversalLearningSystem(
            SingularityLearningContext(
                userId = "learner-singularity",
                topic = "Electric Circuits",
                level = SingularityLevel.Research,
                learningSignals = listOf("concept gap", "needs research path"),
                globalSignals = listOf("global misconception pattern"),
            ),
        )

        assertEquals("learner-singularity", result.learningModel.userId)
        assertTrue(result.knowledgeFusion.sources.contains("Collective AI System"))
        assertTrue(result.intelligenceConnection.systems.contains("Neural AI"))
        assertTrue(result.governance.policies.contains("human-control"))
        assertTrue(result.futureRoadmap.isNotEmpty())
        assertTrue(result.analytics.intelligenceScore >= 90)
    }
}
