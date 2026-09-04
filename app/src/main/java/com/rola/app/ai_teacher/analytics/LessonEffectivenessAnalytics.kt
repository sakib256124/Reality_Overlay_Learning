package com.rola.app.ai_teacher.analytics

import com.rola.app.domain.model.LessonAnalytics
import com.rola.app.domain.model.SkillLevel
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonEffectivenessAnalytics @Inject constructor() {
    fun summarizeLesson(
        lessonId: String,
        classId: String,
        completionRate: Int,
        engagementScore: Int,
        averageAssessmentScore: Int,
    ): LessonAnalytics {
        val normalizedCompletion = completionRate.coerceIn(0, 100)
        val normalizedEngagement = engagementScore.coerceIn(0, 100)
        val normalizedScore = averageAssessmentScore.coerceIn(0, 100)
        return LessonAnalytics(
            analyticsId = "lesson-analytics-${UUID.randomUUID()}",
            lessonId = lessonId,
            classId = classId,
            completionRate = normalizedCompletion,
            engagementScore = normalizedEngagement,
            averageAssessmentScore = normalizedScore,
            difficultySignal = difficultySignal(normalizedCompletion, normalizedEngagement, normalizedScore),
            improvementNotes = improvementNotes(normalizedCompletion, normalizedEngagement, normalizedScore),
        )
    }

    private fun difficultySignal(
        completionRate: Int,
        engagementScore: Int,
        averageAssessmentScore: Int,
    ): SkillLevel = when {
        averageAssessmentScore < 60 || completionRate < 55 -> SkillLevel.Beginner
        averageAssessmentScore >= 85 && engagementScore >= 75 -> SkillLevel.Advanced
        else -> SkillLevel.Intermediate
    }

    private fun improvementNotes(
        completionRate: Int,
        engagementScore: Int,
        averageAssessmentScore: Int,
    ): List<String> = buildList {
        if (completionRate < 70) add("Shorten the lesson sequence or add checkpoint pauses.")
        if (engagementScore < 70) add("Add a more visible AR task or peer challenge.")
        if (averageAssessmentScore < 70) add("Reteach prerequisite concepts and add scaffolded practice.")
        if (isEmpty()) add("Lesson is effective; add optional extension tasks for advanced learners.")
    }
}
