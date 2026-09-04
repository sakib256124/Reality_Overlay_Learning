package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.AttentionRecordEntity
import com.rola.app.data.database.entities.BrainSignalEntity
import com.rola.app.data.database.entities.CognitiveReportEntity
import com.rola.app.data.database.entities.NeuralCognitiveStateEntity
import com.rola.app.data.database.entities.NeuralInteractionEntity
import com.rola.app.data.database.entities.NeuralLearningPredictionEntity
import com.rola.app.data.database.entities.NeuralLearningStateEntity
import com.rola.app.data.database.entities.NeuralProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface NeuralAIDao {
    @Query("SELECT * FROM neural_profiles WHERE userId = :userId ORDER BY updatedAt DESC LIMIT 1")
    fun observeProfile(userId: String): Flow<NeuralProfileEntity?>

    @Query("SELECT * FROM brain_signals WHERE userId = :userId ORDER BY timestamp DESC LIMIT :limit")
    suspend fun recentSignals(userId: String, limit: Int = 100): List<BrainSignalEntity>

    @Query("SELECT * FROM neural_cognitive_states WHERE userId = :userId ORDER BY timestamp DESC LIMIT 1")
    fun observeLatestCognitiveState(userId: String): Flow<NeuralCognitiveStateEntity?>

    @Query("SELECT * FROM neural_learning_states WHERE userId = :userId ORDER BY updatedAt DESC LIMIT 1")
    fun observeLatestLearningState(userId: String): Flow<NeuralLearningStateEntity?>

    @Query("SELECT * FROM neural_learning_predictions WHERE userId = :userId ORDER BY rowid DESC LIMIT 1")
    fun observeLatestPrediction(userId: String): Flow<NeuralLearningPredictionEntity?>

    @Query("SELECT * FROM neural_interactions WHERE userId = :userId ORDER BY timestamp DESC LIMIT :limit")
    fun observeRecentInteractions(userId: String, limit: Int = 20): Flow<List<NeuralInteractionEntity>>

    @Query("SELECT * FROM cognitive_reports WHERE userId = :userId ORDER BY createdAt DESC LIMIT :limit")
    fun observeReports(userId: String, limit: Int = 10): Flow<List<CognitiveReportEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: NeuralProfileEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertBrainSignal(signal: BrainSignalEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCognitiveState(state: NeuralCognitiveStateEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLearningState(state: NeuralLearningStateEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertInteraction(interaction: NeuralInteractionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAttentionRecord(record: AttentionRecordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPrediction(prediction: NeuralLearningPredictionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertReport(report: CognitiveReportEntity)
}
