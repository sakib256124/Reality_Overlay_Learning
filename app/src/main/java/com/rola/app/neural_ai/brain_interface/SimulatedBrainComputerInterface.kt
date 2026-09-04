package com.rola.app.neural_ai.brain_interface

import com.rola.app.neural_ai.cognitive_signal.NeuralConnectionStatus
import com.rola.app.neural_ai.cognitive_signal.NeuralDevice
import com.rola.app.neural_ai.cognitive_signal.NeuralDeviceSession
import com.rola.app.neural_ai.cognitive_signal.NeuralDeviceType
import com.rola.app.neural_ai.cognitive_signal.NeuralSignalSample
import com.rola.app.neural_ai.cognitive_signal.SignalQuality
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

@Singleton
class SimulatedBrainComputerInterface @Inject constructor() : BrainComputerInterface {
    override val connectionStatus = MutableStateFlow(NeuralConnectionStatus.Disconnected)

    override suspend fun discoverDevices(): List<NeuralDevice> {
        connectionStatus.update { NeuralConnectionStatus.Scanning }
        delay(250)
        connectionStatus.update { NeuralConnectionStatus.Disconnected }
        return listOf(
            NeuralDevice(
                deviceId = "simulated-neural-device",
                name = "ROLA Neural Simulator",
                type = NeuralDeviceType.Simulated,
                supportsRealtimeSignals = true,
                firmwareVersion = "future-adapter-1",
            ),
        )
    }

    override suspend fun connect(device: NeuralDevice): Result<NeuralDeviceSession> = runCatching {
        connectionStatus.update { NeuralConnectionStatus.Connecting }
        delay(200)
        val session = NeuralDeviceSession(
            sessionId = "neural-session-${UUID.randomUUID()}",
            device = device,
            connectedAt = System.currentTimeMillis(),
        )
        connectionStatus.update { NeuralConnectionStatus.Connected }
        session
    }.onFailure {
        connectionStatus.update { NeuralConnectionStatus.Error }
    }

    override fun observeSignals(session: NeuralDeviceSession, userId: String): Flow<NeuralSignalSample> = flow {
        var index = 0
        while (true) {
            val cycle = index % 6
            emit(
                NeuralSignalSample(
                    signalId = "signal-${UUID.randomUUID()}",
                    userId = userId,
                    sessionId = session.sessionId,
                    attentionScore = listOf(0.72f, 0.68f, 0.48f, 0.36f, 0.61f, 0.82f)[cycle],
                    engagementScore = listOf(0.78f, 0.7f, 0.55f, 0.42f, 0.63f, 0.86f)[cycle],
                    mentalWorkloadScore = listOf(0.42f, 0.56f, 0.74f, 0.86f, 0.62f, 0.38f)[cycle],
                    fatigueScore = listOf(0.22f, 0.28f, 0.41f, 0.63f, 0.45f, 0.25f)[cycle],
                    signalQuality = if (cycle == 3) SignalQuality.Fair else SignalQuality.Good,
                ),
            )
            index += 1
            delay(1_500)
        }
    }

    override suspend fun disconnect() {
        connectionStatus.update { NeuralConnectionStatus.Disconnected }
    }
}
