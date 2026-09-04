package com.rola.app.quantum_ai.simulation

import com.rola.app.quantum_ai.intelligence.QuantumSimulationPlan
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuantumSimulationManager @Inject constructor(
    private val simulationEngine: QuantumSimulationEngine,
) {
    fun prepareEducationalSimulation(topic: String): QuantumSimulationPlan =
        simulationEngine.createSimulation(topic)
}
