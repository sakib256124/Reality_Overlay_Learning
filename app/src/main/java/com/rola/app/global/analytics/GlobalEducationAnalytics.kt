package com.rola.app.global.analytics

import com.rola.app.domain.model.GlobalAnalyticsReport
import com.rola.app.domain.model.GlobalInstitution
import com.rola.app.domain.model.SharedKnowledgeResource
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalEducationAnalytics @Inject constructor() {
    fun report(
        region: String,
        institutions: List<GlobalInstitution>,
        resources: List<SharedKnowledgeResource>,
        collaborationCount: Int,
    ): GlobalAnalyticsReport {
        val topics = resources.flatMap { it.tags }.groupingBy { it }.eachCount()
        return GlobalAnalyticsReport(
            reportId = "global-report-${UUID.randomUUID()}",
            region = region,
            popularTopics = topics.entries.sortedByDescending { it.value }.map { it.key }.take(8),
            knowledgeGaps = inferKnowledgeGaps(resources),
            activeInstitutionCount = institutions.count { it.publicDirectory },
            sharedResourceCount = resources.size,
            collaborationCount = collaborationCount,
        )
    }

    private fun inferKnowledgeGaps(resources: List<SharedKnowledgeResource>): List<String> {
        val required = listOf("biology", "physics", "chemistry", "engineering", "earth science")
        val available = resources.flatMap { it.tags }.map { it.lowercase() }.toSet()
        return required.filterNot { it in available }.take(5)
    }
}
