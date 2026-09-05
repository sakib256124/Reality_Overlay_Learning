package com.rola.app.reasoning_ai.explanation

import com.rola.app.reasoning_ai.reasoning_core.ExplanationRecord
import com.rola.app.reasoning_ai.reasoning_core.ProblemSolution
import javax.inject.Inject

class ExplanationGenerationEngine @Inject constructor() {
    fun explain(solution: ProblemSolution): ExplanationRecord =
        ExplanationRecord(
            explanationId = "explain-${solution.solutionId}",
            steps = listOf("read the problem", "name known facts", "apply rule", "check answer"),
            beginnerExplanation = "Break the problem into small steps and solve one relationship at a time.",
            expertExplanation = solution.answer,
            realWorldExample = "Like troubleshooting a circuit: check source, connection, rule, and outcome.",
        )
}
