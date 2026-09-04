package com.rola.app.embodied_ai.navigation

import com.rola.app.domain.model.RobotNavigationPlan
import com.rola.app.domain.model.RobotNavigationTarget
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RobotNavigationManager @Inject constructor() {
    fun planPath(
        robotId: String,
        target: RobotNavigationTarget,
        obstacleCount: Int,
    ): RobotNavigationPlan =
        RobotNavigationPlan(
            planId = "robot-navigation-${UUID.randomUUID()}",
            robotId = robotId,
            target = target,
            pathSteps = listOf(
                "Check safety radius ${target.safetyRadiusMeters}m.",
                "Move toward ${target.label}.",
                "Stop before learner workspace.",
                "Face interaction target.",
            ),
            safeToMove = obstacleCount <= 2 && target.safetyRadiusMeters >= 0.5f,
        )
}
