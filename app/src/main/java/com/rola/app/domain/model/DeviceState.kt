package com.rola.app.domain.model

enum class DeviceState {
    Connected,
    Disconnected,
    Connecting,
    Error,
}

enum class DeviceConnectionType {
    Bluetooth,
    WiFi,
    UsbC,
    CloudRelay,
}

enum class DisplayCapability {
    None,
    Notifications,
    HeadsUpText,
    ArOverlay,
    SpatialAnchoredPanel,
}

enum class AudioCapability {
    None,
    DeviceSpeaker,
    BluetoothAudio,
    SpatialAudio,
}

enum class WearableDisplayMode {
    MobileFloatingCard,
    CompactHeadsUp,
    SpatialAnchoredPanel,
}

enum class WearableAudioOutput {
    PhoneSpeaker,
    WearableSpeaker,
    BluetoothHeadset,
}

enum class WearableInteractionMode {
    VoiceFirst,
    TouchGesture,
    HandTracking,
    Controller,
}

enum class WearableCommand {
    ScanObject,
    ExplainThis,
    StartQuiz,
    Translate,
    StopAudio,
}
