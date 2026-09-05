package com.rola.app.predictive_ai.potential

import com.rola.app.predictive_ai.intelligence.PotentialBand
import com.rola.app.predictive_ai.intelligence.PotentialProfile
import com.rola.app.predictive_ai.intelligence.PredictionContext
import javax.inject.Inject

class PotentialAnalysisEngine @Inject constructor() {
    fun analyze(context: PredictionContext): PotentialProfile =
        PotentialProfile(
            profileId = "potential-${context.userId}",
            strengths = context.interests + listOf("analytical reasoning", "problem solving"),
            creativityScore = if (context.interests.any { it.contains("research", true) }) 92 else 84,
            researchPotential = if (context.quizScores.average() >= 80) PotentialBand.Exceptional else PotentialBand.Strong,
            suggestedPath = "advanced AI research pathway",
        )
}
