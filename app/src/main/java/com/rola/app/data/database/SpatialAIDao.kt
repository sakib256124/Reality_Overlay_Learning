package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.DigitalTwinEntity
import com.rola.app.data.database.entities.LearningEnvironmentEntity
import com.rola.app.data.database.entities.Spatial3DAssetEntity
import com.rola.app.data.database.entities.SpatialInteractionHistoryEntity
import com.rola.app.data.database.entities.SpatialSessionEntity
import com.rola.app.data.database.entities.SpatialSimulationEntity
import com.rola.app.data.database.entities.SpatialWorldEntity
import com.rola.app.data.database.entities.VirtualClassroomEntity
import com.rola.app.data.database.entities.VirtualLessonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SpatialAIDao {
    @Query("SELECT * FROM spatial_worlds ORDER BY createdAt DESC")
    fun observeSpatialWorlds(): Flow<List<SpatialWorldEntity>>

    @Query("SELECT * FROM digital_twins ORDER BY name")
    fun observeDigitalTwins(): Flow<List<DigitalTwinEntity>>

    @Query("SELECT * FROM spatial_sessions WHERE learnerId = :learnerId ORDER BY updatedAt DESC LIMIT 20")
    fun observeSessions(learnerId: String): Flow<List<SpatialSessionEntity>>

    @Query("SELECT * FROM interaction_history WHERE sessionId = :sessionId ORDER BY timestamp DESC")
    suspend fun interactionsForSession(sessionId: String): List<SpatialInteractionHistoryEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWorld(world: SpatialWorldEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDigitalTwin(twin: DigitalTwinEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertVirtualClassroom(classroom: VirtualClassroomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertVirtualLessons(lessons: List<VirtualLessonEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSimulations(simulations: List<SpatialSimulationEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSession(session: SpatialSessionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertInteraction(event: SpatialInteractionHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLearningEnvironment(environment: LearningEnvironmentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAsset(asset: Spatial3DAssetEntity)
}
