package com.rola.app.data.wearable

import com.rola.app.domain.model.AudioCapability
import com.rola.app.domain.model.DeviceConnectionType
import com.rola.app.domain.model.DeviceState
import com.rola.app.domain.model.DisplayCapability
import com.rola.app.domain.model.WearableCommand
import com.rola.app.domain.model.WearableDevice
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

@Singleton
class WearableManager @Inject constructor() : DeviceConnector {
    private val _discoveredDevices = MutableStateFlow<List<WearableDevice>>(emptyList())
    override val discoveredDevices: StateFlow<List<WearableDevice>> = _discoveredDevices.asStateFlow()

    private val _connectionState = MutableStateFlow(DeviceState.Disconnected)
    override val connectionState: StateFlow<DeviceState> = _connectionState.asStateFlow()

    private val _connectedDevice = MutableStateFlow<WearableDevice?>(null)
    override val connectedDevice: StateFlow<WearableDevice?> = _connectedDevice.asStateFlow()

    private var securePairingToken: String? = null

    override suspend fun discoverDevices(connectionTypes: Set<DeviceConnectionType>): List<WearableDevice> {
        delay(DISCOVERY_DELAY_MILLIS)
        val devices = simulatedDevices.filter { it.connectionType in connectionTypes }
        _discoveredDevices.value = devices
        return devices
    }

    override suspend fun connect(device: WearableDevice): WearableConnectionResult {
        _connectionState.value = DeviceState.Connecting
        _discoveredDevices.update { devices ->
            devices.map {
                if (it.deviceId == device.deviceId) it.copy(state = DeviceState.Connecting) else it
            }
        }
        delay(CONNECTION_DELAY_MILLIS)

        val trustedDevice = device.copy(
            state = DeviceState.Connected,
            isTrusted = true,
            batteryLevel = device.normalizedBatteryLevel,
        )
        securePairingToken = buildSecurePairingToken(trustedDevice)
        _connectedDevice.value = trustedDevice
        _connectionState.value = DeviceState.Connected
        _discoveredDevices.update { devices ->
            devices.map {
                when (it.deviceId) {
                    trustedDevice.deviceId -> trustedDevice
                    else -> it.copy(state = DeviceState.Disconnected)
                }
            }
        }
        return WearableConnectionResult.Success
    }

    override suspend fun disconnect(): WearableConnectionResult {
        securePairingToken = null
        _connectedDevice.value = null
        _connectionState.value = DeviceState.Disconnected
        _discoveredDevices.update { devices ->
            devices.map { it.copy(state = DeviceState.Disconnected, isTrusted = false) }
        }
        return WearableConnectionResult.Success
    }

    override suspend fun sendCommand(command: WearableCommand): WearableConnectionResult {
        val device = _connectedDevice.value
            ?: return WearableConnectionResult.Error("Connect a wearable before sending commands.")
        if (!device.isTrusted || securePairingToken == null) {
            _connectionState.value = DeviceState.Error
            return WearableConnectionResult.Error("Secure wearable pairing is not established.")
        }
        delay(COMMAND_DELAY_MILLIS)
        return WearableConnectionResult.Success
    }

    override suspend fun sendLearningFrame(frame: WearableLearningFrame): WearableConnectionResult {
        val device = _connectedDevice.value
            ?: return WearableConnectionResult.Error("Connect a wearable before sending learning content.")
        if (!device.supportsSpatialDisplay && frame.displayMode.contains("Spatial")) {
            return WearableConnectionResult.Error("${device.deviceName} does not support spatial panels.")
        }
        return sendCommand(WearableCommand.ExplainThis)
    }

    private fun buildSecurePairingToken(device: WearableDevice): String =
        "${device.deviceId}:${device.connectionType}:${System.currentTimeMillis()}".hashCode().toString()

    private companion object {
        const val DISCOVERY_DELAY_MILLIS = 650L
        const val CONNECTION_DELAY_MILLIS = 500L
        const val COMMAND_DELAY_MILLIS = 120L

        val simulatedDevices = listOf(
            WearableDevice(
                deviceId = "xr-glasses-reference-01",
                deviceName = "Smart AR Glasses",
                manufacturer = "Reference XR",
                connectionType = DeviceConnectionType.Bluetooth,
                batteryLevel = 84,
                displayCapability = DisplayCapability.ArOverlay,
                audioCapability = AudioCapability.BluetoothAudio,
            ),
            WearableDevice(
                deviceId = "heads-up-viewer-02",
                deviceName = "Heads-Up Learning Viewer",
                manufacturer = "Open Wearable",
                connectionType = DeviceConnectionType.WiFi,
                batteryLevel = 67,
                displayCapability = DisplayCapability.SpatialAnchoredPanel,
                audioCapability = AudioCapability.SpatialAudio,
            ),
            WearableDevice(
                deviceId = "smart-audio-frame-03",
                deviceName = "Smart Audio Frames",
                manufacturer = "Reference Audio",
                connectionType = DeviceConnectionType.Bluetooth,
                batteryLevel = 58,
                displayCapability = DisplayCapability.HeadsUpText,
                audioCapability = AudioCapability.DeviceSpeaker,
            ),
        )
    }
}
