package com.rola.app.domain.model

data class GlobalEducationDashboard(
    val tenantScope: TenantScope,
    val directory: List<GlobalInstitution>,
    val importableResources: List<SharedKnowledgeResource>,
    val marketplaceListings: List<MarketplaceListing>,
    val collaborationRooms: List<CollaborationRoom>,
    val communityFeed: List<CommunityPost>,
    val analyticsReport: GlobalAnalyticsReport,
    val opportunities: List<GlobalOpportunity>,
    val contributionScore: ContributionScore?,
    val badges: List<RecognitionBadge>,
)
