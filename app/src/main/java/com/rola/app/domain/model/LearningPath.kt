package com.rola.app.domain.model

data class LearningPath(
    val pathId: String,
    val title: String,
    val description: String,
    val targetLevel: SkillLevel,
    val topic: String,
    val nodeIds: List<String>,
    val estimatedMinutes: Int,
    val generatedBy: String = "knowledge_graph",
    val updatedAt: Long = System.currentTimeMillis(),
)

data class LearningPathStep(
    val node: KnowledgeNode,
    val order: Int,
    val reason: String,
)
