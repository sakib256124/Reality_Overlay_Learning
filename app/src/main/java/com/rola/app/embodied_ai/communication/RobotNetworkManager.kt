package com.rola.app.embodied_ai.communication

import com.rola.app.domain.model.EducationalRobot
import com.rola.app.domain.model.RobotNetworkState
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RobotNetworkManager @Inject constructor() {
    fun schoolDeployment(
        institutionId: String,
        robots: List<EducationalRobot>,
        sharedKnowledgeTopics: List<String>,
    ): RobotNetworkState =
        RobotNetworkState(
            networkId = "robot-network-${UUID.randomUUID()}",
            institutionId = institutionId,
            robotIds = robots.map { it.robotId },
            sharedKnowledgeTopics = sharedKnowledgeTopics.distinct().take(12),
            centralManagementStatus = if (robots.all { it.connectionStatus.name.contains("Connected") }) {
                "All robot units synchronized with cloud AI platform."
            } else {
                "Some robot units are operating with offline capability."
            },
        )
}
