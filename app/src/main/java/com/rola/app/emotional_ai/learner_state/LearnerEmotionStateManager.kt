package com.rola.app.emotional_ai.learner_state

import javax.inject.Inject

class LearnerEmotionStateManager @Inject constructor() {
    fun profile(context: EmotionalLearningContext, state: LearnerEmotionState): EmotionProfile =
        EmotionProfile(
            profileId = "emotion-profile-${context.userId}",
            userId = context.userId,
            patterns = listOf("confidence:${state.confidence}", "confusion:${state.confusion}", "engagement:${state.engagement}"),
            preferredSupport = if (state.frustration > 50) "calm encouragement with simpler examples" else "challenge with guided autonomy",
        )
}
