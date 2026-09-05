package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.EvolutionExperimentEntity
import com.rola.app.data.database.entities.FeedbackRecordEntity
import com.rola.app.data.database.entities.ImprovementActionEntity
import com.rola.app.data.database.entities.ModelVersionEntity
import com.rola.app.data.database.entities.PerformanceMetricEntity
import com.rola.app.data.database.entities.SelfEvolutionHistoryEntity
import com.rola.app.data.database.entities.SelfOptimizationResultEntity
import com.rola.app.data.database.entities.SystemGrowthAnalyticsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SelfEvolvingAIDao {
    @Query("SELECT * FROM self_ai_evolution_history ORDER BY rowid DESC LIMIT 1") fun observeHistory(): Flow<SelfEvolutionHistoryEntity?>
    @Query("SELECT * FROM performance_metrics ORDER BY rowid DESC LIMIT 1") fun observePerformance(): Flow<PerformanceMetricEntity?>
    @Query("SELECT * FROM improvement_actions ORDER BY rowid DESC LIMIT 1") fun observeImprovement(): Flow<ImprovementActionEntity?>
    @Query("SELECT * FROM model_versions ORDER BY rowid DESC LIMIT 1") fun observeModel(): Flow<ModelVersionEntity?>
    @Query("SELECT * FROM feedback_records ORDER BY rowid DESC LIMIT 1") fun observeFeedback(): Flow<FeedbackRecordEntity?>
    @Query("SELECT * FROM self_optimization_results ORDER BY rowid DESC LIMIT 1") fun observeOptimization(): Flow<SelfOptimizationResultEntity?>
    @Query("SELECT * FROM evolution_experiments ORDER BY rowid DESC LIMIT 1") fun observeExperiment(): Flow<EvolutionExperimentEntity?>
    @Query("SELECT * FROM system_growth_analytics ORDER BY rowid DESC LIMIT 1") fun observeGrowth(): Flow<SystemGrowthAnalyticsEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertHistory(value: SelfEvolutionHistoryEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertPerformance(value: PerformanceMetricEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertImprovement(value: ImprovementActionEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertModel(value: ModelVersionEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertFeedback(value: FeedbackRecordEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertOptimization(value: SelfOptimizationResultEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertExperiment(value: EvolutionExperimentEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertGrowth(value: SystemGrowthAnalyticsEntity)
}
