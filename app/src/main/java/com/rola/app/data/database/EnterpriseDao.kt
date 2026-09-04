package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.AssignmentEntity
import com.rola.app.data.database.entities.AssignmentSubmissionEntity
import com.rola.app.data.database.entities.ClassGroupEntity
import com.rola.app.data.database.entities.ClassroomSessionEntity
import com.rola.app.data.database.entities.CourseEntity
import com.rola.app.data.database.entities.DepartmentEntity
import com.rola.app.data.database.entities.EnterpriseAuditLogEntity
import com.rola.app.data.database.entities.EnterpriseRoleEntity
import com.rola.app.data.database.entities.InstitutionAnalyticsEntity
import com.rola.app.data.database.entities.InstitutionEntity
import com.rola.app.data.database.entities.InstitutionMemberEntity
import com.rola.app.data.database.entities.LMSConnectionEntity
import com.rola.app.data.database.entities.StudentReportEntity
import com.rola.app.domain.model.ClassroomSessionStatus
import kotlinx.coroutines.flow.Flow

@Dao
interface EnterpriseDao {
    @Query("SELECT * FROM enterprise_roles WHERE userId = :userId AND active = 1")
    fun observeRoles(userId: String): Flow<List<EnterpriseRoleEntity>>

    @Query("SELECT * FROM institutions WHERE institutionId = :institutionId LIMIT 1")
    suspend fun getInstitution(institutionId: String): InstitutionEntity?

    @Query("SELECT * FROM classes WHERE teacherId = :teacherId AND active = 1 ORDER BY name")
    fun observeTeacherClasses(teacherId: String): Flow<List<ClassGroupEntity>>

    @Query("SELECT * FROM classes WHERE studentIds LIKE '%' || :studentId || '%' AND active = 1 ORDER BY name")
    fun observeStudentClasses(studentId: String): Flow<List<ClassGroupEntity>>

    @Query("SELECT * FROM assignments WHERE teacherId = :teacherId ORDER BY deadline")
    fun observeTeacherAssignments(teacherId: String): Flow<List<AssignmentEntity>>

    @Query("SELECT * FROM assignments WHERE classId IN (:classIds) ORDER BY deadline")
    fun observeAssignmentsForClasses(classIds: List<String>): Flow<List<AssignmentEntity>>

    @Query("SELECT * FROM submissions WHERE assignmentId IN (:assignmentIds) ORDER BY submittedAt DESC")
    fun observeSubmissionsForAssignments(assignmentIds: List<String>): Flow<List<AssignmentSubmissionEntity>>

    @Query("SELECT * FROM student_reports WHERE studentId = :studentId ORDER BY updatedAt DESC LIMIT 1")
    fun observeStudentReport(studentId: String): Flow<StudentReportEntity?>

    @Query("SELECT * FROM teacher_analytics WHERE institutionId = :institutionId ORDER BY generatedAt DESC LIMIT 20")
    fun observeInstitutionAnalytics(institutionId: String): Flow<List<InstitutionAnalyticsEntity>>

    @Query("SELECT * FROM classroom_sessions WHERE classId = :classId AND status = :status ORDER BY startedAt DESC LIMIT 1")
    suspend fun getClassroomSession(classId: String, status: ClassroomSessionStatus): ClassroomSessionEntity?

    @Query("SELECT * FROM lms_connections WHERE institutionId = :institutionId")
    fun observeLmsConnections(institutionId: String): Flow<List<LMSConnectionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertRoles(roles: List<EnterpriseRoleEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertInstitutions(institutions: List<InstitutionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertDepartments(departments: List<DepartmentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertCourses(courses: List<CourseEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertClasses(classes: List<ClassGroupEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertMembers(members: List<InstitutionMemberEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAssignments(assignments: List<AssignmentEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSubmissions(submissions: List<AssignmentSubmissionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertSessions(sessions: List<ClassroomSessionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertAnalytics(reports: List<InstitutionAnalyticsEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertStudentReports(reports: List<StudentReportEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsertLmsConnections(connections: List<LMSConnectionEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAuditLogs(logs: List<EnterpriseAuditLogEntity>)
}
