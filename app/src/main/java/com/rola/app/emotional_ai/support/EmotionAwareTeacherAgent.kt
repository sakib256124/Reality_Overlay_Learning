package com.rola.app.emotional_ai.support

import com.rola.app.emotional_ai.learner_state.EmotionalLearningContext
import com.rola.app.emotional_ai.learner_state.LearnerEmotionState
import javax.inject.Inject

class EmotionAwareTeacherAgent @Inject constructor() {
    fun adapt(context: EmotionalLearningContext, state: LearnerEmotionState): String =
        if (state.confusion > 60 || state.frustration > 50) {
            "Use slower pacing, simpler ${context.topic} example, and confidence-building activity."
        } else {
            "Use deeper analysis, faster pace, and applied examples for ${context.topic}."
        }
}
