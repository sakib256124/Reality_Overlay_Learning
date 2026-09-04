package com.rola.app.data.research

import com.rola.app.data.knowledgegraph.KnowledgeGraphRepository
import com.rola.app.domain.model.KnowledgeNode
import com.rola.app.domain.model.KnowledgeNodeType
import com.rola.app.domain.model.KnowledgeRelation
import com.rola.app.domain.model.KnowledgeUpdate
import com.rola.app.domain.model.KnowledgeUpdateStatus
import com.rola.app.domain.model.ResearchPriority
import com.rola.app.domain.model.ResearchTask
import com.rola.app.domain.model.ResearchTaskStatus
import com.rola.app.domain.model.ScientificSource
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResearchAgent @Inject constructor(
    private val knowledgeGraphRepository: KnowledgeGraphRepository,
    private val knowledgeCollector: KnowledgeCollector,
    private val contentAnalyzer: ContentAnalyzer,
    private val verificationEngine: KnowledgeVerificationEngine,
    private val contentGenerator: ScientificContentGenerator,
) {
    suspend fun analyzeKnowledgeGaps(topic: String): List<ResearchTask> {
        val search = knowledgeGraphRepository.semanticSearch(topic)
        val foundNames = search.graph.nodes.map { it.name.lowercase() }.toSet()
        val missingTopics = expectedSubtopics(topic)
            .filterNot { expected -> foundNames.any { node -> node.contains(expected.lowercase()) } }

        return missingTopics.mapIndexed { index, missing ->
            ResearchTask(
                taskId = "research-${UUID.randomUUID()}",
                topic = missing,
                reason = "Knowledge gap detected while expanding $topic.",
                priority = if (index == 0) ResearchPriority.High else ResearchPriority.Medium,
                status = ResearchTaskStatus.Pending,
                suggestedSources = listOf("Educational database", "Peer-reviewed overview", "ROLA internal knowledge graph"),
            )
        }
    }

    fun generateResearchTask(
        topic: String,
        reason: String = "Admin requested scientific knowledge expansion.",
        priority: ResearchPriority = ResearchPriority.Medium,
    ): ResearchTask = ResearchTask(
        taskId = "research-${UUID.randomUUID()}",
        topic = topic.trim(),
        reason = reason,
        priority = priority,
        suggestedSources = listOf("Scientific article", "Educational database", "Structured dataset"),
    )

    fun prepareKnowledgeUpdate(
        task: ResearchTask,
        sources: List<ScientificSource>,
        previousUpdates: List<KnowledgeUpdate>,
        internalKnowledge: List<String> = emptyList(),
    ): ResearchOutput {
        val collected = knowledgeCollector.collect(task.copy(status = ResearchTaskStatus.Collecting), sources, internalKnowledge)
        val verification = verificationEngine.verify(collected, previousUpdates)
        val update = contentAnalyzer.buildKnowledgeUpdate(collected, verification)
        val materials = contentGenerator.generateMaterials(update)
        return ResearchOutput(
            task = task.copy(status = ResearchTaskStatus.AwaitingApproval, updatedAt = System.currentTimeMillis()),
            update = update,
            learningMaterials = materials,
        )
    }

    suspend fun applyApprovedUpdate(update: KnowledgeUpdate): KnowledgeUpdate {
        require(update.status == KnowledgeUpdateStatus.Approved || update.verification.isApprovalReady) {
            "Knowledge update must be approved or pass verification before graph application."
        }

        val nodeId = "research-node-${update.topic.slug()}"
        val node = KnowledgeNode(
            nodeId = nodeId,
            name = update.topic,
            type = KnowledgeNodeType.ScientificConcept,
            description = update.definitions.firstOrNull() ?: update.summary,
            category = "Research Expansion",
            aliases = listOf(update.topic.lowercase()),
            tags = listOf("ai-research", "verified-content"),
            verified = true,
            source = update.sourceIds.joinToString(separator = ", ").ifBlank { "Research Assistant" },
        )
        knowledgeGraphRepository.addVerifiedConcept(node)

        update.relationSuggestions
            .filter { it.confidence >= 0.65f }
            .forEach { suggestion ->
                knowledgeGraphRepository.addVerifiedRelation(
                    KnowledgeRelation(
                        relationId = "research-rel-${UUID.randomUUID()}",
                        sourceNodeId = nodeId,
                        targetNodeId = "concept-${suggestion.targetName.slug()}",
                        type = suggestion.relationType,
                        description = suggestion.evidence,
                        confidence = suggestion.confidence,
                        verified = true,
                        createdBy = "research-agent",
                    ),
                )
            }

        return update.copy(status = KnowledgeUpdateStatus.AppliedToGraph)
    }

    suspend fun tutorResearchContext(question: String): String {
        val graphContext = knowledgeGraphRepository.groundedTutorContext(question)
        return if (graphContext.isBlank()) {
            "Research assistant has no verified expansion for this question yet."
        } else {
            "Research-validated context:\n$graphContext"
        }
    }

    private fun expectedSubtopics(topic: String): List<String> {
        val lower = topic.lowercase()
        return when {
            "plant" in lower -> listOf("Plant cellular structure", "Photosynthesis mechanism", "Chlorophyll", "Plant adaptation")
            "electric" in lower -> listOf("Charge movement", "Conductors", "Circuit safety", "Energy conversion")
            "metal" in lower -> listOf("Atomic structure", "Conductivity", "Alloys", "Corrosion")
            else -> listOf("$topic definition", "$topic properties", "$topic applications", "$topic examples")
        }
    }

    private fun String.slug(): String =
        lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-').ifBlank { "topic" }
}

data class ResearchOutput(
    val task: ResearchTask,
    val update: KnowledgeUpdate,
    val learningMaterials: List<com.rola.app.domain.model.LearningMaterial>,
)
