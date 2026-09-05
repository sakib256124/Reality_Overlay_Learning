package com.rola.app.self_evolving_ai.evolution_core

import javax.inject.Inject

class EvolutionMemoryManager @Inject constructor() {
    fun remember(action: ImprovementAction, experiment: EvolutionExperimentResult): EvolutionMemory =
        EvolutionMemory(
            memoryId = "evolution-memory-${action.actionId}",
            previousImprovements = listOf(action.workflowImprovement, action.knowledgeDeliveryImprovement),
            successfulStrategies = listOf(experiment.winningStrategy),
            failedExperiments = listOf("strategy discarded: ${experiment.strategyA}"),
            evolutionHistory = listOf("validated ${action.actionId}", "tested ${experiment.experimentId}"),
        )
}
