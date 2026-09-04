package com.rola.app.embodied_ai

import com.rola.app.data.database.EmbodiedAIDao
import com.rola.app.data.database.entities.RobotInteractionEntity
import com.rola.app.data.database.entities.RobotProfileEntity
import com.rola.app.data.database.entities.RobotSessionEntity
import com.rola.app.domain.model.EducationalRobot
import com.rola.app.domain.model.EducationalRobotType
import com.rola.app.domain.model.RobotBatteryStatus
import com.rola.app.domain.model.RobotCapability
import com.rola.app.domain.model.RobotConnectionStatus
import com.rola.app.domain.model.RobotDashboardState
import com.rola.app.domain.model.RobotInputMode
import com.rola.app.domain.model.RobotInteraction
import com.rola.app.domain.model.RobotSensor
import com.rola.app.domain.model.RobotSession
import com.rola.app.domain.model.RobotSessionStatus
import com.rola.app.domain.model.RobotTeachingMode
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class EmbodiedAIRepository @Inject constructor(
    private val embodiedAIDao: EmbodiedAIDao,
) {
    fun observeDashboard(): Flow<RobotDashboardState> =
        combine(
            embodiedAIDao.observeRobots(),
            embodiedAIDao.observeActiveSessions(),
            embodiedAIDao.observeRecentInteractions(),
        ) { robots, sessions, interactions ->
            RobotDashboardState(
                robots = robots.map { it.toDomain() },
                activeSessions = sessions.map { it.toDomain() },
                recentInteractions = interactions.map { it.toDomain() },
                safetyReports = emptyList(),
                classroomPlans = emptyList(),
                networkState = null,
            )
        }

    suspend fun saveRobot(robot: EducationalRobot) {
        embodiedAIDao.upsertRobot(robot.toEntity())
    }

    suspend fun saveSession(session: RobotSession) {
        embodiedAIDao.upsertSession(
            RobotSessionEntity(
                sessionId = session.sessionId,
                robotId = session.robotId,
                classroomId = session.classroomId,
                teacherId = session.teacherId,
                status = session.status,
                activeTopic = session.activeTopic,
                startedAt = session.startedAt,
                updatedAt = session.updatedAt,
            ),
        )
    }

    suspend fun saveInteraction(interaction: RobotInteraction) {
        embodiedAIDao.upsertInteraction(
            RobotInteractionEntity(
                interactionId = interaction.interactionId,
                robotId = interaction.robotId,
                studentId = interaction.studentId,
                inputMode = interaction.inputMode,
                inputText = interaction.inputText,
                responseText = interaction.responseText,
                emotionResponse = interaction.emotionResponse,
                timestamp = interaction.timestamp,
            ),
        )
    }
}

private fun EducationalRobot.toEntity(): RobotProfileEntity =
    RobotProfileEntity(
        robotId = robotId,
        name = name,
        robotType = robotType,
        capabilities = capabilities.map { it.name },
        sensors = sensors.map { it.name },
        teachingModes = teachingModes.map { it.name },
        batteryPercent = batteryStatus.percent,
        charging = batteryStatus.charging,
        connectionStatus = connectionStatus,
        learningEnvironment = learningEnvironment,
    )

private fun RobotProfileEntity.toDomain(): EducationalRobot =
    EducationalRobot(
        robotId = robotId,
        name = name,
        robotType = robotType,
        capabilities = capabilities.mapNotNull { runCatching { RobotCapability.valueOf(it) }.getOrNull() }.toSet(),
        sensors = sensors.mapNotNull { runCatching { RobotSensor.valueOf(it) }.getOrNull() }.toSet(),
        teachingModes = teachingModes.mapNotNull { runCatching { RobotTeachingMode.valueOf(it) }.getOrNull() }.toSet(),
        batteryStatus = RobotBatteryStatus(batteryPercent, charging),
        connectionStatus = connectionStatus,
        learningEnvironment = learningEnvironment,
    )

private fun RobotSessionEntity.toDomain(): RobotSession =
    RobotSession(sessionId, robotId, classroomId, teacherId, status, activeTopic, startedAt, updatedAt)

private fun RobotInteractionEntity.toDomain(): RobotInteraction =
    RobotInteraction(
        interactionId = interactionId,
        robotId = robotId,
        studentId = studentId,
        inputMode = inputMode,
        inputText = inputText,
        responseText = responseText,
        emotionResponse = emotionResponse,
        timestamp = timestamp,
    )
