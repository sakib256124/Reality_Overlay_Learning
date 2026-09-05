package com.rola.app.education_orchestration.governance

import com.rola.app.education_orchestration.ecosystem_core.EcosystemOptimization
import com.rola.app.education_orchestration.ecosystem_core.EducationQualityScore
import javax.inject.Inject

class EducationQualityManager @Inject constructor() {
    fun evaluate(optimization: EcosystemOptimization): EducationQualityScore {
        val overall = listOf(optimization.learningQualityScore, optimization.performanceScore, optimization.userExperienceScore, optimization.outcomeScore).average().toInt()
        return EducationQualityScore(
            qualityId = "quality-${optimization.optimizationId}",
            learningEffectiveness = optimization.outcomeScore,
            aiResponseQuality = optimization.learningQualityScore,
            contentAccuracy = 93,
            userSatisfaction = optimization.userExperienceScore,
            overallScore = overall,
        )
    }
}
