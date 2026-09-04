package com.rola.app.data.firestore

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.rola.app.data.database.entities.LearningProfileEntity
import com.rola.app.data.database.entities.ObjectEntity
import com.rola.app.data.database.entities.QuizResultEntity
import com.rola.app.data.database.entities.RecommendationEntity
import com.rola.app.data.database.entities.ScanHistoryEntity
import com.rola.app.data.database.entities.UserEntity
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreService @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val firebaseAuth: FirebaseAuth,
) {
    val currentUserId: String?
        get() = firebaseAuth.currentUser?.uid

    suspend fun uploadUser(user: UserEntity) {
        firestore.collection(USERS_COLLECTION)
            .document(user.userId)
            .set(user.toFirestoreMap())
            .await()
    }

    suspend fun uploadObject(objectEntity: ObjectEntity) {
        firestore.collection(OBJECTS_COLLECTION)
            .document(objectEntity.objectId)
            .set(objectEntity.toFirestoreMap())
            .await()
    }

    suspend fun uploadScan(scan: ScanHistoryEntity) {
        firestore.collection(SCAN_HISTORY_COLLECTION)
            .document(scan.scanId)
            .set(scan.toFirestoreMap())
            .await()
    }

    suspend fun uploadQuizResult(result: QuizResultEntity) {
        firestore.collection(USERS_COLLECTION)
            .document(result.userId)
            .collection(QUIZ_RESULTS_COLLECTION)
            .document(result.resultId)
            .set(result.toFirestoreMap())
            .await()
    }

    suspend fun uploadLearningProfile(profile: LearningProfileEntity) {
        firestore.collection(LEARNING_PROFILES_COLLECTION)
            .document(profile.userId)
            .set(profile.toFirestoreMap())
            .await()

        firestore.collection(LEARNING_ANALYTICS_COLLECTION)
            .document(profile.userId)
            .set(profile.toAnalyticsMap())
            .await()
    }

    suspend fun uploadRecommendation(recommendation: RecommendationEntity) {
        firestore.collection(RECOMMENDATIONS_COLLECTION)
            .document(recommendation.recommendationId)
            .set(recommendation.toFirestoreMap())
            .await()
    }

    suspend fun downloadUserScans(userId: String): List<ScanHistoryEntity> =
        firestore.collection(SCAN_HISTORY_COLLECTION)
            .whereEqualTo("userId", userId)
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                document.data?.toScanHistoryEntity(document.id)
            }

    suspend fun downloadQuizResults(userId: String): List<QuizResultEntity> =
        firestore.collection(USERS_COLLECTION)
            .document(userId)
            .collection(QUIZ_RESULTS_COLLECTION)
            .get()
            .await()
            .documents
            .mapNotNull { document ->
                document.data?.toQuizResultEntity(document.id, userId)
            }

    suspend fun downloadObjects(objectIds: Set<String>): List<ObjectEntity> {
        if (objectIds.isEmpty()) return emptyList()

        return objectIds.chunked(FIRESTORE_IN_QUERY_LIMIT).flatMap { chunk ->
            firestore.collection(OBJECTS_COLLECTION)
                .whereIn("objectId", chunk)
                .get()
                .await()
                .documents
                .mapNotNull { document ->
                    document.data?.toObjectEntity(document.id)
                }
        }
    }

    suspend fun deleteScan(scanId: String) {
        firestore.collection(SCAN_HISTORY_COLLECTION)
            .document(scanId)
            .delete()
            .await()
    }

    suspend fun deleteUserHistory(userId: String) {
        firestore.collection(SCAN_HISTORY_COLLECTION)
            .whereEqualTo("userId", userId)
            .get()
            .await()
            .documents
            .forEach { document -> document.reference.delete().await() }
    }

    private fun UserEntity.toFirestoreMap(): Map<String, Any?> = mapOf(
        "userId" to userId,
        "name" to name,
        "email" to email,
        "profileImage" to profileImage,
        "updatedAt" to updatedAt,
    )

    private fun ObjectEntity.toFirestoreMap(): Map<String, Any?> = mapOf(
        "objectId" to objectId,
        "name" to name,
        "category" to category,
        "scientificName" to scientificName,
        "description" to description,
        "uses" to uses,
        "facts" to facts,
        "imageUrl" to imageUrl,
        "updatedAt" to updatedAt,
    )

    private fun ScanHistoryEntity.toFirestoreMap(): Map<String, Any?> = mapOf(
        "scanId" to scanId,
        "userId" to userId,
        "objectId" to objectId,
        "timestamp" to timestamp,
        "confidenceScore" to confidenceScore,
        "learningStatus" to learningStatus.name,
        "updatedAt" to updatedAt,
    )

    private fun QuizResultEntity.toFirestoreMap(): Map<String, Any?> = mapOf(
        "resultId" to resultId,
        "userId" to userId,
        "quizId" to quizId,
        "score" to score,
        "totalQuestions" to totalQuestions,
        "percentage" to percentage,
        "completionTime" to completionTime,
        "timestamp" to timestamp,
        "updatedAt" to updatedAt,
    )

    private fun LearningProfileEntity.toFirestoreMap(): Map<String, Any?> = mapOf(
        "userId" to userId,
        "totalObjectsLearned" to totalObjectsLearned,
        "averageQuizScore" to averageQuizScore,
        "favoriteCategories" to favoriteCategories,
        "weakAreas" to weakAreas,
        "learningLevel" to learningLevel.name,
        "learningStreak" to learningStreak,
        "totalLearningTimeMillis" to totalLearningTimeMillis,
        "frequentlySearchedTopics" to frequentlySearchedTopics,
        "difficultConcepts" to difficultConcepts,
        "preferredLanguage" to preferredLanguage,
        "learningSpeed" to learningSpeed.name,
        "personalizationEnabled" to personalizationEnabled,
        "updatedAt" to updatedAt,
    )

    private fun LearningProfileEntity.toAnalyticsMap(): Map<String, Any?> = mapOf(
        "userId" to userId,
        "progressPercent" to toDomain().progressPercent,
        "totalObjectsLearned" to totalObjectsLearned,
        "averageQuizScore" to averageQuizScore,
        "learningLevel" to learningLevel.name,
        "learningStreak" to learningStreak,
        "favoriteCategories" to favoriteCategories,
        "weakAreas" to weakAreas,
        "updatedAt" to updatedAt,
    )

    private fun RecommendationEntity.toFirestoreMap(): Map<String, Any?> = mapOf(
        "recommendationId" to recommendationId,
        "userId" to userId,
        "title" to title,
        "description" to description,
        "topic" to topic,
        "type" to type.name,
        "priority" to priority.name,
        "targetSkillLevel" to targetSkillLevel.name,
        "createdAt" to createdAt,
        "completed" to completed,
        "updatedAt" to updatedAt,
    )

    private fun Map<String, Any>.toScanHistoryEntity(documentId: String): ScanHistoryEntity? {
        val scanId = this["scanId"] as? String ?: documentId
        val userId = this["userId"] as? String ?: return null
        val objectId = this["objectId"] as? String ?: return null
        val status = (this["learningStatus"] as? String)
            ?.let { runCatching { com.rola.app.domain.model.LearningStatus.valueOf(it) }.getOrNull() }
            ?: com.rola.app.domain.model.LearningStatus.Scanned

        return ScanHistoryEntity(
            scanId = scanId,
            userId = userId,
            objectId = objectId,
            timestamp = (this["timestamp"] as? Number)?.toLong() ?: 0L,
            confidenceScore = (this["confidenceScore"] as? Number)?.toFloat() ?: 0f,
            learningStatus = status,
            isSynced = true,
            updatedAt = (this["updatedAt"] as? Number)?.toLong() ?: 0L,
        )
    }

    private fun Map<String, Any>.toQuizResultEntity(
        documentId: String,
        fallbackUserId: String,
    ): QuizResultEntity? {
        return QuizResultEntity(
            resultId = this["resultId"] as? String ?: documentId,
            userId = this["userId"] as? String ?: fallbackUserId,
            quizId = this["quizId"] as? String ?: return null,
            score = (this["score"] as? Number)?.toInt() ?: 0,
            totalQuestions = (this["totalQuestions"] as? Number)?.toInt() ?: 0,
            percentage = (this["percentage"] as? Number)?.toInt() ?: 0,
            completionTime = (this["completionTime"] as? Number)?.toLong() ?: 0L,
            timestamp = (this["timestamp"] as? Number)?.toLong() ?: 0L,
            isSynced = true,
            updatedAt = (this["updatedAt"] as? Number)?.toLong() ?: 0L,
        )
    }

    @Suppress("UNCHECKED_CAST")
    private fun Map<String, Any>.toObjectEntity(documentId: String): ObjectEntity? {
        val objectId = this["objectId"] as? String ?: documentId
        return ObjectEntity(
            objectId = objectId,
            name = this["name"] as? String ?: return null,
            category = this["category"] as? String ?: "",
            scientificName = this["scientificName"] as? String ?: "",
            description = this["description"] as? String ?: "",
            uses = this["uses"] as? List<String> ?: emptyList(),
            facts = this["facts"] as? List<String> ?: emptyList(),
            imageUrl = this["imageUrl"] as? String ?: "",
            isSynced = true,
            updatedAt = (this["updatedAt"] as? Number)?.toLong() ?: 0L,
        )
    }

    private companion object {
        const val USERS_COLLECTION = "users"
        const val OBJECTS_COLLECTION = "objects"
        const val SCAN_HISTORY_COLLECTION = "scanHistory"
        const val QUIZ_RESULTS_COLLECTION = "quizResults"
        const val LEARNING_PROFILES_COLLECTION = "learningProfiles"
        const val RECOMMENDATIONS_COLLECTION = "recommendations"
        const val LEARNING_ANALYTICS_COLLECTION = "learningAnalytics"
        const val FIRESTORE_IN_QUERY_LIMIT = 10
    }
}
