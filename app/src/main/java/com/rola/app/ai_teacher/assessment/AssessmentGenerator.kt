package com.rola.app.ai_teacher.assessment

import com.rola.app.domain.model.AITeacherAssessment
import com.rola.app.domain.model.SkillLevel
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AssessmentGenerator @Inject constructor() {
    fun generateAssessment(
        topic: String,
        level: SkillLevel,
    ): AITeacherAssessment = AITeacherAssessment(
        assessmentId = "assessment-${UUID.randomUUID()}",
        title = "$topic ${level.name} Assessment",
        difficulty = level,
        mcqQuestions = mcqFor(topic, level),
        practicalTasks = practicalTasksFor(topic, level),
        arAssignments = arAssignmentsFor(topic, level),
        researchActivities = researchFor(topic, level),
    )

    private fun mcqFor(topic: String, level: SkillLevel): List<String> {
        val base = listOf(
            "Which statement best defines $topic?",
            "Which example shows $topic in the real world?",
            "What evidence would support an explanation of $topic?",
        )
        return when (level) {
            SkillLevel.Beginner -> base + "Which observation is most directly connected to $topic?"
            SkillLevel.Intermediate -> base + "Which cause-and-effect chain best explains $topic?"
            SkillLevel.Advanced -> base + "Which exception or limitation should be considered?"
        }
    }

    private fun practicalTasksFor(topic: String, level: SkillLevel): List<String> = when (level) {
        SkillLevel.Beginner -> listOf(
            "Explain $topic using one observed object.",
            "Draw and label a simple example of $topic.",
        )
        SkillLevel.Intermediate -> listOf(
            "Compare $topic across two examples.",
            "Use evidence to write a claim, evidence, reasoning response about $topic.",
        )
        SkillLevel.Advanced -> listOf(
            "Design a short investigation that tests a claim about $topic.",
            "Evaluate two explanations and identify the stronger evidence.",
        )
    }

    private fun arAssignmentsFor(topic: String, level: SkillLevel): List<String> = when (level) {
        SkillLevel.Beginner -> listOf("Scan a relevant object and identify evidence of $topic.")
        SkillLevel.Intermediate -> listOf("Use AR labels to explain how two parts interact in $topic.")
        SkillLevel.Advanced -> listOf("Create an AR investigation plan that manipulates one variable related to $topic.")
    }

    private fun researchFor(topic: String, level: SkillLevel): List<String> = when (level) {
        SkillLevel.Beginner -> listOf("Find one trusted source that explains $topic and summarize it.")
        SkillLevel.Intermediate -> listOf("Compare two trusted sources and list agreements and differences about $topic.")
        SkillLevel.Advanced -> listOf("Use three sources to identify evidence, uncertainty, and a possible research question about $topic.")
    }
}
