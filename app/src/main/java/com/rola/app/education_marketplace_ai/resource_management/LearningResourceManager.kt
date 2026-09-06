package com.rola.app.education_marketplace_ai.resource_management

import com.rola.app.education_marketplace_ai.marketplace_core.LearningResourceCatalog
import com.rola.app.education_marketplace_ai.marketplace_core.MarketplaceResourceDiscovery
import javax.inject.Inject

class LearningResourceManager @Inject constructor() {
    fun organize(discovery: MarketplaceResourceDiscovery): LearningResourceCatalog =
        LearningResourceCatalog(
            catalogId = "catalog-${discovery.discoveryId}",
            digitalCourses = listOf("adaptive AI course", "global research mini-course"),
            lessons = discovery.resources.map { "lesson for $it" },
            videos = listOf("short concept video", "lab demonstration video"),
            documents = listOf("teacher guide", "accessible notes"),
            arExperiences = listOf("AR object lesson", "3D marketplace preview"),
            virtualLabs = listOf("simulation lab", "digital twin lab"),
            researchContent = listOf("verified paper summary", "dataset explainer"),
            versionControl = true,
            accessibilityReady = true,
        )
}
