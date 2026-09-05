package com.rola.app.planning_ai.optimization

import com.rola.app.planning_ai.planning_core.LearningSchedule
import com.rola.app.planning_ai.planning_core.OptimizedPlan
import com.rola.app.planning_ai.planning_core.PlanningRequest
import javax.inject.Inject

class PlanOptimizationEngine @Inject constructor() {
    fun optimize(request: PlanningRequest, schedule: LearningSchedule): OptimizedPlan {
        val timeScore = (request.availableHoursPerWeek * 8).coerceIn(40, 96)
        val gapPenalty = (request.knowledgeGaps.size * 3).coerceAtMost(18)
        return OptimizedPlan(
            optimizationId = "optimization-${schedule.scheduleId}",
            efficiencyScore = (90 - gapPenalty).coerceAtLeast(60),
            resourceUsageScore = if (request.resources.isEmpty()) 72 else 92,
            timeManagementScore = timeScore,
            knowledgeGrowthScore = (request.currentAbility + 18).coerceAtMost(98),
            recommendations = listOf("cache the active plan", "prioritize weakest gaps", "reserve review blocks for retention"),
        )
    }
}
