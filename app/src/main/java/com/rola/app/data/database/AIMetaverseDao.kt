package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.MetaverseAnalyticsEntity
import com.rola.app.data.database.entities.MetaverseAvatarInteractionEntity
import com.rola.app.data.database.entities.MetaverseCommunitySpaceEntity
import com.rola.app.data.database.entities.MetaverseDigitalSpaceEntity
import com.rola.app.data.database.entities.MetaverseLearningAvatarEntity
import com.rola.app.data.database.entities.MetaverseSessionEntity
import com.rola.app.data.database.entities.MetaverseVirtualClassroomEntity
import com.rola.app.data.database.entities.MetaverseVirtualExperimentEntity
import com.rola.app.data.database.entities.MetaverseVirtualWorldEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AIMetaverseDao {
    @Query("SELECT * FROM metaverse_virtual_worlds WHERE institutionId = :institutionId ORDER BY rowid DESC LIMIT 1")
    fun observeLatestWorld(institutionId: String): Flow<MetaverseVirtualWorldEntity?>

    @Query("SELECT * FROM metaverse_learning_avatars WHERE learnerId = :learnerId ORDER BY rowid DESC LIMIT 1")
    fun observeAvatar(learnerId: String): Flow<MetaverseLearningAvatarEntity?>

    @Query("SELECT * FROM metaverse_virtual_classrooms ORDER BY rowid DESC LIMIT 10")
    fun observeClassrooms(): Flow<List<MetaverseVirtualClassroomEntity>>

    @Query("SELECT * FROM metaverse_community_spaces ORDER BY rowid DESC LIMIT 10")
    fun observeCommunities(): Flow<List<MetaverseCommunitySpaceEntity>>

    @Query("SELECT * FROM metaverse_analytics WHERE learnerId = :learnerId ORDER BY rowid DESC LIMIT 1")
    fun observeAnalytics(learnerId: String): Flow<MetaverseAnalyticsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertWorld(world: MetaverseVirtualWorldEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSpaces(spaces: List<MetaverseDigitalSpaceEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAvatar(avatar: MetaverseLearningAvatarEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertClassroom(classroom: MetaverseVirtualClassroomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSession(session: MetaverseSessionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertInteraction(interaction: MetaverseAvatarInteractionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertExperiment(experiment: MetaverseVirtualExperimentEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCommunity(community: MetaverseCommunitySpaceEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAnalytics(report: MetaverseAnalyticsEntity)
}

