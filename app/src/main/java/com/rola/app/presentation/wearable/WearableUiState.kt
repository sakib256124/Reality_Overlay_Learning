package com.rola.app.presentation.wearable

import com.rola.app.domain.model.DeviceState
import com.rola.app.domain.model.WearableAudioOutput
import com.rola.app.domain.model.WearableDevice
import com.rola.app.domain.model.WearableDisplayMode
import com.rola.app.domain.model.WearableInteractionMode

enum class WearableStatus {
    Idle,
    Discovering,
    Connecting,
    Connected,
    Sending,
    Error,
}

data class WearableUiState(
    val status: WearableStatus = WearableStatus.Idle,
    val discoveredDevices: List<WearableDevice> = emptyList(),
    val connectedDevice: WearableDevice? = null,
    val connectionState: DeviceState = DeviceState.Disconnected,
    val displayMode: WearableDisplayMode = WearableDisplayMode.CompactHeadsUp,
    val audioOutput: WearableAudioOutput = WearableAudioOutput.WearableSpeaker,
    val voiceSensitivity: Float = 0.68f,
    val interactionMode: WearableInteractionMode = WearableInteractionMode.VoiceFirst,
    val lowPowerMode: Boolean = false,
    val adaptiveFrameIntervalMillis: Long = 500L,
    val usesCompactOverlay: Boolean = true,
    val lastCommand: String? = null,
    val lastTransferSummary: String? = null,
    val errorMessage: String? = null,
) {
    val isConnected: Boolean
        get() = connectedDevice != null && connectionState == DeviceState.Connected

    val connectionLabel: String
        get() = connectedDevice?.let { "${it.deviceName} (${it.connectionType.name})" }
            ?: "No wearable connected"
}
