package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.ExpertiseProfileEntity
import com.rola.app.data.database.entities.KnowledgeConnectionEntity
import com.rola.app.data.database.entities.LearningExperienceEntity
import com.rola.app.data.database.entities.LearningTimelineEntity
import com.rola.app.data.database.entities.LifelongMemoryEntity
import com.rola.app.data.database.entities.MemoryAnalyticsEntity
import com.rola.app.data.database.entities.MemoryHistoryEntity
import com.rola.app.data.database.entities.PersonalKnowledgeGraphEntity
import com.rola.app.data.database.entities.SkillEvolutionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LifelongMemoryDao {
    @Query("SELECT * FROM lifelong_memory WHERE userId = :userId ORDER BY rowid DESC LIMIT 1") fun observeMemory(userId: String): Flow<LifelongMemoryEntity?>
    @Query("SELECT * FROM personal_knowledge_graph ORDER BY rowid DESC LIMIT 1") fun observeGraph(): Flow<PersonalKnowledgeGraphEntity?>
    @Query("SELECT * FROM learning_timeline WHERE userId = :userId ORDER BY rowid DESC LIMIT 1") fun observeTimeline(userId: String): Flow<LearningTimelineEntity?>
    @Query("SELECT * FROM expertise_profile ORDER BY rowid DESC LIMIT 1") fun observeExpertise(): Flow<ExpertiseProfileEntity?>
    @Query("SELECT * FROM memory_analytics ORDER BY rowid DESC LIMIT 1") fun observeAnalytics(): Flow<MemoryAnalyticsEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertMemory(value: LifelongMemoryEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertGraph(value: PersonalKnowledgeGraphEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertExperience(value: LearningExperienceEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertEvolution(value: SkillEvolutionEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertHistory(value: MemoryHistoryEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertConnection(value: KnowledgeConnectionEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertTimeline(value: LearningTimelineEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertExpertise(value: ExpertiseProfileEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAnalytics(value: MemoryAnalyticsEntity)
}
