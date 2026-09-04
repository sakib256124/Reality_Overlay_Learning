package com.rola.app.global.knowledge_sharing

import com.rola.app.domain.model.GlobalOpportunity
import com.rola.app.domain.model.MarketplaceListing
import com.rola.app.domain.model.PriceTier
import com.rola.app.domain.model.ResourceVisibility
import com.rola.app.domain.model.SharedKnowledgeResource
import com.rola.app.domain.model.TenantScope
import com.rola.app.global.network.TenantManager
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KnowledgeSharingManager @Inject constructor(
    private val tenantManager: TenantManager,
) {
    fun publishResource(
        tenant: TenantScope,
        resource: SharedKnowledgeResource,
    ): SharedKnowledgeResource {
        require(tenant.owns(resource.ownerInstitutionId)) { "Resource owner must match tenant scope." }
        return resource.copy(
            resourceId = resource.resourceId.ifBlank { "resource-${UUID.randomUUID()}" },
            tags = resource.tags.distinct().take(20),
        )
    }

    fun importableResources(
        tenant: TenantScope,
        resources: List<SharedKnowledgeResource>,
        connectedInstitutionIds: Set<String>,
    ): List<SharedKnowledgeResource> = resources
        .filter { tenantManager.canAccessResource(tenant, it, connectedInstitutionIds) }
        .filterNot { tenant.owns(it.ownerInstitutionId) }
        .sortedByDescending { it.rating }

    fun marketplace(resources: List<SharedKnowledgeResource>): List<MarketplaceListing> =
        resources
            .filter { it.visibility == ResourceVisibility.Marketplace || it.visibility == ResourceVisibility.Public }
            .map {
                MarketplaceListing(
                    listingId = "listing-${it.resourceId}",
                    resourceId = it.resourceId,
                    priceTier = if (it.license == com.rola.app.domain.model.SharingLicense.Premium) PriceTier.Premium else PriceTier.Free,
                    contributorId = it.ownerInstitutionId,
                    approved = true,
                    featured = it.rating >= 4.5f,
                    publishedAt = it.createdAt,
                )
            }

    fun globalOpportunities(
        tenant: TenantScope,
        resources: List<SharedKnowledgeResource>,
    ): List<GlobalOpportunity> = resources
        .filter { it.languageCode in tenant.languageCodes || it.visibility == ResourceVisibility.Public }
        .groupBy { it.tags.firstOrNull() ?: it.contentType.name }
        .map { (topic, topicResources) ->
            GlobalOpportunity(
                opportunityId = "opportunity-${topic.lowercase().replace(Regex("[^a-z0-9]+"), "-")}",
                title = "Global collaboration on $topic",
                description = "Connect with institutions sharing ${topicResources.size} resources.",
                relatedInstitutionIds = topicResources.map { it.ownerInstitutionId }.distinct().take(5),
                topic = topic,
                languageCodes = topicResources.map { it.languageCode }.distinct(),
            )
        }
}
