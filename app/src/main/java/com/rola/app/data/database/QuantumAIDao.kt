package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.KnowledgeDiscoveryRecordEntity
import com.rola.app.data.database.entities.LearningOptimizationHistoryEntity
import com.rola.app.data.database.entities.OptimizationResultEntity
import com.rola.app.data.database.entities.QuantumAnalyticsEntity
import com.rola.app.data.database.entities.QuantumDecisionEntity
import com.rola.app.data.database.entities.QuantumModelEntity
import com.rola.app.data.database.entities.QuantumPredictionEntity
import com.rola.app.data.database.entities.QuantumProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuantumAIDao {
    @Query("SELECT * FROM optimization_results WHERE learnerId = :learnerId ORDER BY rowid DESC LIMIT 1")
    fun observeLatestOptimization(learnerId: String): Flow<OptimizationResultEntity?>

    @Query("SELECT * FROM quantum_predictions WHERE learnerId = :learnerId ORDER BY rowid DESC LIMIT 1")
    fun observeLatestPrediction(learnerId: String): Flow<QuantumPredictionEntity?>

    @Query("SELECT * FROM quantum_decisions WHERE learnerId = :learnerId ORDER BY rowid DESC LIMIT 10")
    fun observeRecentDecisions(learnerId: String): Flow<List<QuantumDecisionEntity>>

    @Query("SELECT * FROM knowledge_discovery_records ORDER BY rowid DESC LIMIT 10")
    fun observeKnowledgeDiscovery(): Flow<List<KnowledgeDiscoveryRecordEntity>>

    @Query("SELECT * FROM quantum_analytics WHERE institutionId = :institutionId ORDER BY rowid DESC LIMIT 1")
    fun observeLatestAnalytics(institutionId: String): Flow<QuantumAnalyticsEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertProfile(profile: QuantumProfileEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertModel(model: QuantumModelEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertOptimization(result: OptimizationResultEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDecision(decision: QuantumDecisionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertOptimizationHistory(history: LearningOptimizationHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertPrediction(prediction: QuantumPredictionEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertKnowledgeDiscovery(record: KnowledgeDiscoveryRecordEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAnalytics(report: QuantumAnalyticsEntity)
}
