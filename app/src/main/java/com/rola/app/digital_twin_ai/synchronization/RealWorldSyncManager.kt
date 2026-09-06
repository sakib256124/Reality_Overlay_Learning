package com.rola.app.digital_twin_ai.synchronization

import com.rola.app.digital_twin_ai.twin_core.DigitalTwinRequest
import com.rola.app.digital_twin_ai.twin_core.RealWorldDataSync
import javax.inject.Inject

class RealWorldSyncManager @Inject constructor() {
    fun sync(request: DigitalTwinRequest): RealWorldDataSync =
        RealWorldDataSync(
            syncId = "sync-${request.learnerId}",
            sensorInputs = request.sensorData,
            iotDevices = listOf("classroom sensor gateway", "lab IoT bridge"),
            arScanUpdates = request.arScanSignals,
            externalDataSources = listOf("teacher-approved dataset", "simulation reference model"),
            privacyProtected = true,
        )
}
