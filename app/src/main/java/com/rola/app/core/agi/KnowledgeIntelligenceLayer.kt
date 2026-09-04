package com.rola.app.core.agi

import com.rola.app.domain.model.KnowledgeEvolutionProposal
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KnowledgeIntelligenceLayer @Inject constructor() {
    fun detectMissingTopics(
        institutionId: String,
        topic: String,
        researchSignals: List<String>,
        knownConcepts: List<String>,
    ): KnowledgeEvolutionProposal {
        val missing = researchSignals
            .map { it.substringBefore(":").trim() }
            .filter { it.isNotBlank() && knownConcepts.none { known -> known.equals(it, ignoreCase = true) } }
            .distinct()
            .take(6)
            .ifEmpty { listOf("$topic misconception review", "$topic advanced application") }
        return KnowledgeEvolutionProposal(
            proposalId = "knowledge-evolution-${UUID.randomUUID()}",
            institutionId = institutionId,
            topic = topic,
            missingConcepts = missing,
            curriculumUpdates = missing.map { "Add teacher-reviewed lesson segment for $it." },
        )
    }

    fun knowledgeGrowthSummary(proposals: List<KnowledgeEvolutionProposal>): List<String> =
        proposals.map { "${it.topic}: ${it.missingConcepts.size} concepts proposed for teacher approval." }
}
