package com.rola.app.mastery_ai.assessment

import com.rola.app.mastery_ai.mastery_engine.ContinuousAssessmentResult
import com.rola.app.mastery_ai.mastery_engine.MasteryRequest
import javax.inject.Inject

class ContinuousAssessmentEngine @Inject constructor() {
    fun assess(request: MasteryRequest): ContinuousAssessmentResult {
        val trend = request.previousPerformance.takeLast(3).average().takeIf { !it.isNaN() }?.toInt() ?: request.knowledgeLevel
        return ContinuousAssessmentResult(
            assessmentId = "assessment-${request.learnerId}",
            dailyProgress = trend.coerceIn(0, 100),
            practicalPerformance = request.practicalAbility,
            knowledgeRetention = ((request.knowledgeLevel + request.learningConsistency) / 2).coerceIn(0, 100),
            skillGrowth = (trend - (request.previousPerformance.firstOrNull() ?: trend)).coerceAtLeast(0),
            fairnessExplanation = "Scores use multiple evidence types instead of one exam-only signal.",
        )
    }
}
