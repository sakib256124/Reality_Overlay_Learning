package com.rola.app.emotional_ai.support

import com.rola.app.emotional_ai.learner_state.EmotionalSupportPlan
import com.rola.app.emotional_ai.learner_state.LearnerEmotionState
import com.rola.app.emotional_ai.motivation.MotivationAnalysisEngine
import javax.inject.Inject

class EmotionalSupportAgent @Inject constructor() {
    fun support(state: LearnerEmotionState, teacherAdaptation: String): EmotionalSupportPlan =
        EmotionalSupportPlan(
            supportId = "support-${state.stateId}",
            message = if (state.frustration > 50) "Pause, simplify, and rebuild confidence." else "Keep momentum with a focused challenge.",
            teacherAdaptation = teacherAdaptation,
            recommendations = listOf("explain emotional reasoning transparently", "keep human control", "avoid harmful assumptions"),
        )
}
