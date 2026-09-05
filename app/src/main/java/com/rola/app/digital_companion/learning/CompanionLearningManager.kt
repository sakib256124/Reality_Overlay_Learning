package com.rola.app.digital_companion.learning

import com.rola.app.digital_companion.companion_core.CompanionEmotionState
import com.rola.app.digital_companion.companion_core.CompanionLearningContext
import com.rola.app.digital_companion.companion_core.CompanionLearningPlan
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanionLearningManager @Inject constructor(
    private val planner: CompanionLearningPlanner,
) {
    fun guideLearning(
        context: CompanionLearningContext,
        emotionState: CompanionEmotionState,
    ): CompanionLearningPlan = planner.plan(context, emotionState)
}

