package com.rola.app.unit

import com.rola.app.reasoning_ai.decision.ReasoningDecisionManager
import com.rola.app.reasoning_ai.explanation.ExplanationGenerationEngine
import com.rola.app.reasoning_ai.explanation.ReasoningTeacherAgent
import com.rola.app.reasoning_ai.inference.InferenceEngine
import com.rola.app.reasoning_ai.logical_engine.DomainReasoningManager
import com.rola.app.reasoning_ai.logical_engine.LogicalReasoningEngine
import com.rola.app.reasoning_ai.problem_solving.CriticalThinkingEngine
import com.rola.app.reasoning_ai.problem_solving.ProblemSolvingEngine
import com.rola.app.reasoning_ai.reasoning_core.AIReasoningEngine
import com.rola.app.reasoning_ai.reasoning_core.PersonalReasoningProfile
import com.rola.app.reasoning_ai.reasoning_core.ReasoningConfidence
import com.rola.app.reasoning_ai.reasoning_core.ReasoningCoreManager
import com.rola.app.reasoning_ai.reasoning_core.ReasoningDomain
import com.rola.app.reasoning_ai.reasoning_core.ReasoningRequest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ReasoningAIPlatformTest {
    private val engine = AIReasoningEngine(
        ReasoningCoreManager(),
        LogicalReasoningEngine(),
        InferenceEngine(),
        ProblemSolvingEngine(),
        ExplanationGenerationEngine(),
        ReasoningDecisionManager(),
        ReasoningTeacherAgent(),
        DomainReasoningManager(),
        CriticalThinkingEngine(),
        PersonalReasoningProfile(),
    )

    @Test
    fun reasoningCycle_solvesProblemExplainsInferenceAndImprovesCriticalThinking() {
        val result = engine.solve(
            ReasoningRequest(
                userId = "learner-reasoning",
                question = "Why does current change when resistance changes?",
                domain = ReasoningDomain.Science,
                availableKnowledge = listOf("Ohm law", "electric circuit", "cause effect"),
                studentAnswer = "current gets tired",
            ),
        )

        assertEquals(ReasoningConfidence.Verified, result.solution.confidence)
        assertTrue(result.trace.logicalSteps.any { it.contains("cause-effect") })
        assertTrue(result.inference.hiddenDiscoveries.isNotEmpty())
        assertTrue(result.explanation.steps.isNotEmpty())
        assertTrue(result.criticalThinking.recommendations.contains("test assumptions"))
        assertTrue(result.decision.verified)
    }
}
