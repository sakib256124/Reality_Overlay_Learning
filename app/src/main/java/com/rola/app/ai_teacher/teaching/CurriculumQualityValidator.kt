package com.rola.app.ai_teacher.teaching

import com.rola.app.domain.model.CurriculumQualityReport
import com.rola.app.domain.model.GeneratedCurriculum
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CurriculumQualityValidator @Inject constructor() {
    fun validate(
        curriculum: GeneratedCurriculum,
        knowledgeGraphEvidence: String,
    ): CurriculumQualityReport {
        val lessons = curriculum.modules.flatMap { it.lessons }
        val hasObjectives = curriculum.modules.all { it.objective.isNotBlank() && it.lessons.all { lesson -> lesson.objectives.isNotEmpty() } }
        val hasAssessments = curriculum.assessments.isNotEmpty() &&
            curriculum.assessments.all {
                it.mcqQuestions.isNotEmpty() &&
                    it.practicalTasks.isNotEmpty() &&
                    it.arAssignments.isNotEmpty() &&
                    it.researchActivities.isNotEmpty()
            }
        val hasAr = lessons.all { lesson -> lesson.arActivities.isNotEmpty() }
        val hasExperiments = lessons.all { it.experiments.isNotEmpty() }
        val hasTeacherReviewableContent = lessons.all {
            it.explanation.length >= 40 && it.practiceQuestions.size >= 3 && it.summary.isNotBlank()
        }
        val evidenceScore = when {
            knowledgeGraphEvidence.isBlank() -> 0.72f
            knowledgeGraphEvidence.contains(curriculum.request.topic, ignoreCase = true) -> 0.93f
            else -> 0.86f
        }
        return CurriculumQualityReport(
            reportId = "quality-${UUID.randomUUID()}",
            scientificAccuracy = evidenceScore,
            levelAlignment = if (curriculum.modules.all { module -> module.lessons.all { it.difficulty == curriculum.request.studentLevel } }) 0.9f else 0.65f,
            objectiveCoverage = if (hasObjectives && hasAssessments) 0.92f else 0.6f,
            contentConsistency = when {
                hasAr && hasExperiments && hasTeacherReviewableContent -> 0.9f
                hasAr && hasExperiments -> 0.82f
                hasAr -> 0.76f
                else -> 0.68f
            },
            notes = listOf(
                "Knowledge graph evidence: ${if (knowledgeGraphEvidence.isBlank()) "limited" else "available"}",
                "Objectives aligned: $hasObjectives",
                "Assessments include MCQ, practical, AR, and research tasks: $hasAssessments",
                "AR activities included: $hasAr",
                "Experiments included: $hasExperiments",
                "Teacher reviewable content: $hasTeacherReviewableContent",
            ),
        )
    }

    fun placeholderReport(): CurriculumQualityReport = CurriculumQualityReport(
        reportId = "quality-draft",
        scientificAccuracy = 0f,
        levelAlignment = 0f,
        objectiveCoverage = 0f,
        contentConsistency = 0f,
        notes = listOf("Pending validation."),
    )
}
