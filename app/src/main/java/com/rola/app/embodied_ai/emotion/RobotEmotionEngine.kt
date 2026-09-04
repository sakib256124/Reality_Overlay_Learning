package com.rola.app.embodied_ai.emotion

import com.rola.app.domain.model.EngagementLevel
import com.rola.app.domain.model.RobotEmotionReport
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RobotEmotionEngine @Inject constructor() {
    fun analyze(
        studentId: String,
        engagementSignals: List<String>,
        recentScores: List<Int>,
    ): RobotEmotionReport {
        val average = recentScores.average().takeIf { !it.isNaN() } ?: 65.0
        val confusion = engagementSignals.count { it.contains("confused", ignoreCase = true) || it.contains("stuck", ignoreCase = true) } * 30
        val interest = 50 + engagementSignals.count { it.contains("curious", ignoreCase = true) || it.contains("excited", ignoreCase = true) } * 20
        return RobotEmotionReport(
            reportId = "robot-emotion-${UUID.randomUUID()}",
            studentId = studentId,
            engagement = when {
                interest >= 80 -> EngagementLevel.High
                engagementSignals.isNotEmpty() -> EngagementLevel.Moderate
                else -> EngagementLevel.Low
            },
            confusionRisk = confusion.coerceIn(0, 100),
            interestLevel = interest.coerceIn(0, 100),
            confidence = (average.toInt() - confusion / 4).coerceIn(0, 100),
            robotResponse = when {
                confusion >= 50 -> "Change explanation and add a concrete example."
                interest >= 80 -> "Offer an interactive activity or challenge."
                average < 60 -> "Ask a simpler check question before moving on."
                else -> "Continue supportive teaching."
            },
        )
    }
}
