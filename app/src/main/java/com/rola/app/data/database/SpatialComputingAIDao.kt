package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.SpatialComputingAnalyticsEntity
import com.rola.app.data.database.entities.SpatialComputingEnvironmentEntity
import com.rola.app.data.database.entities.SpatialComputingEnvironmentModelEntity
import com.rola.app.data.database.entities.SpatialComputingImmersiveSessionEntity
import com.rola.app.data.database.entities.SpatialComputingInteractionEntity
import com.rola.app.data.database.entities.SpatialComputingLearningExperienceEntity
import com.rola.app.data.database.entities.SpatialComputingObjectEntity
import com.rola.app.data.database.entities.SpatialComputingVirtualClassroomEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SpatialComputingAIDao {
    @Query("SELECT * FROM spatial_computing_environments ORDER BY rowid DESC LIMIT 1") fun observeEnvironment(): Flow<SpatialComputingEnvironmentEntity?>
    @Query("SELECT * FROM spatial_computing_objects ORDER BY rowid DESC LIMIT 1") fun observeObject(): Flow<SpatialComputingObjectEntity?>
    @Query("SELECT * FROM spatial_computing_immersive_sessions ORDER BY rowid DESC LIMIT 1") fun observeSession(): Flow<SpatialComputingImmersiveSessionEntity?>
    @Query("SELECT * FROM spatial_computing_interactions ORDER BY rowid DESC LIMIT 1") fun observeInteraction(): Flow<SpatialComputingInteractionEntity?>
    @Query("SELECT * FROM spatial_computing_virtual_classrooms ORDER BY rowid DESC LIMIT 1") fun observeClassroom(): Flow<SpatialComputingVirtualClassroomEntity?>
    @Query("SELECT * FROM spatial_computing_analytics ORDER BY rowid DESC LIMIT 1") fun observeAnalytics(): Flow<SpatialComputingAnalyticsEntity?>
    @Query("SELECT * FROM spatial_computing_environment_models ORDER BY rowid DESC LIMIT 1") fun observeModel(): Flow<SpatialComputingEnvironmentModelEntity?>
    @Query("SELECT * FROM spatial_computing_learning_experiences ORDER BY rowid DESC LIMIT 1") fun observeLearningExperience(): Flow<SpatialComputingLearningExperienceEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertEnvironment(value: SpatialComputingEnvironmentEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertObject(value: SpatialComputingObjectEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertSession(value: SpatialComputingImmersiveSessionEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertInteraction(value: SpatialComputingInteractionEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertClassroom(value: SpatialComputingVirtualClassroomEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAnalytics(value: SpatialComputingAnalyticsEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertModel(value: SpatialComputingEnvironmentModelEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertLearningExperience(value: SpatialComputingLearningExperienceEntity)
}
