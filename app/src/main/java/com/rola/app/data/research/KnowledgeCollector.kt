package com.rola.app.data.research

import com.rola.app.domain.model.ResearchTask
import com.rola.app.domain.model.ScientificSource
import javax.inject.Inject
import javax.inject.Singleton

data class CollectedKnowledge(
    val task: ResearchTask,
    val sources: List<ScientificSource>,
    val extractedText: String,
    val collectedAt: Long = System.currentTimeMillis(),
)

@Singleton
class KnowledgeCollector @Inject constructor(
    private val sourceManager: SourceManager,
) {
    fun collect(
        task: ResearchTask,
        availableSources: List<ScientificSource>,
        internalKnowledge: List<String> = emptyList(),
    ): CollectedKnowledge {
        val sources = sourceManager.sourcesForTopic(task.topic, availableSources.ifEmpty { sourceManager.trustedSeedSources() })
        val sourceContext = sources.joinToString(separator = "\n") { source ->
            "${source.title} (${source.publisher}) covers ${source.topics.joinToString()}."
        }
        val internalContext = internalKnowledge.joinToString(separator = "\n")
        return CollectedKnowledge(
            task = task,
            sources = sources,
            extractedText = listOf(task.reason, sourceContext, internalContext)
                .filter { it.isNotBlank() }
                .joinToString(separator = "\n"),
        )
    }
}
