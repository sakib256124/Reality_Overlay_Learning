package com.rola.app.mastery_ai.competency

import com.rola.app.mastery_ai.mastery_engine.CompetencyScore
import com.rola.app.mastery_ai.mastery_engine.MasteryLevel
import com.rola.app.mastery_ai.mastery_engine.MasteryRequest
import javax.inject.Inject

class CompetencyAssessmentEngine @Inject constructor() {
    fun assess(request: MasteryRequest): CompetencyScore {
        val creativity = (request.previousPerformance.average().takeIf { !it.isNaN() }?.toInt() ?: request.learningConsistency).coerceIn(0, 100)
        val realWorld = ((request.practicalAbility + request.problemSolvingCapability) / 2).coerceIn(0, 100)
        val total = listOf(request.knowledgeLevel, request.practicalAbility, request.problemSolvingCapability, creativity, realWorld).average().toInt()
        return CompetencyScore(
            scoreId = "competency-${request.learnerId}",
            conceptMastery = request.knowledgeLevel,
            practicalApplication = request.practicalAbility,
            criticalThinking = request.problemSolvingCapability,
            creativity = creativity,
            realWorldPerformance = realWorld,
            level = when {
                total >= 90 -> MasteryLevel.Expert
                total >= 75 -> MasteryLevel.Advanced
                total >= 55 -> MasteryLevel.Intermediate
                else -> MasteryLevel.Beginner
            },
        )
    }
}
