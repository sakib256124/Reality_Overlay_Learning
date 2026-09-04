package com.rola.app.data.wearable

import com.rola.app.domain.model.DeviceConnectionType
import com.rola.app.domain.model.DeviceState
import com.rola.app.domain.model.WearableCommand
import com.rola.app.domain.model.WearableDevice
import kotlinx.coroutines.flow.StateFlow

interface DeviceConnector {
    val discoveredDevices: StateFlow<List<WearableDevice>>
    val connectionState: StateFlow<DeviceState>
    val connectedDevice: StateFlow<WearableDevice?>

    suspend fun discoverDevices(connectionTypes: Set<DeviceConnectionType>): List<WearableDevice>

    suspend fun connect(device: WearableDevice): WearableConnectionResult

    suspend fun disconnect(): WearableConnectionResult

    suspend fun sendCommand(command: WearableCommand): WearableConnectionResult

    suspend fun sendLearningFrame(frame: WearableLearningFrame): WearableConnectionResult
}

data class WearableLearningFrame(
    val objectName: String,
    val summary: String,
    val languageCode: String,
    val displayMode: String,
    val shouldSpeak: Boolean,
    val timestamp: Long = System.currentTimeMillis(),
)

sealed class WearableConnectionResult {
    data object Success : WearableConnectionResult()
    data class Error(val message: String) : WearableConnectionResult()
}

interface EyeTrackingProvider {
    val isAvailable: Boolean
    fun startSession(): WearableConnectionResult = WearableConnectionResult.Error("Eye tracking is not available on this device.")
}

interface HandTrackingProvider {
    val isAvailable: Boolean
    fun startSession(): WearableConnectionResult = WearableConnectionResult.Error("Hand tracking is not available on this device.")
}

interface SpatialMappingProvider {
    val isAvailable: Boolean
    fun startSession(): WearableConnectionResult = WearableConnectionResult.Error("Spatial mapping is not available on this device.")
}

interface DepthSensorProvider {
    val isAvailable: Boolean
    fun startSession(): WearableConnectionResult = WearableConnectionResult.Error("Depth sensing is not available on this device.")
}
