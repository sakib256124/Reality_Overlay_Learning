package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.KnowledgeNodeEntity
import com.rola.app.data.database.entities.KnowledgeRelationEntity
import com.rola.app.data.database.entities.LearningPathEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KnowledgeGraphDao {
    @Query("SELECT COUNT(*) FROM knowledge_nodes")
    suspend fun countNodes(): Int

    @Query("SELECT * FROM knowledge_nodes ORDER BY category, name")
    fun observeNodes(): Flow<List<KnowledgeNodeEntity>>

    @Query("SELECT * FROM knowledge_relations ORDER BY type")
    fun observeRelations(): Flow<List<KnowledgeRelationEntity>>

    @Query("SELECT * FROM learning_paths ORDER BY targetLevel, topic")
    fun observeLearningPaths(): Flow<List<LearningPathEntity>>

    @Query("SELECT * FROM knowledge_nodes ORDER BY category, name")
    suspend fun getNodes(): List<KnowledgeNodeEntity>

    @Query("SELECT * FROM knowledge_relations ORDER BY type")
    suspend fun getRelations(): List<KnowledgeRelationEntity>

    @Query("SELECT * FROM learning_paths WHERE topic LIKE '%' || :topic || '%' ORDER BY targetLevel LIMIT :limit")
    suspend fun getLearningPaths(topic: String, limit: Int = 5): List<LearningPathEntity>

    @Query("SELECT * FROM knowledge_nodes WHERE nodeId = :nodeId LIMIT 1")
    suspend fun getNode(nodeId: String): KnowledgeNodeEntity?

    @Query(
        """
        SELECT * FROM knowledge_nodes
        WHERE name LIKE '%' || :query || '%'
            OR description LIKE '%' || :query || '%'
            OR category LIKE '%' || :query || '%'
            OR aliases LIKE '%' || :query || '%'
            OR tags LIKE '%' || :query || '%'
        ORDER BY verified DESC, name
        LIMIT :limit
        """,
    )
    suspend fun searchNodes(query: String, limit: Int = 12): List<KnowledgeNodeEntity>

    @Query(
        """
        SELECT * FROM knowledge_relations
        WHERE sourceNodeId = :nodeId OR targetNodeId = :nodeId
        ORDER BY confidence DESC
        LIMIT :limit
        """,
    )
    suspend fun getRelationsForNode(nodeId: String, limit: Int = 30): List<KnowledgeRelationEntity>

    @Query(
        """
        SELECT * FROM knowledge_relations
        WHERE relationId IN (:relationIds)
        """,
    )
    suspend fun getRelationsByIds(relationIds: List<String>): List<KnowledgeRelationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertNodes(nodes: List<KnowledgeNodeEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRelations(relations: List<KnowledgeRelationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLearningPaths(paths: List<LearningPathEntity>)
}
