package com.rola.app.neural_ai.cognitive_signal

enum class NeuralDeviceType {
    EEGHeadset,
    NeuralSensor,
    BrainActivityMonitor,
    WearableNeuralSystem,
    Simulated,
}

enum class NeuralConnectionStatus {
    Disconnected,
    Scanning,
    Connecting,
    Connected,
    Error,
}

enum class SignalQuality {
    Poor,
    Fair,
    Good,
    Excellent,
}

data class NeuralDevice(
    val deviceId: String,
    val name: String,
    val type: NeuralDeviceType,
    val supportsRealtimeSignals: Boolean,
    val firmwareVersion: String? = null,
)

data class NeuralDeviceSession(
    val sessionId: String,
    val device: NeuralDevice,
    val connectedAt: Long,
)

data class NeuralSignalSample(
    val signalId: String,
    val userId: String,
    val sessionId: String,
    val attentionScore: Float,
    val engagementScore: Float,
    val mentalWorkloadScore: Float,
    val fatigueScore: Float,
    val signalQuality: SignalQuality,
    val timestamp: Long = System.currentTimeMillis(),
) {
    fun sanitized(): NeuralSignalSample = copy(
        attentionScore = attentionScore.coerceIn(0f, 1f),
        engagementScore = engagementScore.coerceIn(0f, 1f),
        mentalWorkloadScore = mentalWorkloadScore.coerceIn(0f, 1f),
        fatigueScore = fatigueScore.coerceIn(0f, 1f),
    )
}

data class ProcessedNeuralSignal(
    val signalId: String,
    val userId: String,
    val sessionId: String,
    val attentionScore: Float,
    val engagementScore: Float,
    val cognitiveLoadScore: Float,
    val fatigueScore: Float,
    val confidence: Float,
    val timestamp: Long,
)
