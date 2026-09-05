package com.rola.app.emotional_ai.motivation

import com.rola.app.emotional_ai.learner_state.EmotionalLearningContext
import com.rola.app.emotional_ai.learner_state.LearnerEmotionState
import com.rola.app.emotional_ai.learner_state.MotivationRecord
import javax.inject.Inject

class MotivationAnalysisEngine @Inject constructor() {
    fun analyze(context: EmotionalLearningContext, state: LearnerEmotionState): MotivationRecord =
        MotivationRecord(
            recordId = "motivation-${context.userId}",
            strategy = if (state.confidence < 70) "confidence rebuilding" else "goal acceleration",
            encouragement = "You are making progress in ${context.topic}; one smaller step will make the next idea clearer.",
            goalAdjustment = if (state.confusion > 60) "split current goal into two simpler checkpoints" else "keep current goal and add challenge practice",
        )
}
