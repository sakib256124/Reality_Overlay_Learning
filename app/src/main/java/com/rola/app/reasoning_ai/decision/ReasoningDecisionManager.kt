package com.rola.app.reasoning_ai.decision

import com.rola.app.reasoning_ai.reasoning_core.ProblemSolution
import com.rola.app.reasoning_ai.reasoning_core.ReasoningConfidence
import com.rola.app.reasoning_ai.reasoning_core.ReasoningDecision
import javax.inject.Inject

class ReasoningDecisionManager @Inject constructor() {
    fun decide(solution: ProblemSolution): ReasoningDecision =
        ReasoningDecision("decision-${solution.solutionId}", "Use ${solution.method} and show every reasoning step.", solution.confidence == ReasoningConfidence.Verified, humanReviewSupported = true)
}
