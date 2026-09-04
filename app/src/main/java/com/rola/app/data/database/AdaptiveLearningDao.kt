package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.LearningProfileEntity
import com.rola.app.data.database.entities.RecommendationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface AdaptiveLearningDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: LearningProfileEntity)

    @Query("SELECT * FROM learning_profiles WHERE userId = :userId LIMIT 1")
    suspend fun getProfile(userId: String): LearningProfileEntity?

    @Query("SELECT * FROM learning_profiles WHERE userId = :userId LIMIT 1")
    fun observeProfile(userId: String): Flow<LearningProfileEntity?>

    @Query("SELECT * FROM learning_profiles WHERE isSynced = 0")
    suspend fun getUnsyncedProfiles(): List<LearningProfileEntity>

    @Query("UPDATE learning_profiles SET isSynced = 1 WHERE userId = :userId")
    suspend fun markProfileSynced(userId: String)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRecommendations(recommendations: List<RecommendationEntity>)

    @Query(
        """
        SELECT * FROM recommendations
        WHERE userId = :userId
        ORDER BY completed ASC,
            CASE priority
                WHEN 'High' THEN 0
                WHEN 'Medium' THEN 1
                ELSE 2
            END ASC,
            createdAt DESC
        """,
    )
    fun observeRecommendations(userId: String): Flow<List<RecommendationEntity>>

    @Query("SELECT * FROM recommendations WHERE userId = :userId ORDER BY createdAt DESC")
    suspend fun getRecommendations(userId: String): List<RecommendationEntity>

    @Query("SELECT * FROM recommendations WHERE isSynced = 0")
    suspend fun getUnsyncedRecommendations(): List<RecommendationEntity>

    @Query("UPDATE recommendations SET completed = 1, isSynced = 0, updatedAt = :timestamp WHERE recommendationId = :recommendationId")
    suspend fun markRecommendationCompleted(recommendationId: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE recommendations SET isSynced = 1 WHERE recommendationId = :recommendationId")
    suspend fun markRecommendationSynced(recommendationId: String)

    @Query("DELETE FROM recommendations WHERE userId = :userId AND completed = 0")
    suspend fun deleteOpenRecommendations(userId: String)
}
