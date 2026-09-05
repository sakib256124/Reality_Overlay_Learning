package com.rola.app.unit

import com.rola.app.predictive_ai.forecasting.FutureLearningSimulator
import com.rola.app.predictive_ai.forecasting.KnowledgeTrendAnalyzer
import com.rola.app.predictive_ai.intelligence.FutureMentorAgent
import com.rola.app.predictive_ai.intelligence.PredictionContext
import com.rola.app.predictive_ai.optimization.GrowthOptimizationManager
import com.rola.app.predictive_ai.optimization.PredictionAnalyticsManager
import com.rola.app.predictive_ai.potential.PotentialAnalysisEngine
import com.rola.app.predictive_ai.prediction_engine.LearningPredictionEngine
import com.rola.app.predictive_ai.prediction_engine.PredictiveAIEngine
import com.rola.app.predictive_ai.recommendation.FutureLearningRoadmapEngine
import com.rola.app.predictive_ai.recommendation.FutureRecommendationEngine
import com.rola.app.predictive_ai.skill_analysis.SkillForecastingEngine
import org.junit.Assert.assertTrue
import org.junit.Test

class PredictiveAIPlatformTest {
    private val engine = PredictiveAIEngine(
        LearningPredictionEngine(),
        SkillForecastingEngine(),
        PotentialAnalysisEngine(),
        FutureLearningRoadmapEngine(),
        FutureRecommendationEngine(),
        GrowthOptimizationManager(),
        KnowledgeTrendAnalyzer(),
        FutureLearningSimulator(),
        FutureMentorAgent(),
        PredictionAnalyticsManager(),
    )

    @Test
    fun predictionCycle_generatesForecastPotentialRoadmapOptimizationAndPrivacyAnalytics() {
        val result = engine.predictFuture(
            PredictionContext("learner-predictive", "AI Programming", listOf("math gap", "project completed"), listOf(82, 88, 91), listOf("visual learner"), listOf("research", "programming")),
        )

        assertTrue(result.prediction.confidenceScore >= 90)
        assertTrue(result.potential.suggestedPath.contains("AI research"))
        assertTrue(result.skillModel.futureSkills.contains("machine learning"))
        assertTrue(result.roadmap.researchRoadmap.isNotEmpty())
        assertTrue(result.roadmap.skillRoadmap.any { it.contains("Prioritize") })
        assertTrue(result.optimization.resourceSelection.contains("Lifelong Memory"))
        assertTrue(result.simulation.futureSuccessProbability >= 80)
        assertTrue(result.analytics.privacyProtected)
    }
}
