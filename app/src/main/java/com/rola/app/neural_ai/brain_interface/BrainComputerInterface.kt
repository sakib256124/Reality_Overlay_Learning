package com.rola.app.neural_ai.brain_interface

import com.rola.app.neural_ai.cognitive_signal.NeuralConnectionStatus
import com.rola.app.neural_ai.cognitive_signal.NeuralDevice
import com.rola.app.neural_ai.cognitive_signal.NeuralDeviceSession
import com.rola.app.neural_ai.cognitive_signal.NeuralSignalSample
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface BrainComputerInterface {
    val connectionStatus: StateFlow<NeuralConnectionStatus>

    suspend fun discoverDevices(): List<NeuralDevice>

    suspend fun connect(device: NeuralDevice): Result<NeuralDeviceSession>

    fun observeSignals(session: NeuralDeviceSession, userId: String): Flow<NeuralSignalSample>

    suspend fun disconnect()
}
