package com.rola.app.spatial_ai.digital_twin

import com.rola.app.domain.model.DigitalTwin
import com.rola.app.domain.model.DigitalTwinType
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DigitalTwinManager @Inject constructor() {
    fun createTwin(
        sourceObjectId: String,
        name: String,
        type: DigitalTwinType,
    ): DigitalTwin = DigitalTwin(
        twinId = "digital-twin-${UUID.randomUUID()}",
        sourceObjectId = sourceObjectId,
        name = name,
        twinType = type,
        modelUri = "models/digital_twins/${name.slug()}.glb",
        behaviorModel = behaviorFor(type, name),
        explanation = "$name is represented as an interactive digital twin for inspection, manipulation, and guided learning.",
        manipulableProperties = when (type) {
            DigitalTwinType.Machine -> listOf("speed", "load", "friction")
            DigitalTwinType.HumanAnatomy -> listOf("layer", "bloodFlow", "organSystem")
            DigitalTwinType.ScientificModel -> listOf("scale", "variable", "timeStep")
            DigitalTwinType.IndustrialSystem -> listOf("pressure", "temperature", "throughput")
            DigitalTwinType.EngineeringStructure -> listOf("load", "material", "stress")
            DigitalTwinType.RealObject -> listOf("scale", "labels", "crossSection")
        },
    )

    fun analyzeTwin(twin: DigitalTwin): List<String> = listOf(
        "Model: ${twin.modelUri}",
        "Behavior: ${twin.behaviorModel}",
        "Manipulations: ${twin.manipulableProperties.joinToString()}",
    )

    private fun behaviorFor(type: DigitalTwinType, name: String): String = when (type) {
        DigitalTwinType.Machine -> "$name responds to force, motion, load, and efficiency changes."
        DigitalTwinType.HumanAnatomy -> "$name shows anatomy layers, system relationships, and functional flow."
        DigitalTwinType.ScientificModel -> "$name simulates variables, evidence, and model limitations."
        DigitalTwinType.IndustrialSystem -> "$name models flow, constraints, risk, and process behavior."
        DigitalTwinType.EngineeringStructure -> "$name demonstrates stress, load paths, and material tradeoffs."
        DigitalTwinType.RealObject -> "$name supports labels, measurements, comparison, and explanation."
    }

    private fun String.slug(): String = lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-')
}
