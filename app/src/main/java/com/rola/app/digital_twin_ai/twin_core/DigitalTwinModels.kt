package com.rola.app.digital_twin_ai.twin_core

enum class TwinDomain { Machine, HumanBody, Building, ScientificModel, IndustrialSystem, NaturalEnvironment }
enum class TwinStatus { Draft, Synced, Simulating, LearningReady, NeedsHumanApproval }

data class DigitalTwinRequest(
    val learnerId: String,
    val realWorldObject: String,
    val domain: TwinDomain,
    val sensorData: List<String>,
    val arScanSignals: List<String>,
    val learningGoal: String,
)

data class DigitalTwinProfile(val twinId: String, val objectName: String, val domain: TwinDomain, val status: TwinStatus, val safetyNotes: List<String>)
data class TwinModel(val modelId: String, val modelType: String, val components: List<String>, val interactiveFeatures: List<String>, val visualizationPlan: String)
data class RealWorldDataSync(val syncId: String, val sensorInputs: List<String>, val iotDevices: List<String>, val arScanUpdates: List<String>, val externalDataSources: List<String>, val privacyProtected: Boolean)
data class SimulationRun(val simulationId: String, val scenarios: List<String>, val predictions: List<String>, val visualizations: List<String>, val interactiveLearningTasks: List<String>)
data class TwinAnalysisReport(val reportId: String, val behaviorPatterns: List<String>, val performanceInsights: List<String>, val possibleOutcomes: List<String>, val confidenceScore: Int)
data class TwinLearningSession(val sessionId: String, val explanations: List<String>, val guidedExperiments: List<String>, val conceptDemonstrations: List<String>, val skillDevelopmentTasks: List<String>)
data class TwinPredictionRecord(val predictionId: String, val failurePredictions: List<String>, val futureBehavior: List<String>, val performanceChanges: List<String>, val experimentalOutcomes: List<String>)
data class TwinCollaborationSpace(val collaborationId: String, val students: List<String>, val teachers: List<String>, val researchers: List<String>, val aiAgents: List<String>, val sharedExperiments: List<String>)
data class DigitalTwinResult(
    val resultId: String,
    val twin: DigitalTwinProfile,
    val model: TwinModel,
    val sync: RealWorldDataSync,
    val simulation: SimulationRun,
    val analysis: TwinAnalysisReport,
    val learningSession: TwinLearningSession,
    val prediction: TwinPredictionRecord,
    val collaboration: TwinCollaborationSpace,
)
