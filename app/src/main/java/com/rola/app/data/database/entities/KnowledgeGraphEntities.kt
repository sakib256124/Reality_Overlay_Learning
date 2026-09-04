package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rola.app.domain.model.KnowledgeNode
import com.rola.app.domain.model.KnowledgeNodeType
import com.rola.app.domain.model.KnowledgeRelation
import com.rola.app.domain.model.KnowledgeRelationType
import com.rola.app.domain.model.LearningPath
import com.rola.app.domain.model.SkillLevel

@Entity(
    tableName = "knowledge_nodes",
    indices = [
        Index(value = ["name"]),
        Index(value = ["type"]),
        Index(value = ["category"]),
        Index(value = ["verified"]),
    ],
)
data class KnowledgeNodeEntity(
    @PrimaryKey val nodeId: String,
    val name: String,
    val type: KnowledgeNodeType,
    val description: String,
    val category: String,
    val aliases: List<String> = emptyList(),
    val tags: List<String> = emptyList(),
    val verified: Boolean = true,
    val source: String = "ROLA Knowledge Graph",
    val isSynced: Boolean = true,
    val updatedAt: Long = System.currentTimeMillis(),
) {
    fun toDomain(): KnowledgeNode = KnowledgeNode(
        nodeId = nodeId,
        name = name,
        type = type,
        description = description,
        category = category,
        aliases = aliases,
        tags = tags,
        verified = verified,
        source = source,
        updatedAt = updatedAt,
    )
}

@Entity(
    tableName = "knowledge_relations",
    indices = [
        Index(value = ["sourceNodeId"]),
        Index(value = ["targetNodeId"]),
        Index(value = ["type"]),
        Index(value = ["verified"]),
    ],
)
data class KnowledgeRelationEntity(
    @PrimaryKey val relationId: String,
    val sourceNodeId: String,
    val targetNodeId: String,
    val type: KnowledgeRelationType,
    val description: String,
    val confidence: Float = 1f,
    val verified: Boolean = true,
    val createdBy: String = "system",
    val isSynced: Boolean = true,
    val updatedAt: Long = System.currentTimeMillis(),
) {
    fun toDomain(): KnowledgeRelation = KnowledgeRelation(
        relationId = relationId,
        sourceNodeId = sourceNodeId,
        targetNodeId = targetNodeId,
        type = type,
        description = description,
        confidence = confidence,
        verified = verified,
        createdBy = createdBy,
        updatedAt = updatedAt,
    )
}

@Entity(
    tableName = "learning_paths",
    indices = [
        Index(value = ["topic"]),
        Index(value = ["targetLevel"]),
    ],
)
data class LearningPathEntity(
    @PrimaryKey val pathId: String,
    val title: String,
    val description: String,
    val targetLevel: SkillLevel,
    val topic: String,
    val nodeIds: List<String>,
    val estimatedMinutes: Int,
    val generatedBy: String = "knowledge_graph",
    val isSynced: Boolean = true,
    val updatedAt: Long = System.currentTimeMillis(),
) {
    fun toDomain(): LearningPath = LearningPath(
        pathId = pathId,
        title = title,
        description = description,
        targetLevel = targetLevel,
        topic = topic,
        nodeIds = nodeIds,
        estimatedMinutes = estimatedMinutes,
        generatedBy = generatedBy,
        updatedAt = updatedAt,
    )
}

fun KnowledgeNode.toEntity(isSynced: Boolean = true): KnowledgeNodeEntity = KnowledgeNodeEntity(
    nodeId = nodeId,
    name = name,
    type = type,
    description = description,
    category = category,
    aliases = aliases,
    tags = tags,
    verified = verified,
    source = source,
    isSynced = isSynced,
    updatedAt = updatedAt,
)

fun KnowledgeRelation.toEntity(isSynced: Boolean = true): KnowledgeRelationEntity = KnowledgeRelationEntity(
    relationId = relationId,
    sourceNodeId = sourceNodeId,
    targetNodeId = targetNodeId,
    type = type,
    description = description,
    confidence = confidence,
    verified = verified,
    createdBy = createdBy,
    isSynced = isSynced,
    updatedAt = updatedAt,
)

fun LearningPath.toEntity(isSynced: Boolean = true): LearningPathEntity = LearningPathEntity(
    pathId = pathId,
    title = title,
    description = description,
    targetLevel = targetLevel,
    topic = topic,
    nodeIds = nodeIds,
    estimatedMinutes = estimatedMinutes,
    generatedBy = generatedBy,
    isSynced = isSynced,
    updatedAt = updatedAt,
)
