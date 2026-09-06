package com.rola.app.digital_twin_ai.twin_core

import javax.inject.Inject

class DigitalTwinManager @Inject constructor() {
    fun createProfile(request: DigitalTwinRequest): DigitalTwinProfile =
        DigitalTwinProfile(
            twinId = "ai-digital-twin-${request.learnerId}",
            objectName = request.realWorldObject,
            domain = request.domain,
            status = TwinStatus.LearningReady,
            safetyNotes = listOf("protect real-world data", "run simulations inside safe boundaries", "require human approval for high-risk experiments"),
        )
}
