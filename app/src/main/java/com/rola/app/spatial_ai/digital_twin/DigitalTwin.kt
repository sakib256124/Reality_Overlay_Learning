package com.rola.app.spatial_ai.digital_twin

import com.rola.app.domain.model.DigitalTwin

data class DigitalTwinLearningState(
    val twin: DigitalTwin,
    val activeProperty: String,
    val learnerManipulation: String,
    val aiExplanation: String,
) {
    val readyForInteraction: Boolean
        get() = activeProperty in twin.manipulableProperties && learnerManipulation.isNotBlank()
}
