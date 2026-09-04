package com.rola.app.domain.model

data class WearableDevice(
    val deviceId: String,
    val deviceName: String,
    val manufacturer: String,
    val connectionType: DeviceConnectionType,
    val batteryLevel: Int,
    val displayCapability: DisplayCapability,
    val audioCapability: AudioCapability,
    val state: DeviceState = DeviceState.Disconnected,
    val isTrusted: Boolean = false,
) {
    val supportsSpatialDisplay: Boolean
        get() = displayCapability == DisplayCapability.ArOverlay ||
            displayCapability == DisplayCapability.SpatialAnchoredPanel

    val supportsVoiceOutput: Boolean
        get() = audioCapability != AudioCapability.None

    val normalizedBatteryLevel: Int
        get() = batteryLevel.coerceIn(0, 100)
}
