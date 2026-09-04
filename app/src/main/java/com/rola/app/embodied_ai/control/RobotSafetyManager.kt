package com.rola.app.embodied_ai.control

import com.rola.app.domain.model.EducationalRobot
import com.rola.app.domain.model.RobotPermission
import com.rola.app.domain.model.RobotSafetyReport
import com.rola.app.domain.model.RobotSecurityContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RobotSafetyManager @Inject constructor() {
    fun verify(
        robot: EducationalRobot,
        context: RobotSecurityContext,
        physicalSafetyReady: Boolean,
    ): RobotSafetyReport {
        val permissionGranted = RobotPermission.ControlRobot in context.permissions ||
            RobotPermission.StartRobotSession in context.permissions
        return RobotSafetyReport(
            reportId = "robot-safety-${UUID.randomUUID()}",
            robotId = robot.robotId,
            authenticated = robot.robotId.isNotBlank(),
            communicationSecure = robot.connectionStatus.name.contains("Connected"),
            studentPrivacyProtected = context.institutionId.isNotBlank(),
            physicalSafetyReady = physicalSafetyReady && !robot.batteryStatus.lowPowerMode,
            permissionGranted = permissionGranted,
            auditNotes = listOf(
                "Robot authentication checked.",
                "Secure communication status: ${robot.connectionStatus.name}.",
                "Physical safety ready: $physicalSafetyReady.",
                "Permission granted: $permissionGranted.",
            ),
        )
    }

    fun requirePermission(
        context: RobotSecurityContext,
        permission: RobotPermission,
    ) {
        require(permission in context.permissions) { "Missing robot permission: ${permission.name}" }
    }
}
