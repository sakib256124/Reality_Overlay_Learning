package com.rola.app.domain.model

data class Recommendation(
    val recommendationId: String,
    val userId: String,
    val title: String,
    val description: String,
    val topic: String,
    val type: RecommendationType,
    val priority: RecommendationPriority,
    val targetSkillLevel: SkillLevel,
    val createdAt: Long = System.currentTimeMillis(),
    val completed: Boolean = false,
)
