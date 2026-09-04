package com.rola.app.embodied_ai.teaching

import com.rola.app.domain.model.RobotTeachingAction
import com.rola.app.domain.model.RobotTeachingActionType
import com.rola.app.domain.model.SkillLevel
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TeachingRobotAgent @Inject constructor() {
    fun explainLesson(
        robotId: String,
        topic: String,
        question: String,
        level: SkillLevel,
        detectedObject: String? = null,
    ): RobotTeachingAction =
        RobotTeachingAction(
            actionId = "robot-action-${UUID.randomUUID()}",
            robotId = robotId,
            actionType = if (question.isBlank()) RobotTeachingActionType.Explain else RobotTeachingActionType.AnswerQuestion,
            topic = topic,
            message = messageFor(topic, question, level, detectedObject),
            demonstration = detectedObject?.let { "Point camera to $it and show a visual demonstration." }
                ?: "Use gesture, voice, and AR overlay to demonstrate $topic.",
            adaptedDifficulty = level,
        )

    fun guideExperiment(
        robotId: String,
        topic: String,
        level: SkillLevel,
    ): RobotTeachingAction =
        RobotTeachingAction(
            actionId = "robot-action-${UUID.randomUUID()}",
            robotId = robotId,
            actionType = RobotTeachingActionType.GuideExperiment,
            topic = topic,
            message = "Predict, observe, explain, and compare evidence for $topic.",
            demonstration = "Robot demonstrates the first step and asks learners to complete the next step.",
            adaptedDifficulty = level,
        )

    private fun messageFor(
        topic: String,
        question: String,
        level: SkillLevel,
        detectedObject: String?,
    ): String {
        val objectLine = detectedObject?.let { " I can see $it, so we can use it as evidence." }.orEmpty()
        return when (level) {
            SkillLevel.Beginner -> "Let's explain $topic with one simple idea and one example.$objectLine ${question.ifBlank { "" }}"
            SkillLevel.Intermediate -> "$topic connects evidence, cause, and effect.$objectLine ${question.ifBlank { "" }}"
            SkillLevel.Advanced -> "$topic can be reasoned through mechanisms, constraints, and exceptions.$objectLine ${question.ifBlank { "" }}"
        }.trim()
    }
}
