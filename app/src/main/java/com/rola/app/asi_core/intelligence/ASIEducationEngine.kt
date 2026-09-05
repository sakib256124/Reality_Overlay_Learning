package com.rola.app.asi_core.intelligence

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ASIEducationEngine @Inject constructor() {
    fun decisionFor(
        challenge: ASIEducationChallenge,
        reasoningTrace: AdvancedReasoningTrace,
        strategy: LearningStrategyPlan,
    ): ASIEducationDecision {
        val average = challenge.learningHistory.average().takeIf { !it.isNaN() } ?: 60.0
        val impact = if (challenge.problemStatement.contains("curriculum", ignoreCase = true)) {
            ASIDecisionImpact.Curriculum
        } else {
            ASIDecisionImpact.PersonalLesson
        }
        val risk = when {
            impact == ASIDecisionImpact.Curriculum -> ASIRiskLevel.High
            average < 50 -> ASIRiskLevel.Medium
            else -> ASIRiskLevel.Low
        }
        return ASIEducationDecision(
            decisionId = "asi-decision-${reasoningTrace.traceId}",
            learnerId = challenge.learnerId,
            topic = challenge.topic,
            impact = impact,
            action = strategy.learningSequence.joinToString(" -> "),
            explanation = reasoningTrace.explanation,
            riskLevel = risk,
        )
    }
}
