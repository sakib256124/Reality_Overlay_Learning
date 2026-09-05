package com.rola.app.lifelong_memory.evolution

import com.rola.app.lifelong_memory.memory_core.MemoryEvolutionState
import com.rola.app.lifelong_memory.memory_core.PersonalKnowledgeGraphState
import javax.inject.Inject

class MemoryEvolutionManager @Inject constructor() {
    fun evolve(graph: PersonalKnowledgeGraphState): MemoryEvolutionState =
        MemoryEvolutionState(
            evolutionId = "memory-evolution-${graph.graphId}",
            organizedKnowledge = graph.concepts + graph.skills,
            outdatedInformationRemoved = listOf("duplicate short-term fragments", "low-confidence stale notes"),
            newConnections = graph.experiences.map { "connect experience:$it to expertise:${graph.expertise.name}" },
        )
}
