package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.AIReasoningHistoryEntity
import com.rola.app.data.database.entities.CriticalThinkingAnalyticsEntity
import com.rola.app.data.database.entities.ExplanationRecordEntity
import com.rola.app.data.database.entities.InferenceRecordEntity
import com.rola.app.data.database.entities.ProblemSolutionEntity
import com.rola.app.data.database.entities.ReasoningImprovementEntity
import com.rola.app.data.database.entities.ReasoningProfileEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReasoningAIDao {
    @Query("SELECT * FROM reasoning_profiles ORDER BY rowid DESC LIMIT 1") fun observeProfile(): Flow<ReasoningProfileEntity?>
    @Query("SELECT * FROM problem_solutions ORDER BY rowid DESC LIMIT 1") fun observeSolution(): Flow<ProblemSolutionEntity?>
    @Query("SELECT * FROM explanation_records ORDER BY rowid DESC LIMIT 1") fun observeExplanation(): Flow<ExplanationRecordEntity?>
    @Query("SELECT * FROM critical_thinking_analytics ORDER BY rowid DESC LIMIT 1") fun observeCritical(): Flow<CriticalThinkingAnalyticsEntity?>
    @Query("SELECT * FROM reasoning_improvements ORDER BY rowid DESC LIMIT 1") fun observeImprovement(): Flow<ReasoningImprovementEntity?>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertProfile(value: ReasoningProfileEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertHistory(value: AIReasoningHistoryEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertSolution(value: ProblemSolutionEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertInference(value: InferenceRecordEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertExplanation(value: ExplanationRecordEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertCritical(value: CriticalThinkingAnalyticsEntity)
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun upsertImprovement(value: ReasoningImprovementEntity)
}
