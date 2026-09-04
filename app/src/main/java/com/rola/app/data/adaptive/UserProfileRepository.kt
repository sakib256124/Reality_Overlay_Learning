package com.rola.app.data.adaptive

import com.rola.app.data.database.AdaptiveLearningDao
import com.rola.app.data.database.ChatMessageDao
import com.rola.app.data.database.QuizResultDao
import com.rola.app.data.database.ScanHistoryDao
import com.rola.app.data.database.entities.toEntity
import com.rola.app.data.firestore.FirestoreService
import com.rola.app.data.firestore.SyncManager
import com.rola.app.data.translation.TranslationRepository
import com.rola.app.domain.model.LearningPattern
import com.rola.app.domain.model.LearningProfile
import com.rola.app.domain.model.Recommendation
import com.rola.app.domain.repository.LearningHistoryRepository
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

@Singleton
class UserProfileRepository @Inject constructor(
    private val adaptiveLearningDao: AdaptiveLearningDao,
    private val scanHistoryDao: ScanHistoryDao,
    private val quizResultDao: QuizResultDao,
    private val chatMessageDao: ChatMessageDao,
    private val learningHistoryRepository: LearningHistoryRepository,
    private val translationRepository: TranslationRepository,
    private val learningAnalyzer: LearningAnalyzer,
    private val recommendationEngine: RecommendationEngine,
    private val aiRecommendationService: AIRecommendationService,
    private val firestoreService: FirestoreService,
    private val syncManager: SyncManager,
) {
    private val syncScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    val currentUserId: String
        get() = learningHistoryRepository.currentUserId

    fun observeProfile(): Flow<LearningProfile?> =
        adaptiveLearningDao.observeProfile(currentUserId).map { it?.toDomain() }

    fun observeRecommendations(): Flow<List<Recommendation>> =
        adaptiveLearningDao.observeRecommendations(currentUserId).map { rows -> rows.map { it.toDomain() } }

    fun observeAdaptiveSummary(): Flow<AdaptiveSummary> =
        combine(observeProfile(), observeRecommendations()) { profile, recommendations ->
            val refreshedProfile = profile ?: refreshLearningProfile()
            val pattern = detectPattern()
            AdaptiveSummary(
                profile = refreshedProfile,
                pattern = pattern,
                recommendations = recommendations.ifEmpty { refreshRecommendations(refreshedProfile, pattern) },
                roadmap = aiRecommendationService.learningRoadmap(refreshedProfile, pattern),
                nextTopic = recommendationEngine.predictNextTopic(refreshedProfile, pattern),
                predictedTrend = recommendationEngine.predictImprovementTrend(pattern),
                explanationComplexity = recommendationEngine.explanationComplexity(refreshedProfile),
            )
        }

    suspend fun refreshLearningProfile(): LearningProfile {
        val userId = currentUserId
        val scans = scanHistoryDao.getUserHistoryWithObjectsSnapshot(userId).map { it.toDomain() }
        val quizResults = quizResultDao.getResultsSnapshot(userId).map { it.toDomain() }
        val messages = chatMessageDao.getUserMessagesSnapshot(userId).map { it.toDomain() }
        val existing = adaptiveLearningDao.getProfile(userId)?.toDomain()
        val analyzedProfile = learningAnalyzer.buildProfile(
            userId = userId,
            scans = scans,
            quizResults = quizResults,
            messages = messages,
            preferredLanguage = translationRepository.selectedLanguageCode(),
            personalizationEnabled = existing?.personalizationEnabled ?: true,
        )
        val profile = analyzedProfile.copy(
            frequentlySearchedTopics = mergeTopics(
                existing?.frequentlySearchedTopics.orEmpty(),
                analyzedProfile.frequentlySearchedTopics,
            ),
        )
        adaptiveLearningDao.upsertProfile(profile.toEntity(isSynced = false))
        scheduleSync()
        return profile
    }

    suspend fun refreshRecommendations(): List<Recommendation> {
        val profile = refreshLearningProfile()
        val pattern = detectPattern()
        return refreshRecommendations(profile, pattern)
    }

    suspend fun detectPattern(): LearningPattern {
        val userId = currentUserId
        val scans = scanHistoryDao.getUserHistoryWithObjectsSnapshot(userId).map { it.toDomain() }
        val quizResults = quizResultDao.getResultsSnapshot(userId).map { it.toDomain() }
        val messages = chatMessageDao.getUserMessagesSnapshot(userId).map { it.toDomain() }
        return learningAnalyzer.detectPattern(scans, quizResults, messages)
    }

    suspend fun personalizedTutorPrefix(): String =
        (adaptiveLearningDao.getProfile(currentUserId)?.toDomain() ?: refreshLearningProfile())
            .takeIf { it.personalizationEnabled }
            ?.let(aiRecommendationService::tutorPersonalizationPrefix)
            .orEmpty()

    suspend fun adaptiveQuizSkillLevel() =
        recommendationEngine.adaptiveQuizDifficulty(
            adaptiveLearningDao.getProfile(currentUserId)?.toDomain() ?: refreshLearningProfile(),
        )

    suspend fun setPersonalizationEnabled(enabled: Boolean) {
        val profile = adaptiveLearningDao.getProfile(currentUserId)?.toDomain() ?: refreshLearningProfile()
        adaptiveLearningDao.upsertProfile(profile.copy(personalizationEnabled = enabled).toEntity(isSynced = false))
        scheduleSync()
    }

    suspend fun recordSearchTopic(query: String) {
        val topic = query.trim().takeIf { it.length >= MIN_SEARCH_TOPIC_LENGTH } ?: return
        val profile = adaptiveLearningDao.getProfile(currentUserId)?.toDomain() ?: refreshLearningProfile()
        adaptiveLearningDao.upsertProfile(
            profile.copy(
                frequentlySearchedTopics = mergeTopics(listOf(topic), profile.frequentlySearchedTopics),
                updatedAt = System.currentTimeMillis(),
            ).toEntity(isSynced = false),
        )
        scheduleSync()
    }

    suspend fun markRecommendationCompleted(recommendationId: String) {
        adaptiveLearningDao.markRecommendationCompleted(recommendationId)
        scheduleSync()
    }

    private suspend fun refreshRecommendations(
        profile: LearningProfile,
        pattern: LearningPattern,
    ): List<Recommendation> {
        if (!profile.personalizationEnabled) return emptyList()
        val completedIds = adaptiveLearningDao.getRecommendations(profile.userId)
            .filter { it.completed }
            .map { it.recommendationId }
            .toSet()
        val recommendations = recommendationEngine.generateRecommendations(profile, pattern)
            .filterNot { it.recommendationId in completedIds }
        adaptiveLearningDao.deleteOpenRecommendations(profile.userId)
        adaptiveLearningDao.upsertRecommendations(recommendations.map { it.toEntity(isSynced = false) })
        scheduleSync()
        return recommendations
    }

    private fun scheduleSync() {
        if (firestoreService.currentUserId == null) return
        syncScope.launch {
            runCatching { syncManager.sync(currentUserId) }
        }
    }

    private fun mergeTopics(
        priorityTopics: List<String>,
        existingTopics: List<String>,
    ): List<String> = (priorityTopics + existingTopics)
        .map { it.trim() }
        .filter { it.isNotBlank() }
        .distinctBy { it.lowercase() }
        .take(MAX_PROFILE_TOPICS)

    private companion object {
        const val MIN_SEARCH_TOPIC_LENGTH = 3
        const val MAX_PROFILE_TOPICS = 5
    }
}

data class AdaptiveSummary(
    val profile: LearningProfile,
    val pattern: LearningPattern,
    val recommendations: List<Recommendation>,
    val roadmap: List<String>,
    val nextTopic: String,
    val predictedTrend: String,
    val explanationComplexity: String,
)
