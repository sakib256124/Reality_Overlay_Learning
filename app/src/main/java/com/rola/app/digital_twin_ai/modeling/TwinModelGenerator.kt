package com.rola.app.digital_twin_ai.modeling

import com.rola.app.digital_twin_ai.twin_core.DigitalTwinRequest
import com.rola.app.digital_twin_ai.twin_core.TwinDomain
import com.rola.app.digital_twin_ai.twin_core.TwinModel
import javax.inject.Inject

class TwinModelGenerator @Inject constructor() {
    fun generate(request: DigitalTwinRequest): TwinModel {
        val components = when (request.domain) {
            TwinDomain.Machine -> listOf("power source", "moving parts", "control system", "failure points")
            TwinDomain.HumanBody -> listOf("organs", "signals", "feedback loops", "health indicators")
            TwinDomain.Building -> listOf("structure", "energy flow", "safety systems", "occupancy zones")
            TwinDomain.ScientificModel -> listOf("variables", "forces", "constraints", "observations")
            TwinDomain.IndustrialSystem -> listOf("process line", "sensors", "quality checks", "maintenance states")
            TwinDomain.NaturalEnvironment -> listOf("climate factors", "ecosystem actors", "resource cycles", "risk signals")
        }
        return TwinModel(
            modelId = "twin-model-${request.learnerId}",
            modelType = request.domain.name,
            components = components,
            interactiveFeatures = listOf("inspect", "simulate", "change variables", "compare outcomes"),
            visualizationPlan = "Create an interactive learning twin for ${request.realWorldObject}.",
        )
    }
}
