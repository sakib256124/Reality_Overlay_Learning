package com.rola.app.core.agi

import com.rola.app.domain.model.SimulationLearningScenario
import com.rola.app.domain.model.SimulationScenarioType
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SimulationLearningEnvironment @Inject constructor() {
    fun createScenario(
        topic: String,
        type: SimulationScenarioType = SimulationScenarioType.ARLaboratory,
    ): SimulationLearningScenario = SimulationLearningScenario(
        scenarioId = "simulation-${UUID.randomUUID()}",
        title = "$topic ${type.name}",
        scenarioType = type,
        topic = topic,
        instructions = when (type) {
            SimulationScenarioType.VirtualExperiment -> listOf("Set a variable.", "Predict the outcome.", "Run the virtual experiment.", "Explain the result.")
            SimulationScenarioType.ARLaboratory -> listOf("Scan an object.", "Place labels.", "Adjust one overlay variable.", "Record evidence.")
            SimulationScenarioType.ScientificSimulation -> listOf("Inspect the model.", "Change conditions.", "Compare states.", "Write a claim.")
            SimulationScenarioType.InteractiveScenario -> listOf("Choose a role.", "Make a decision.", "Observe feedback.", "Reflect on tradeoffs.")
        },
        expectedLearningSignals = listOf(
            "Learner identifies evidence for $topic.",
            "Learner explains cause and effect.",
            "Learner transfers the concept to a new example.",
        ),
    )
}
