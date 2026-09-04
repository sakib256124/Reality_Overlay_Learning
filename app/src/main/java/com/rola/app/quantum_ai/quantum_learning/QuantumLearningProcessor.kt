package com.rola.app.quantum_ai.quantum_learning

import com.rola.app.quantum_ai.intelligence.QuantumComputeMode
import com.rola.app.quantum_ai.intelligence.QuantumLearningInput
import com.rola.app.quantum_ai.intelligence.QuantumLearningProfile
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuantumLearningProcessor @Inject constructor() {
    fun buildProfile(input: QuantumLearningInput): QuantumLearningProfile {
        val average = input.learningHistoryScores.average().takeIf { !it.isNaN() } ?: 55.0
        val readiness = ((average.toInt() + input.behaviorSignals.size * 4 - input.knowledgeGaps.size * 5).coerceIn(20, 95))
        return QuantumLearningProfile(
            profileId = "quantum-profile-${UUID.randomUUID()}",
            learnerId = input.learnerId,
            computeMode = QuantumComputeMode.QuantumInspired,
            optimizationReadinessPercent = readiness,
            preferredExplanationStyle = if (input.cognitiveProfile.contains("visual", ignoreCase = true)) "visual-spatial" else "step-by-step",
            activeGoals = input.learningGoals.ifEmpty { listOf("Build ${input.topic} foundation") },
        )
    }
}
