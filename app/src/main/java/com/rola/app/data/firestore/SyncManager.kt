package com.rola.app.data.firestore

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import androidx.core.content.getSystemService
import com.rola.app.data.database.AdaptiveLearningDao
import com.rola.app.data.database.ObjectDao
import com.rola.app.data.database.QuizResultDao
import com.rola.app.data.database.ScanHistoryDao
import com.rola.app.data.database.UserDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SyncManager @Inject constructor(
    private val userDao: UserDao,
    private val objectDao: ObjectDao,
    private val scanHistoryDao: ScanHistoryDao,
    private val quizResultDao: QuizResultDao,
    private val adaptiveLearningDao: AdaptiveLearningDao,
    private val firestoreService: FirestoreService,
    @dagger.hilt.android.qualifiers.ApplicationContext private val context: Context,
) {
    private val syncScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val syncMutex = Mutex()
    private var isNetworkCallbackRegistered = false

    fun startAutoSync(userIdProvider: () -> String?) {
        if (isNetworkCallbackRegistered) return
        val connectivityManager = context.getSystemService<ConnectivityManager>() ?: return
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                val userId = userIdProvider() ?: return
                syncScope.launch { sync(userId) }
            }
        })
        isNetworkCallbackRegistered = true
    }

    suspend fun sync(userId: String): Result<Unit> = runCatching {
        syncMutex.withLock {
            uploadLocalChanges()
            downloadCloudHistory(userId)
        }
    }

    private suspend fun uploadLocalChanges() {
        userDao.getUnsyncedUsers().forEach { user ->
            firestoreService.uploadUser(user)
            userDao.markUserSynced(user.userId)
        }

        objectDao.getUnsyncedObjects().forEach { objectEntity ->
            firestoreService.uploadObject(objectEntity)
            objectDao.markObjectSynced(objectEntity.objectId)
        }

        scanHistoryDao.getUnsyncedScans().forEach { scan ->
            firestoreService.uploadScan(scan)
            scanHistoryDao.markScanSynced(scan.scanId)
        }

        quizResultDao.getUnsyncedResults().forEach { result ->
            firestoreService.uploadQuizResult(result)
            quizResultDao.markResultSynced(result.resultId)
        }

        adaptiveLearningDao.getUnsyncedProfiles().forEach { profile ->
            firestoreService.uploadLearningProfile(profile)
            adaptiveLearningDao.markProfileSynced(profile.userId)
        }

        adaptiveLearningDao.getUnsyncedRecommendations().forEach { recommendation ->
            firestoreService.uploadRecommendation(recommendation)
            adaptiveLearningDao.markRecommendationSynced(recommendation.recommendationId)
        }
    }

    private suspend fun downloadCloudHistory(userId: String) {
        val remoteScans = firestoreService.downloadUserScans(userId)
        val remoteObjectIds = remoteScans.map { it.objectId }.toSet()
        val remoteObjects = firestoreService.downloadObjects(remoteObjectIds)

        if (remoteObjects.isNotEmpty()) {
            objectDao.insertObjects(remoteObjects)
        }

        val localById = scanHistoryDao.getHistorySnapshot(userId).associateBy { it.scanId }
        val mergedScans = remoteScans.filter { remote ->
            val local = localById[remote.scanId]
            local == null || remote.updatedAt >= local.updatedAt
        }

        if (mergedScans.isNotEmpty()) {
            scanHistoryDao.insertScans(mergedScans)
        }

        val remoteResults = firestoreService.downloadQuizResults(userId)
        val localResultsById = quizResultDao.getResultsSnapshot(userId).associateBy { it.resultId }
        val mergedResults = remoteResults.filter { remote ->
            val local = localResultsById[remote.resultId]
            local == null || remote.updatedAt >= local.updatedAt
        }

        if (mergedResults.isNotEmpty()) {
            quizResultDao.insertResults(mergedResults)
        }
    }
}
