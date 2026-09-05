package com.rola.app.emotional_ai

import com.rola.app.data.database.EmotionalAIDao
import com.rola.app.data.database.entities.EmotionLearningPatternEntity
import com.rola.app.data.database.entities.EmotionalAIAnalyticsEntity
import com.rola.app.data.database.entities.EmotionalAIProfileEntity
import com.rola.app.data.database.entities.EngagementHistoryEntity
import com.rola.app.data.database.entities.LearnerEmotionStateEntity
import com.rola.app.data.database.entities.MotivationRecordEntity
import com.rola.app.data.database.entities.SupportRecommendationEntity
import com.rola.app.emotional_ai.learner_state.EmotionalAIResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class EmotionalAIRepository @Inject constructor(private val dao: EmotionalAIDao) {
    fun observeDashboard(): Flow<EmotionalDashboardState> =
        combine(dao.observeState(), dao.observeMotivation(), dao.observeEngagement(), dao.observeAnalytics(), dao.observeSupport()) { state, motivation, engagement, analytics, support ->
            EmotionalDashboardState(
                currentState = state?.tone.orEmpty(),
                motivationLevel = analytics?.motivationScore ?: 0,
                engagementScore = analytics?.engagementScore ?: 0,
                learningConfidence = analytics?.confidenceScore ?: 0,
                recommendations = support?.recommendations.orEmpty(),
                supportMessage = support?.message.orEmpty(),
                teachingAdaptation = support?.teacherAdaptation ?: engagement?.lessonFormat.orEmpty(),
                motivationStrategy = motivation?.strategy.orEmpty(),
            )
        }

    suspend fun save(result: EmotionalAIResult) {
        dao.upsertProfile(EmotionalAIProfileEntity(result.profile.profileId, result.profile.userId, result.profile.patterns, result.profile.preferredSupport))
        dao.upsertState(LearnerEmotionStateEntity(result.state.stateId, result.state.confidence, result.state.motivation, result.state.interest, result.state.frustration, result.state.confusion, result.state.stress, result.state.engagement, result.state.tone.name))
        dao.upsertMotivation(MotivationRecordEntity(result.motivation.recordId, result.motivation.strategy, result.motivation.encouragement, result.motivation.goalAdjustment))
        dao.upsertEngagement(EngagementHistoryEntity(result.engagement.planId, result.engagement.lessonFormat, result.engagement.activitySelection, result.engagement.difficultyLevel, result.engagement.environment))
        dao.upsertAnalytics(EmotionalAIAnalyticsEntity(result.analytics.analyticsId, result.analytics.engagementScore, result.analytics.confidenceScore, result.analytics.motivationScore, result.analytics.report))
        dao.upsertSupport(SupportRecommendationEntity(result.support.supportId, result.support.message, result.support.teacherAdaptation, result.support.recommendations))
        dao.upsertPattern(EmotionLearningPatternEntity(result.pattern.patternId, result.pattern.trendSummary, result.pattern.confidenceTrend, result.pattern.motivationTrend))
    }
}

data class EmotionalDashboardState(
    val currentState: String = "",
    val motivationLevel: Int = 0,
    val engagementScore: Int = 0,
    val learningConfidence: Int = 0,
    val recommendations: List<String> = emptyList(),
    val supportMessage: String = "",
    val teachingAdaptation: String = "",
    val motivationStrategy: String = "",
)
