package com.rola.app.embodied_ai.perception

import com.rola.app.domain.model.RobotPerceptionFrame
import com.rola.app.domain.model.RobotVisionReport
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RobotVisionEngine @Inject constructor() {
    fun analyze(frame: RobotPerceptionFrame): RobotVisionReport =
        RobotVisionReport(
            reportId = "robot-vision-${UUID.randomUUID()}",
            recognizedObjects = frame.detectedObjects,
            sceneUnderstanding = frame.classroomEnvironment,
            studentActivity = frame.learningActivities.firstOrNull() ?: "Monitoring classroom readiness",
            classroomMonitoringNotes = buildList {
                if (frame.detectedStudents.isEmpty()) add("No students detected.")
                if (frame.detectedObjects.isEmpty()) add("No learning objects detected.")
                if (frame.physicalInteractions.isNotEmpty()) add("Physical interactions: ${frame.physicalInteractions.joinToString()}.")
                if (isEmpty()) add("Classroom scene is ready for embodied teaching.")
            },
            confidence = frame.confidence,
        )
}
