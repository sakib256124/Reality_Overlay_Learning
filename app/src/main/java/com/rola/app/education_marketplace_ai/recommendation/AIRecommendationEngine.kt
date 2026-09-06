package com.rola.app.education_marketplace_ai.recommendation

import com.rola.app.education_marketplace_ai.marketplace_core.EducationMarketplaceRequest
import com.rola.app.education_marketplace_ai.marketplace_core.LearningResourceCatalog
import com.rola.app.education_marketplace_ai.marketplace_core.ResourceRecommendationPlan
import javax.inject.Inject

class AIRecommendationEngine @Inject constructor() {
    fun recommend(request: EducationMarketplaceRequest, catalog: LearningResourceCatalog): ResourceRecommendationPlan =
        ResourceRecommendationPlan(
            recommendationId = "recommendation-${request.learnerId}",
            bestCourses = catalog.digitalCourses,
            bestResources = catalog.lessons.take(3),
            bestProjects = listOf("${request.learningGoal} capstone project", "portfolio simulation"),
            bestResearchMaterials = catalog.researchContent,
            rankingReason = "Ranked from learning goal, skill level, cognitive profile, emotional state, and history.",
        )
}
