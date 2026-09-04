package com.rola.app.domain.model

data class ScientificSource(
    val sourceId: String,
    val title: String,
    val url: String,
    val sourceType: ScientificSourceType,
    val reliability: SourceReliability,
    val publisher: String = "",
    val authors: List<String> = emptyList(),
    val publicationYear: Int? = null,
    val topics: List<String> = emptyList(),
    val trusted: Boolean = reliability != SourceReliability.Unverified,
    val addedAt: Long = System.currentTimeMillis(),
)

enum class ScientificSourceType {
    ScientificArticle,
    EducationalDatabase,
    StructuredDataset,
    InternalKnowledgeBase,
}

enum class SourceReliability(val score: Float) {
    Unverified(0.2f),
    Educational(0.65f),
    PeerReviewed(0.9f),
    OfficialDataset(0.95f),
}
