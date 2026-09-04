package com.rola.app.spatial_ai.simulation

import com.rola.app.domain.model.SpatialSimulation
import com.rola.app.domain.model.SpatialSimulationType
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.sqrt

@Singleton
class SimulationEngine @Inject constructor() {
    fun updateSimulation(
        simulation: SpatialSimulation,
        changedParameters: Map<String, Float>,
    ): SpatialSimulation {
        val parameters = simulation.parameters + changedParameters
        return simulation.copy(
            parameters = parameters,
            explanation = explanationFor(simulation.simulationType, simulation.title, parameters),
        )
    }

    fun gravityExperiment(heightMeters: Float, gravity: Float): Float =
        sqrt((2f * heightMeters.coerceAtLeast(0f)) / gravity.coerceAtLeast(0.1f))

    fun explainReaction(substances: List<String>): String =
        "Mixing ${substances.joinToString()} creates a virtual reaction model; students compare evidence, energy change, and safety constraints."

    private fun explanationFor(
        type: SpatialSimulationType,
        title: String,
        parameters: Map<String, Float>,
    ): String = when (type) {
        SpatialSimulationType.Physics -> "$title updates motion, force, energy, or gravity using ${parameters.keys.joinToString()}."
        SpatialSimulationType.Chemistry -> "$title explains reaction change, substance interaction, and safety using ${parameters.keys.joinToString()}."
        SpatialSimulationType.Biology -> "$title shows structure, function, and system behavior using ${parameters.keys.joinToString()}."
        SpatialSimulationType.Engineering -> "$title demonstrates load, stress, design tradeoffs, and system performance."
    }
}
