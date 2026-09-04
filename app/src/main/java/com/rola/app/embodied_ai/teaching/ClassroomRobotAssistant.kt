package com.rola.app.embodied_ai.teaching

import com.rola.app.domain.model.ClassroomRobotSupportPlan
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ClassroomRobotAssistant @Inject constructor() {
    fun supportLesson(
        robotId: String,
        teacherLesson: String,
        topic: String,
    ): ClassroomRobotSupportPlan =
        ClassroomRobotSupportPlan(
            planId = "robot-classroom-plan-${UUID.randomUUID()}",
            robotId = robotId,
            teacherLesson = teacherLesson,
            assistanceSteps = listOf(
                "Listen for student questions about $topic.",
                "Explain $topic with a physical object demonstration.",
                "Guide a short group activity.",
                "Report progress signals to the teacher.",
            ),
            studentInteractionPrompts = listOf(
                "What do you notice?",
                "Which object helps explain $topic?",
                "What should we test next?",
            ),
            analyticsSignals = listOf("question frequency", "interaction time", "confidence signal", "activity completion"),
        )
}
