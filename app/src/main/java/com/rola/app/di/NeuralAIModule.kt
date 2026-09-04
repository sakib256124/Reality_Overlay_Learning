package com.rola.app.di

import com.rola.app.neural_ai.brain_interface.BrainComputerInterface
import com.rola.app.neural_ai.brain_interface.SimulatedBrainComputerInterface
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NeuralAIModule {
    @Binds
    @Singleton
    abstract fun bindBrainComputerInterface(
        implementation: SimulatedBrainComputerInterface,
    ): BrainComputerInterface
}
