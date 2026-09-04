package com.rola.app.data.repository

import com.rola.app.data.database.ObjectDao
import com.rola.app.data.database.ScanHistoryDao
import com.rola.app.data.database.UserDao
import com.rola.app.data.database.entities.ObjectEntity
import com.rola.app.data.database.entities.ScanHistoryEntity
import com.rola.app.data.database.entities.toEntity
import com.rola.app.data.firestore.FirestoreService
import com.rola.app.data.firestore.SyncManager
import com.rola.app.domain.model.LearningObject
import com.rola.app.domain.model.LearningStatus
import com.rola.app.domain.model.ScanHistory
import com.rola.app.domain.model.User
import com.rola.app.domain.repository.LearningHistoryRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Singleton
class LearningHistoryRepositoryImpl @Inject constructor(
    private val userDao: UserDao,
    private val objectDao: ObjectDao,
    private val scanHistoryDao: ScanHistoryDao,
    private val firestoreService: FirestoreService,
    private val syncManager: SyncManager,
) : LearningHistoryRepository {
    private val syncScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override val currentUserId: String
        get() = firestoreService.currentUserId ?: LOCAL_USER_ID

    init {
        syncManager.startAutoSync { firestoreService.currentUserId }
    }

    override suspend fun saveUser(user: User) {
        userDao.insertUser(user.toEntity(isSynced = false))
        scheduleSync()
    }

    override suspend fun saveLearningObject(learningObject: LearningObject) {
        objectDao.insertObject(learningObject.toEntity(isSynced = false))
        scheduleSync()
    }

    override suspend fun saveScan(
        objectId: String,
        objectName: String,
        category: String,
        confidenceScore: Float,
        learningStatus: LearningStatus,
    ) {
        ensureLocalUser()
        ensureLearningObject(objectId = objectId, objectName = objectName, category = category)

        scanHistoryDao.insertScan(
            ScanHistoryEntity(
                scanId = UUID.randomUUID().toString(),
                userId = currentUserId,
                objectId = objectId,
                timestamp = System.currentTimeMillis(),
                confidenceScore = confidenceScore.coerceIn(0f, 1f),
                learningStatus = learningStatus,
                isSynced = false,
            ),
        )
        scheduleSync()
    }

    override fun observeUserHistory(
        query: String,
        category: String?,
    ): Flow<List<ScanHistory>> =
        scanHistoryDao.searchUserHistory(
            userId = currentUserId,
            query = query,
            category = category?.takeIf { it.isNotBlank() },
        ).map { rows -> rows.map { it.toDomain() } }

    override fun observeHistoryCategories(): Flow<List<String>> =
        scanHistoryDao.observeHistoryCategories(currentUserId)

    override suspend fun deleteScan(scanId: String) {
        val userId = currentUserId
        scanHistoryDao.deleteScan(userId, scanId)
        if (firestoreService.currentUserId != null) {
            runCatching { firestoreService.deleteScan(scanId) }
        }
    }

    override suspend fun deleteHistory() {
        val userId = currentUserId
        scanHistoryDao.deleteHistory(userId)
        if (firestoreService.currentUserId != null) {
            runCatching { firestoreService.deleteUserHistory(userId) }
        }
    }

    override suspend fun syncNow() {
        val userId = firestoreService.currentUserId ?: return
        syncManager.sync(userId).getOrThrow()
    }

    private suspend fun ensureLocalUser() {
        val userId = currentUserId
        if (userDao.getUser(userId) != null) return

        userDao.insertUser(
            com.rola.app.data.database.entities.UserEntity(
                userId = userId,
                name = if (userId == LOCAL_USER_ID) "Offline Learner" else "ROLA Learner",
                email = "",
                profileImage = "",
                isSynced = false,
            ),
        )
    }

    private suspend fun ensureLearningObject(
        objectId: String,
        objectName: String,
        category: String,
    ) {
        if (objectDao.getObjectById(objectId) != null) return

        objectDao.insertObject(defaultLearningObject(objectId, objectName, category))
    }

    private fun defaultLearningObject(
        objectId: String,
        objectName: String,
        category: String,
    ): ObjectEntity {
        fallbackObjects[objectId]?.let { return it.copy(isSynced = false) }

        return ObjectEntity(
            objectId = objectId,
            name = objectName,
            category = category.ifBlank { "Recognized Object" },
            scientificName = "",
            description = "Object recognized by the AR scanner.",
            uses = emptyList(),
            facts = emptyList(),
            imageUrl = "",
            isSynced = false,
        )
    }

    private fun scheduleSync() {
        if (firestoreService.currentUserId == null) return
        syncScope.launch {
            runCatching { syncManager.sync(currentUserId) }
        }
    }

    private companion object {
        const val LOCAL_USER_ID = "local_user"

        val fallbackObjects = listOf(
            ObjectEntity(
                objectId = "bottle",
                name = "Bottle",
                category = "Plastic Object",
                scientificName = "Polyethylene terephthalate container",
                description = "A lightweight container commonly used to store and transport liquids.",
                uses = listOf("Storage", "Transportation", "Measured dispensing"),
                facts = listOf("PET is recyclable.", "Clear bottles are often made from PET polymer."),
                imageUrl = "",
            ),
            ObjectEntity(
                objectId = "book",
                name = "Book",
                category = "Learning Object",
                scientificName = "Printed cellulose fiber medium",
                description = "A bound collection of pages used to preserve and share information.",
                uses = listOf("Reading", "Reference", "Education"),
                facts = listOf("Paper is mostly cellulose fiber.", "Books can last for centuries when stored carefully."),
                imageUrl = "",
            ),
            ObjectEntity(
                objectId = "cup",
                name = "Cup",
                category = "Household Object",
                scientificName = "Drinking vessel",
                description = "A small open container designed for holding drinks.",
                uses = listOf("Drinking", "Measuring small amounts", "Serving"),
                facts = listOf("Cups may be ceramic, glass, plastic, metal, or paper."),
                imageUrl = "",
            ),
        ).associateBy { it.objectId }
    }
}
