package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.QuizResultEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface QuizResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: QuizResultEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResults(results: List<QuizResultEntity>)

    @Query("SELECT * FROM quiz_results WHERE userId = :userId ORDER BY timestamp DESC")
    fun observeResults(userId: String): Flow<List<QuizResultEntity>>

    @Query("SELECT * FROM quiz_results WHERE userId = :userId")
    suspend fun getResultsSnapshot(userId: String): List<QuizResultEntity>

    @Query("SELECT * FROM quiz_results WHERE isSynced = 0")
    suspend fun getUnsyncedResults(): List<QuizResultEntity>

    @Query("UPDATE quiz_results SET isSynced = 1 WHERE resultId = :resultId")
    suspend fun markResultSynced(resultId: String)
}
