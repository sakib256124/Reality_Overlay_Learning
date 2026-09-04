package com.rola.app.domain.model

data class GlobalInstitution(
    val globalInstitutionId: String,
    val institutionId: String,
    val name: String,
    val countryCode: String,
    val primaryLanguage: String,
    val educationSystem: String,
    val profile: String,
    val verificationStatus: VerificationStatus = VerificationStatus.Pending,
    val publicDirectory: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis(),
)

enum class VerificationStatus {
    Pending,
    Verified,
    Rejected,
    Suspended,
}

data class InstitutionConnection(
    val connectionId: String,
    val sourceInstitutionId: String,
    val targetInstitutionId: String,
    val connectionType: InstitutionConnectionType,
    val status: CollaborationStatus = CollaborationStatus.Pending,
    val createdAt: Long = System.currentTimeMillis(),
)

enum class InstitutionConnectionType {
    KnowledgeExchange,
    ResearchPartner,
    SisterSchool,
    MarketplaceProvider,
}

data class VerificationRecord(
    val recordId: String,
    val institutionId: String,
    val reviewerId: String,
    val status: VerificationStatus,
    val notes: String,
    val reviewedAt: Long = System.currentTimeMillis(),
)

data class SharedKnowledgeResource(
    val resourceId: String,
    val ownerInstitutionId: String,
    val title: String,
    val description: String,
    val contentType: SharedContentType,
    val languageCode: String,
    val license: SharingLicense,
    val visibility: ResourceVisibility,
    val tags: List<String> = emptyList(),
    val sourceIds: List<String> = emptyList(),
    val downloadUrl: String = "",
    val rating: Float = 0f,
    val version: Int = 1,
    val createdAt: Long = System.currentTimeMillis(),
)

enum class SharedContentType {
    ArLesson,
    ThreeDModel,
    QuizDatabase,
    ScientificArticle,
    ResearchContent,
    LearningPathway,
    InteractiveSimulation,
}

enum class SharingLicense {
    Open,
    InstitutionOnly,
    Premium,
    ResearchAttribution,
}

enum class ResourceVisibility {
    Private,
    ConnectedInstitutions,
    Public,
    Marketplace,
}

data class MarketplaceListing(
    val listingId: String,
    val resourceId: String,
    val priceTier: PriceTier,
    val contributorId: String,
    val approved: Boolean = false,
    val featured: Boolean = false,
    val publishedAt: Long? = null,
)

enum class PriceTier {
    Free,
    Premium,
    Sponsored,
}

data class CollaborationRoom(
    val roomId: String,
    val title: String,
    val hostInstitutionId: String,
    val participantInstitutionIds: List<String>,
    val purpose: CollaborationPurpose,
    val status: CollaborationStatus = CollaborationStatus.Pending,
    val createdAt: Long = System.currentTimeMillis(),
)

enum class CollaborationPurpose {
    InstitutionCollaboration,
    TeacherCollaboration,
    ResearchCollaboration,
    StudentProject,
}

enum class CollaborationStatus {
    Pending,
    Active,
    Completed,
    Archived,
}

data class ProjectWorkspace(
    val workspaceId: String,
    val roomId: String,
    val title: String,
    val memberIds: List<String>,
    val resourceIds: List<String>,
    val milestones: List<String>,
)

data class SharedLearningSession(
    val sharedSessionId: String,
    val roomId: String,
    val facilitatorId: String,
    val topic: String,
    val languageCodes: List<String>,
    val sharedObjectIds: List<String>,
    val live: Boolean = false,
)

data class CommunityPost(
    val postId: String,
    val authorId: String,
    val institutionId: String,
    val title: String,
    val body: String,
    val tags: List<String>,
    val languageCode: String,
    val createdAt: Long = System.currentTimeMillis(),
)

data class DiscussionThread(
    val threadId: String,
    val resourceId: String?,
    val title: String,
    val participantIds: List<String>,
    val messageCount: Int = 0,
    val updatedAt: Long = System.currentTimeMillis(),
)

data class LearningGroup(
    val groupId: String,
    val title: String,
    val countryCodes: List<String>,
    val languageCodes: List<String>,
    val memberIds: List<String>,
    val challengeTopic: String,
)

data class ContributionScore(
    val contributorId: String,
    val contributorType: ContributorType,
    val score: Int,
    val resourceCount: Int,
    val collaborationCount: Int,
    val updatedAt: Long = System.currentTimeMillis(),
)

enum class ContributorType {
    Institution,
    Teacher,
    Student,
    Researcher,
}

data class RecognitionBadge(
    val badgeId: String,
    val contributorId: String,
    val title: String,
    val reason: String,
    val awardedAt: Long = System.currentTimeMillis(),
)

data class GlobalAnalyticsReport(
    val reportId: String,
    val region: String,
    val popularTopics: List<String>,
    val knowledgeGaps: List<String>,
    val activeInstitutionCount: Int,
    val sharedResourceCount: Int,
    val collaborationCount: Int,
    val generatedAt: Long = System.currentTimeMillis(),
)

data class GlobalOpportunity(
    val opportunityId: String,
    val title: String,
    val description: String,
    val relatedInstitutionIds: List<String>,
    val topic: String,
    val languageCodes: List<String>,
)

data class TenantScope(
    val institutionId: String,
    val countryCode: String,
    val languageCodes: List<String>,
    val educationSystem: String,
) {
    fun owns(resourceInstitutionId: String): Boolean = institutionId == resourceInstitutionId
}
