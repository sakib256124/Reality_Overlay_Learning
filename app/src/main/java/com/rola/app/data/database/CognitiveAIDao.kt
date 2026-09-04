package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.BehaviorAnalyticsEntity
import com.rola.app.data.database.entities.CognitiveAIDecisionEntity
import com.rola.app.data.database.entities.CognitiveActivityEntity
import com.rola.app.data.database.entities.CognitiveLearnerModelEntity
import com.rola.app.data.database.entities.CognitiveMemoryRecordEntity
import com.rola.app.data.database.entities.CognitiveProfileEntity
import com.rola.app.data.database.entities.CognitiveSkillMapEntity
import com.rola.app.data.database.entities.EmotionAnalyticsEntity
import com.rola.app.data.database.entities.LearningPatternEntity
import com.rola.app.data.database.entities.LearningPredictionEntity
import com.rola.app.data.database.entities.PersonalLearningPlanEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CognitiveAIDao {
    @Query("SELECT * FROM cognitive_profiles WHERE userId = :userId ORDER BY updatedAt DESC LIMIT 1")
    fun observeProfile(userId: String): Flow<CognitiveProfileEntity?>

    @Query("SELECT * FROM cognitive_skill_maps WHERE userId = :userId ORDER BY mastery ASC")
    fun observeSkillMap(userId: String): Flow<List<CognitiveSkillMapEntity>>

    @Query("SELECT * FROM cognitive_ai_decisions WHERE userId = :userId")
    fun observeDecisions(userId: String): Flow<List<CognitiveAIDecisionEntity>>

    @Query("SELECT * FROM cognitive_activity_events WHERE userId = :userId ORDER BY timestamp DESC LIMIT :limit")
    suspend fun recentActivities(userId: String, limit: Int = 100): List<CognitiveActivityEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: CognitiveProfileEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLearnerModel(model: CognitiveLearnerModelEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMemoryRecords(records: List<CognitiveMemoryRecordEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLearningPatterns(patterns: List<LearningPatternEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBehaviorAnalytics(report: BehaviorAnalyticsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertEmotionAnalytics(report: EmotionAnalyticsEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSkillMaps(skills: List<CognitiveSkillMapEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPrediction(prediction: LearningPredictionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPersonalPlan(plan: PersonalLearningPlanEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDecision(decision: CognitiveAIDecisionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertActivities(activities: List<CognitiveActivityEntity>)
}
