package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.ScanHistoryEntity
import com.rola.app.data.database.entities.ScanHistoryWithObject
import kotlinx.coroutines.flow.Flow

@Dao
interface ScanHistoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScan(scan: ScanHistoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertScans(scans: List<ScanHistoryEntity>)

    @Query(
        """
        SELECT scan_history.scanId,
            scan_history.userId,
            scan_history.objectId,
            learning_objects.name AS objectName,
            learning_objects.category AS category,
            scan_history.timestamp,
            scan_history.confidenceScore,
            scan_history.learningStatus,
            learning_objects.imageUrl AS imageUrl
        FROM scan_history
        INNER JOIN learning_objects ON scan_history.objectId = learning_objects.objectId
        WHERE scan_history.userId = :userId
        ORDER BY scan_history.timestamp DESC
        """,
    )
    fun getUserHistory(userId: String): Flow<List<ScanHistoryWithObject>>

    @Query(
        """
        SELECT scan_history.scanId,
            scan_history.userId,
            scan_history.objectId,
            learning_objects.name AS objectName,
            learning_objects.category AS category,
            scan_history.timestamp,
            scan_history.confidenceScore,
            scan_history.learningStatus,
            learning_objects.imageUrl AS imageUrl
        FROM scan_history
        INNER JOIN learning_objects ON scan_history.objectId = learning_objects.objectId
        WHERE scan_history.userId = :userId
            AND (
                learning_objects.name LIKE '%' || :query || '%'
                OR learning_objects.category LIKE '%' || :query || '%'
                OR learning_objects.scientificName LIKE '%' || :query || '%'
            )
            AND (:category IS NULL OR learning_objects.category = :category)
        ORDER BY scan_history.timestamp DESC
        """,
    )
    fun searchUserHistory(
        userId: String,
        query: String,
        category: String?,
    ): Flow<List<ScanHistoryWithObject>>

    @Query("SELECT DISTINCT learning_objects.category FROM scan_history INNER JOIN learning_objects ON scan_history.objectId = learning_objects.objectId WHERE scan_history.userId = :userId ORDER BY learning_objects.category ASC")
    fun observeHistoryCategories(userId: String): Flow<List<String>>

    @Query("DELETE FROM scan_history WHERE userId = :userId")
    suspend fun deleteHistory(userId: String)

    @Query("DELETE FROM scan_history WHERE scanId = :scanId AND userId = :userId")
    suspend fun deleteScan(userId: String, scanId: String)

    @Query("SELECT * FROM scan_history WHERE isSynced = 0")
    suspend fun getUnsyncedScans(): List<ScanHistoryEntity>

    @Query("SELECT * FROM scan_history WHERE userId = :userId")
    suspend fun getHistorySnapshot(userId: String): List<ScanHistoryEntity>

    @Query(
        """
        SELECT scan_history.scanId,
            scan_history.userId,
            scan_history.objectId,
            learning_objects.name AS objectName,
            learning_objects.category AS category,
            scan_history.timestamp,
            scan_history.confidenceScore,
            scan_history.learningStatus,
            learning_objects.imageUrl AS imageUrl
        FROM scan_history
        INNER JOIN learning_objects ON scan_history.objectId = learning_objects.objectId
        WHERE scan_history.userId = :userId
        ORDER BY scan_history.timestamp DESC
        """,
    )
    suspend fun getUserHistoryWithObjectsSnapshot(userId: String): List<ScanHistoryWithObject>

    @Query("UPDATE scan_history SET isSynced = 1 WHERE scanId = :scanId")
    suspend fun markScanSynced(scanId: String)
}
