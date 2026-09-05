package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "emotion_profiles", indices = [Index(value = ["userId"])])
data class EmotionalAIProfileEntity(@PrimaryKey val profileId: String, val userId: String, val patterns: List<String>, val preferredSupport: String)

@Entity(tableName = "learner_emotion_states", indices = [Index(value = ["confidence"]), Index(value = ["tone"])])
data class LearnerEmotionStateEntity(@PrimaryKey val stateId: String, val confidence: Int, val motivation: Int, val interest: Int, val frustration: Int, val confusion: Int, val stress: Int, val engagement: Int, val tone: String)

@Entity(tableName = "motivation_records", indices = [Index(value = ["recordId"])])
data class MotivationRecordEntity(@PrimaryKey val recordId: String, val strategy: String, val encouragement: String, val goalAdjustment: String)

@Entity(tableName = "engagement_history", indices = [Index(value = ["planId"])])
data class EngagementHistoryEntity(@PrimaryKey val planId: String, val lessonFormat: String, val activitySelection: String, val difficultyLevel: String, val environment: String)

@Entity(tableName = "emotional_analytics", indices = [Index(value = ["engagementScore"])])
data class EmotionalAIAnalyticsEntity(@PrimaryKey val analyticsId: String, val engagementScore: Int, val confidenceScore: Int, val motivationScore: Int, val report: String)

@Entity(tableName = "support_recommendations", indices = [Index(value = ["supportId"])])
data class SupportRecommendationEntity(@PrimaryKey val supportId: String, val message: String, val teacherAdaptation: String, val recommendations: List<String>)

@Entity(tableName = "emotion_learning_patterns", indices = [Index(value = ["patternId"])])
data class EmotionLearningPatternEntity(@PrimaryKey val patternId: String, val trendSummary: String, val confidenceTrend: String, val motivationTrend: String)
