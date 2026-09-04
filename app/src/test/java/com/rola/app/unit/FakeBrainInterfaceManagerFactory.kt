package com.rola.app.unit

import com.rola.app.neural_ai.brain_interface.BrainInterfaceManager
import com.rola.app.neural_ai.brain_interface.SimulatedBrainComputerInterface

object FakeBrainInterfaceManagerFactory {
    fun create(): BrainInterfaceManager = BrainInterfaceManager(SimulatedBrainComputerInterface())
}
