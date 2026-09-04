package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.ContentVersionEntity
import com.rola.app.data.database.entities.KnowledgeUpdateEntity
import com.rola.app.data.database.entities.LearningMaterialEntity
import com.rola.app.data.database.entities.ResearchTaskEntity
import com.rola.app.data.database.entities.ScientificSourceEntity
import com.rola.app.domain.model.KnowledgeUpdateStatus
import com.rola.app.domain.model.ResearchTaskStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface ResearchDao {
    @Query("SELECT * FROM research_tasks ORDER BY priority DESC, updatedAt DESC")
    fun observeResearchTasks(): Flow<List<ResearchTaskEntity>>

    @Query("SELECT * FROM knowledge_updates ORDER BY createdAt DESC")
    fun observeKnowledgeUpdates(): Flow<List<KnowledgeUpdateEntity>>

    @Query("SELECT * FROM scientific_sources ORDER BY trusted DESC, addedAt DESC")
    fun observeSources(): Flow<List<ScientificSourceEntity>>

    @Query("SELECT * FROM learning_materials ORDER BY updatedAt DESC")
    fun observeLearningMaterials(): Flow<List<LearningMaterialEntity>>

    @Query("SELECT * FROM research_tasks WHERE status = :status ORDER BY priority DESC, updatedAt DESC")
    suspend fun getTasksByStatus(status: ResearchTaskStatus): List<ResearchTaskEntity>

    @Query("SELECT * FROM knowledge_updates WHERE status = :status ORDER BY createdAt DESC")
    suspend fun getUpdatesByStatus(status: KnowledgeUpdateStatus): List<KnowledgeUpdateEntity>

    @Query("SELECT * FROM scientific_sources WHERE trusted = 1 ORDER BY addedAt DESC")
    suspend fun getTrustedSources(): List<ScientificSourceEntity>

    @Query("SELECT * FROM learning_materials WHERE topic LIKE '%' || :topic || '%' ORDER BY version DESC")
    suspend fun getMaterialsForTopic(topic: String): List<LearningMaterialEntity>

    @Query("SELECT * FROM knowledge_updates WHERE topic LIKE '%' || :topic || '%' ORDER BY version DESC")
    suspend fun getUpdatesForTopic(topic: String): List<KnowledgeUpdateEntity>

    @Query("SELECT COUNT(*) FROM research_tasks WHERE status = :status")
    suspend fun countTasksByStatus(status: ResearchTaskStatus): Int

    @Query("SELECT COUNT(*) FROM knowledge_updates WHERE status = :status")
    suspend fun countUpdatesByStatus(status: KnowledgeUpdateStatus): Int

    @Query("SELECT COUNT(*) FROM scientific_sources WHERE trusted = 1")
    suspend fun countTrustedSources(): Int

    @Query("SELECT COUNT(*) FROM learning_materials")
    suspend fun countLearningMaterials(): Int

    @Query("SELECT COUNT(*) FROM knowledge_updates WHERE status = 'AppliedToGraph'")
    suspend fun countAppliedUpdates(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertResearchTasks(tasks: List<ResearchTaskEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSources(sources: List<ScientificSourceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertKnowledgeUpdates(updates: List<KnowledgeUpdateEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLearningMaterials(materials: List<LearningMaterialEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertContentVersions(versions: List<ContentVersionEntity>)

    @Query("UPDATE research_tasks SET status = :status, updatedAt = :updatedAt WHERE taskId = :taskId")
    suspend fun updateTaskStatus(taskId: String, status: ResearchTaskStatus, updatedAt: Long = System.currentTimeMillis())

    @Query(
        """
        UPDATE knowledge_updates
        SET status = :status, approvedBy = :approvedBy, approvedAt = :approvedAt
        WHERE updateId = :updateId
        """,
    )
    suspend fun updateKnowledgeUpdateStatus(
        updateId: String,
        status: KnowledgeUpdateStatus,
        approvedBy: String?,
        approvedAt: Long?,
    )
}
