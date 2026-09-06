package com.rola.app.unit

import com.rola.app.digital_twin_ai.education.TwinCollaborationManager
import com.rola.app.digital_twin_ai.education.TwinLearningManager
import com.rola.app.digital_twin_ai.intelligence.TwinIntelligenceAnalyzer
import com.rola.app.digital_twin_ai.intelligence.TwinPredictionEngine
import com.rola.app.digital_twin_ai.modeling.TwinModelGenerator
import com.rola.app.digital_twin_ai.simulation.SimulationEngine
import com.rola.app.digital_twin_ai.synchronization.RealWorldSyncManager
import com.rola.app.digital_twin_ai.twin_core.AIDigitalTwinEngine
import com.rola.app.digital_twin_ai.twin_core.DigitalTwinManager
import com.rola.app.digital_twin_ai.twin_core.DigitalTwinRequest
import com.rola.app.digital_twin_ai.twin_core.TwinDomain
import com.rola.app.digital_twin_ai.twin_core.TwinStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DigitalTwinAIPlatformTest {
    private val engine = AIDigitalTwinEngine(
        DigitalTwinManager(),
        TwinModelGenerator(),
        RealWorldSyncManager(),
        SimulationEngine(),
        TwinIntelligenceAnalyzer(),
        TwinLearningManager(),
        TwinPredictionEngine(),
        TwinCollaborationManager(),
    )

    @Test
    fun digitalTwin_createsModelSyncSimulationPredictionAndLearningSession() {
        val result = engine.createLearningTwin(
            DigitalTwinRequest(
                learnerId = "twin-learner",
                realWorldObject = "electric motor",
                domain = TwinDomain.Machine,
                sensorData = listOf("temperature: normal", "vibration: mild", "speed: stable"),
                arScanSignals = listOf("camera scan: motor body", "object recognition: rotating shaft"),
                learningGoal = "understand electromechanical systems",
            ),
        )

        assertEquals(TwinStatus.LearningReady, result.twin.status)
        assertTrue(result.model.components.contains("control system"))
        assertTrue(result.sync.privacyProtected)
        assertTrue(result.simulation.scenarios.contains("stress condition"))
        assertTrue(result.analysis.confidenceScore >= 80)
        assertTrue(result.learningSession.guidedExperiments.contains("change one variable"))
        assertTrue(result.prediction.failurePredictions.isNotEmpty())
        assertTrue(result.collaboration.aiAgents.contains("Research AI"))
        assertTrue(result.twin.safetyNotes.any { it.contains("human approval") })
    }
}
