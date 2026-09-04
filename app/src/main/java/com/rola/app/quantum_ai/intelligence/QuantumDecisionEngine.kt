package com.rola.app.quantum_ai.intelligence

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuantumDecisionEngine @Inject constructor() {
    fun decide(
        input: QuantumLearningInput,
        optimization: QuantumOptimizationResult,
        prediction: QuantumPrediction,
    ): QuantumAIDecision {
        val needsSupport = prediction.futurePerformancePercent < 70 || input.knowledgeGaps.isNotEmpty()
        return QuantumAIDecision(
            decisionId = "quantum-decision-${UUID.randomUUID()}",
            learnerId = input.learnerId,
            topic = input.topic,
            decisionType = if (needsSupport) QuantumDecisionType.NextLearningActivity else QuantumDecisionType.KnowledgeExpansion,
            educationalAction = if (needsSupport) {
                "Start ${optimization.optimizedPath.firstOrNull() ?: "guided review"} with diagnostic assessment."
            } else {
                "Open exploratory knowledge expansion for ${input.topic}."
            },
            confidencePercent = optimization.learningOptimizationScore.coerceIn(45, 94),
            explanation = optimization.explanation,
            humanControlRequired = true,
        )
    }
}
