package com.rola.app.ai_teacher.lesson

import com.rola.app.domain.model.ARLearningActivity
import com.rola.app.domain.model.GeneratedLesson
import com.rola.app.domain.model.SkillLevel
import com.rola.app.domain.model.TeachingActivityType
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonGenerator @Inject constructor() {
    fun generateLesson(
        topic: String,
        objective: String,
        level: SkillLevel,
        relatedConcepts: List<String>,
    ): GeneratedLesson {
        val concepts = relatedConcepts.distinct().ifEmpty { listOf(topic) }
        return GeneratedLesson(
            lessonId = "lesson-${UUID.randomUUID()}",
            title = topic,
            objectives = listOf(
                "Define $topic using grade-appropriate vocabulary.",
                "Connect $topic to a real-world object or situation.",
                "Use evidence from an AR activity to explain $topic.",
                objective,
            ).distinct(),
            explanation = explanationFor(topic, level),
            examples = examplesFor(topic, concepts),
            experiments = experimentsFor(topic, level),
            arActivities = arActivities(topic, concepts),
            practiceQuestions = practiceQuestionsFor(topic, level, concepts),
            difficulty = level,
            applications = applicationsFor(topic, concepts),
            summary = "$topic connects observation, explanation, practice, and reflection so learners can transfer the idea beyond a single example.",
            teacherNotes = listOf(
                "Begin with a diagnostic question before showing the AR overlay.",
                "Ask students to name evidence before giving formal vocabulary.",
                "Close with one individual response and one peer discussion prompt.",
            ),
        )
    }

    private fun explanationFor(topic: String, level: SkillLevel): String = when (level) {
        SkillLevel.Beginner -> "$topic is introduced through simple observations, familiar examples, and one key idea at a time."
        SkillLevel.Intermediate -> "$topic is explained by connecting vocabulary, cause-and-effect reasoning, and real-world applications."
        SkillLevel.Advanced -> "$topic is explored through mechanisms, evidence, exceptions, and links to adjacent scientific concepts."
    }

    private fun examplesFor(topic: String, relatedConcepts: List<String>): List<String> =
        (listOf("A real object that demonstrates $topic") + relatedConcepts.map { "$topic connected to $it" })
            .distinct()
            .take(4)

    private fun experimentsFor(topic: String, level: SkillLevel): List<String> {
        val base = listOf(
            "Observe $topic in a classroom-safe object and record visible evidence.",
            "Compare what changes when one variable is adjusted.",
        )
        return when (level) {
            SkillLevel.Beginner -> base + "Sort examples and non-examples of $topic."
            SkillLevel.Intermediate -> base + "Build a cause-and-effect claim from the observation."
            SkillLevel.Advanced -> base + "Design a fair test that exposes a limitation or exception."
        }
    }

    private fun arActivities(topic: String, relatedConcepts: List<String>): List<ARLearningActivity> = listOf(
        ARLearningActivity(
            activityId = "activity-${UUID.randomUUID()}",
            title = "AR observation: $topic",
            description = "Scan or inspect a relevant object, attach labels to visible evidence, and capture one observation about $topic.",
            suggestedObjectIds = relatedConcepts.map { it.lowercase().replace(Regex("[^a-z0-9]+"), "-") }.take(5),
            activityType = TeachingActivityType.ArObservation,
            estimatedMinutes = 8,
        ),
        ARLearningActivity(
            activityId = "activity-${UUID.randomUUID()}",
            title = "3D exploration: $topic model",
            description = "Use a 3D model or diagram to rotate, zoom, label parts, and explain structure or process flow.",
            suggestedObjectIds = relatedConcepts.map { it.lowercase().replace(Regex("[^a-z0-9]+"), "-") }.take(5),
            activityType = TeachingActivityType.ThreeDExploration,
            estimatedMinutes = 10,
        ),
        ARLearningActivity(
            activityId = "activity-${UUID.randomUUID()}",
            title = "Interactive challenge",
            description = "Students adjust one variable in an overlay or virtual experiment and predict the result before observing it.",
            suggestedObjectIds = relatedConcepts.map { it.lowercase().replace(Regex("[^a-z0-9]+"), "-") }.take(5),
            activityType = TeachingActivityType.VirtualExperiment,
            estimatedMinutes = 12,
        ),
    )

    private fun practiceQuestionsFor(
        topic: String,
        level: SkillLevel,
        relatedConcepts: List<String>,
    ): List<String> {
        val anchor = relatedConcepts.firstOrNull() ?: "another concept"
        val base = listOf(
            "What is $topic?",
            "Which evidence helps explain $topic?",
            "How does $topic connect to $anchor?",
        )
        return when (level) {
            SkillLevel.Beginner -> base + "Which object in the room best shows $topic?"
            SkillLevel.Intermediate -> base + "What changes if one condition is different?"
            SkillLevel.Advanced -> base + "What limitation or exception should a researcher consider?"
        }
    }

    private fun applicationsFor(topic: String, relatedConcepts: List<String>): List<String> =
        listOf(
            "$topic in everyday decisions",
            "$topic in science or engineering design",
            "$topic connected to ${relatedConcepts.firstOrNull() ?: "a nearby concept"}",
        ).distinct()
}
