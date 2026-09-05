package com.rola.app.planning_ai.scheduling

import com.rola.app.planning_ai.planning_core.LearningSchedule
import com.rola.app.planning_ai.planning_core.LearningStrategy
import com.rola.app.planning_ai.planning_core.PlanningHorizon
import com.rola.app.planning_ai.planning_core.PlanningRequest
import javax.inject.Inject

class LearningScheduleManager @Inject constructor() {
    fun schedule(request: PlanningRequest, strategy: LearningStrategy): LearningSchedule {
        val dailyMinutes = (request.availableHoursPerWeek * 60 / 7).coerceAtLeast(20)
        return LearningSchedule(
            scheduleId = "schedule-${strategy.strategyId}",
            dailyActivities = listOf("$dailyMinutes min concept learning", "$dailyMinutes min practice", "5 min reflection"),
            weeklyPlan = listOf("diagnose gaps", "learn with ${strategy.methods.first()}", "quiz and adjust"),
            monthlyGoals = listOf("complete one project", "raise mastery by 10%", "review memory retention"),
            roadmap = PlanningHorizon.values().map { "${it.name}: ${request.desiredOutcome}" },
        )
    }
}
