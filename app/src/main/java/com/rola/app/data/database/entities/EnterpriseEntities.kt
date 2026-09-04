package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rola.app.domain.model.Assignment
import com.rola.app.domain.model.AssignmentSubmission
import com.rola.app.domain.model.AssignmentType
import com.rola.app.domain.model.ClassGroup
import com.rola.app.domain.model.ClassroomSession
import com.rola.app.domain.model.ClassroomSessionStatus
import com.rola.app.domain.model.Course
import com.rola.app.domain.model.Department
import com.rola.app.domain.model.EnterpriseAuditLog
import com.rola.app.domain.model.EnterpriseUserRole
import com.rola.app.domain.model.InstitutionAnalyticsReport
import com.rola.app.domain.model.InstitutionMember
import com.rola.app.domain.model.InstitutionProfile
import com.rola.app.domain.model.InstitutionType
import com.rola.app.domain.model.LMSConnection
import com.rola.app.domain.model.LMSConnectionStatus
import com.rola.app.domain.model.LMSProvider
import com.rola.app.domain.model.Permission
import com.rola.app.domain.model.StudentReport
import com.rola.app.domain.model.SubmissionStatus
import com.rola.app.domain.model.UserRole

@Entity(tableName = "enterprise_roles", indices = [Index(value = ["institutionId"]), Index(value = ["role"])])
data class EnterpriseRoleEntity(
    @PrimaryKey val roleId: String,
    val userId: String,
    val institutionId: String,
    val role: UserRole,
    val permissions: List<String>,
    val active: Boolean,
    val updatedAt: Long,
) {
    fun toDomain(): EnterpriseUserRole = EnterpriseUserRole(
        userId = userId,
        institutionId = institutionId,
        role = role,
        permissions = permissions.mapNotNull { runCatching { Permission.valueOf(it) }.getOrNull() },
        active = active,
        updatedAt = updatedAt,
    )
}

@Entity(tableName = "institutions", indices = [Index(value = ["name"]), Index(value = ["active"])])
data class InstitutionEntity(
    @PrimaryKey val institutionId: String,
    val name: String,
    val type: InstitutionType,
    val domain: String,
    val region: String,
    val adminUserIds: List<String>,
    val active: Boolean,
    val updatedAt: Long,
) {
    fun toDomain(): InstitutionProfile = InstitutionProfile(
        institutionId = institutionId,
        name = name,
        type = type,
        domain = domain,
        region = region,
        adminUserIds = adminUserIds,
        active = active,
        updatedAt = updatedAt,
    )
}

@Entity(tableName = "departments", indices = [Index(value = ["institutionId"])])
data class DepartmentEntity(
    @PrimaryKey val departmentId: String,
    val institutionId: String,
    val name: String,
    val description: String,
) {
    fun toDomain(): Department = Department(departmentId, institutionId, name, description)
}

@Entity(tableName = "courses", indices = [Index(value = ["institutionId"]), Index(value = ["departmentId"])])
data class CourseEntity(
    @PrimaryKey val courseId: String,
    val institutionId: String,
    val departmentId: String,
    val title: String,
    val description: String,
    val subject: String,
) {
    fun toDomain(): Course = Course(courseId, institutionId, departmentId, title, description, subject)
}

@Entity(tableName = "classes", indices = [Index(value = ["institutionId"]), Index(value = ["courseId"]), Index(value = ["teacherId"])])
data class ClassGroupEntity(
    @PrimaryKey val classId: String,
    val institutionId: String,
    val courseId: String,
    val teacherId: String,
    val name: String,
    val studentIds: List<String>,
    val schedule: String,
    val active: Boolean,
) {
    fun toDomain(): ClassGroup = ClassGroup(classId, institutionId, courseId, teacherId, name, studentIds, schedule, active)
}

@Entity(tableName = "members", indices = [Index(value = ["institutionId"]), Index(value = ["userId"]), Index(value = ["role"])])
data class InstitutionMemberEntity(
    @PrimaryKey val memberId: String,
    val institutionId: String,
    val userId: String,
    val role: UserRole,
    val displayName: String,
    val email: String,
    val parentUserIds: List<String>,
    val active: Boolean,
) {
    fun toDomain(): InstitutionMember = InstitutionMember(memberId, institutionId, userId, role, displayName, email, parentUserIds, active)
}

@Entity(tableName = "assignments", indices = [Index(value = ["teacherId"]), Index(value = ["classId"]), Index(value = ["deadline"])])
data class AssignmentEntity(
    @PrimaryKey val assignmentId: String,
    val teacherId: String,
    val classId: String,
    val title: String,
    val description: String,
    val assignmentType: AssignmentType,
    val deadline: Long,
    val evaluationCriteria: List<String>,
    val targetObjectIds: List<String>,
    val createdAt: Long,
    val updatedAt: Long,
) {
    fun toDomain(): Assignment = Assignment(
        assignmentId,
        teacherId,
        classId,
        title,
        description,
        assignmentType,
        deadline,
        evaluationCriteria,
        targetObjectIds,
        createdAt,
        updatedAt,
    )
}

@Entity(tableName = "submissions", indices = [Index(value = ["assignmentId"]), Index(value = ["studentId"]), Index(value = ["status"])])
data class AssignmentSubmissionEntity(
    @PrimaryKey val submissionId: String,
    val assignmentId: String,
    val studentId: String,
    val status: SubmissionStatus,
    val score: Int?,
    val feedback: String,
    val submittedAt: Long?,
) {
    fun toDomain(): AssignmentSubmission = AssignmentSubmission(submissionId, assignmentId, studentId, status, score, feedback, submittedAt)
}

