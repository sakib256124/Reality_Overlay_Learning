package com.rola.app.data.knowledgegraph

import com.rola.app.domain.model.ConceptGraph
import com.rola.app.domain.model.KnowledgeNode
import com.rola.app.domain.model.KnowledgeRelation
import com.rola.app.domain.model.SemanticSearchResult
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SemanticSearchEngine @Inject constructor(
    private val relationshipEngine: RelationshipEngine,
) {
    fun search(
        query: String,
        nodes: List<KnowledgeNode>,
        relations: List<KnowledgeRelation>,
        limit: Int = DEFAULT_LIMIT,
    ): SemanticSearchResult {
        val tokens = tokenize(query).flatMap { token -> listOf(token) + synonymMap[token].orEmpty() }.distinct()
        val ranked = nodes
            .map { node -> node to scoreNode(node, tokens) }
            .filter { (_, score) -> score > 0 }
            .sortedWith(compareByDescending<Pair<KnowledgeNode, Int>> { it.second }.thenBy { it.first.name })
            .map { it.first }
            .take(limit)

        val graph = relationshipEngine.buildConceptGraph(
            centralNodeId = ranked.firstOrNull()?.nodeId,
            seedNodes = ranked.take(3),
            allNodes = nodes,
            allRelations = relations + relationshipEngine.inferRelations(nodes, relations),
        )
        return SemanticSearchResult(
            query = query,
            primaryNodes = ranked,
            graph = graph,
            explanation = explain(query, ranked, graph),
        )
    }

    private fun scoreNode(node: KnowledgeNode, tokens: List<String>): Int {
        val searchable = listOf(
            node.name,
            node.type.displayName,
            node.description,
            node.category,
            node.aliases.joinToString(" "),
            node.tags.joinToString(" "),
        ).joinToString(" ").lowercase()

        return tokens.sumOf { token ->
            when {
                node.name.equals(token, ignoreCase = true) -> 12
                node.aliases.any { it.equals(token, ignoreCase = true) } -> 10
                node.tags.any { it.equals(token, ignoreCase = true) } -> 7
                searchable.contains(token) -> 4
                token.length >= 4 && searchable.contains(token.take(4)) -> 1
                else -> 0
            }
        }
    }

    private fun explain(
        query: String,
        ranked: List<KnowledgeNode>,
        graph: ConceptGraph,
    ): String {
        val top = ranked.firstOrNull()?.name ?: return "No connected concepts found for \"$query\" yet."
        val linked = graph.neighbors(ranked.first().nodeId).take(4).joinToString { it.name }
        return if (linked.isBlank()) {
            "$top is the closest concept in the knowledge graph."
        } else {
            "$top connects to $linked, giving a broader learning route."
        }
    }

    private fun tokenize(query: String): List<String> =
        query.lowercase()
            .replace(Regex("[^\\p{L}\\p{N}\\s]"), " ")
            .split(Regex("\\s+"))
            .filter { it.length >= 3 && it !in stopWords }

    private companion object {
        const val DEFAULT_LIMIT = 8

        val stopWords = setOf(
            "how",
            "does",
            "work",
            "why",
            "what",
            "this",
            "that",
            "with",
            "from",
            "about",
            "learn",
            "explain",
        )

        val synonymMap = mapOf(
            "electric" to listOf("electricity", "current", "circuit", "conductor"),
            "electricity" to listOf("current", "charge", "circuit", "conductor", "copper"),
            "wire" to listOf("wiring", "copper", "conductor"),
            "wires" to listOf("wiring", "copper", "conductor"),
            "plant" to listOf("leaf", "photosynthesis", "biology"),
            "plants" to listOf("leaf", "photosynthesis", "biology"),
            "energy" to listOf("battery", "chemical", "electricity"),
            "material" to listOf("metal", "copper", "iron"),
            "materials" to listOf("metal", "copper", "iron"),
        )
    }
}
