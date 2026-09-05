package com.rola.app.self_evolving_ai.performance_analysis

import com.rola.app.self_evolving_ai.evolution_core.AIPerformanceReport
import com.rola.app.self_evolving_ai.evolution_core.SelfEvolutionRequest
import javax.inject.Inject

class PerformanceAnalysisEngine @Inject constructor() {
    fun analyze(request: SelfEvolutionRequest): AIPerformanceReport {
        val weaknesses = listOfNotNull(
            "AI response quality".takeIf { request.aiResponseQuality < 80 },
            "teaching effectiveness".takeIf { request.teachingEffectiveness < 80 },
            "recommendation accuracy".takeIf { request.recommendationAccuracy < 80 },
            "learning outcomes".takeIf { request.learningOutcomes < 80 },
            "system performance".takeIf { request.systemPerformance < 80 },
        )
        return AIPerformanceReport(
            reportId = "performance-${request.systemId}",
            responseQuality = request.aiResponseQuality,
            teachingEffectiveness = request.teachingEffectiveness,
            recommendationAccuracy = request.recommendationAccuracy,
            learningOutcomes = request.learningOutcomes,
            userSatisfaction = request.userSatisfaction,
            systemPerformance = request.systemPerformance,
            weaknesses = weaknesses,
        )
    }
}
