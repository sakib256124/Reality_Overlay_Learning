package com.rola.app.mastery_ai.improvement

import com.rola.app.mastery_ai.mastery_engine.LearningGapReport
import com.rola.app.mastery_ai.mastery_engine.MasteryRequest
import com.rola.app.mastery_ai.mastery_engine.SkillImprovementPlan
import javax.inject.Inject

class SkillImprovementEngine @Inject constructor() {
    fun improve(request: MasteryRequest, gaps: LearningGapReport): SkillImprovementPlan =
        SkillImprovementPlan(
            improvementId = "improvement-${request.learnerId}",
            practiceTasks = gaps.weakSkills.map { "complete three $it practice rounds" }.ifEmpty { listOf("complete an expert transfer task") },
            projectRecommendations = listOf("build a portfolio project demonstrating ${request.skillName}", "explain the project decisions"),
            learningChallenges = listOf("daily retrieval challenge", "weekly real-world application", "peer or AI review challenge"),
            longTermImprovement = "Blend Predictive AI, Learning Memory, and Planning AI signals into the next mastery cycle.",
        )
}
