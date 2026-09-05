package com.rola.app.emotional_ai.learner_state

enum class EmotionalTone { Calm, Curious, Confused, Frustrated, Motivated }

data class EmotionalLearningContext(
    val userId: String,
    val topic: String,
    val recentMessage: String,
    val interactionSpeed: Int,
    val quizConfidence: Int,
    val engagementSignals: List<String>,
    val achievementHistory: List<String>,
)

data class LearnerEmotionState(
    val stateId: String,
    val confidence: Int,
    val motivation: Int,
    val interest: Int,
    val frustration: Int,
    val confusion: Int,
    val stress: Int,
    val engagement: Int,
    val tone: EmotionalTone,
)

data class EmotionProfile(val profileId: String, val userId: String, val patterns: List<String>, val preferredSupport: String)
data class MotivationRecord(val recordId: String, val strategy: String, val encouragement: String, val goalAdjustment: String)
data class EngagementPlan(val planId: String, val lessonFormat: String, val activitySelection: String, val difficultyLevel: String, val environment: String)
data class EmotionalSupportPlan(val supportId: String, val message: String, val teacherAdaptation: String, val recommendations: List<String>)
data class EmotionLearningPattern(val patternId: String, val trendSummary: String, val confidenceTrend: String, val motivationTrend: String)
data class EmotionalAnalyticsReport(val analyticsId: String, val engagementScore: Int, val confidenceScore: Int, val motivationScore: Int, val report: String)

data class EmotionalAIResult(
    val resultId: String,
    val state: LearnerEmotionState,
    val profile: EmotionProfile,
    val motivation: MotivationRecord,
    val engagement: EngagementPlan,
    val support: EmotionalSupportPlan,
    val pattern: EmotionLearningPattern,
    val analytics: EmotionalAnalyticsReport,
    val consentRequired: Boolean,
)
