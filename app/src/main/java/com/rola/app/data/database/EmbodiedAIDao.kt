package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.RobotAnalyticsEntity
import com.rola.app.data.database.entities.RobotClassroomSessionEntity
import com.rola.app.data.database.entities.RobotInteractionEntity
import com.rola.app.data.database.entities.RobotMemoryEntity
import com.rola.app.data.database.entities.RobotProfileEntity
import com.rola.app.data.database.entities.RobotSessionEntity
import com.rola.app.data.database.entities.RobotTeachingActivityEntity
import com.rola.app.data.database.entities.StudentRobotHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmbodiedAIDao {
    @Query("SELECT * FROM robot_profiles ORDER BY name")
    fun observeRobots(): Flow<List<RobotProfileEntity>>

    @Query("SELECT * FROM robot_sessions WHERE status != 'Completed' ORDER BY updatedAt DESC")
    fun observeActiveSessions(): Flow<List<RobotSessionEntity>>

    @Query("SELECT * FROM robot_interactions ORDER BY timestamp DESC LIMIT :limit")
    fun observeRecentInteractions(limit: Int = 20): Flow<List<RobotInteractionEntity>>

    @Query("SELECT * FROM robot_analytics ORDER BY reportId DESC LIMIT :limit")
    fun observeRobotAnalytics(limit: Int = 20): Flow<List<RobotAnalyticsEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRobot(robot: RobotProfileEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSession(session: RobotSessionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertInteraction(interaction: RobotInteractionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMemory(memory: RobotMemoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTeachingActivity(activity: RobotTeachingActivityEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertClassroomSession(session: RobotClassroomSessionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAnalytics(analytics: RobotAnalyticsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStudentHistory(history: StudentRobotHistoryEntity)
}
