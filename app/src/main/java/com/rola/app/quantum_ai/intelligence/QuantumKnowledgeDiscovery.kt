package com.rola.app.quantum_ai.intelligence

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuantumKnowledgeDiscovery @Inject constructor() {
    fun discover(input: QuantumLearningInput): QuantumKnowledgeDiscoveryResult {
        val discovered = (input.knowledgeGaps + input.researchKeywords()).distinct().take(6)
        return QuantumKnowledgeDiscoveryResult(
            discoveryId = "quantum-discovery-${UUID.randomUUID()}",
            topic = input.topic,
            hiddenRelationships = discovered.map { "${input.topic} has a learning dependency with $it." },
            discoveredConcepts = discovered,
            scientificSignals = input.behaviorSignals.filter { it.contains("research", ignoreCase = true) }.ifEmpty { listOf("No external research source attached yet") },
            expansionRecommendation = "Create teacher-reviewed graph edges before using discoveries in student-facing content.",
        )
    }

    private fun QuantumLearningInput.researchKeywords(): List<String> =
        behaviorSignals.map { it.substringBefore(":").trim() }.filter { it.isNotBlank() }
}
