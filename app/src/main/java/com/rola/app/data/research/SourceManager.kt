package com.rola.app.data.research

import com.rola.app.domain.model.ScientificSource
import com.rola.app.domain.model.ScientificSourceType
import com.rola.app.domain.model.SourceReliability
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SourceManager @Inject constructor() {
    fun trustedSeedSources(): List<ScientificSource> = listOf(
        ScientificSource(
            sourceId = "source-khan-academy",
            title = "Khan Academy Science",
            url = "https://www.khanacademy.org/science",
            sourceType = ScientificSourceType.EducationalDatabase,
            reliability = SourceReliability.Educational,
            publisher = "Khan Academy",
            topics = listOf("biology", "physics", "chemistry"),
        ),
        ScientificSource(
            sourceId = "source-nasa-education",
            title = "NASA STEM Engagement",
            url = "https://www.nasa.gov/stem",
            sourceType = ScientificSourceType.EducationalDatabase,
            reliability = SourceReliability.OfficialDataset,
            publisher = "NASA",
            topics = listOf("space", "physics", "engineering"),
        ),
        ScientificSource(
            sourceId = "source-internal-rola-kg",
            title = "ROLA Verified Knowledge Graph",
            url = "internal://knowledge-graph",
            sourceType = ScientificSourceType.InternalKnowledgeBase,
            reliability = SourceReliability.PeerReviewed,
            publisher = "ROLA",
            topics = listOf("objects", "materials", "learning-paths"),
        ),
    )

    fun reliabilityFor(source: ScientificSource): Float =
        if (source.trusted) source.reliability.score else source.reliability.score * 0.5f

    fun sourcesForTopic(
        topic: String,
        availableSources: List<ScientificSource>,
        limit: Int = 3,
    ): List<ScientificSource> {
        val normalizedTopic = topic.lowercase()
        return availableSources
            .sortedWith(
                compareByDescending<ScientificSource> { source ->
                    source.topics.count { normalizedTopic.contains(it.lowercase()) || it.lowercase().contains(normalizedTopic) }
                }.thenByDescending { reliabilityFor(it) },
            )
            .take(limit)
    }
}
