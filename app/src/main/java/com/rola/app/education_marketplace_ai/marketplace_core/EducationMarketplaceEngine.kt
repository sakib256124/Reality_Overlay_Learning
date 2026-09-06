package com.rola.app.education_marketplace_ai.marketplace_core

import com.rola.app.education_marketplace_ai.analytics.MarketplaceAnalyticsManager
import com.rola.app.education_marketplace_ai.creator_network.CreatorNetworkManager
import com.rola.app.education_marketplace_ai.intelligence.AICourseGenerator
import com.rola.app.education_marketplace_ai.intelligence.AdaptiveResourceEngine
import com.rola.app.education_marketplace_ai.quality_control.ResourceQualityAnalyzer
import com.rola.app.education_marketplace_ai.recommendation.AIRecommendationEngine
import com.rola.app.education_marketplace_ai.resource_management.LearningResourceManager
import com.rola.app.education_marketplace_ai.resource_management.ResourceIntelligenceManager
import javax.inject.Inject

class EducationMarketplaceEngine @Inject constructor(
    private val resourceIntelligenceManager: ResourceIntelligenceManager,
    private val learningResourceManager: LearningResourceManager,
    private val recommendationEngine: AIRecommendationEngine,
    private val creatorNetworkManager: CreatorNetworkManager,
    private val qualityAnalyzer: ResourceQualityAnalyzer,
    private val adaptiveResourceEngine: AdaptiveResourceEngine,
    private val courseGenerator: AICourseGenerator,
    private val analyticsManager: MarketplaceAnalyticsManager,
) {
    fun buildMarketplace(request: EducationMarketplaceRequest): EducationMarketplaceResult {
        val discovery = resourceIntelligenceManager.discover(request)
        val catalog = learningResourceManager.organize(discovery)
        val recommendations = recommendationEngine.recommend(request, catalog)
        val quality = qualityAnalyzer.evaluate(catalog)
        return EducationMarketplaceResult(
            resultId = "education-marketplace-${request.learnerId}",
            discovery = discovery,
            catalog = catalog,
            recommendations = recommendations,
            creatorNetwork = creatorNetworkManager.connect(request),
            quality = quality,
            adaptation = adaptiveResourceEngine.adapt(request),
            course = courseGenerator.generate(request),
            analytics = analyticsManager.analyze(recommendations, quality),
            status = MarketplaceStatus.NeedsValidation,
        )
    }
}
