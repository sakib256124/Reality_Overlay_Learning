package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rola.app.domain.model.AITeacherAssessment
import com.rola.app.domain.model.ARLearningActivity
import com.rola.app.domain.model.AdaptiveTeachingPlan
import com.rola.app.domain.model.CurriculumQualityReport
import com.rola.app.domain.model.CurriculumRequest
import com.rola.app.domain.model.GeneratedCurriculum
import com.rola.app.domain.model.GeneratedLesson
import com.rola.app.domain.model.LearningSpeed
import com.rola.app.domain.model.LessonAnalytics
import com.rola.app.domain.model.SkillLevel
import com.rola.app.domain.model.TeacherApprovalStatus
import com.rola.app.domain.model.TeacherReviewRecord
import com.rola.app.domain.model.TeachingActivityType

@Entity(tableName = "ai_curriculums", indices = [Index(value = ["teacherId"]), Index(value = ["institutionId"]), Index(value = ["approvalStatus"])])
data class AICurriculumEntity(
    @PrimaryKey val curriculumId: String,
    val teacherId: String,
    val institutionId: String,
    val subject: String,
    val topic: String,
    val gradeLevel: String,
    val learningObjective: String,
    val durationWeeks: Int,
    val studentLevel: SkillLevel,
    val languageCode: String,
    val title: String,
    val overview: String,
    val moduleIds: List<String>,
    val assessmentIds: List<String>,
    val scientificAccuracy: Float,
    val levelAlignment: Float,
    val objectiveCoverage: Float,
    val contentConsistency: Float,
    val qualityNotes: List<String>,
    val approvalStatus: TeacherApprovalStatus,
    val ownerUserId: String,
    val createdAt: Long,
    val updatedAt: Long,
) {
    fun toRequest(): CurriculumRequest = CurriculumRequest(subject, topic, gradeLevel, learningObjective, durationWeeks, studentLevel, languageCode)
}

@Entity(tableName = "curriculum_modules", indices = [Index(value = ["curriculumId"])])
data class CurriculumModuleEntity(
    @PrimaryKey val moduleId: String,
    val curriculumId: String,
    val title: String,
    val objective: String,
    val lessonIds: List<String>,
    val estimatedHours: Int,
)

@Entity(tableName = "lessons", indices = [Index(value = ["curriculumId"]), Index(value = ["difficulty"])])
data class GeneratedLessonEntity(
    @PrimaryKey val lessonId: String,
    val curriculumId: String,
    val title: String,
    val objectives: List<String>,
    val explanation: String,
    val examples: List<String>,
    val experiments: List<String>,
    val arActivityIds: List<String>,
    val practiceQuestions: List<String>,
    val difficulty: SkillLevel,
) {
    fun toDomain(activities: List<ARLearningActivity> = emptyList()): GeneratedLesson = GeneratedLesson(
        lessonId = lessonId,
        title = title,
        objectives = objectives,
        explanation = explanation,
        examples = examples,
        experiments = experiments,
        arActivities = activities,
        practiceQuestions = practiceQuestions,
        difficulty = difficulty,
    )
}

@Entity(tableName = "activities", indices = [Index(value = ["lessonId"]), Index(value = ["activityType"])])
data class ARLearningActivityEntity(
    @PrimaryKey val activityId: String,
    val lessonId: String,
    val title: String,
    val description: String,
    val suggestedObjectIds: List<String>,
    val activityType: TeachingActivityType,
    val estimatedMinutes: Int,
) {
    fun toDomain(): ARLearningActivity = ARLearningActivity(activityId, title, description, suggestedObjectIds, activityType, estimatedMinutes)
}

@Entity(tableName = "assessments", indices = [Index(value = ["curriculumId"]), Index(value = ["difficulty"])])
data class AITeacherAssessmentEntity(
    @PrimaryKey val assessmentId: String,
    val curriculumId: String,
    val title: String,
    val difficulty: SkillLevel,
    val mcqQuestions: List<String>,
    val practicalTasks: List<String>,
    val arAssignments: List<String>,
    val researchActivities: List<String>,
) {
    fun toDomain(): AITeacherAssessment = AITeacherAssessment(
        assessmentId,
        title,
        difficulty,
        mcqQuestions,
        practicalTasks,
        arAssignments,
        researchActivities,
    )
}

@Entity(tableName = "ai_teaching_plans", indices = [Index(value = ["teacherId"]), Index(value = ["studentLevel"])])
data class AdaptiveTeachingPlanEntity(
    @PrimaryKey val planId: String,
    val teacherId: String,
    val studentLevel: SkillLevel,
    val explanationComplexity: String,
    val learningSpeed: LearningSpeed,
    val exampleStrategy: String,
    val activityStrategy: String,
    val recommendedInterventions: List<String>,
) {
    fun toDomain(): AdaptiveTeachingPlan = AdaptiveTeachingPlan(
        planId,
        teacherId,
        studentLevel,
        explanationComplexity,
        learningSpeed,
        exampleStrategy,
        activityStrategy,
        recommendedInterventions,
    )
}