@Entity(tableName = "classroom_sessions", indices = [Index(value = ["classId"]), Index(value = ["teacherId"]), Index(value = ["status"])])
data class ClassroomSessionEntity(
    @PrimaryKey val sessionId: String,
    val classId: String,
    val teacherId: String,
    val title: String,
    val sharedObjectIds: List<String>,
    val participantIds: List<String>,
    val status: ClassroomSessionStatus,
    val startedAt: Long?,
    val endedAt: Long?,
) {
    fun toDomain(): ClassroomSession = ClassroomSession(sessionId, classId, teacherId, title, sharedObjectIds, participantIds, status, startedAt, endedAt)
}

@Entity(tableName = "teacher_analytics", indices = [Index(value = ["institutionId"]), Index(value = ["classId"]), Index(value = ["generatedAt"])])
data class InstitutionAnalyticsEntity(
    @PrimaryKey val reportId: String,
    val institutionId: String,
    val classId: String?,
    val completionRate: Int,
    val averageQuizScore: Int,
    val engagementScore: Int,
    val weakAreas: List<String>,
    val learningTrends: List<String>,
    val generatedAt: Long,
) {
    fun toDomain(): InstitutionAnalyticsReport = InstitutionAnalyticsReport(
        reportId,
        institutionId,
        classId,
        completionRate,
        averageQuizScore,
        engagementScore,
        weakAreas,
        learningTrends,
        generatedAt,
    )
}

@Entity(tableName = "student_reports", indices = [Index(value = ["studentId"]), Index(value = ["classId"])])
data class StudentReportEntity(
    @PrimaryKey val reportId: String,
    val studentId: String,
    val classId: String,
    val progressPercent: Int,
    val completedLessons: Int,
    val averageQuizScore: Int,
    val achievements: List<String>,
    val recommendations: List<String>,
    val updatedAt: Long,
) {
    fun toDomain(): StudentReport = StudentReport(
        reportId,
        studentId,
        classId,
        progressPercent,
        completedLessons,
        averageQuizScore,
        achievements,
        recommendations,
        updatedAt,
    )
}

@Entity(tableName = "lms_connections", indices = [Index(value = ["institutionId"]), Index(value = ["provider"]), Index(value = ["status"])])
data class LMSConnectionEntity(
    @PrimaryKey val connectionId: String,
    val institutionId: String,
    val provider: LMSProvider,
    val status: LMSConnectionStatus,
    val syncEnabled: Boolean,
    val lastSyncAt: Long?,
) {
    fun toDomain(): LMSConnection = LMSConnection(connectionId, institutionId, provider, status, syncEnabled, lastSyncAt)
}

@Entity(tableName = "enterprise_audit_logs", indices = [Index(value = ["institutionId"]), Index(value = ["actorUserId"]), Index(value = ["timestamp"])])
data class EnterpriseAuditLogEntity(
    @PrimaryKey val auditId: String,
    val institutionId: String,
    val actorUserId: String,
    val action: String,
    val targetType: String,
    val targetId: String,
    val timestamp: Long,
) {
    fun toDomain(): EnterpriseAuditLog = EnterpriseAuditLog(auditId, institutionId, actorUserId, action, targetType, targetId, timestamp)
}

fun EnterpriseUserRole.toEntity(): EnterpriseRoleEntity = EnterpriseRoleEntity(
    roleId = "$institutionId-$userId-${role.name}",
    userId = userId,
    institutionId = institutionId,
    role = role,
    permissions = permissions.map { it.name },
    active = active,
    updatedAt = updatedAt,
)

fun InstitutionProfile.toEntity(): InstitutionEntity = InstitutionEntity(institutionId, name, type, domain, region, adminUserIds, active, updatedAt)
fun Department.toEntity(): DepartmentEntity = DepartmentEntity(departmentId, institutionId, name, description)
fun Course.toEntity(): CourseEntity = CourseEntity(courseId, institutionId, departmentId, title, description, subject)
fun ClassGroup.toEntity(): ClassGroupEntity = ClassGroupEntity(classId, institutionId, courseId, teacherId, name, studentIds, schedule, active)
fun InstitutionMember.toEntity(): InstitutionMemberEntity = InstitutionMemberEntity(memberId, institutionId, userId, role, displayName, email, parentUserIds, active)
fun Assignment.toEntity(): AssignmentEntity = AssignmentEntity(assignmentId, teacherId, classId, title, description, assignmentType, deadline, evaluationCriteria, targetObjectIds, createdAt, updatedAt)
fun AssignmentSubmission.toEntity(): AssignmentSubmissionEntity = AssignmentSubmissionEntity(submissionId, assignmentId, studentId, status, score, feedback, submittedAt)
fun ClassroomSession.toEntity(): ClassroomSessionEntity = ClassroomSessionEntity(sessionId, classId, teacherId, title, sharedObjectIds, participantIds, status, startedAt, endedAt)
fun InstitutionAnalyticsReport.toEntity(): InstitutionAnalyticsEntity = InstitutionAnalyticsEntity(reportId, institutionId, classId, completionRate, averageQuizScore, engagementScore, weakAreas, learningTrends, generatedAt)
fun StudentReport.toEntity(): StudentReportEntity = StudentReportEntity(reportId, studentId, classId, progressPercent, completedLessons, averageQuizScore, achievements, recommendations, updatedAt)
fun LMSConnection.toEntity(): LMSConnectionEntity = LMSConnectionEntity(connectionId, institutionId, provider, status, syncEnabled, lastSyncAt)
fun EnterpriseAuditLog.toEntity(): EnterpriseAuditLogEntity = EnterpriseAuditLogEntity(auditId, institutionId, actorUserId, action, targetType, targetId, timestamp)
