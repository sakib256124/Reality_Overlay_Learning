package com.rola.app.quantum_ai.optimization

import com.rola.app.quantum_ai.intelligence.QuantumLearningInput
import com.rola.app.quantum_ai.intelligence.QuantumLearningProfile
import com.rola.app.quantum_ai.intelligence.QuantumOptimizationResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuantumLearningOptimizer @Inject constructor(
    private val optimizationEngine: QuantumOptimizationEngine,
) {
    fun optimizeLearning(input: QuantumLearningInput, profile: QuantumLearningProfile): QuantumOptimizationResult =
        optimizationEngine.optimize(input, profile)
}
