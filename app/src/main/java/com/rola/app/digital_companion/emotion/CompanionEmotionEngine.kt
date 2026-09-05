package com.rola.app.digital_companion.emotion

import com.rola.app.digital_companion.companion_core.CompanionConfidence
import com.rola.app.digital_companion.companion_core.CompanionEmotionState
import com.rola.app.digital_companion.companion_core.CompanionLearningContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanionEmotionEngine @Inject constructor() {
    fun analyze(context: CompanionLearningContext): CompanionEmotionState {
        val average = context.recentScores.average().takeIf { !it.isNaN() } ?: 60.0
        val confidence = when {
            average < 60 -> CompanionConfidence.NeedsSupport
            average < 80 -> CompanionConfidence.Developing
            else -> CompanionConfidence.Confident
        }
        return CompanionEmotionState(
            stateId = "companion-emotion-${UUID.randomUUID()}",
            motivationPercent = if (average < 60) 58 else 78,
            confidence = confidence,
            frustrationPercent = (100 - average.toInt()).coerceIn(5, 65),
            engagementPercent = if (context.preferredModalities.isNotEmpty()) 84 else 68,
            recommendedResponse = if (confidence == CompanionConfidence.NeedsSupport) {
                "Provide encouragement, simplify explanation, and generate short practice."
            } else {
                "Offer challenge, reflection, and next-step learning."
            },
        )
    }
}

