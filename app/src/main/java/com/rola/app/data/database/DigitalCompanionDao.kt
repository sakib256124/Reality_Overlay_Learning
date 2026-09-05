package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.CompanionAnalyticsEntity
import com.rola.app.data.database.entities.CompanionConversationEntity
import com.rola.app.data.database.entities.CompanionLearningGoalEntity
import com.rola.app.data.database.entities.CompanionMemoryEntity
import com.rola.app.data.database.entities.CompanionPersonalityEntity
import com.rola.app.data.database.entities.CompanionRecommendationEntity
import com.rola.app.data.database.entities.DigitalCompanionEntity
import com.rola.app.data.database.entities.RelationshipHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DigitalCompanionDao {
    @Query("SELECT * FROM digital_companions WHERE userId = :userId ORDER BY rowid DESC LIMIT 1")
    fun observeCompanion(userId: String): Flow<DigitalCompanionEntity?>

    @Query("SELECT * FROM companion_memory WHERE userId = :userId ORDER BY updatedAt DESC LIMIT 10")
    fun observeMemory(userId: String): Flow<List<CompanionMemoryEntity>>

    @Query("SELECT * FROM relationship_history WHERE userId = :userId ORDER BY rowid DESC LIMIT 1")
    fun observeRelationship(userId: String): Flow<RelationshipHistoryEntity?>

    @Query("SELECT * FROM companion_recommendations WHERE userId = :userId ORDER BY rowid DESC LIMIT 1")
    fun observeRecommendations(userId: String): Flow<CompanionRecommendationEntity?>

    @Query("SELECT * FROM companion_analytics WHERE userId = :userId ORDER BY rowid DESC LIMIT 1")
    fun observeAnalytics(userId: String): Flow<CompanionAnalyticsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCompanion(companion: DigitalCompanionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMemories(memories: List<CompanionMemoryEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertConversation(conversation: CompanionConversationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPersonality(personality: CompanionPersonalityEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertGoals(goals: List<CompanionLearningGoalEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRelationship(relationship: RelationshipHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRecommendation(recommendation: CompanionRecommendationEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAnalytics(analytics: CompanionAnalyticsEntity)
}

