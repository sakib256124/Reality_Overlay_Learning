package com.rola.app.emotional_ai.engagement

import com.rola.app.emotional_ai.learner_state.LearnerEmotionState
import javax.inject.Inject

class EngagementAnalyzer @Inject constructor() {
    fun score(state: LearnerEmotionState): Int = ((state.engagement + state.interest + state.motivation) / 3).coerceIn(0, 100)
}
