package com.rola.app.cognitive_ai.decision

import com.rola.app.cognitive_ai.reasoning.LearningReasoningEngine
import com.rola.app.domain.model.CognitiveDecision
import com.rola.app.domain.model.CognitiveRecommendation
import com.rola.app.domain.model.LearnerCognitiveProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EducationalDecisionEngine @Inject constructor(
    private val learningReasoningEngine: LearningReasoningEngine,
) {
    fun decide(
        profile: LearnerCognitiveProfile,
        recommendation: CognitiveRecommendation,
    ): CognitiveDecision =
        learningReasoningEngine.strategyDecision(profile, recommendation.nextLesson).copy(
            recommendedActivities = recommendation.practiceActivities + recommendation.arExperience,
            explanation = "Decision is explainable: ${recommendation.rationale}",
            requiresConsent = !profile.consent.cognitiveAnalysisEnabled,
        )
}
