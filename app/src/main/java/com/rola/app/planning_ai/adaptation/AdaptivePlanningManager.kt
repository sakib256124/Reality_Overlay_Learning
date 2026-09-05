package com.rola.app.planning_ai.adaptation

import com.rola.app.planning_ai.planning_core.AdaptivePlanChange
import com.rola.app.planning_ai.planning_core.OptimizedPlan
import com.rola.app.planning_ai.planning_core.PlanningRequest
import javax.inject.Inject

class AdaptivePlanningManager @Inject constructor() {
    fun adapt(request: PlanningRequest, optimization: OptimizedPlan): AdaptivePlanChange {
        val needsSupport = request.emotionalState.contains("stress", ignoreCase = true) || optimization.efficiencyScore < 75
        return AdaptivePlanChange(
            changeId = "adaptive-${optimization.optimizationId}",
            trigger = if (needsSupport) "performance or emotional support needed" else "plan on track",
            scheduleAdjustment = if (needsSupport) "reduce load and add recovery review" else "keep weekly acceleration",
            methodAdjustment = if (needsSupport) "switch to guided examples" else "increase independent problem solving",
            roadmapUpdate = "Update roadmap around ${request.desiredOutcome} with learner approval.",
        )
    }
}
