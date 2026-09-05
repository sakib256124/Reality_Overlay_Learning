package com.rola.app.emotional_ai.adaptation

import com.rola.app.emotional_ai.learner_state.EngagementPlan
import com.rola.app.emotional_ai.learner_state.EmotionProfile
import javax.inject.Inject

class HumanCenteredLearningManager @Inject constructor() {
    fun adapt(profile: EmotionProfile, plan: EngagementPlan): List<String> =
        listOf(profile.preferredSupport, plan.lessonFormat, plan.activitySelection, plan.difficultyLevel)
}
