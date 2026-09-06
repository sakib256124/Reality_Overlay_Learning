package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.AIDigitalTwinEntity
import com.rola.app.data.database.entities.TwinAnalyticsEntity
import com.rola.app.data.database.entities.TwinExperimentResultEntity
import com.rola.app.data.database.entities.TwinLearningSessionEntity
import com.rola.app.data.database.entities.TwinModelEntity
import com.rola.app.data.database.entities.TwinPredictionRecordEntity
import com.rola.app.data.database.entities.TwinRealWorldDataEntity
import com.rola.app.data.database.entities.TwinSimulationHistoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DigitalTwinAIDao {
    @Query("SELECT * FROM ai_digital_twins ORDER BY rowid DESC LIMIT 1") fun observeTwin(): Flow<AIDigitalTwinEntity?>
    @Query("SELECT * FROM twin_models ORDER BY rowid DESC LIMIT 1") fun observeModel(): Flow<TwinModelEntity?>
    @Query("SELECT * FROM twin_simulation_history ORDER BY rowid DESC LIMIT 1") fun observeSimulation(): Flow<TwinSimulationHistoryEntity?>
    @Query("SELECT * FROM twin_real_world_data ORDER BY rowid DESC LIMIT 1") fun observeSync(): Flow<TwinRealWorldDataEntity?>
    @Query("SELECT * FROM twin_analytics ORDER BY rowid DESC LIMIT 1") fun observeAnalytics(): Flow<TwinAnalyticsEntity?>
    @Query("SELECT * FROM twin_experiment_results ORDER BY rowid DESC LIMIT 1") fun observeExperiment(): Flow<TwinExperimentResultEntity?>
    @Query("SELECT * FROM twin_learning_sessions ORDER BY rowid DESC LIMIT 1") fun observeLearningSession(): Flow<TwinLearningSessionEntity?>
    @Query("SELECT * FROM twin_prediction_records ORDER BY rowid DESC LIMIT 1") fun observePrediction(): Flow<TwinPredictionRecordEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertTwin(value: AIDigitalTwinEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertModel(value: TwinModelEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertSimulation(value: TwinSimulationHistoryEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertSync(value: TwinRealWorldDataEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertAnalytics(value: TwinAnalyticsEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertExperiment(value: TwinExperimentResultEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertLearningSession(value: TwinLearningSessionEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertPrediction(value: TwinPredictionRecordEntity)
}
