package com.rola.app.digital_twin_ai.twin_core

import com.rola.app.digital_twin_ai.education.TwinCollaborationManager
import com.rola.app.digital_twin_ai.education.TwinLearningManager
import com.rola.app.digital_twin_ai.intelligence.TwinIntelligenceAnalyzer
import com.rola.app.digital_twin_ai.intelligence.TwinPredictionEngine
import com.rola.app.digital_twin_ai.modeling.TwinModelGenerator
import com.rola.app.digital_twin_ai.simulation.SimulationEngine
import com.rola.app.digital_twin_ai.synchronization.RealWorldSyncManager
import javax.inject.Inject

class AIDigitalTwinEngine @Inject constructor(
    private val digitalTwinManager: DigitalTwinManager,
    private val modelGenerator: TwinModelGenerator,
    private val syncManager: RealWorldSyncManager,
    private val simulationEngine: SimulationEngine,
    private val analyzer: TwinIntelligenceAnalyzer,
    private val learningManager: TwinLearningManager,
    private val predictionEngine: TwinPredictionEngine,
    private val collaborationManager: TwinCollaborationManager,
) {
    fun createLearningTwin(request: DigitalTwinRequest): DigitalTwinResult {
        val model = modelGenerator.generate(request)
        val simulation = simulationEngine.simulate(request, model)
        val analysis = analyzer.analyze(simulation)
        return DigitalTwinResult(
            resultId = "digital-twin-result-${request.learnerId}",
            twin = digitalTwinManager.createProfile(request),
            model = model,
            sync = syncManager.sync(request),
            simulation = simulation,
            analysis = analysis,
            learningSession = learningManager.teach(request, analysis),
            prediction = predictionEngine.predict(simulation),
            collaboration = collaborationManager.collaborate(request),
        )
    }
}
