package com.rola.app.domain.model

data class KnowledgeUpdate(
    val updateId: String,
    val taskId: String,
    val topic: String,
    val summary: String,
    val definitions: List<String>,
    val properties: List<String>,
    val applications: List<String>,
    val examples: List<String>,
    val relationSuggestions: List<RelationSuggestion>,
    val difficultyLevel: SkillLevel,
    val sourceIds: List<String>,
    val verification: KnowledgeVerification,
    val status: KnowledgeUpdateStatus = KnowledgeUpdateStatus.PendingReview,
    val version: Int = 1,
    val createdAt: Long = System.currentTimeMillis(),
    val approvedAt: Long? = null,
    val approvedBy: String? = null,
)

data class RelationSuggestion(
    val sourceName: String,
    val targetName: String,
    val relationType: KnowledgeRelationType,
    val evidence: String,
    val confidence: Float,
)

data class KnowledgeVerification(
    val reliabilityScore: Float,
    val duplicateRisk: Float,
    val consistencyScore: Float,
    val contradictionRisk: Float,
    val moderationStatus: ModerationStatus,
    val notes: List<String> = emptyList(),
) {
    val isApprovalReady: Boolean
        get() = reliabilityScore >= 0.65f &&
            duplicateRisk <= 0.45f &&
            consistencyScore >= 0.7f &&
            contradictionRisk <= 0.35f &&
            moderationStatus == ModerationStatus.Safe
}

enum class KnowledgeUpdateStatus {
    Draft,
    PendingReview,
    Approved,
    Rejected,
    AppliedToGraph,
}

enum class ModerationStatus {
    Safe,
    NeedsReview,
    Blocked,
}
