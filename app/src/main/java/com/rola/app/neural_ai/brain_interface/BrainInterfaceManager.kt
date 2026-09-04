package com.rola.app.neural_ai.brain_interface

import com.rola.app.neural_ai.cognitive_signal.NeuralConnectionStatus
import com.rola.app.neural_ai.cognitive_signal.NeuralDevice
import com.rola.app.neural_ai.cognitive_signal.NeuralDeviceSession
import com.rola.app.neural_ai.cognitive_signal.NeuralSignalSample
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

@Singleton
class BrainInterfaceManager @Inject constructor(
    private val brainComputerInterface: BrainComputerInterface,
) {
    val connectionStatus: StateFlow<NeuralConnectionStatus> = brainComputerInterface.connectionStatus

    suspend fun compatibleDevices(): List<NeuralDevice> = brainComputerInterface.discoverDevices()

    suspend fun connectToBestAvailableDevice(): Result<NeuralDeviceSession> {
        val device = compatibleDevices().firstOrNull { it.supportsRealtimeSignals }
            ?: return Result.failure(IllegalStateException("No compatible neural interface adapter found."))
        return brainComputerInterface.connect(device)
    }

    fun observeSignals(session: NeuralDeviceSession, userId: String): Flow<NeuralSignalSample> =
        brainComputerInterface.observeSignals(session, userId)

    suspend fun stopSession() {
        brainComputerInterface.disconnect()
    }
}
