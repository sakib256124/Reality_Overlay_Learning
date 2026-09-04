package com.rola.app.embodied_ai

import com.rola.app.domain.model.EducationalRobot
import com.rola.app.domain.model.EducationalRobotType
import com.rola.app.domain.model.RobotBatteryStatus
import com.rola.app.domain.model.RobotCapability
import com.rola.app.domain.model.RobotConnectionStatus
import com.rola.app.domain.model.RobotDecision
import com.rola.app.domain.model.RobotInputMode
import com.rola.app.domain.model.RobotPermission
import com.rola.app.domain.model.RobotSecurityContext
import com.rola.app.domain.model.RobotSensor
import com.rola.app.domain.model.RobotTeachingMode
import com.rola.app.domain.model.SkillLevel
import com.rola.app.embodied_ai.communication.RobotNetworkManager
import com.rola.app.embodied_ai.control.RobotDecisionEngine
import com.rola.app.embodied_ai.control.RobotMemoryManager
import com.rola.app.embodied_ai.control.RobotSafetyManager
import com.rola.app.embodied_ai.emotion.RobotEmotionEngine
import com.rola.app.embodied_ai.interaction.RobotInteractionManager
import com.rola.app.embodied_ai.interaction.VoiceInteractionManager
import com.rola.app.embodied_ai.perception.RobotPerceptionManager
import com.rola.app.embodied_ai.perception.RobotVisionEngine
import com.rola.app.embodied_ai.robot.RobotController
import com.rola.app.embodied_ai.teaching.ClassroomRobotAssistant
import com.rola.app.embodied_ai.teaching.TeachingRobotAgent
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EmbodiedAIEngine @Inject constructor(
    private val robotController: RobotController,
    private val robotPerceptionManager: RobotPerceptionManager,
    private val robotVisionEngine: RobotVisionEngine,
    private val teachingRobotAgent: TeachingRobotAgent,
    private val robotInteractionManager: RobotInteractionManager,
    private val voiceInteractionManager: VoiceInteractionManager,
    private val robotMemoryManager: RobotMemoryManager,
    private val robotEmotionEngine: RobotEmotionEngine,
    private val robotDecisionEngine: RobotDecisionEngine,
    private val classroomRobotAssistant: ClassroomRobotAssistant,
    private val robotNetworkManager: RobotNetworkManager,
    private val robotSafetyManager: RobotSafetyManager,
) {
    fun createDefaultRobot(
        name: String,
        environment: String,
        context: RobotSecurityContext,
    ): EducationalRobot {
        robotSafetyManager.requirePermission(context, RobotPermission.RegisterRobot)
        return EducationalRobot(
            robotId = "education-robot-${UUID.randomUUID()}",
            name = name.ifBlank { "ROLA Teaching Robot" },
            robotType = EducationalRobotType.ClassroomRobot,
            capabilities = setOf(
                RobotCapability.VoiceConversation,
                RobotCapability.GestureInteraction,
                RobotCapability.ObjectRecognition,
                RobotCapability.SceneUnderstanding,
                RobotCapability.ExperimentGuidance,
                RobotCapability.ARDemonstration,
                RobotCapability.Translation,
                RobotCapability.EmotionResponse,
            ),
            sensors = setOf(RobotSensor.Camera, RobotSensor.Depth, RobotSensor.Microphone, RobotSensor.Motion),
            teachingModes = setOf(RobotTeachingMode.Explain, RobotTeachingMode.Demonstrate, RobotTeachingMode.AskQuestions, RobotTeachingMode.GuideExperiment, RobotTeachingMode.GiveFeedback),
            batteryStatus = RobotBatteryStatus(percent = 84, charging = false),
            connectionStatus = RobotConnectionStatus.CloudConnected,
            learningEnvironment = environment,
        )
    }

    fun runTeachingInteraction(
        robot: EducationalRobot,
        studentId: String,
        topic: String,
        question: String,
        context: RobotSecurityContext,
    ): RobotDecision {
        robotSafetyManager.requirePermission(context, RobotPermission.ControlRobot)
        val connected = robotController.connect(robot)
        val normalized = voiceInteractionManager.normalizeUtterance(question)
        val perception = robotPerceptionManager.understandEnvironment(
            robotId = connected.robotId,
            students = listOf(studentId),
            objects = listOf(topic),
            activities = listOf("student question"),
            physicalInteractions = listOf("voice conversation"),
        )
        val vision = robotVisionEngine.analyze(perception)
        val interaction = robotInteractionManager.respond(
            robotId = connected.robotId,
            studentId = studentId,
            inputMode = RobotInputMode.Voice,
            inputText = normalized,
            emotionalSignal = "confused",
        )
        val memory = robotMemoryManager.buildMemory(connected.robotId, studentId, listOf(interaction), 62, listOf("voice", "AR demonstration"))
        val emotion = robotEmotionEngine.analyze(studentId, listOf(interaction.emotionResponse), listOf(memory.learningProgress))
        val decision = robotDecisionEngine.decide(connected.robotId, studentId, topic, emotion)
        val teachingAction = teachingRobotAgent.explainLesson(
            robotId = connected.robotId,
            topic = topic,
            question = normalized,
            level = decision.teachingAction.adaptedDifficulty,
            detectedObject = vision.recognizedObjects.firstOrNull(),
        )
        return decision.copy(teachingAction = teachingAction)
    }

    fun classroomSupport(robot: EducationalRobot, teacherLesson: String, topic: String) =
        classroomRobotAssistant.supportLesson(robot.robotId, teacherLesson, topic)

    fun networkState(institutionId: String, robots: List<EducationalRobot>, topics: List<String>) =
        robotNetworkManager.schoolDeployment(institutionId, robots, topics)

    fun safetyReport(robot: EducationalRobot, context: RobotSecurityContext) =
        robotSafetyManager.verify(robot, context, physicalSafetyReady = true)

    fun speak(text: String, languageCode: String): String =
        voiceInteractionManager.speak(text, languageCode)
}
