package com.rola.app.digital_twin_ai

import com.rola.app.data.database.DigitalTwinAIDao
import com.rola.app.data.database.entities.AIDigitalTwinEntity
import com.rola.app.data.database.entities.TwinAnalyticsEntity
import com.rola.app.data.database.entities.TwinExperimentResultEntity
import com.rola.app.data.database.entities.TwinLearningSessionEntity
import com.rola.app.data.database.entities.TwinModelEntity
import com.rola.app.data.database.entities.TwinPredictionRecordEntity
import com.rola.app.data.database.entities.TwinRealWorldDataEntity
import com.rola.app.data.database.entities.TwinSimulationHistoryEntity
import com.rola.app.digital_twin_ai.twin_core.DigitalTwinResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class DigitalTwinAIRepository @Inject constructor(private val dao: DigitalTwinAIDao) {
    fun observeDashboard(): Flow<DigitalTwinDashboardState> =
        combine(
            dao.observeTwin(),
            dao.observeModel(),
            dao.observeSimulation(),
            dao.observeSync(),
            dao.observeAnalytics(),
            dao.observeExperiment(),
            dao.observeLearningSession(),
            dao.observePrediction(),
        ) { values ->
            val twin = values[0] as AIDigitalTwinEntity?
            val model = values[1] as TwinModelEntity?
            val simulation = values[2] as TwinSimulationHistoryEntity?
            val sync = values[3] as TwinRealWorldDataEntity?
            val analytics = values[4] as TwinAnalyticsEntity?
            val experiment = values[5] as TwinExperimentResultEntity?
            val session = values[6] as TwinLearningSessionEntity?
            val prediction = values[7] as TwinPredictionRecordEntity?
            DigitalTwinDashboardState(
                activeTwins = listOfNotNull(twin?.objectName, model?.modelType),
                simulationStatus = simulation?.scenarios.orEmpty(),
                learningProgress = analytics?.confidenceScore ?: 0,
                analysisReports = analytics?.behaviorPatterns.orEmpty() + analytics?.possibleOutcomes.orEmpty(),
                experimentHistory = experiment?.guidedExperiments.orEmpty() + experiment?.sharedExperiments.orEmpty(),
                syncSummary = sync?.arScanUpdates.orEmpty() + sync?.sensorInputs.orEmpty(),
                predictions = prediction?.failurePredictions.orEmpty() + prediction?.futureBehavior.orEmpty(),
                learningSessions = session?.skillDevelopmentTasks.orEmpty(),
                safetyStatus = twin?.safetyNotes.orEmpty().joinToString(),
            )
        }

    suspend fun save(result: DigitalTwinResult) {
        dao.upsertTwin(AIDigitalTwinEntity(result.twin.twinId, result.twin.objectName, result.twin.domain.name, result.twin.status.name, result.twin.safetyNotes))
        dao.upsertModel(TwinModelEntity(result.model.modelId, result.model.modelType, result.model.components, result.model.interactiveFeatures, result.model.visualizationPlan))
        dao.upsertSimulation(TwinSimulationHistoryEntity(result.simulation.simulationId, result.simulation.scenarios, result.simulation.predictions, result.simulation.visualizations, result.simulation.interactiveLearningTasks))
        dao.upsertSync(TwinRealWorldDataEntity(result.sync.syncId, result.sync.sensorInputs, result.sync.iotDevices, result.sync.arScanUpdates, result.sync.externalDataSources, result.sync.privacyProtected))
        dao.upsertAnalytics(TwinAnalyticsEntity(result.analysis.reportId, result.analysis.behaviorPatterns, result.analysis.performanceInsights, result.analysis.possibleOutcomes, result.analysis.confidenceScore))
        dao.upsertExperiment(TwinExperimentResultEntity(result.collaboration.collaborationId, result.learningSession.sessionId, result.learningSession.guidedExperiments, result.collaboration.sharedExperiments, safetyApproved = true))
        dao.upsertLearningSession(TwinLearningSessionEntity(result.learningSession.sessionId, result.learningSession.explanations, result.learningSession.guidedExperiments, result.learningSession.conceptDemonstrations, result.learningSession.skillDevelopmentTasks))
        dao.upsertPrediction(TwinPredictionRecordEntity(result.prediction.predictionId, result.prediction.failurePredictions, result.prediction.futureBehavior, result.prediction.performanceChanges, result.prediction.experimentalOutcomes))
    }
}

data class DigitalTwinDashboardState(
    val activeTwins: List<String> = emptyList(),
    val simulationStatus: List<String> = emptyList(),
    val learningProgress: Int = 0,
    val analysisReports: List<String> = emptyList(),
    val experimentHistory: List<String> = emptyList(),
    val syncSummary: List<String> = emptyList(),
    val predictions: List<String> = emptyList(),
    val learningSessions: List<String> = emptyList(),
    val safetyStatus: String = "",
)
