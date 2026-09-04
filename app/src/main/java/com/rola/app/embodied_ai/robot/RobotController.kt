package com.rola.app.embodied_ai.robot

import com.rola.app.domain.model.EducationalRobot
import com.rola.app.domain.model.RobotConnectionStatus
import com.rola.app.domain.model.RobotPerformanceProfile
import com.rola.app.domain.model.RobotTeachingAction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RobotController @Inject constructor() {
    fun connect(robot: EducationalRobot): EducationalRobot =
        robot.copy(connectionStatus = RobotConnectionStatus.Connected)

    fun executeTeachingAction(action: RobotTeachingAction): String =
        "${action.actionType.name}: ${action.message} Demonstration: ${action.demonstration}"

    fun performanceProfile(robot: EducationalRobot): RobotPerformanceProfile =
        RobotPerformanceProfile(
            edgeProcessingEnabled = true,
            cloudSupportEnabled = robot.connectionStatus == RobotConnectionStatus.CloudConnected,
            lowLatencyMode = true,
            sensorOptimization = if (robot.batteryStatus.lowPowerMode) "Reduced camera and microphone cadence" else "Balanced sensor cadence",
            batteryPlan = if (robot.batteryStatus.lowPowerMode) "Low power teaching mode" else "Standard classroom operation",
            offlineCapability = true,
        )
}
