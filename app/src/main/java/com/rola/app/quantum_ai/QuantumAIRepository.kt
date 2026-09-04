package com.rola.app.quantum_ai

import com.rola.app.data.database.QuantumAIDao
import com.rola.app.data.database.entities.KnowledgeDiscoveryRecordEntity
import com.rola.app.data.database.entities.LearningOptimizationHistoryEntity
import com.rola.app.data.database.entities.OptimizationResultEntity
import com.rola.app.data.database.entities.QuantumAnalyticsEntity
import com.rola.app.data.database.entities.QuantumDecisionEntity
import com.rola.app.data.database.entities.QuantumModelEntity
import com.rola.app.data.database.entities.QuantumPredictionEntity
import com.rola.app.data.database.entities.QuantumProfileEntity
import com.rola.app.quantum_ai.intelligence.QuantumAIResult
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class QuantumAIRepository @Inject constructor(
    private val quantumAIDao: QuantumAIDao,
) {
    fun observeDashboard(learnerId: String, institutionId: String): Flow<QuantumAIDashboardState> =
        combine(
            quantumAIDao.observeLatestOptimization(learnerId),
            quantumAIDao.observeLatestPrediction(learnerId),
            quantumAIDao.observeRecentDecisions(learnerId),
            quantumAIDao.observeKnowledgeDiscovery(),
            quantumAIDao.observeLatestAnalytics(institutionId),
        ) { optimization, prediction, decisions, discoveries, analytics ->
            QuantumAIDashboardState(
                learningOptimizationScore = optimization?.learningOptimizationScore ?: 0,
                predictionAccuracyPercent = analytics?.predictionAccuracyPercent ?: 0,
                aiImprovementPercent = analytics?.aiImprovementPercent ?: 0,
                futurePerformancePercent = prediction?.futurePerformancePercent ?: 0,
                decisions = decisions.map { "${it.topic}: ${it.educationalAction}" },
                knowledgeDiscovery = discoveries.map { "${it.topic}: ${it.discoveredConcepts.joinToString()}" },
                intelligenceGrowth = analytics?.systemIntelligenceGrowth.orEmpty(),
            )
        }

    suspend fun saveResult(result: QuantumAIResult) {
        quantumAIDao.upsertProfile(
            QuantumProfileEntity(
                profileId = result.profile.profileId,
                learnerId = result.profile.learnerId,
                computeMode = result.profile.computeMode.name,
                optimizationReadinessPercent = result.profile.optimizationReadinessPercent,
                preferredExplanationStyle = result.profile.preferredExplanationStyle,
                activeGoals = result.profile.activeGoals,
                updatedAt = result.profile.updatedAt,
            ),
        )
        quantumAIDao.upsertModel(
            QuantumModelEntity(
                modelId = result.modelState.modelId,
                name = result.modelState.name,
                computeMode = result.modelState.computeMode.name,
                version = result.modelState.version,
                optimizationScope = result.modelState.optimizationScope,
            ),
        )
        quantumAIDao.upsertOptimization(
            OptimizationResultEntity(
                optimizationId = result.optimization.optimizationId,
                learnerId = result.optimization.learnerId,
                topic = result.optimization.topic,
                learningOptimizationScore = result.optimization.learningOptimizationScore,
                optimizedPath = result.optimization.optimizedPath,
                curriculumSequence = result.optimization.curriculumSequence,
                assessmentStrategy = result.optimization.assessmentStrategy,
                recommendationStrategy = result.optimization.recommendationStrategy,
                explanation = result.optimization.explanation,
            ),
        )
        quantumAIDao.upsertDecision(
            QuantumDecisionEntity(
                decisionId = result.decision.decisionId,
                learnerId = result.decision.learnerId,
                topic = result.decision.topic,
                decisionType = result.decision.decisionType.name,
                educationalAction = result.decision.educationalAction,
                confidencePercent = result.decision.confidencePercent,
                explanation = result.decision.explanation,
                humanControlRequired = result.decision.humanControlRequired,
            ),
        )
        quantumAIDao.upsertOptimizationHistory(
            LearningOptimizationHistoryEntity(
                historyId = "quantum-history-${UUID.randomUUID()}",
                learnerId = result.optimization.learnerId,
                topic = result.optimization.topic,
                summary = result.optimization.explanation,
                score = result.optimization.learningOptimizationScore,
                createdAt = System.currentTimeMillis(),
            ),
        )
        quantumAIDao.upsertPrediction(
            QuantumPredictionEntity(
                predictionId = result.prediction.predictionId,
                learnerId = result.prediction.learnerId,
                topic = result.prediction.topic,
                futurePerformancePercent = result.prediction.futurePerformancePercent,
                skillDevelopment = result.prediction.skillDevelopment,
                learningChallenges = result.prediction.learningChallenges,
                knowledgeRequirements = result.prediction.knowledgeRequirements,
                longTermRoadmap = result.prediction.longTermRoadmap,
            ),
        )
        quantumAIDao.upsertKnowledgeDiscovery(
            KnowledgeDiscoveryRecordEntity(
                discoveryId = result.knowledgeDiscovery.discoveryId,
                topic = result.knowledgeDiscovery.topic,
                hiddenRelationships = result.knowledgeDiscovery.hiddenRelationships,
                discoveredConcepts = result.knowledgeDiscovery.discoveredConcepts,
                scientificSignals = result.knowledgeDiscovery.scientificSignals,
                expansionRecommendation = result.knowledgeDiscovery.expansionRecommendation,
            ),
        )
        quantumAIDao.upsertAnalytics(
            QuantumAnalyticsEntity(
                reportId = result.analytics.reportId,
                institutionId = result.analytics.institutionId,
                learningOptimizationScore = result.analytics.learningOptimizationScore,
                aiImprovementPercent = result.analytics.aiImprovementPercent,
                predictionAccuracyPercent = result.analytics.predictionAccuracyPercent,
                systemIntelligenceGrowth = result.analytics.systemIntelligenceGrowth,
                auditNotes = result.analytics.auditNotes,
            ),
        )
    }
}

data class QuantumAIDashboardState(
    val learningOptimizationScore: Int = 0,
    val predictionAccuracyPercent: Int = 0,
    val aiImprovementPercent: Int = 0,
    val futurePerformancePercent: Int = 0,
    val decisions: List<String> = emptyList(),
    val knowledgeDiscovery: List<String> = emptyList(),
    val intelligenceGrowth: List<String> = emptyList(),
)
