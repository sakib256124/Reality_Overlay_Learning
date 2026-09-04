package com.rola.app.ai_teacher.teaching

import com.rola.app.data.database.AITeacherDao
import com.rola.app.data.database.entities.AITeacherAssessmentEntity
import com.rola.app.data.database.entities.CurriculumModuleEntity
import com.rola.app.data.database.entities.GeneratedLessonEntity
import com.rola.app.data.database.entities.toCurriculumEntity
import com.rola.app.data.database.entities.toDomain
import com.rola.app.data.database.entities.toEntity
import com.rola.app.domain.model.AITeacherAssessment
import com.rola.app.domain.model.AITeacherDashboard
import com.rola.app.domain.model.CurriculumModule
import com.rola.app.domain.model.GeneratedCurriculum
import com.rola.app.domain.model.GeneratedLesson
import com.rola.app.domain.model.LessonAnalytics
import com.rola.app.domain.model.TeacherApprovalStatus
import com.rola.app.domain.model.TeacherReviewRecord
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

@Singleton
class AITeacherRepository @Inject constructor(
    private val aiTeacherDao: AITeacherDao,
) {
    fun observeDashboard(
        teacherId: String,
        classId: String,
    ): Flow<AITeacherDashboard> =
        combine(
            observeCurriculumsForTeacher(teacherId),
            aiTeacherDao.observeLessonAnalytics(classId).map { rows -> rows.map { it.toDomain() } },
            aiTeacherDao.observeTeachingPlans(teacherId).map { rows -> rows.map { it.toDomain() } },
        ) { curriculums, analytics, plans ->
            val lessons = curriculums.flatMap { curriculum -> curriculum.modules.flatMap { it.lessons } }
            AITeacherDashboard(
                generatedLessons = lessons,
                curriculumPlans = curriculums,
                studentInsights = analytics.map { analyticsRow ->
                    "${analyticsRow.averageAssessmentScore}% average for ${analyticsRow.lessonId}; difficulty signal ${analyticsRow.difficultySignal.name}."
                }.ifEmpty {
                    plans.flatMap { it.recommendedInterventions }
                },
                suggestedImprovements = analytics.flatMap { it.improvementNotes }.ifEmpty {
                    plans.map { it.activityStrategy }
                },
                pendingApprovals = curriculums.filter { it.approvalStatus == TeacherApprovalStatus.PendingReview },
                lessonAnalytics = analytics,
            )
        }

    fun observeCurriculumsForTeacher(teacherId: String): Flow<List<GeneratedCurriculum>> =
        aiTeacherDao.observeCurriculumsForTeacher(teacherId).map { rows ->
            rows.map { curriculum -> loadCurriculum(curriculum) }
        }

    suspend fun saveCurriculum(curriculum: GeneratedCurriculum) {
        val moduleRows = curriculum.modules.map { it.toEntity(curriculum.curriculumId) }
        val lessonRows = curriculum.modules.flatMap { module -> module.lessons.map { it.toEntity(curriculum.curriculumId) } }
        val activityRows = curriculum.modules.flatMap { module ->
            module.lessons.flatMap { lesson -> lesson.arActivities.map { it.toEntity(lesson.lessonId) } }
        }
        aiTeacherDao.upsertGeneratedCurriculum(
            curriculum = curriculum.toCurriculumEntity(),
            modules = moduleRows,
            lessons = lessonRows,
            activities = activityRows,
            assessments = curriculum.assessments.map { it.toEntity(curriculum.curriculumId) },
        )
    }

    suspend fun saveReview(review: TeacherReviewRecord) {
        aiTeacherDao.upsertReview(review.toEntity())
        aiTeacherDao.updateCurriculumStatus(review.contentId, review.status)
    }

    suspend fun saveTeachingPlan(plan: com.rola.app.domain.model.AdaptiveTeachingPlan) {
        aiTeacherDao.upsertTeachingPlan(plan.toEntity())
    }

    suspend fun saveAnalytics(analytics: List<LessonAnalytics>) {
        aiTeacherDao.upsertLessonAnalytics(analytics.map { it.toEntity() })
    }

    private suspend fun loadCurriculum(curriculum: com.rola.app.data.database.entities.AICurriculumEntity): GeneratedCurriculum {
        val lessonRows = aiTeacherDao.getLessons(curriculum.curriculumId)
        val lessonsById = lessonRows.associateBy { it.lessonId }
        val modules = aiTeacherDao.getModules(curriculum.curriculumId).map { module ->
            module.toDomain(lessonIdsToLessons(module.lessonIds, lessonsById))
        }
        return curriculum.toDomain(
            modules = modules,
            assessments = aiTeacherDao.getAssessments(curriculum.curriculumId).map { it.toDomain() },
        )
    }

    private suspend fun lessonIdsToLessons(
        lessonIds: List<String>,
        lessonsById: Map<String, GeneratedLessonEntity>,
    ): List<GeneratedLesson> = lessonIds.mapNotNull { lessonId ->
        lessonsById[lessonId]?.let { lesson ->
            lesson.toDomain(aiTeacherDao.getActivities(lesson.lessonId).map { it.toDomain() })
        }
    }

    private fun CurriculumModuleEntity.toDomain(lessons: List<GeneratedLesson>): CurriculumModule =
        CurriculumModule(moduleId, title, objective, lessons, estimatedHours)

    private fun AITeacherAssessmentEntity.toDomain(): AITeacherAssessment =
        AITeacherAssessment(assessmentId, title, difficulty, mcqQuestions, practicalTasks, arAssignments, researchActivities)
}
