package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.EmotionLearningPatternEntity
import com.rola.app.data.database.entities.EmotionalAIAnalyticsEntity
import com.rola.app.data.database.entities.EmotionalAIProfileEntity
import com.rola.app.data.database.entities.EngagementHistoryEntity
import com.rola.app.data.database.entities.LearnerEmotionStateEntity
import com.rola.app.data.database.entities.MotivationRecordEntity
import com.rola.app.data.database.entities.SupportRecommendationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface EmotionalAIDao {
    @Query("SELECT * FROM learner_emotion_states ORDER BY rowid DESC LIMIT 1") fun observeState(): Flow<LearnerEmotionStateEntity?>
    @Query("SELECT * FROM motivation_records ORDER BY rowid DESC LIMIT 1") fun observeMotivation(): Flow<MotivationRecordEntity?>
    @Query("SELECT * FROM engagement_history ORDER BY rowid DESC LIMIT 1") fun observeEngagement(): Flow<EngagementHistoryEntity?>
    @Query("SELECT * FROM emotional_analytics ORDER BY rowid DESC LIMIT 1") fun observeAnalytics(): Flow<EmotionalAIAnalyticsEntity?>
    @Query("SELECT * FROM support_recommendations ORDER BY rowid DESC LIMIT 1") fun observeSupport(): Flow<SupportRecommendationEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertProfile(value: EmotionalAIProfileEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertState(value: LearnerEmotionStateEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertMotivation(value: MotivationRecordEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertEngagement(value: EngagementHistoryEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAnalytics(value: EmotionalAIAnalyticsEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertSupport(value: SupportRecommendationEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertPattern(value: EmotionLearningPatternEntity)
}
