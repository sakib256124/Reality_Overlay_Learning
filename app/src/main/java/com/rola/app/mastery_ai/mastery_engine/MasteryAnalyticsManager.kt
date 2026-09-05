package com.rola.app.mastery_ai.mastery_engine

import javax.inject.Inject

class MasteryAnalyticsManager @Inject constructor() {
    fun summarize(profile: SkillMasteryProfile, competency: CompetencyScore, gaps: LearningGapReport, assessment: ContinuousAssessmentResult): MasteryAnalytics =
        MasteryAnalytics(
            analyticsId = "analytics-${profile.profileId}",
            masteryProgress = listOf(profile.knowledgeLevel, profile.practicalAbility, competency.criticalThinking, assessment.knowledgeRetention).average().toInt(),
            improvementAreas = gaps.weakSkills + gaps.missingConcepts,
            futureRecommendations = listOf("continue until stable ${competency.level.name} evidence", "add project-based mastery proof", "request human review for high-stakes decisions"),
            biasCheck = "No single assessment controls the decision; scoring is transparent and feedback-aware.",
        )
}
