package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.rola.app.data.database.entities.AICurriculumEntity
import com.rola.app.data.database.entities.AITeacherAssessmentEntity
import com.rola.app.data.database.entities.ARLearningActivityEntity
import com.rola.app.data.database.entities.AdaptiveTeachingPlanEntity
import com.rola.app.data.database.entities.CurriculumModuleEntity
import com.rola.app.data.database.entities.GeneratedLessonEntity
import com.rola.app.data.database.entities.LessonAnalyticsEntity
import com.rola.app.data.database.entities.TeacherReviewEntity
import com.rola.app.domain.model.TeacherApprovalStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface AITeacherDao {
    @Query("SELECT * FROM ai_curriculums WHERE teacherId = :teacherId ORDER BY updatedAt DESC")
    fun observeCurriculumsForTeacher(teacherId: String): Flow<List<AICurriculumEntity>>

    @Query("SELECT * FROM ai_curriculums WHERE institutionId = :institutionId ORDER BY updatedAt DESC")
    fun observeCurriculumsForInstitution(institutionId: String): Flow<List<AICurriculumEntity>>

    @Query("SELECT * FROM ai_curriculums WHERE approvalStatus = :status ORDER BY updatedAt DESC")
    fun observeCurriculumsByStatus(status: TeacherApprovalStatus): Flow<List<AICurriculumEntity>>

    @Query("SELECT * FROM lessons WHERE curriculumId IN (:curriculumIds) ORDER BY title")
    fun observeLessonsForCurriculums(curriculumIds: List<String>): Flow<List<GeneratedLessonEntity>>

    @Query("SELECT * FROM teacher_reviews WHERE teacherId = :teacherId ORDER BY reviewedAt DESC")
    fun observeTeacherReviews(teacherId: String): Flow<List<TeacherReviewEntity>>

    @Query("SELECT * FROM lesson_analytics WHERE classId = :classId ORDER BY generatedAt DESC LIMIT 20")
    fun observeLessonAnalytics(classId: String): Flow<List<LessonAnalyticsEntity>>

    @Query("SELECT * FROM ai_teaching_plans WHERE teacherId = :teacherId ORDER BY planId DESC LIMIT 20")
    fun observeTeachingPlans(teacherId: String): Flow<List<AdaptiveTeachingPlanEntity>>

    @Query("SELECT * FROM curriculum_modules WHERE curriculumId = :curriculumId ORDER BY title")
    suspend fun getModules(curriculumId: String): List<CurriculumModuleEntity>

    @Query("SELECT * FROM lessons WHERE curriculumId = :curriculumId ORDER BY title")
    suspend fun getLessons(curriculumId: String): List<GeneratedLessonEntity>

    @Query("SELECT * FROM activities WHERE lessonId = :lessonId ORDER BY title")
    suspend fun getActivities(lessonId: String): List<ARLearningActivityEntity>

    @Query("SELECT * FROM assessments WHERE curriculumId = :curriculumId ORDER BY title")
    suspend fun getAssessments(curriculumId: String): List<AITeacherAssessmentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCurriculum(curriculum: AICurriculumEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertModules(modules: List<CurriculumModuleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLessons(lessons: List<GeneratedLessonEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertActivities(activities: List<ARLearningActivityEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAssessments(assessments: List<AITeacherAssessmentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertTeachingPlan(plan: AdaptiveTeachingPlanEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertReview(review: TeacherReviewEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLessonAnalytics(analytics: List<LessonAnalyticsEntity>)

    @Query("UPDATE ai_curriculums SET approvalStatus = :status, updatedAt = :timestamp WHERE curriculumId = :curriculumId")
    suspend fun updateCurriculumStatus(
        curriculumId: String,
        status: TeacherApprovalStatus,
        timestamp: Long = System.currentTimeMillis(),
    )

    @Transaction
    suspend fun upsertGeneratedCurriculum(
        curriculum: AICurriculumEntity,
        modules: List<CurriculumModuleEntity>,
        lessons: List<GeneratedLessonEntity>,
        activities: List<ARLearningActivityEntity>,
        assessments: List<AITeacherAssessmentEntity>,
    ) {
        upsertCurriculum(curriculum)
        upsertModules(modules)
        upsertLessons(lessons)
        upsertActivities(activities)
        upsertAssessments(assessments)
    }
}
