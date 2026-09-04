package com.rola.app.cognitive_ai.emotion

import com.rola.app.domain.model.CognitiveLearningActivity
import com.rola.app.domain.model.EmotionLearningReport
import com.rola.app.domain.model.EngagementLevel
import com.rola.app.domain.model.MotivationLevel
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmotionLearningAnalyzer @Inject constructor() {
    fun analyze(activities: List<CognitiveLearningActivity>): EmotionLearningReport {
        val lowScores = activities.count { (it.score ?: 100) < 60 }
        val longSessions = activities.count { it.durationMillis >= 10 * 60_000 }
        val confidence = ((activities.mapNotNull { it.score }.average().takeIf { !it.isNaN() } ?: 65.0).toInt() - lowScores * 5)
            .coerceIn(0, 100)
        val frustration = (lowScores * 22 - longSessions * 4).coerceIn(0, 100)
        return EmotionLearningReport(
            reportId = "emotion-report-${UUID.randomUUID()}",
            userId = activities.lastOrNull()?.userId.orEmpty(),
            engagement = when {
                longSessions >= 3 -> EngagementLevel.High
                activities.size >= 3 -> EngagementLevel.Moderate
                else -> EngagementLevel.Low
            },
            frustrationRisk = frustration,
            motivation = when {
                confidence >= 80 -> MotivationLevel.Strong
                frustration >= 40 -> MotivationLevel.NeedsSupport
                else -> MotivationLevel.Steady
            },
            confidence = confidence,
            recommendedAdjustment = when {
                frustration >= 40 -> "Use simpler explanation, more examples, and an interactive activity."
                confidence < 50 -> "Start with a quick success task before assessment."
                longSessions >= 3 -> "Offer advanced challenge and learner choice."
                else -> "Continue balanced guidance and retrieval practice."
            },
        )
    }
}
