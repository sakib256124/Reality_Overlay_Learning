package com.rola.app.unit

import com.rola.app.asi_core.collaboration.ASIWorldEducationNetwork
import com.rola.app.asi_core.collaboration.HumanAICollaborationManager
import com.rola.app.asi_core.creativity.CreativeKnowledgeEngine
import com.rola.app.asi_core.governance.ASIGovernanceManager
import com.rola.app.asi_core.intelligence.ASIApprovalStatus
import com.rola.app.asi_core.intelligence.ASIEducationChallenge
import com.rola.app.asi_core.intelligence.ASIEducationEngine
import com.rola.app.asi_core.intelligence.ASIEngine
import com.rola.app.asi_core.intelligence.ASIStakeholder
import com.rola.app.asi_core.intelligence.LearningStrategyOptimizer
import com.rola.app.asi_core.intelligence.SuperIntelligenceManager
import com.rola.app.asi_core.knowledge.UniversalKnowledgeEngine
import com.rola.app.asi_core.reasoning.AdvancedReasoningEngine
import com.rola.app.asi_core.reasoning.SuperReasoningEngine
import com.rola.app.asi_core.self_improvement.SelfImprovementEngine
import com.rola.app.asi_core.self_improvement.SelfImprovingEducationEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ASIEducationPlatformTest {
    private val engine = ASIEngine(
        superIntelligenceManager = SuperIntelligenceManager(),
        educationEngine = ASIEducationEngine(),
        reasoningEngine = SuperReasoningEngine(AdvancedReasoningEngine()),
        knowledgeEngine = UniversalKnowledgeEngine(),
        selfImprovingEducationEngine = SelfImprovingEducationEngine(SelfImprovementEngine()),
        creativeKnowledgeEngine = CreativeKnowledgeEngine(),
        collaborationManager = HumanAICollaborationManager(),
        strategyOptimizer = LearningStrategyOptimizer(),
        governanceManager = ASIGovernanceManager(),
        worldEducationNetwork = ASIWorldEducationNetwork(),
    )

    @Test
    fun asiEngine_createsReasonedHumanControlledLearningStrategy() {
        val result = engine.solveEducationChallenge(sampleChallenge())

        assertTrue(result.reasoningTrace.reasoningSteps.any { it.contains("prerequisite", ignoreCase = true) })
        assertTrue(result.learningStrategy.learningSequence.contains("Simplify explanation"))
        assertTrue(result.governanceRecord.humanOverrideAvailable)
    }

    @Test
    fun asiEngine_generatesCreativeKnowledgeAndGlobalInsight() {
        val result = engine.solveEducationChallenge(sampleChallenge())

        assertTrue(result.creativeOutput.educationalApproaches.isNotEmpty())
        assertTrue(result.knowledgeMap.newKnowledgeLinks.isNotEmpty())
        assertTrue(result.worldInsight.knowledgeSharingPlan.contains("approved", ignoreCase = true))
    }

    @Test
    fun governance_requiresHumanReviewForCurriculumImpact() {
        val result = engine.solveEducationChallenge(sampleChallenge())

        assertEquals(ASIApprovalStatus.NeedsHumanReview, result.governanceRecord.approvalStatus)
        assertTrue(result.selfImprovementLog.requiresOfflineValidation)
    }

    private fun sampleChallenge(): ASIEducationChallenge =
        ASIEducationChallenge(
            challengeId = "asi-challenge-1",
            learnerId = "learner-1",
            institutionId = "school-1",
            topic = "Scientific Reasoning",
            problemStatement = "Learner struggles to connect evidence, claim, and explanation in curriculum.",
            cognitiveSignals = listOf("concept gap: evidence selection", "attention: moderate"),
            neuralSignals = listOf("workload: high"),
            quantumInsights = listOf("optimized route: prerequisite repair before simulation"),
            learningHistory = listOf(52, 60, 66, 58),
            stakeholders = listOf(ASIStakeholder.Teacher, ASIStakeholder.Student, ASIStakeholder.Researcher),
        )
}
