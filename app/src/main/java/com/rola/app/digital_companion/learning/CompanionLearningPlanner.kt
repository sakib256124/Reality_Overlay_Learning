package com.rola.app.digital_companion.learning

import com.rola.app.digital_companion.companion_core.CompanionEmotionState
import com.rola.app.digital_companion.companion_core.CompanionLearningContext
import com.rola.app.digital_companion.companion_core.CompanionLearningPlan
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanionLearningPlanner @Inject constructor() {
    fun plan(
        context: CompanionLearningContext,
        emotionState: CompanionEmotionState,
    ): CompanionLearningPlan =
        CompanionLearningPlan(
            planId = "companion-plan-${UUID.randomUUID()}",
            dailyStudyPlan = listOf("Review ${context.topic}", "Ask one question", "Complete one practice activity"),
            weeklyGoals = listOf(context.currentGoal, "Explain ${context.topic} with an example"),
            skillRoadmap = listOf("Foundation", "Guided practice", "Independent explanation", "Applied project"),
            practiceSchedule = if (emotionState.frustrationPercent > 35) {
                listOf("5-minute practice", "short feedback", "confidence check")
            } else {
                listOf("15-minute challenge", "reflection", "quiz checkpoint")
            },
            recommendations = listOf(emotionState.recommendedResponse, "Use ${context.preferredModalities.joinToString()} for the next session."),
        )
}

