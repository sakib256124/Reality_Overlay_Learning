package com.rola.app.data.research

import com.rola.app.domain.model.KnowledgeRelationType
import com.rola.app.domain.model.KnowledgeUpdate
import com.rola.app.domain.model.KnowledgeVerification
import com.rola.app.domain.model.ModerationStatus
import com.rola.app.domain.model.RelationSuggestion
import com.rola.app.domain.model.SkillLevel
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

data class ContentAnalysis(
    val definitions: List<String>,
    val properties: List<String>,
    val applications: List<String>,
    val examples: List<String>,
    val relationships: List<RelationSuggestion>,
    val difficultyLevel: SkillLevel,
)

@Singleton
class ContentAnalyzer @Inject constructor() {
    fun analyze(collectedKnowledge: CollectedKnowledge): ContentAnalysis {
        val topic = collectedKnowledge.task.topic.cleanTopic()
        val text = collectedKnowledge.extractedText
        val difficulty = when {
            text.contains("quantum", ignoreCase = true) || text.contains("molecular", ignoreCase = true) -> SkillLevel.Advanced
            text.length > 600 -> SkillLevel.Intermediate
            else -> SkillLevel.Beginner
        }

        return ContentAnalysis(
            definitions = listOf("$topic is a scientific learning topic connected to observable real-world objects."),
            properties = inferProperties(topic, text),
            applications = inferApplications(topic, text),
            examples = inferExamples(topic),
            relationships = inferRelationships(topic, text),
            difficultyLevel = difficulty,
        )
    }

    fun buildKnowledgeUpdate(
        collectedKnowledge: CollectedKnowledge,
        verification: KnowledgeVerification,
    ): KnowledgeUpdate {
        val analysis = analyze(collectedKnowledge)
        val topic = collectedKnowledge.task.topic.cleanTopic()
        return KnowledgeUpdate(
            updateId = "update-${UUID.randomUUID()}",
            taskId = collectedKnowledge.task.taskId,
            topic = topic,
            summary = "$topic research update: definitions, properties, applications, examples, and graph relationships prepared for review.",
            definitions = analysis.definitions,
            properties = analysis.properties,
            applications = analysis.applications,
            examples = analysis.examples,
            relationSuggestions = analysis.relationships,
            difficultyLevel = analysis.difficultyLevel,
            sourceIds = collectedKnowledge.sources.map { it.sourceId },
            verification = verification,
        )
    }

    private fun inferProperties(topic: String, text: String): List<String> {
        val properties = mutableListOf<String>()
        if (text.contains("energy", ignoreCase = true)) properties += "$topic involves energy transfer or storage."
        if (text.contains("cell", ignoreCase = true)) properties += "$topic includes cellular structure or biological organization."
        if (text.contains("material", ignoreCase = true)) properties += "$topic can be described by observable material properties."
        return properties.ifEmpty { listOf("$topic has measurable properties that can be compared across examples.") }
    }

    private fun inferApplications(topic: String, text: String): List<String> {
        val applications = mutableListOf<String>()
        if (text.contains("engineering", ignoreCase = true)) applications += "$topic supports engineering and design decisions."
        if (text.contains("biology", ignoreCase = true)) applications += "$topic helps explain living systems."
        if (text.contains("physics", ignoreCase = true)) applications += "$topic helps explain forces, energy, or matter."
        return applications.ifEmpty { listOf("$topic can be connected to classroom explanations and real-world observation.") }
    }

    private fun inferExamples(topic: String): List<String> = listOf(
        "Observe $topic in a familiar object.",
        "Compare $topic with a related concept.",
        "Ask how $topic changes across different environments.",
    )

    private fun inferRelationships(topic: String, text: String): List<RelationSuggestion> {
        val suggestions = mutableListOf<RelationSuggestion>()
        if (text.contains("plant", ignoreCase = true) || topic.contains("plant", ignoreCase = true)) {
            suggestions += RelationSuggestion(topic, "Plant", KnowledgeRelationType.RelatedTo, "Research mentions plant biology.", 0.82f)
        }
        if (text.contains("energy", ignoreCase = true)) {
            suggestions += RelationSuggestion(topic, "Energy", KnowledgeRelationType.RelatedTo, "Research mentions energy transfer or storage.", 0.76f)
        }
        if (text.contains("application", ignoreCase = true) || text.contains("used", ignoreCase = true)) {
            suggestions += RelationSuggestion(topic, "Real-world Application", KnowledgeRelationType.AppliedIn, "Research mentions practical use.", 0.7f)
        }
        return suggestions.ifEmpty {
            listOf(RelationSuggestion(topic, "Scientific Concept", KnowledgeRelationType.IsA, "Topic is educational scientific content.", 0.65f))
        }
    }

    private fun String.cleanTopic(): String =
        trim().replace(Regex("\\s+"), " ").replaceFirstChar { it.uppercase() }
}

@Singleton
class KnowledgeVerificationEngine @Inject constructor(
    private val sourceManager: SourceManager,
) {
    fun verify(
        collectedKnowledge: CollectedKnowledge,
        existingUpdates: List<KnowledgeUpdate>,
    ): KnowledgeVerification {
        val reliability = collectedKnowledge.sources
            .map { sourceManager.reliabilityFor(it) }
            .ifEmpty { listOf(0.2f) }
            .average()
            .toFloat()
        val duplicateRisk = if (existingUpdates.any { it.topic.equals(collectedKnowledge.task.topic, ignoreCase = true) }) 0.72f else 0.15f
        val contradictionRisk = if (collectedKnowledge.extractedText.contains("unknown", ignoreCase = true)) 0.42f else 0.12f
        val consistencyScore = 1f - contradictionRisk
        val moderation = if (reliability < 0.4f || contradictionRisk > 0.5f) ModerationStatus.NeedsReview else ModerationStatus.Safe

        return KnowledgeVerification(
            reliabilityScore = reliability.coerceIn(0f, 1f),
            duplicateRisk = duplicateRisk,
            consistencyScore = consistencyScore.coerceIn(0f, 1f),
            contradictionRisk = contradictionRisk,
            moderationStatus = moderation,
            notes = listOf(
                "Sources checked: ${collectedKnowledge.sources.size}",
                "Duplicate risk: ${(duplicateRisk * 100).toInt()}%",
                "Contradiction risk: ${(contradictionRisk * 100).toInt()}%",
            ),
        )
    }
}
