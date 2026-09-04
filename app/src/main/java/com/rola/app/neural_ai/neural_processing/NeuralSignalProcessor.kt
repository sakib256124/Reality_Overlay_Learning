package com.rola.app.neural_ai.neural_processing

import com.rola.app.neural_ai.cognitive_signal.NeuralSignalSample
import com.rola.app.neural_ai.cognitive_signal.ProcessedNeuralSignal
import com.rola.app.neural_ai.cognitive_signal.SignalQuality
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NeuralSignalProcessor @Inject constructor() {
    fun process(sample: NeuralSignalSample): ProcessedNeuralSignal {
        val sanitized = sample.sanitized()
        val qualityConfidence = when (sanitized.signalQuality) {
            SignalQuality.Poor -> 0.35f
            SignalQuality.Fair -> 0.6f
            SignalQuality.Good -> 0.82f
            SignalQuality.Excellent -> 0.95f
        }

        // Cognitive load blends mental workload and fatigue so lesson adaptation reacts before overload.
        val load = ((sanitized.mentalWorkloadScore * 0.7f) + (sanitized.fatigueScore * 0.3f)).coerceIn(0f, 1f)
        return ProcessedNeuralSignal(
            signalId = sanitized.signalId.ifBlank { "processed-${UUID.randomUUID()}" },
            userId = sanitized.userId,
            sessionId = sanitized.sessionId,
            attentionScore = sanitized.attentionScore,
            engagementScore = sanitized.engagementScore,
            cognitiveLoadScore = load,
            fatigueScore = sanitized.fatigueScore,
            confidence = qualityConfidence,
            timestamp = sanitized.timestamp,
        )
    }
}
