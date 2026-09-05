package com.rola.app.self_evolving_ai

import com.rola.app.data.database.SelfEvolvingAIDao
import com.rola.app.data.database.entities.EvolutionExperimentEntity
import com.rola.app.data.database.entities.FeedbackRecordEntity
import com.rola.app.data.database.entities.ImprovementActionEntity
import com.rola.app.data.database.entities.ModelVersionEntity
import com.rola.app.data.database.entities.PerformanceMetricEntity
import com.rola.app.data.database.entities.SelfEvolutionHistoryEntity
import com.rola.app.data.database.entities.SelfOptimizationResultEntity
import com.rola.app.data.database.entities.SystemGrowthAnalyticsEntity
import com.rola.app.self_evolving_ai.evolution_core.SelfEvolutionResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class SelfEvolvingAIRepository @Inject constructor(private val dao: SelfEvolvingAIDao) {
    fun observeDashboard(): Flow<EvolutionDashboardState> =
        combine(
            dao.observeHistory(),
            dao.observePerformance(),
            dao.observeImprovement(),
            dao.observeModel(),
            dao.observeFeedback(),
            dao.observeOptimization(),
            dao.observeExperiment(),
            dao.observeGrowth(),
        ) { values ->
            val history = values[0] as SelfEvolutionHistoryEntity?
            val performance = values[1] as PerformanceMetricEntity?
            val improvement = values[2] as ImprovementActionEntity?
            val model = values[3] as ModelVersionEntity?
            val feedback = values[4] as FeedbackRecordEntity?
            val optimization = values[5] as SelfOptimizationResultEntity?
            val experiment = values[6] as EvolutionExperimentEntity?
            val growth = values[7] as SystemGrowthAnalyticsEntity?
            EvolutionDashboardState(
                improvementScore = experiment?.improvementScore ?: optimization?.qualityScore ?: 0,
                evolutionHistory = growth?.evolutionHistory.orEmpty() + history?.status.orEmpty(),
                successfulOptimizations = growth?.successfulStrategies.orEmpty() + optimization?.learningPathOptimization.orEmpty(),
                modelPerformance = listOfNotNull(model?.performanceComparison, model?.deploymentStage),
                futureImprovementPlans = listOfNotNull(improvement?.workflowImprovement, improvement?.recommendationImprovement, feedback?.behaviorImprovement),
                safetyStatus = if (history?.humanApprovalRequired == true) "Human approval required with rollback support." else "",
                weaknesses = performance?.weaknesses.orEmpty(),
            )
        }

    suspend fun save(result: SelfEvolutionResult) {
        dao.upsertHistory(SelfEvolutionHistoryEntity(result.resultId, result.performanceReport.reportId, result.improvementAction.actionId, result.modelEvolution.modelVersionId, result.status.name, result.governance.humanApprovalRequired, result.governance.rollbackCapability))
        dao.upsertPerformance(PerformanceMetricEntity(result.performanceReport.reportId, result.performanceReport.responseQuality, result.performanceReport.teachingEffectiveness, result.performanceReport.recommendationAccuracy, result.performanceReport.learningOutcomes, result.performanceReport.userSatisfaction, result.performanceReport.systemPerformance, result.performanceReport.weaknesses))
        dao.upsertImprovement(ImprovementActionEntity(result.improvementAction.actionId, result.improvementAction.teachingStrategyImprovement, result.improvementAction.recommendationImprovement, result.improvementAction.workflowImprovement, result.improvementAction.knowledgeDeliveryImprovement, result.improvementAction.personalizationAccuracy, result.improvementAction.validationRequired))
        dao.upsertModel(ModelVersionEntity(result.modelEvolution.modelVersionId, result.modelEvolution.previousVersion, result.modelEvolution.newVersion, result.modelEvolution.performanceComparison, result.modelEvolution.deploymentStage.name, result.modelEvolution.rollbackSupported))
        dao.upsertFeedback(FeedbackRecordEntity(result.feedbackLearning.feedbackId, result.feedbackLearning.studentFeedback, result.feedbackLearning.teacherFeedback, result.feedbackLearning.aiPerformanceFeedback, result.feedbackLearning.learningResults, result.feedbackLearning.behaviorImprovement))
        dao.upsertOptimization(SelfOptimizationResultEntity(result.learningOptimization.optimizationId, result.learningOptimization.learningPathOptimization, result.learningOptimization.contentDeliveryOptimization, result.learningOptimization.assessmentOptimization, result.learningOptimization.difficultyAdjustment, result.learningOptimization.engagementStrategy, result.learningOptimization.qualityScore))
        dao.upsertExperiment(EvolutionExperimentEntity(result.experiment.experimentId, result.experiment.strategyA, result.experiment.strategyB, result.experiment.winningStrategy, result.experiment.improvementScore))
        dao.upsertGrowth(SystemGrowthAnalyticsEntity(result.memory.memoryId, result.memory.previousImprovements, result.memory.successfulStrategies, result.memory.failedExperiments, result.memory.evolutionHistory, result.adaptiveEducation.workflowUpdate, result.experiment.improvementScore))
    }
}

data class EvolutionDashboardState(
    val improvementScore: Int = 0,
    val evolutionHistory: List<String> = emptyList(),
    val successfulOptimizations: List<String> = emptyList(),
    val modelPerformance: List<String> = emptyList(),
    val futureImprovementPlans: List<String> = emptyList(),
    val safetyStatus: String = "",
    val weaknesses: List<String> = emptyList(),
)
