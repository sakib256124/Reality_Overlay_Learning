package com.rola.app.embodied_ai.perception

import com.rola.app.domain.model.RobotPerceptionFrame
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RobotPerceptionManager @Inject constructor() {
    fun understandEnvironment(
        robotId: String,
        students: List<String>,
        objects: List<String>,
        activities: List<String>,
        physicalInteractions: List<String>,
    ): RobotPerceptionFrame =
        RobotPerceptionFrame(
            frameId = "robot-perception-${UUID.randomUUID()}",
            robotId = robotId,
            detectedStudents = students.distinct(),
            detectedObjects = objects.distinct(),
            classroomEnvironment = environmentSummary(students, objects, activities),
            learningActivities = activities.distinct(),
            physicalInteractions = physicalInteractions.distinct(),
            confidence = confidenceFor(students, objects, activities),
        )

    private fun environmentSummary(
        students: List<String>,
        objects: List<String>,
        activities: List<String>,
    ): String =
        "Classroom contains ${students.distinct().size} learners, ${objects.distinct().size} learning objects, and ${activities.distinct().size} active tasks."

    private fun confidenceFor(
        students: List<String>,
        objects: List<String>,
        activities: List<String>,
    ): Float =
        (0.45f + students.distinct().size * 0.08f + objects.distinct().size * 0.06f + activities.distinct().size * 0.05f)
            .coerceIn(0f, 0.95f)
}
