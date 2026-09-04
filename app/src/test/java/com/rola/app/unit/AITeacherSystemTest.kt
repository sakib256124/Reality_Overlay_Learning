package com.rola.app.unit

import com.rola.app.ai_teacher.assessment.AssessmentGenerator
import com.rola.app.ai_teacher.analytics.LessonEffectivenessAnalytics
import com.rola.app.ai_teacher.lesson.LessonGenerator
import com.rola.app.ai_teacher.personalization.AdaptiveTeachingEngine
import com.rola.app.ai_teacher.teaching.CurriculumQualityValidator
import com.rola.app.domain.model.CurriculumModule
import com.rola.app.domain.model.CurriculumRequest
import com.rola.app.domain.model.GeneratedCurriculum
import com.rola.app.domain.model.LearningProfile
import com.rola.app.domain.model.LearningSpeed
import com.rola.app.domain.model.SkillLevel
import com.rola.app.domain.model.TeacherPermission
import com.rola.app.domain.model.TeacherSecurityContext
import com.rola.app.domain.model.TeachingActivityType
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class AITeacherSystemTest {
    private val lessonGenerator = LessonGenerator()
    private val assessmentGenerator = AssessmentGenerator()
    private val adaptiveTeachingEngine = AdaptiveTeachingEngine()
    private val qualityValidator = CurriculumQualityValidator()
    private val lessonEffectivenessAnalytics = LessonEffectivenessAnalytics()

    @Test
    fun generatedLessonIncludesObjectivesArActivitiesAndPractice() {
        val lesson = lessonGenerator.generateLesson(
            topic = "Electricity",
            objective = "Explain current and voltage.",
            level = SkillLevel.Beginner,
            relatedConcepts = listOf("Copper", "Circuit"),
        )

        assertEquals(SkillLevel.Beginner, lesson.difficulty)
        assertTrue(lesson.objectives.any { it.contains("Electricity") })
        assertTrue(lesson.arActivities.isNotEmpty())
        assertTrue(lesson.arActivities.any { it.activityType == TeachingActivityType.ThreeDExploration })
        assertTrue(lesson.applications.isNotEmpty())
        assertTrue(lesson.practiceQuestions.isNotEmpty())
        assertTrue(lesson.summary.isNotBlank())
    }

    @Test
    fun advancedAssessmentAddsHigherOrderQuestion() {
        val assessment = assessmentGenerator.generateAssessment("Metals", SkillLevel.Advanced)

        assertEquals(SkillLevel.Advanced, assessment.difficulty)
        assertTrue(assessment.mcqQuestions.any { it.contains("exception", ignoreCase = true) })
        assertTrue(assessment.arAssignments.isNotEmpty())
        assertTrue(assessment.practicalTasks.isNotEmpty())
        assertTrue(assessment.researchActivities.isNotEmpty())
    }

    @Test
    fun qualityValidationRequiresObjectivesAssessmentAndArActivity() {
        val lesson = lessonGenerator.generateLesson("Circuits", "Build a simple circuit.", SkillLevel.Intermediate, listOf("Battery"))
        val curriculum = GeneratedCurriculum(
            curriculumId = "curriculum-1",
            teacherId = "teacher-1",
            institutionId = "institution-1",
            request = CurriculumRequest("Physics", "Electricity", "Grade 7", "Build a simple circuit.", 1, SkillLevel.Intermediate),
            title = "Electricity",
            overview = "A short electricity unit.",
            modules = listOf(CurriculumModule("module-1", "Current", "Explain current.", listOf(lesson), 2)),
            assessments = listOf(assessmentGenerator.generateAssessment("Circuits", SkillLevel.Intermediate)),
            quality = qualityValidator.placeholderReport(),
        )

        val report = qualityValidator.validate(curriculum, "Knowledge graph context is available.")

        assertTrue(report.publishReady)
        assertTrue(report.scientificAccuracy >= 0.75f)
    }

    @Test
    fun adaptiveTeachingUsesStudentHistorySignals() {
        val plan = adaptiveTeachingEngine.adapt(
            teacherId = "teacher-1",
            profile = LearningProfile(
                userId = "student-1",
                totalObjectsLearned = 12,
                averageQuizScore = 55,
                favoriteCategories = listOf("Physics"),
                weakAreas = listOf("Circuits"),
                learningLevel = SkillLevel.Beginner,
                learningStreak = 2,
                totalLearningTimeMillis = 3_600_000,
                frequentlySearchedTopics = listOf("Battery"),
                difficultConcepts = listOf("Resistance"),
                preferredLanguage = "en",
                learningSpeed = LearningSpeed.SlowAndSteady,
            ),
        )

        assertEquals(LearningSpeed.SlowAndSteady, plan.learningSpeed)
        assertFalse(plan.recommendedInterventions.isEmpty())
        assertTrue(plan.exampleStrategy.contains("Physics"))
        assertTrue(plan.knowledgeGaps.contains("Resistance"))
        assertTrue(plan.extraPractice.isNotEmpty())
        assertTrue(plan.recommendations.isNotEmpty())
    }

    @Test
    fun teacherSecurityContextModelsReviewScope() {
        val securityContext = TeacherSecurityContext(
            teacherId = "teacher-1",
            institutionId = "institution-1",
            permissions = setOf(TeacherPermission.ReviewContent, TeacherPermission.ApproveContent),
        )

        assertTrue(TeacherPermission.ReviewContent in securityContext.permissions)
        assertTrue(TeacherPermission.ApproveContent in securityContext.permissions)
        assertFalse(TeacherPermission.DistributeContent in securityContext.permissions)
    }

    @Test
    fun lessonAnalyticsFlagsReteachingWhenScoresAreLow() {
        val analytics = lessonEffectivenessAnalytics.summarizeLesson(
            lessonId = "lesson-1",
            classId = "class-1",
            completionRate = 52,
            engagementScore = 64,
            averageAssessmentScore = 58,
        )

        assertEquals(SkillLevel.Beginner, analytics.difficultySignal)
        assertTrue(analytics.improvementNotes.any { it.contains("Reteach", ignoreCase = true) })
    }
}
