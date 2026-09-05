package com.rola.app.reasoning_ai.reasoning_core

import com.rola.app.reasoning_ai.decision.ReasoningDecisionManager
import com.rola.app.reasoning_ai.explanation.ExplanationGenerationEngine
import com.rola.app.reasoning_ai.explanation.ReasoningTeacherAgent
import com.rola.app.reasoning_ai.inference.InferenceEngine
import com.rola.app.reasoning_ai.logical_engine.DomainReasoningManager
import com.rola.app.reasoning_ai.logical_engine.LogicalReasoningEngine
import com.rola.app.reasoning_ai.problem_solving.CriticalThinkingEngine
import com.rola.app.reasoning_ai.problem_solving.ProblemSolvingEngine
import javax.inject.Inject

class AIReasoningEngine @Inject constructor(
    private val coreManager: ReasoningCoreManager,
    private val logicalReasoningEngine: LogicalReasoningEngine,
    private val inferenceEngine: InferenceEngine,
    private val problemSolvingEngine: ProblemSolvingEngine,
    private val explanationGenerationEngine: ExplanationGenerationEngine,
    private val decisionManager: ReasoningDecisionManager,
    private val teacherAgent: ReasoningTeacherAgent,
    private val domainReasoningManager: DomainReasoningManager,
    private val criticalThinkingEngine: CriticalThinkingEngine,
    private val personalProfile: PersonalReasoningProfile,
) {
    fun solve(request: ReasoningRequest): ReasoningResult {
        val profile = personalProfile.build(request)
        val baseTrace = coreManager.analyze(request)
        val trace = baseTrace.copy(logicalSteps = baseTrace.logicalSteps + teacherAgent.diagnose(request) + domainReasoningManager.modelFor(request.domain))
        val logicalSteps = logicalReasoningEngine.reason(trace)
        val solution = problemSolvingEngine.solve(request, logicalSteps)
        return ReasoningResult(
            resultId = "reasoning-ai-${request.userId}",
            profile = profile,
            trace = trace,
            solution = solution,
            inference = inferenceEngine.infer(request),
            explanation = explanationGenerationEngine.explain(solution),
            criticalThinking = criticalThinkingEngine.develop(trace),
            decision = decisionManager.decide(solution),
        )
    }
}
