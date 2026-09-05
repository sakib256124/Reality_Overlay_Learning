package com.rola.app.emotional_ai.adaptation

import com.rola.app.emotional_ai.learner_state.EngagementPlan
import javax.inject.Inject

class AdaptiveEmotionLearningManager @Inject constructor() {
    fun apply(plan: EngagementPlan): String = "${plan.lessonFormat} with ${plan.activitySelection} at ${plan.difficultyLevel} difficulty"
}
