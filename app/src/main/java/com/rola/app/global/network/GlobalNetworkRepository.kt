package com.rola.app.global.network

import com.rola.app.domain.model.CollaborationRoom
import com.rola.app.domain.model.CommunityPost
import com.rola.app.domain.model.ContributionScore
import com.rola.app.domain.model.GlobalEducationDashboard
import com.rola.app.domain.model.GlobalInstitution
import com.rola.app.domain.model.InstitutionConnection
import com.rola.app.domain.model.SharedKnowledgeResource
import com.rola.app.domain.model.TenantScope
import com.rola.app.global.analytics.GlobalEducationAnalytics
import com.rola.app.global.community.CommunityManager
import com.rola.app.global.community.ReputationSystem
import com.rola.app.global.institutions.GlobalInstitutionNetwork
import com.rola.app.global.knowledge_sharing.KnowledgeSharingManager
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GlobalNetworkRepository @Inject constructor(
    private val institutionNetwork: GlobalInstitutionNetwork,
    private val knowledgeSharingManager: KnowledgeSharingManager,
    private val communityManager: CommunityManager,
    private val analytics: GlobalEducationAnalytics,
    private val reputationSystem: ReputationSystem,
) {
    fun dashboard(
        tenant: TenantScope,
        institutions: List<GlobalInstitution>,
        connections: List<InstitutionConnection>,
        resources: List<SharedKnowledgeResource>,
        rooms: List<CollaborationRoom>,
        posts: List<CommunityPost>,
        contributionScore: ContributionScore? = null,
    ): GlobalEducationDashboard {
        val connected = institutionNetwork.activeConnections(tenant.institutionId, connections)
        val directory = institutionNetwork.directory(institutions)
        val importable = knowledgeSharingManager.importableResources(tenant, resources, connected)
        val report = analytics.report(
            region = tenant.countryCode,
            institutions = institutions,
            resources = resources,
            collaborationCount = rooms.size,
        )
        return GlobalEducationDashboard(
            tenantScope = tenant,
            directory = directory,
            importableResources = importable,
            marketplaceListings = knowledgeSharingManager.marketplace(resources),
            collaborationRooms = rooms.filter { tenant.institutionId in it.participantInstitutionIds || tenant.institutionId == it.hostInstitutionId },
            communityFeed = communityManager.feed(posts, languageCode = tenant.languageCodes.firstOrNull()),
            analyticsReport = report,
            opportunities = knowledgeSharingManager.globalOpportunities(tenant, resources),
            contributionScore = contributionScore,
            badges = contributionScore?.let(reputationSystem::badges).orEmpty(),
        )
    }
}
