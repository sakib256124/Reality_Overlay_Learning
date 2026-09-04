package com.rola.app.ai_teacher.teaching

import com.rola.app.ai_teacher.assessment.AssessmentGenerator
import com.rola.app.ai_teacher.curriculum.CurriculumGenerator
import com.rola.app.ai_teacher.lesson.LessonGenerator
import com.rola.app.ai_teacher.personalization.AdaptiveTeachingEngine
import com.rola.app.data.adaptive.UserProfileRepository
import com.rola.app.domain.model.AITeacherAssessment
import com.rola.app.domain.model.AdaptiveTeachingPlan
import com.rola.app.domain.model.ClassroomAssistantResponse
import com.rola.app.domain.model.CurriculumRequest
import com.rola.app.domain.model.GeneratedCurriculum
import com.rola.app.domain.model.GeneratedLesson
import com.rola.app.domain.model.SkillLevel
import com.rola.app.domain.model.TeacherPermission
import com.rola.app.domain.model.TeacherApprovalStatus
import com.rola.app.domain.model.TeacherReviewRecord
import com.rola.app.domain.model.TeacherSecurityContext
import com.rola.app.domain.model.AICurriculumTeachingPlan
import com.rola.app.domain.model.TeachingPlanRequest
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AITeacherEngine @Inject constructor(
    private val curriculumGenerator: CurriculumGenerator,
    private val lessonGenerator: LessonGenerator,
    private val assessmentGenerator: AssessmentGenerator,
    private val adaptiveTeachingEngine: AdaptiveTeachingEngine,
    private val classroomAssistant: ClassroomAssistant,
    private val userProfileRepository: UserProfileRepository,
    private val aiTeacherRepository: AITeacherRepository,
) {
    suspend fun generateCurriculum(
        teacherId: String,
        institutionId: String,
        request: CurriculumRequest,
    ): GeneratedCurriculum {
        requirePermission(
            TeacherSecurityContext(
                teacherId = teacherId,
                institutionId = institutionId,
                permissions = setOf(TeacherPermission.GenerateCurriculum),
            ),
            TeacherPermission.GenerateCurriculum,
        )
        val curriculum = curriculumGenerator.generateCurriculum(
            teacherId = teacherId,
            institutionId = institutionId,
            request = request,
        )
        aiTeacherRepository.saveCurriculum(curriculum)
        return curriculum
    }

    fun generateLesson(
        topic: String,
        objective: String,
        level: SkillLevel,
        relatedConcepts: List<String>,
    ): GeneratedLesson = lessonGenerator.generateLesson(topic, objective, level, relatedConcepts)

    fun generateAssessment(
        topic: String,
        level: SkillLevel,
    ): AITeacherAssessment = assessmentGenerator.generateAssessment(topic, level)

    suspend fun createAdaptiveTeachingPlan(teacherId: String): AdaptiveTeachingPlan {
        val profile = runCatching { userProfileRepository.refreshLearningProfile() }.getOrNull()
        return adaptiveTeachingEngine.adapt(teacherId, profile).also {
            aiTeacherRepository.saveTeachingPlan(it)
        }
    }

    suspend fun answerDuringClass(
        question: String,
        topic: String,
    ): ClassroomAssistantResponse = classroomAssistant.answerDuringClass(question, topic)

    suspend fun generateTeachingPlan(
        securityContext: TeacherSecurityContext,
        request: TeachingPlanRequest,
    ): AICurriculumTeachingPlan {
        requirePermission(securityContext, TeacherPermission.GenerateCurriculum)
        require(securityContext.teacherId == request.teacherId) { "Teacher cannot generate plans for another owner." }
        require(securityContext.institutionId == request.institutionId) { "Institution scope mismatch." }
        return classroomAssistant.generateTeachingPlan(request)
    }

    suspend fun submitForTeacherReview(curriculum: GeneratedCurriculum): GeneratedCurriculum {
        val pending = curriculum.copy(
            approvalStatus = TeacherApprovalStatus.PendingReview,
            updatedAt = System.currentTimeMillis(),
        )
        aiTeacherRepository.saveCurriculum(pending)
        return pending
    }

    suspend fun approveGeneratedContent(
        curriculumId: String,
        teacherId: String,
        comments: String,
        distributeToStudents: Boolean = false,
    ): TeacherReviewRecord {
        val status = if (distributeToStudents) TeacherApprovalStatus.Distributed else TeacherApprovalStatus.Approved
        val review = TeacherReviewRecord(
            reviewId = "teacher-review-${UUID.randomUUID()}",
            contentId = curriculumId,
            teacherId = teacherId,
            status = status,
            comments = comments,
        )
        aiTeacherRepository.saveReview(review)
        return review
    }

    suspend fun approveGeneratedContent(
        curriculumId: String,
        securityContext: TeacherSecurityContext,
        comments: String,
        distributeToStudents: Boolean = false,
    ): TeacherReviewRecord {
        requirePermission(securityContext, TeacherPermission.ApproveContent)
        if (distributeToStudents) {
            requirePermission(securityContext, TeacherPermission.DistributeContent)
        }
        return approveGeneratedContent(
            curriculumId = curriculumId,
            teacherId = securityContext.teacherId,
            comments = comments,
            distributeToStudents = distributeToStudents,
        )
    }

    suspend fun rejectGeneratedContent(
        curriculumId: String,
        teacherId: String,
        comments: String,
    ): TeacherReviewRecord {
        val review = TeacherReviewRecord(
            reviewId = "teacher-review-${UUID.randomUUID()}",
            contentId = curriculumId,
            teacherId = teacherId,
            status = TeacherApprovalStatus.Rejected,
            comments = comments,
        )
        aiTeacherRepository.saveReview(review)
        return review
    }

    fun recommendActivities(lesson: GeneratedLesson): List<String> =
        lesson.arActivities.map { "${it.title}: ${it.description}" } +
            lesson.experiments.map { "Experiment: $it" } +
            lesson.practiceQuestions.take(2).map { "Check: $it" }

    fun validateTeacherCanReview(
        curriculum: GeneratedCurriculum,
        securityContext: TeacherSecurityContext,
    ): Boolean = curriculum.institutionId == securityContext.institutionId &&
        curriculum.ownerUserId == securityContext.teacherId &&
        TeacherPermission.ReviewContent in securityContext.permissions

    private fun requirePermission(
        securityContext: TeacherSecurityContext,
        permission: TeacherPermission,
    ) {
        require(permission in securityContext.permissions) {
            "Missing AI Teacher permission: ${permission.name}"
        }
    }
}