@Entity(tableName = "teacher_reviews", indices = [Index(value = ["contentId"]), Index(value = ["teacherId"]), Index(value = ["status"])])
data class TeacherReviewEntity(
    @PrimaryKey val reviewId: String,
    val contentId: String,
    val teacherId: String,
    val status: TeacherApprovalStatus,
    val comments: String,
    val reviewedAt: Long,
) {
    fun toDomain(): TeacherReviewRecord = TeacherReviewRecord(reviewId, contentId, teacherId, status, comments, reviewedAt)
}

@Entity(tableName = "lesson_analytics", indices = [Index(value = ["lessonId"]), Index(value = ["classId"]), Index(value = ["generatedAt"])])
data class LessonAnalyticsEntity(
    @PrimaryKey val analyticsId: String,
    val lessonId: String,
    val classId: String,
    val completionRate: Int,
    val engagementScore: Int,
    val averageAssessmentScore: Int,
    val difficultySignal: SkillLevel,
    val improvementNotes: List<String>,
    val generatedAt: Long,
) {
    fun toDomain(): LessonAnalytics = LessonAnalytics(
        analyticsId,
        lessonId,
        classId,
        completionRate,
        engagementScore,
        averageAssessmentScore,
        difficultySignal,
        improvementNotes,
        generatedAt,
    )
}

fun GeneratedCurriculum.toCurriculumEntity(): AICurriculumEntity = AICurriculumEntity(
    curriculumId = curriculumId,
    teacherId = teacherId,
    institutionId = institutionId,
    subject = request.subject,
    topic = request.topic,
    gradeLevel = request.gradeLevel,
    learningObjective = request.learningObjective,
    durationWeeks = request.durationWeeks,
    studentLevel = request.studentLevel,
    languageCode = request.languageCode,
    title = title,
    overview = overview,
    moduleIds = modules.map { it.moduleId },
    assessmentIds = assessments.map { it.assessmentId },
    scientificAccuracy = quality.scientificAccuracy,
    levelAlignment = quality.levelAlignment,
    objectiveCoverage = quality.objectiveCoverage,
    contentConsistency = quality.contentConsistency,
    qualityNotes = quality.notes,
    approvalStatus = approvalStatus,
    ownerUserId = ownerUserId,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun com.rola.app.domain.model.CurriculumModule.toEntity(curriculumId: String): CurriculumModuleEntity =
    CurriculumModuleEntity(moduleId, curriculumId, title, objective, lessons.map { it.lessonId }, estimatedHours)

fun GeneratedLesson.toEntity(curriculumId: String): GeneratedLessonEntity = GeneratedLessonEntity(
    lessonId,
    curriculumId,
    title,
    objectives,
    explanation,
    examples,
    experiments,
    arActivities.map { it.activityId },
    practiceQuestions,
    difficulty,
)

fun ARLearningActivity.toEntity(lessonId: String): ARLearningActivityEntity =
    ARLearningActivityEntity(activityId, lessonId, title, description, suggestedObjectIds, activityType, estimatedMinutes)

fun AITeacherAssessment.toEntity(curriculumId: String): AITeacherAssessmentEntity =
    AITeacherAssessmentEntity(assessmentId, curriculumId, title, difficulty, mcqQuestions, practicalTasks, arAssignments, researchActivities)

fun AdaptiveTeachingPlan.toEntity(): AdaptiveTeachingPlanEntity = AdaptiveTeachingPlanEntity(
    planId,
    teacherId,
    studentLevel,
    explanationComplexity,
    learningSpeed,
    exampleStrategy,
    activityStrategy,
    recommendedInterventions,
)

fun TeacherReviewRecord.toEntity(): TeacherReviewEntity = TeacherReviewEntity(reviewId, contentId, teacherId, status, comments, reviewedAt)

fun LessonAnalytics.toEntity(): LessonAnalyticsEntity = LessonAnalyticsEntity(
    analyticsId,
    lessonId,
    classId,
    completionRate,
    engagementScore,
    averageAssessmentScore,
    difficultySignal,
    improvementNotes,
    generatedAt,
)

fun AICurriculumEntity.toDomain(
    modules: List<com.rola.app.domain.model.CurriculumModule>,
    assessments: List<AITeacherAssessment>,
): GeneratedCurriculum = GeneratedCurriculum(
    curriculumId = curriculumId,
    teacherId = teacherId,
    institutionId = institutionId,
    request = toRequest(),
    title = title,
    overview = overview,
    modules = modules,
    assessments = assessments,
    quality = CurriculumQualityReport(
        reportId = "quality-$curriculumId",
        scientificAccuracy = scientificAccuracy,
        levelAlignment = levelAlignment,
        objectiveCoverage = objectiveCoverage,
        contentConsistency = contentConsistency,
        notes = qualityNotes,
    ),
    approvalStatus = approvalStatus,
    ownerUserId = ownerUserId,
    createdAt = createdAt,
    updatedAt = updatedAt,
)
