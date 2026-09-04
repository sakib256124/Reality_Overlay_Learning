package com.rola.app.unit

import com.rola.app.domain.model.CollaborationPurpose
import com.rola.app.domain.model.CollaborationRoom
import com.rola.app.domain.model.CollaborationStatus
import com.rola.app.domain.model.CommunityPost
import com.rola.app.domain.model.ContributorType
import com.rola.app.domain.model.GlobalInstitution
import com.rola.app.domain.model.ResourceVisibility
import com.rola.app.domain.model.SharedContentType
import com.rola.app.domain.model.SharedKnowledgeResource
import com.rola.app.domain.model.SharingLicense
import com.rola.app.domain.model.TenantScope
import com.rola.app.domain.model.VerificationStatus
import com.rola.app.global.analytics.GlobalEducationAnalytics
import com.rola.app.global.collaboration.CollaborationManager
import com.rola.app.global.community.CommunityManager
import com.rola.app.global.community.ReputationSystem
import com.rola.app.global.institutions.GlobalInstitutionNetwork
import com.rola.app.global.knowledge_sharing.KnowledgeSharingManager
import com.rola.app.global.network.TenantManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class GlobalEducationNetworkTest {
    private val tenantManager = TenantManager()
    private val institutionNetwork = GlobalInstitutionNetwork()
    private val sharingManager = KnowledgeSharingManager(tenantManager)
    private val collaborationManager = CollaborationManager()
    private val communityManager = CommunityManager()
    private val analytics = GlobalEducationAnalytics()
    private val reputation = ReputationSystem()

    @Test
    fun privateResourcesStayInsideTenant() {
        val tenant = TenantScope("institution-a", "BD", listOf("bn", "en"), "National Curriculum")
        val privateResource = resource(owner = "institution-b", visibility = ResourceVisibility.Private)

        assertFalse(tenantManager.canAccessResource(tenant, privateResource, connectedInstitutionIds = emptySet()))
    }

    @Test
    fun verifiedInstitutionsAppearInDirectory() {
        val directory = institutionNetwork.directory(
            listOf(
                institution("institution-a", VerificationStatus.Verified, public = true),
                institution("institution-b", VerificationStatus.Pending, public = false),
            ),
        )

        assertEquals(listOf("institution-a"), directory.map { it.institutionId })
    }

    @Test
    fun marketplaceIncludesPublicAndMarketplaceResources() {
        val listings = sharingManager.marketplace(
            listOf(
                resource("institution-a", ResourceVisibility.Marketplace),
                resource("institution-b", ResourceVisibility.Private),
            ),
        )

        assertEquals(1, listings.size)
        assertTrue(listings.first().approved)
    }

    @Test
    fun collaborationRoomCreatesLiveSharedSessionWhenActive() {
        val room = collaborationManager.activateRoom(
            CollaborationRoom(
                roomId = "room-1",
                title = "Metals Project",
                hostInstitutionId = "institution-a",
                participantInstitutionIds = listOf("institution-a", "institution-b"),
                purpose = CollaborationPurpose.StudentProject,
            ),
        )
        val session = collaborationManager.sharedSession(room, "teacher-1", "Metals", listOf("en", "bn"), listOf("iron"))

        assertEquals(CollaborationStatus.Active, room.status)
        assertTrue(session.live)
    }

    @Test
    fun communityFeedFiltersByLanguageAndTopic() {
        val posts = communityManager.feed(
            posts = listOf(
                CommunityPost("post-1", "student-1", "institution-a", "Copper", "Found a wire.", listOf("physics"), "en"),
                CommunityPost("post-2", "student-2", "institution-b", "Leaf", "Observed a plant.", listOf("biology"), "bn"),
            ),
            languageCode = "bn",
            topic = "biology",
        )

        assertEquals(listOf("post-2"), posts.map { it.postId })
    }

    @Test
    fun globalAnalyticsDetectsPopularTopicsAndGaps() {
        val report = analytics.report(
            region = "global",
            institutions = listOf(institution("institution-a", VerificationStatus.Verified, public = true)),
            resources = listOf(resource("institution-a", ResourceVisibility.Public, tags = listOf("physics", "physics"))),
            collaborationCount = 2,
        )

        assertEquals("physics", report.popularTopics.first())
        assertTrue(report.knowledgeGaps.isNotEmpty())
    }

    @Test
    fun reputationAwardsContributorBadges() {
        val score = reputation.contributionScore("teacher-1", ContributorType.Teacher, resourceCount = 3, collaborationCount = 3)
        val badges = reputation.badges(score)

        assertTrue(score.score > 0)
        assertTrue(badges.any { it.title == "Global Collaborator" })
    }

    private fun institution(
        id: String,
        status: VerificationStatus,
        public: Boolean,
    ): GlobalInstitution = GlobalInstitution(
        globalInstitutionId = "global-$id",
        institutionId = id,
        name = id,
        countryCode = "BD",
        primaryLanguage = "bn",
        educationSystem = "National Curriculum",
        profile = "Science institution",
        verificationStatus = status,
        publicDirectory = public,
    )

    private fun resource(
        owner: String,
        visibility: ResourceVisibility,
        tags: List<String> = listOf("physics"),
    ): SharedKnowledgeResource = SharedKnowledgeResource(
        resourceId = "resource-$owner-${visibility.name}",
        ownerInstitutionId = owner,
        title = "Shared Physics Lesson",
        description = "AR-ready lesson content.",
        contentType = SharedContentType.ArLesson,
        languageCode = "en",
        license = SharingLicense.Open,
        visibility = visibility,
        tags = tags,
        rating = 4.8f,
    )
}
