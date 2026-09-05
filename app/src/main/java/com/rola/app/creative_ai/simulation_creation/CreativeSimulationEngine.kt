package com.rola.app.creative_ai.simulation_creation

import com.rola.app.creative_ai.creativity_engine.CreativeAIRequest
import com.rola.app.creative_ai.creativity_engine.CreativeSimulation
import javax.inject.Inject

class CreativeSimulationEngine @Inject constructor() {
    fun create(request: CreativeAIRequest): CreativeSimulation =
        CreativeSimulation(
            simulationId = "creative-simulation-${request.topic.lowercase().replace(" ", "-")}",
            virtualExperiments = listOf("virtual lab for ${request.topic}", "cause-effect sandbox"),
            arActivities = listOf("AR overlay challenge", "spatial concept walk-through"),
            digitalTwinScenarios = listOf("digital twin what-if scenario", "metaverse classroom prototype"),
        )
}
