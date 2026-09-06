package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.VirtualCampusAIAvatarEntity
import com.rola.app.data.database.entities.VirtualCampusAnalyticsEntity
import com.rola.app.data.database.entities.VirtualCampusClassroomEntity
import com.rola.app.data.database.entities.VirtualCampusCollaborationSessionEntity
import com.rola.app.data.database.entities.VirtualCampusEntity
import com.rola.app.data.database.entities.VirtualCampusLabEntity
import com.rola.app.data.database.entities.VirtualCampusLearningActivityEntity
import com.rola.app.data.database.entities.VirtualCampusUserEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface VirtualCampusAIDao {
    @Query("SELECT * FROM virtual_campus_campuses ORDER BY rowid DESC LIMIT 1") fun observeCampus(): Flow<VirtualCampusEntity?>
    @Query("SELECT * FROM virtual_campus_classrooms ORDER BY rowid DESC LIMIT 1") fun observeClassroom(): Flow<VirtualCampusClassroomEntity?>
    @Query("SELECT * FROM virtual_campus_ai_avatars ORDER BY rowid DESC LIMIT 1") fun observeAvatar(): Flow<VirtualCampusAIAvatarEntity?>
    @Query("SELECT * FROM virtual_campus_users ORDER BY rowid DESC LIMIT 1") fun observeUser(): Flow<VirtualCampusUserEntity?>
    @Query("SELECT * FROM virtual_campus_labs ORDER BY rowid DESC LIMIT 1") fun observeLab(): Flow<VirtualCampusLabEntity?>
    @Query("SELECT * FROM virtual_campus_collaboration_sessions ORDER BY rowid DESC LIMIT 1") fun observeCollaboration(): Flow<VirtualCampusCollaborationSessionEntity?>
    @Query("SELECT * FROM virtual_campus_analytics ORDER BY rowid DESC LIMIT 1") fun observeAnalytics(): Flow<VirtualCampusAnalyticsEntity?>
    @Query("SELECT * FROM virtual_campus_learning_activities ORDER BY rowid DESC LIMIT 1") fun observeActivity(): Flow<VirtualCampusLearningActivityEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertCampus(value: VirtualCampusEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertClassroom(value: VirtualCampusClassroomEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAvatar(value: VirtualCampusAIAvatarEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertUser(value: VirtualCampusUserEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertLab(value: VirtualCampusLabEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertCollaboration(value: VirtualCampusCollaborationSessionEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAnalytics(value: VirtualCampusAnalyticsEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertActivity(value: VirtualCampusLearningActivityEntity)
}
