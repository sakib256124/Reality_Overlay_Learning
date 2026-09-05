package com.rola.app.predictive_ai

import com.rola.app.data.database.PredictiveAIDao
import com.rola.app.data.database.entities.FutureRoadmapEntity
import com.rola.app.data.database.entities.FutureSkillModelEntity
import com.rola.app.data.database.entities.GrowthAnalyticsEntity
import com.rola.app.data.database.entities.PotentialProfileEntity
import com.rola.app.data.database.entities.PredictionHistoryEntity
import com.rola.app.data.database.entities.PredictiveLearningPredictionEntity
import com.rola.app.data.database.entities.PredictiveOptimizationResultEntity
import com.rola.app.data.database.entities.TrendAnalysisEntity
import com.rola.app.predictive_ai.intelligence.PredictiveAIResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class PredictiveAIRepository @Inject constructor(private val dao: PredictiveAIDao) {
    fun observeDashboard(userId: String): Flow<PredictiveDashboardState> =
        combine(dao.observePrediction(userId), dao.observeSkills(), dao.observePotential(), dao.observeRoadmap(), dao.observeAnalytics()) { prediction, skills, potential, roadmap, analytics ->
            PredictiveDashboardState(
                learningForecast = prediction?.futurePerformance.orEmpty(),
                futureSkills = skills?.futureSkills.orEmpty(),
                potential = potential?.suggestedPath.orEmpty(),
                careerRoadmap = roadmap?.longTermPlan.orEmpty(),
                growthRecommendations = roadmap?.skillRoadmap.orEmpty(),
                confidenceScore = prediction?.confidenceScore ?: 0,
                growthScore = analytics?.growthScore ?: 0,
            )
        }

    suspend fun save(userId: String, result: PredictiveAIResult) {
        dao.upsertPrediction(PredictiveLearningPredictionEntity(result.prediction.predictionId, userId, result.prediction.futurePerformance, result.prediction.challenges, result.prediction.knowledgeGaps, result.prediction.confidenceScore))
        dao.upsertSkills(FutureSkillModelEntity(result.skillModel.modelId, result.skillModel.futureSkills, result.skillModel.technologyRequirements, result.skillModel.emergingAreas))
        dao.upsertPotential(PotentialProfileEntity(result.potential.profileId, result.potential.strengths, result.potential.creativityScore, result.potential.researchPotential.name, result.potential.suggestedPath))
        dao.upsertAnalytics(GrowthAnalyticsEntity(result.analytics.analyticsId, result.analytics.predictionAccuracy, result.analytics.growthScore, result.analytics.privacyProtected))
        dao.upsertRoadmap(FutureRoadmapEntity(result.roadmap.roadmapId, result.roadmap.longTermPlan, result.roadmap.skillRoadmap, result.roadmap.researchRoadmap, result.roadmap.careerStrategy))
        dao.upsertTrend(TrendAnalysisEntity(result.trendReport.reportId, result.trendReport.scientificTrends, result.trendReport.technologyChanges, result.trendReport.educationDemands))
        dao.upsertHistory(PredictionHistoryEntity(result.simulation.simulationId, result.simulation.currentPathOutcome, result.simulation.optimizedPathOutcome, result.simulation.futureSuccessProbability))
        dao.upsertOptimization(PredictiveOptimizationResultEntity(result.optimization.optimizationId, result.optimization.studyStrategy, result.optimization.practiceFrequency, result.optimization.resourceSelection, result.optimization.difficultyLevel))
    }
}

data class PredictiveDashboardState(
    val learningForecast: String = "",
    val futureSkills: List<String> = emptyList(),
    val potential: String = "",
    val careerRoadmap: List<String> = emptyList(),
    val growthRecommendations: List<String> = emptyList(),
    val confidenceScore: Int = 0,
    val growthScore: Int = 0,
)
