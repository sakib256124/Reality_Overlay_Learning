package com.rola.app.unit

import com.rola.app.education_marketplace_ai.analytics.MarketplaceAnalyticsManager
import com.rola.app.education_marketplace_ai.creator_network.CreatorNetworkManager
import com.rola.app.education_marketplace_ai.intelligence.AICourseGenerator
import com.rola.app.education_marketplace_ai.intelligence.AdaptiveResourceEngine
import com.rola.app.education_marketplace_ai.marketplace_core.EducationMarketplaceEngine
import com.rola.app.education_marketplace_ai.marketplace_core.EducationMarketplaceRequest
import com.rola.app.education_marketplace_ai.marketplace_core.MarketplaceStatus
import com.rola.app.education_marketplace_ai.quality_control.ResourceQualityAnalyzer
import com.rola.app.education_marketplace_ai.recommendation.AIRecommendationEngine
import com.rola.app.education_marketplace_ai.resource_management.LearningResourceManager
import com.rola.app.education_marketplace_ai.resource_management.ResourceIntelligenceManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EducationMarketplaceAIPlatformTest {
    private val engine = EducationMarketplaceEngine(
        ResourceIntelligenceManager(),
        LearningResourceManager(),
        AIRecommendationEngine(),
        CreatorNetworkManager(),
        ResourceQualityAnalyzer(),
        AdaptiveResourceEngine(),
        AICourseGenerator(),
        MarketplaceAnalyticsManager(),
    )

    @Test
    fun marketplace_discoversRecommendsGeneratesValidatesAndSecuresResources() {
        val result = engine.buildMarketplace(
            EducationMarketplaceRequest(
                learnerId = "marketplace-learner",
                learningGoal = "learn applied AI with simulations",
                skillLevel = "intermediate",
                cognitiveProfile = "visual project learner",
                emotionalState = "confident",
                learningHistory = listOf("AI basics", "robotics lab", "knowledge graph lesson"),
            ),
        )

        assertEquals(MarketplaceStatus.NeedsValidation, result.status)
        assertTrue(result.discovery.resources.contains("AI foundations course"))
        assertTrue(result.catalog.versionControl)
        assertTrue(result.catalog.accessibilityReady)
        assertTrue(result.recommendations.bestCourses.contains("adaptive AI course"))
        assertTrue(result.creatorNetwork.aiCreators.contains("Knowledge Engineering"))
        assertTrue(result.quality.scientificReliability >= 90)
        assertTrue(result.quality.validationRequired)
        assertTrue(result.adaptation.expertVersion.contains("research-level"))
        assertTrue(result.course.integratedSystems.contains("Creative AI"))
        assertTrue(result.analytics.learningEffectiveness >= 90)
        assertTrue(result.analytics.trustStatus.contains("secure transactions"))
    }
}
