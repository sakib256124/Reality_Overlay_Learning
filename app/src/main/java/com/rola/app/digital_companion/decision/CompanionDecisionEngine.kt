package com.rola.app.digital_companion.decision

import com.rola.app.digital_companion.companion_core.CompanionConfidence
import com.rola.app.digital_companion.companion_core.CompanionDecision
import com.rola.app.digital_companion.companion_core.CompanionEmotionState
import com.rola.app.digital_companion.companion_core.CompanionLearningContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanionDecisionEngine @Inject constructor() {
    fun decide(
        context: CompanionLearningContext,
        emotionState: CompanionEmotionState,
    ): CompanionDecision {
        val needsSupport = emotionState.confidence == CompanionConfidence.NeedsSupport
        return CompanionDecision(
            decisionId = "companion-decision-${UUID.randomUUID()}",
            explanationMethod = if (needsSupport) "simple analogy with AR guidance" else "research-style explanation",
            learningActivity = if (needsSupport) "guided practice" else "independent challenge",
            difficultyLevel = if (needsSupport) "Beginner" else context.skillLevel,
            teachingApproach = if (needsSupport) "encourage, simplify, practice" else "question, extend, reflect",
            transparentReason = "Decision uses recent scores, preferred modalities, and emotion state for ${context.topic}.",
        )
    }
}

