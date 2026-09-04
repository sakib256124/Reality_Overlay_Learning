package com.rola.app.core.agi

import com.rola.app.core.agi.memory.LearnerMemory
import com.rola.app.domain.model.FutureRecommendation
import com.rola.app.domain.model.LearningGoal
import com.rola.app.domain.model.RecommendationPriority
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GoalPlanner @Inject constructor() {
    fun nextGoal(memory: LearnerMemory): LearningGoal {
        val focus = memory.previousMistakes.firstOrNull()
            ?: memory.goals.firstOrNull { it.progress < it.targetMastery }?.targetSkill
            ?: memory.interests.firstOrNull()
            ?: "Learning foundations"
        return LearningGoal(
            goalId = "learning-goal-${UUID.randomUUID()}",
            learnerId = memory.learnerId,
            title = "Improve $focus mastery",
            targetSkill = focus,
            targetMastery = 80,
            progress = memory.skills.firstOrNull { it.name.equals(focus, ignoreCase = true) }?.mastery ?: 0,
        )
    }

    fun futureRecommendations(memory: LearnerMemory): List<FutureRecommendation> {
        val goal = nextGoal(memory)
        return listOf(
            FutureRecommendation(
                recommendationId = "future-rec-${UUID.randomUUID()}",
                learnerId = memory.learnerId,
                title = "Practice ${goal.targetSkill}",
                rationale = "Current progress is ${goal.progress}% toward ${goal.targetMastery}% mastery.",
                priority = if (goal.progress < 50) RecommendationPriority.High else RecommendationPriority.Medium,
            ),
            FutureRecommendation(
                recommendationId = "future-rec-${UUID.randomUUID()}",
                learnerId = memory.learnerId,
                title = "Connect ${goal.targetSkill} to AR evidence",
                rationale = "Spatial examples improve transfer from observation to explanation.",
                priority = RecommendationPriority.Medium,
            ),
        )
    }
}
