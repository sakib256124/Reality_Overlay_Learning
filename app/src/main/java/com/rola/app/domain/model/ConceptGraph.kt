package com.rola.app.domain.model

data class ConceptGraph(
    val centralNodeId: String?,
    val nodes: List<KnowledgeNode>,
    val relations: List<KnowledgeRelation>,
) {
    val nodeById: Map<String, KnowledgeNode> = nodes.associateBy { it.nodeId }

    fun neighbors(nodeId: String): List<KnowledgeNode> {
        val linkedIds = relations
            .filter { it.sourceNodeId == nodeId || it.targetNodeId == nodeId }
            .flatMap { listOf(it.sourceNodeId, it.targetNodeId) }
            .filterNot { it == nodeId }
            .distinct()
        return linkedIds.mapNotNull(nodeById::get)
    }
}

data class SemanticSearchResult(
    val query: String,
    val primaryNodes: List<KnowledgeNode>,
    val graph: ConceptGraph,
    val explanation: String,
)
