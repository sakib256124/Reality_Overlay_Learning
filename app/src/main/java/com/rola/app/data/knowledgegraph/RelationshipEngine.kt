package com.rola.app.data.knowledgegraph

import com.rola.app.domain.model.ConceptGraph
import com.rola.app.domain.model.KnowledgeNode
import com.rola.app.domain.model.KnowledgeNodeType
import com.rola.app.domain.model.KnowledgeRelation
import com.rola.app.domain.model.KnowledgeRelationType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RelationshipEngine @Inject constructor() {
    fun buildConceptGraph(
        centralNodeId: String?,
        seedNodes: List<KnowledgeNode>,
        allNodes: List<KnowledgeNode>,
        allRelations: List<KnowledgeRelation>,
        maxDepth: Int = DEFAULT_DEPTH,
    ): ConceptGraph {
        val seeds = (centralNodeId?.let { listOf(it) }.orEmpty() + seedNodes.map { it.nodeId }).distinct()
        if (seeds.isEmpty()) return ConceptGraph(null, seedNodes, emptyList())

        val visited = seeds.toMutableSet()
        val frontier = ArrayDeque<Pair<String, Int>>()
        seeds.forEach { frontier.add(it to 0) }
        val selectedRelationIds = linkedSetOf<String>()

        while (frontier.isNotEmpty()) {
            val (currentId, depth) = frontier.removeFirst()
            if (depth >= maxDepth) continue
            allRelations
                .filter { relation -> relation.sourceNodeId == currentId || relation.targetNodeId == currentId }
                .sortedByDescending { it.confidence }
                .take(MAX_BRANCHING)
                .forEach { relation ->
                    selectedRelationIds += relation.relationId
                    val neighbor = if (relation.sourceNodeId == currentId) relation.targetNodeId else relation.sourceNodeId
                    if (visited.add(neighbor)) {
                        frontier.add(neighbor to depth + 1)
                    }
                }
        }

        val nodes = allNodes.filter { it.nodeId in visited }
        val relations = allRelations.filter { it.relationId in selectedRelationIds }
        return ConceptGraph(centralNodeId ?: seedNodes.firstOrNull()?.nodeId, nodes, relations)
    }

    fun inferRelations(
        nodes: List<KnowledgeNode>,
        relations: List<KnowledgeRelation>,
    ): List<KnowledgeRelation> {
        val bySource = relations.groupBy { it.sourceNodeId }
        val existing = relations.map { Triple(it.sourceNodeId, it.targetNodeId, it.type) }.toSet()
        val nodeIds = nodes.map { it.nodeId }.toSet()

        return relations
            .filter { it.type == KnowledgeRelationType.IsA && it.sourceNodeId in nodeIds }
            .flatMap { isA ->
                bySource[isA.targetNodeId].orEmpty()
                    .filter { inherited -> inherited.type in inheritableRelations && inherited.targetNodeId in nodeIds }
                    .mapNotNull { inherited ->
                        val key = Triple(isA.sourceNodeId, inherited.targetNodeId, inherited.type)
                        if (key in existing) {
                            null
                        } else {
                            KnowledgeRelation(
                                relationId = "inferred-${isA.sourceNodeId}-${inherited.type.name}-${inherited.targetNodeId}",
                                sourceNodeId = isA.sourceNodeId,
                                targetNodeId = inherited.targetNodeId,
                                type = inherited.type,
                                description = "Inferred because ${isA.sourceNodeId} is a ${isA.targetNodeId}.",
                                confidence = minOf(isA.confidence, inherited.confidence) * 0.72f,
                                verified = false,
                                createdBy = "rule_engine",
                            )
                        }
                    }
            }
            .distinctBy { it.relationId }
    }

    fun shortestPath(
        sourceNodeId: String,
        targetNodeId: String,
        relations: List<KnowledgeRelation>,
        maxDepth: Int = PATH_DEPTH,
    ): List<KnowledgeRelation> {
        if (sourceNodeId == targetNodeId) return emptyList()
        val queue = ArrayDeque<Pair<String, List<KnowledgeRelation>>>()
        val visited = mutableSetOf(sourceNodeId)
        queue.add(sourceNodeId to emptyList())

        while (queue.isNotEmpty()) {
            val (current, path) = queue.removeFirst()
            if (path.size >= maxDepth) continue
            relations
                .filter { it.sourceNodeId == current || it.targetNodeId == current }
                .sortedByDescending { it.confidence }
                .forEach { relation ->
                    val next = if (relation.sourceNodeId == current) relation.targetNodeId else relation.sourceNodeId
                    if (!visited.add(next)) return@forEach
                    val nextPath = path + relation
                    if (next == targetNodeId) return nextPath
                    queue.add(next to nextPath)
                }
        }
        return emptyList()
    }

    fun explainRelationPath(
        path: List<KnowledgeRelation>,
        nodes: Map<String, KnowledgeNode>,
    ): String {
        if (path.isEmpty()) return "No direct semantic path was found yet."
        return path.joinToString(separator = " -> ") { relation ->
            val source = nodes[relation.sourceNodeId]?.name ?: relation.sourceNodeId
            val target = nodes[relation.targetNodeId]?.name ?: relation.targetNodeId
            "$source ${relation.type.displayName} $target"
        }
    }

    fun pathReason(node: KnowledgeNode, index: Int): String =
        when {
            index == 0 -> "Start with ${node.name} to anchor the topic."
            node.type == KnowledgeNodeType.ScientificConcept -> "Learn the concept that explains the object."
            node.type == KnowledgeNodeType.Application -> "Connect the science to a real-world use."
            node.type == KnowledgeNodeType.Technology -> "See how the idea becomes technology."
            else -> "Build the next linked idea."
        }

    private companion object {
        const val DEFAULT_DEPTH = 2
        const val PATH_DEPTH = 5
        const val MAX_BRANCHING = 6
        val inheritableRelations = setOf(
            KnowledgeRelationType.UsedFor,
            KnowledgeRelationType.AppliedIn,
            KnowledgeRelationType.RelatedTo,
            KnowledgeRelationType.PartOf,
        )
    }
}
