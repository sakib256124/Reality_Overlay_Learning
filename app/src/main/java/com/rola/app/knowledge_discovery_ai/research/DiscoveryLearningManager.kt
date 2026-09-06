package com.rola.app.knowledge_discovery_ai.research

import com.rola.app.knowledge_discovery_ai.discovery_core.DiscoveryLearningPackage
import com.rola.app.knowledge_discovery_ai.discovery_core.KnowledgeDiscovery
import javax.inject.Inject

class DiscoveryLearningManager @Inject constructor() {
    fun integrate(discovery: KnowledgeDiscovery): DiscoveryLearningPackage =
        DiscoveryLearningPackage(
            packageId = "learning-${discovery.discoveryId}",
            lessons = discovery.newConcepts.map { "lesson: $it" },
            courses = listOf("emerging knowledge mini-course", "research literacy pathway"),
            researchProjects = discovery.knowledgeGaps.map { "project: investigate $it" },
            learningActivities = discovery.learningOpportunities,
        )
}
