package com.rola.app.core.agi

import com.rola.app.core.agi.memory.LearnerMemory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PersonalLearningMentor @Inject constructor(
    private val goalPlanner: GoalPlanner,
) {
    fun dailyGuidance(memory: LearnerMemory): List<String> {
        val goal = goalPlanner.nextGoal(memory)
        return listOf(
            "Today: spend 15 minutes on ${goal.targetSkill}.",
            "Review one previous mistake before starting a new activity.",
            "Use one AR observation, one explanation, and one quick question.",
        )
    }

    fun motivation(memory: LearnerMemory): String = when {
        memory.retentionScore >= 80 -> "You are ready for a harder challenge because your recent mastery is strong."
        memory.previousMistakes.isNotEmpty() -> "Your next step is small and clear: fix one mistake pattern at a time."
        else -> "A steady session today will make the next lesson easier."
    }

    fun studyPlan(memory: LearnerMemory): List<String> =
        goalPlanner.futureRecommendations(memory).map { "${it.title}: ${it.rationale}" }
}
