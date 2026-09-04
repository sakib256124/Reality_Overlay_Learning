package com.rola.app.data.knowledgegraph

import com.rola.app.domain.model.ConceptGraph
import com.rola.app.domain.model.KnowledgeNode
import com.rola.app.domain.model.KnowledgeRelation
import com.rola.app.domain.model.LearningPath
import com.rola.app.domain.model.LearningPathStep
import com.rola.app.domain.model.SemanticSearchResult
import com.rola.app.domain.model.SkillLevel
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

@Singleton
class KnowledgeGraphRepository @Inject constructor(
    private val graphDatabaseManager: GraphDatabaseManager,
    private val semanticSearchEngine: SemanticSearchEngine,
    private val relationshipEngine: RelationshipEngine,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        scope.launch {
            graphDatabaseManager.ensureSeedData()
            runCatching { graphDatabaseManager.refreshFromCloud() }
        }
    }

    fun observeConceptGraph(): Flow<ConceptGraph> =
        combine(
            graphDatabaseManager.observeNodes(),
            graphDatabaseManager.observeRelations(),
        ) { nodes, relations ->
            ConceptGraph(
                centralNodeId = nodes.firstOrNull()?.nodeId,
                nodes = nodes,
                relations = relations + relationshipEngine.inferRelations(nodes, relations),
            )
        }

    fun observeLearningPaths(): Flow<List<LearningPath>> =
        graphDatabaseManager.observeLearningPaths()

    suspend fun semanticSearch(query: String): SemanticSearchResult {
        graphDatabaseManager.ensureSeedData()
        val allNodes = graphDatabaseManager.getAllNodes()
        val allRelations = graphDatabaseManager.getAllRelations()
        val direct = if (query.isBlank()) emptyList() else graphDatabaseManager.searchLocal(query)
        val semantic = semanticSearchEngine.search(query, allNodes, allRelations)
        val primary = (direct + semantic.primaryNodes).distinctBy { it.nodeId }
        return semantic.copy(
            primaryNodes = primary,
            graph = relationshipEngine.buildConceptGraph(
                centralNodeId = primary.firstOrNull()?.nodeId,
                seedNodes = primary.take(3),
                allNodes = allNodes,
                allRelations = allRelations + relationshipEngine.inferRelations(allNodes, allRelations),
            ),
        )
    }

    suspend fun graphForNode(nodeId: String): ConceptGraph {
        graphDatabaseManager.ensureSeedData()
        val node = graphDatabaseManager.getNode(nodeId)
        return relationshipEngine.buildConceptGraph(
            centralNodeId = nodeId,
            seedNodes = listOfNotNull(node),
            allNodes = graphDatabaseManager.getAllNodes(),
            allRelations = graphDatabaseManager.getAllRelations(),
            maxDepth = 2,
        )
    }

    suspend fun learningPathFor(
        topic: String,
        level: SkillLevel,
    ): LearningPath? {
        graphDatabaseManager.ensureSeedData()
        val cached = graphDatabaseManager.getLearningPaths(topic, limit = 6)
            .firstOrNull { it.targetLevel == level }
            ?: graphDatabaseManager.getLearningPaths(topic, limit = 6).firstOrNull()
        if (cached != null) return cached

        val search = semanticSearch(topic)
        val orderedNodeIds = search.graph.nodes
            .sortedBy { node -> node.type.ordinal }
            .map { it.nodeId }
            .take(5)
        if (orderedNodeIds.isEmpty()) return null
        return LearningPath(
            pathId = "generated-${topic.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-')}-${level.name}",
            title = "Learn $topic through connected concepts",
            description = "A generated route built from nearby knowledge graph concepts.",
            targetLevel = level,
            topic = topic,
            nodeIds = orderedNodeIds,
            estimatedMinutes = orderedNodeIds.size * 4,
        )
    }

    suspend fun learningPathSteps(path: LearningPath): List<LearningPathStep> {
        val nodes = graphDatabaseManager.getAllNodes().associateBy { it.nodeId }
        return path.nodeIds.mapIndexedNotNull { index, nodeId ->
            nodes[nodeId]?.let { node ->
                LearningPathStep(
                    node = node,
                    order = index + 1,
                    reason = relationshipEngine.pathReason(node, index),
                )
            }
        }
    }

    suspend fun groundedTutorContext(question: String): String {
        val result = semanticSearch(question)
        if (result.primaryNodes.isEmpty()) return ""
        val nodeMap = result.graph.nodeById
        val relationLines = result.graph.relations.take(6).map { relation ->
            relationshipEngine.explainRelationPath(listOf(relation), nodeMap)
        }
        val conceptLines = result.primaryNodes.take(5).map { node ->
            "${node.name}: ${node.description}"
        }
        return (listOf("Knowledge graph context:", result.explanation) + conceptLines + relationLines)
            .joinToString(separator = "\n")
    }

    suspend fun addVerifiedConcept(node: KnowledgeNode) {
        graphDatabaseManager.upsertAdminNode(node.copy(verified = true, source = "Admin Verified"))
    }

    suspend fun addVerifiedRelation(relation: KnowledgeRelation) {
        graphDatabaseManager.upsertAdminRelation(relation.copy(verified = true, createdBy = "admin"))
    }
}
