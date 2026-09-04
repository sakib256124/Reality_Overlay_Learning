package com.rola.app.domain.repository

import com.rola.app.domain.model.LearningObject
import com.rola.app.domain.model.LearningStatus
import com.rola.app.domain.model.ScanHistory
import com.rola.app.domain.model.User
import kotlinx.coroutines.flow.Flow

interface LearningHistoryRepository {
    val currentUserId: String

    suspend fun saveUser(user: User)
    suspend fun saveLearningObject(learningObject: LearningObject)
    suspend fun saveScan(
        objectId: String,
        objectName: String,
        category: String,
        confidenceScore: Float,
        learningStatus: LearningStatus = LearningStatus.Scanned,
    )

    fun observeUserHistory(
        query: String = "",
        category: String? = null,
    ): Flow<List<ScanHistory>>

    fun observeHistoryCategories(): Flow<List<String>>
    suspend fun deleteScan(scanId: String)
    suspend fun deleteHistory()
    suspend fun syncNow()
}
