package com.rola.app.education_marketplace_ai.intelligence

import com.rola.app.education_marketplace_ai.marketplace_core.AdaptiveResourcePlan
import com.rola.app.education_marketplace_ai.marketplace_core.EducationMarketplaceRequest
import javax.inject.Inject

class AdaptiveResourceEngine @Inject constructor() {
    fun adapt(request: EducationMarketplaceRequest): AdaptiveResourcePlan =
        AdaptiveResourcePlan(
            adaptationId = "adaptive-resource-${request.learnerId}",
            beginnerVersion = "simple explanation with guided examples",
            expertVersion = "research-level material with source trail",
            learningStyleFit = request.cognitiveProfile,
            masteryAdjustment = request.skillLevel,
            speedAdjustment = if (request.emotionalState.contains("confident", true)) "accelerated challenge" else "steady support",
        )
}
