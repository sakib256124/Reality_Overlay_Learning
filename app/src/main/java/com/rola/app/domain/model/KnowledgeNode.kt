package com.rola.app.domain.model

data class KnowledgeNode(
    val nodeId: String,
    val name: String,
    val type: KnowledgeNodeType,
    val description: String,
    val category: String,
    val aliases: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val verified: Boolean = true,
    val source: String = "ROLA Knowledge Graph",
    val updatedAt: Long = System.currentTimeMillis(),
)

enum class KnowledgeNodeType(val displayName: String) {
    Object("Object"),
    Material("Material"),
    ScientificConcept("Scientific Concept"),
    Process("Process"),
    Application("Application"),
    Theory("Theory"),
    Technology("Technology"),
}
