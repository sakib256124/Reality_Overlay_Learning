package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.FutureRoadmapEntity
import com.rola.app.data.database.entities.FutureSkillModelEntity
import com.rola.app.data.database.entities.GrowthAnalyticsEntity
import com.rola.app.data.database.entities.PotentialProfileEntity
import com.rola.app.data.database.entities.PredictionHistoryEntity
import com.rola.app.data.database.entities.PredictiveLearningPredictionEntity
import com.rola.app.data.database.entities.PredictiveOptimizationResultEntity
import com.rola.app.data.database.entities.TrendAnalysisEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PredictiveAIDao {
    @Query("SELECT * FROM predictive_learning_predictions WHERE userId = :userId ORDER BY rowid DESC LIMIT 1") fun observePrediction(userId: String): Flow<PredictiveLearningPredictionEntity?>
    @Query("SELECT * FROM future_skill_models ORDER BY rowid DESC LIMIT 1") fun observeSkills(): Flow<FutureSkillModelEntity?>
    @Query("SELECT * FROM potential_profiles ORDER BY rowid DESC LIMIT 1") fun observePotential(): Flow<PotentialProfileEntity?>
    @Query("SELECT * FROM future_roadmaps ORDER BY rowid DESC LIMIT 1") fun observeRoadmap(): Flow<FutureRoadmapEntity?>
    @Query("SELECT * FROM growth_analytics ORDER BY rowid DESC LIMIT 1") fun observeAnalytics(): Flow<GrowthAnalyticsEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertPrediction(value: PredictiveLearningPredictionEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertSkills(value: FutureSkillModelEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertPotential(value: PotentialProfileEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAnalytics(value: GrowthAnalyticsEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertRoadmap(value: FutureRoadmapEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertTrend(value: TrendAnalysisEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertHistory(value: PredictionHistoryEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertOptimization(value: PredictiveOptimizationResultEntity)
}
