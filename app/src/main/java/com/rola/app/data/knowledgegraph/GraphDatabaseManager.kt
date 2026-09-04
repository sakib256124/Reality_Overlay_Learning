package com.rola.app.data.knowledgegraph

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rola.app.data.database.KnowledgeGraphDao
import com.rola.app.data.database.entities.KnowledgeNodeEntity
import com.rola.app.data.database.entities.KnowledgeRelationEntity
import com.rola.app.data.database.entities.LearningPathEntity
import com.rola.app.data.database.entities.toEntity
import com.rola.app.domain.model.KnowledgeNode
import com.rola.app.domain.model.KnowledgeNodeType
import com.rola.app.domain.model.KnowledgeRelation
import com.rola.app.domain.model.KnowledgeRelationType
import com.rola.app.domain.model.LearningPath
import com.rola.app.domain.model.SkillLevel
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await

@Singleton
class GraphDatabaseManager @Inject constructor(
    private val knowledgeGraphDao: KnowledgeGraphDao,
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
) {
    fun observeNodes(): Flow<List<KnowledgeNode>> =
        knowledgeGraphDao.observeNodes().map { rows -> rows.map { it.toDomain() } }

    fun observeRelations(): Flow<List<KnowledgeRelation>> =
        knowledgeGraphDao.observeRelations().map { rows -> rows.map { it.toDomain() } }

    fun observeLearningPaths(): Flow<List<LearningPath>> =
        knowledgeGraphDao.observeLearningPaths().map { rows -> rows.map { it.toDomain() } }

    suspend fun ensureSeedData() {
        if (knowledgeGraphDao.countNodes() > 0) return
        knowledgeGraphDao.upsertNodes(seedNodes.map { it.toEntity(isSynced = true) })
        knowledgeGraphDao.upsertRelations(seedRelations.map { it.toEntity(isSynced = true) })
        knowledgeGraphDao.upsertLearningPaths(seedLearningPaths.map { it.toEntity(isSynced = true) })
    }

    suspend fun searchLocal(query: String, limit: Int = 12): List<KnowledgeNode> =
        knowledgeGraphDao.searchNodes(query.sanitized(), limit).map { it.toDomain() }

    suspend fun getNode(nodeId: String): KnowledgeNode? =
        knowledgeGraphDao.getNode(nodeId)?.toDomain()

    suspend fun getAllNodes(): List<KnowledgeNode> =
        knowledgeGraphDao.getNodes().map { it.toDomain() }

    suspend fun getAllRelations(): List<KnowledgeRelation> =
        knowledgeGraphDao.getRelations().map { it.toDomain() }

    suspend fun getRelationsForNode(nodeId: String, limit: Int = 30): List<KnowledgeRelation> =
        knowledgeGraphDao.getRelationsForNode(nodeId, limit).map { it.toDomain() }

    suspend fun getLearningPaths(topic: String, limit: Int = 5): List<LearningPath> =
        knowledgeGraphDao.getLearningPaths(topic.sanitized(), limit).map { it.toDomain() }

    suspend fun upsertAdminNode(node: KnowledgeNode) {
        require(node.name.length in 2..80) { "Knowledge node name must be 2-80 characters." }
        require(node.description.length in 10..1000) { "Knowledge node description must be 10-1000 characters." }
        knowledgeGraphDao.upsertNodes(listOf(node.toEntity(isSynced = false)))
        uploadNode(node)
    }

    suspend fun upsertAdminRelation(relation: KnowledgeRelation) {
        require(relation.sourceNodeId != relation.targetNodeId) { "A relationship needs two different concepts." }
        require(relation.confidence in 0f..1f) { "Relationship confidence must be between 0 and 1." }
        knowledgeGraphDao.upsertRelations(listOf(relation.toEntity(isSynced = false)))
        uploadRelation(relation)
    }

    suspend fun refreshFromCloud() {
        val cloudNodes = firestore.collection(KNOWLEDGE_NODES_COLLECTION)
            .whereEqualTo("verified", true)
            .get()
            .await()
            .documents
            .mapNotNull { document -> document.data?.toNodeEntity(document.id) }
        val cloudRelations = firestore.collection(KNOWLEDGE_RELATIONS_COLLECTION)
            .whereEqualTo("verified", true)
            .get()
            .await()
            .documents
            .mapNotNull { document -> document.data?.toRelationEntity(document.id) }
        val cloudPaths = firestore.collection(LEARNING_PATHS_COLLECTION)
            .get()
            .await()
            .documents
            .mapNotNull { document -> document.data?.toPathEntity(document.id) }

        if (cloudNodes.isNotEmpty()) knowledgeGraphDao.upsertNodes(cloudNodes)
        if (cloudRelations.isNotEmpty()) knowledgeGraphDao.upsertRelations(cloudRelations)
        if (cloudPaths.isNotEmpty()) knowledgeGraphDao.upsertLearningPaths(cloudPaths)
    }

    private suspend fun uploadNode(node: KnowledgeNode) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        firestore.collection(KNOWLEDGE_NODES_COLLECTION)
            .document(node.nodeId)
            .set(node.toFirestoreMap(userId))
            .await()
    }

    private suspend fun uploadRelation(relation: KnowledgeRelation) {
        val userId = firebaseAuth.currentUser?.uid ?: return
        firestore.collection(KNOWLEDGE_RELATIONS_COLLECTION)
            .document(relation.relationId)
            .set(relation.toFirestoreMap(userId))
            .await()
    }

    @Suppress("UNCHECKED_CAST")
    private fun Map<String, Any>.toNodeEntity(documentId: String): KnowledgeNodeEntity? {
        val type = (this["type"] as? String)
            ?.let { raw -> KnowledgeNodeType.entries.firstOrNull { it.name == raw || it.displayName == raw } }
            ?: KnowledgeNodeType.Object
        return KnowledgeNodeEntity(
            nodeId = this["nodeId"] as? String ?: documentId,
            name = this["name"] as? String ?: return null,
            type = type,
            description = this["description"] as? String ?: "",
            category = this["category"] as? String ?: "",
            aliases = this["aliases"] as? List<String> ?: emptyList(),
            tags = this["tags"] as? List<String> ?: emptyList(),
            verified = this["verified"] as? Boolean ?: false,
            source = this["source"] as? String ?: "Cloud Knowledge Graph",
            isSynced = true,
            updatedAt = (this["updatedAt"] as? Number)?.toLong() ?: System.currentTimeMillis(),
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun Map<String, Any>.toRelationEntity(documentId: String): KnowledgeRelationEntity? {
        val type = (this["type"] as? String)
            ?.let { raw -> KnowledgeRelationType.entries.firstOrNull { it.name == raw || it.wireName == raw } }
            ?: KnowledgeRelationType.RelatedTo
        return KnowledgeRelationEntity(
            relationId = this["relationId"] as? String ?: documentId,
            sourceNodeId = this["sourceNodeId"] as? String ?: return null,
            targetNodeId = this["targetNodeId"] as? String ?: return null,
            type = type,
            description = this["description"] as? String ?: "",
            confidence = (this["confidence"] as? Number)?.toFloat() ?: 0.7f,
            verified = this["verified"] as? Boolean ?: false,
            createdBy = this["createdBy"] as? String ?: "cloud",
            isSynced = true,
            updatedAt = (this["updatedAt"] as? Number)?.toLong() ?: System.currentTimeMillis(),
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun Map<String, Any>.toPathEntity(documentId: String): LearningPathEntity? =
        LearningPathEntity(
            pathId = this["pathId"] as? String ?: documentId,
            title = this["title"] as? String ?: return null,
            description = this["description"] as? String ?: "",
            targetLevel = (this["targetLevel"] as? String)
                ?.let { runCatching { SkillLevel.valueOf(it) }.getOrNull() }
                ?: SkillLevel.Beginner,
            topic = this["topic"] as? String ?: "",
            nodeIds = this["nodeIds"] as? List<String> ?: emptyList(),
            estimatedMinutes = (this["estimatedMinutes"] as? Number)?.toInt() ?: 10,
            generatedBy = this["generatedBy"] as? String ?: "cloud",
            isSynced = true,
            updatedAt = (this["updatedAt"] as? Number)?.toLong() ?: System.currentTimeMillis(),
        )

    private fun KnowledgeNode.toFirestoreMap(userId: String): Map<String, Any?> = mapOf(
        "nodeId" to nodeId,
        "name" to name,
        "type" to type.name,
        "typeLabel" to type.displayName,
        "description" to description,
        "category" to category,
        "aliases" to aliases,
        "tags" to tags,
        "verified" to verified,
        "source" to source,
        "updatedBy" to userId,
        "updatedAt" to updatedAt,
    )

    private fun KnowledgeRelation.toFirestoreMap(userId: String): Map<String, Any?> = mapOf(
        "relationId" to relationId,
        "sourceNodeId" to sourceNodeId,
        "targetNodeId" to targetNodeId,
        "type" to type.wireName,
        "description" to description,
        "confidence" to confidence,
        "verified" to verified,
        "createdBy" to userId,
        "updatedAt" to updatedAt,
    )

    private fun String.sanitized(): String =
        trim().replace(Regex("[^\\p{L}\\p{N}\\s-]"), " ").take(120)

    private companion object {
        const val KNOWLEDGE_NODES_COLLECTION = "knowledgeNodes"
        const val KNOWLEDGE_RELATIONS_COLLECTION = "knowledgeRelations"
        const val LEARNING_PATHS_COLLECTION = "learningPaths"

        val seedNodes = listOf(
            KnowledgeNode("node-copper", "Copper", KnowledgeNodeType.Material, "A reddish metal with high electrical and thermal conductivity.", "Materials", listOf("Cu"), listOf("metal", "conductor")),
            KnowledgeNode("node-metal", "Metal", KnowledgeNodeType.Material, "A class of materials that usually conduct heat and electricity and can be shaped.", "Materials", tags = listOf("material")),
            KnowledgeNode("node-electrical-conductivity", "Electrical Conductivity", KnowledgeNodeType.ScientificConcept, "The ability of a material to allow electric charge to flow through it.", "Physics", listOf("conductivity"), listOf("electricity")),
            KnowledgeNode("node-wire", "Wiring", KnowledgeNodeType.Application, "Conductive paths that move electrical energy or signals between parts of a circuit.", "Technology", listOf("wire", "wires"), listOf("electronics")),
            KnowledgeNode("node-electronics", "Electronics", KnowledgeNodeType.Technology, "Technology that controls electric current to process signals, power devices, and perform computation.", "Technology", tags = listOf("circuits")),
            KnowledgeNode("node-electricity", "Electricity", KnowledgeNodeType.ScientificConcept, "Energy and phenomena caused by electric charge and current.", "Physics", tags = listOf("charge", "current")),
            KnowledgeNode("node-conductor", "Conductor", KnowledgeNodeType.ScientificConcept, "A material or object that lets electric charge move easily.", "Physics", tags = listOf("electricity", "materials")),
            KnowledgeNode("node-circuit", "Circuit", KnowledgeNodeType.Technology, "A closed path that lets electric current flow through components.", "Technology", aliases = listOf("circuits"), tags = listOf("electricity")),
            KnowledgeNode("node-battery", "Battery", KnowledgeNodeType.Object, "An object that stores chemical energy and releases it as electrical energy.", "Energy", tags = listOf("electricity", "chemistry")),
            KnowledgeNode("node-chemical-energy", "Chemical Energy", KnowledgeNodeType.ScientificConcept, "Energy stored in bonds between atoms and molecules.", "Chemistry", tags = listOf("energy")),
            KnowledgeNode("node-photosynthesis", "Photosynthesis", KnowledgeNodeType.Process, "The process plants use to convert light, water, and carbon dioxide into sugars and oxygen.", "Biology", tags = listOf("plants", "energy")),
            KnowledgeNode("node-leaf", "Leaf", KnowledgeNodeType.Object, "A plant part that absorbs light and exchanges gases for photosynthesis.", "Biology", tags = listOf("plants")),
            KnowledgeNode("node-plant", "Plant", KnowledgeNodeType.Object, "A living organism that usually makes food using sunlight.", "Biology", tags = listOf("organism")),
            KnowledgeNode("node-gravity", "Gravity", KnowledgeNodeType.Theory, "An attractive force between masses that shapes motion on Earth and in space.", "Physics", tags = listOf("force")),
            KnowledgeNode("node-construction", "Construction", KnowledgeNodeType.Application, "The process of building structures using materials and engineering principles.", "Engineering", tags = listOf("materials")),
            KnowledgeNode("node-iron", "Iron", KnowledgeNodeType.Material, "A strong magnetic metal widely used in tools, machines, and buildings.", "Materials", listOf("Fe"), listOf("metal")),
        )

        val seedRelations = listOf(
            KnowledgeRelation("rel-copper-metal", "node-copper", "node-metal", KnowledgeRelationType.IsA, "Copper is a metal."),
            KnowledgeRelation("rel-copper-conductivity", "node-copper", "node-electrical-conductivity", KnowledgeRelationType.RelatedTo, "Copper conducts electricity very well."),
            KnowledgeRelation("rel-conductivity-conductor", "node-electrical-conductivity", "node-conductor", KnowledgeRelationType.RelatedTo, "Conductors are identified by high electrical conductivity."),
            KnowledgeRelation("rel-copper-wire", "node-copper", "node-wire", KnowledgeRelationType.UsedFor, "Copper is commonly used for electrical wiring."),
            KnowledgeRelation("rel-wire-electronics", "node-wire", "node-electronics", KnowledgeRelationType.AppliedIn, "Wiring is applied in electronic systems."),
            KnowledgeRelation("rel-electricity-circuit", "node-electricity", "node-circuit", KnowledgeRelationType.RelatedTo, "Electricity flows through circuits."),
            KnowledgeRelation("rel-conductor-circuit", "node-conductor", "node-circuit", KnowledgeRelationType.PartOf, "Conductors form paths inside circuits."),
            KnowledgeRelation("rel-battery-electricity", "node-battery", "node-electricity", KnowledgeRelationType.Causes, "A battery can produce electric current."),
            KnowledgeRelation("rel-battery-chemical-energy", "node-battery", "node-chemical-energy", KnowledgeRelationType.RelatedTo, "A battery stores chemical energy."),
            KnowledgeRelation("rel-plant-leaf", "node-leaf", "node-plant", KnowledgeRelationType.PartOf, "A leaf is part of a plant."),
            KnowledgeRelation("rel-leaf-photosynthesis", "node-leaf", "node-photosynthesis", KnowledgeRelationType.RelatedTo, "Leaves are major sites of photosynthesis."),
            KnowledgeRelation("rel-photosynthesis-chemical-energy", "node-photosynthesis", "node-chemical-energy", KnowledgeRelationType.Causes, "Photosynthesis stores light energy as chemical energy."),
            KnowledgeRelation("rel-iron-metal", "node-iron", "node-metal", KnowledgeRelationType.IsA, "Iron is a metal."),
            KnowledgeRelation("rel-iron-construction", "node-iron", "node-construction", KnowledgeRelationType.UsedFor, "Iron and steel are used in construction."),
        )

        val seedLearningPaths = listOf(
            LearningPath("path-electricity-beginner", "Electricity Foundations", "Start with energy sources, then follow current through conductors and circuits.", SkillLevel.Beginner, "electricity", listOf("node-battery", "node-electricity", "node-conductor", "node-circuit"), 18),
            LearningPath("path-copper-intermediate", "Why Copper Matters", "Connect copper's material properties to wiring and electronics.", SkillLevel.Intermediate, "copper", listOf("node-copper", "node-metal", "node-electrical-conductivity", "node-wire", "node-electronics"), 20),
            LearningPath("path-plants-beginner", "Plant Energy Route", "Understand plants by following leaves, photosynthesis, and stored chemical energy.", SkillLevel.Beginner, "plants", listOf("node-plant", "node-leaf", "node-photosynthesis", "node-chemical-energy"), 16),
            LearningPath("path-materials-beginner", "Useful Materials", "Compare metals and their real-world applications.", SkillLevel.Beginner, "materials", listOf("node-metal", "node-copper", "node-iron", "node-construction"), 15),
        )
    }
}
