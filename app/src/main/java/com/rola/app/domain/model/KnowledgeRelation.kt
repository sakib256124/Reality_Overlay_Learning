package com.rola.app.domain.model

data class KnowledgeRelation(
    val relationId: String,
    val sourceNodeId: String,
    val targetNodeId: String,
    val type: KnowledgeRelationType,
    val description: String,
    val confidence: Float = 1f,
    val verified: Boolean = true,
    val createdBy: String = "system",
    val updatedAt: Long = System.currentTimeMillis(),
)

enum class KnowledgeRelationType(val wireName: String, val displayName: String) {
    IsA("is_a", "is a"),
    MadeOf("made_of", "made of"),
    UsedFor("used_for", "used for"),
    RelatedTo("related_to", "related to"),
    PartOf("part_of", "part of"),
    Causes("causes", "causes"),
    DiscoveredBy("discovered_by", "discovered by"),
    AppliedIn("applied_in", "applied in"),
}
