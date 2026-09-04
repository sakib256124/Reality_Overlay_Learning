package com.rola.app.domain.model

data class EnterpriseUserRole(
    val userId: String,
    val institutionId: String,
    val role: UserRole,
    val permissions: List<Permission> = role.defaultPermissions,
    val active: Boolean = true,
    val updatedAt: Long = System.currentTimeMillis(),
)

enum class UserRole(val displayName: String) {
    SuperAdmin("Super Admin"),
    InstitutionAdmin("Institution Admin"),
    Teacher("Teacher"),
    Student("Student"),
    Parent("Parent"),
}

enum class Permission {
    ManagePlatform,
    ManageInstitution,
    ManageDepartments,
    ManageCourses,
    ManageClasses,
    ManageTeachers,
    ManageStudents,
    CreateAssignments,
    ReviewSubmissions,
    ViewClassAnalytics,
    ViewOwnProgress,
    ViewChildProgress,
    JoinClassroomSession,
    ManageLearningMaterials,
}

val UserRole.defaultPermissions: List<Permission>
    get() = when (this) {
        UserRole.SuperAdmin -> Permission.entries
        UserRole.InstitutionAdmin -> listOf(
            Permission.ManageInstitution,
            Permission.ManageDepartments,
            Permission.ManageCourses,
            Permission.ManageClasses,
            Permission.ManageTeachers,
            Permission.ManageStudents,
            Permission.ViewClassAnalytics,
            Permission.ManageLearningMaterials,
        )
        UserRole.Teacher -> listOf(
            Permission.ManageClasses,
            Permission.CreateAssignments,
            Permission.ReviewSubmissions,
            Permission.ViewClassAnalytics,
            Permission.ManageLearningMaterials,
        )
        UserRole.Student -> listOf(
            Permission.ViewOwnProgress,
            Permission.JoinClassroomSession,
        )
        UserRole.Parent -> listOf(Permission.ViewChildProgress)
    }

data class InstitutionProfile(
    val institutionId: String,
    val name: String,
    val type: InstitutionType,
    val domain: String,
    val region: String,
    val adminUserIds: List<String> = emptyList(),
    val active: Boolean = true,
    val updatedAt: Long = System.currentTimeMillis(),
)

enum class InstitutionType {
    School,
    University,
    TrainingCenter,
    Organization,
}

data class Department(
    val departmentId: String,
    val institutionId: String,
    val name: String,
    val description: String,
)

data class Course(
    val courseId: String,
    val institutionId: String,
    val departmentId: String,
    val title: String,
    val description: String,
    val subject: String,
)

data class ClassGroup(
    val classId: String,
    val institutionId: String,
    val courseId: String,
    val teacherId: String,
    val name: String,
    val studentIds: List<String> = emptyList(),
    val schedule: String = "",
    val active: Boolean = true,
)

data class InstitutionMember(
    val memberId: String,
    val institutionId: String,
    val userId: String,
    val role: UserRole,
    val displayName: String,
    val email: String = "",
    val parentUserIds: List<String> = emptyList(),
    val active: Boolean = true,
)

data class Assignment(
    val assignmentId: String,
    val teacherId: String,
    val classId: String,
    val title: String,
    val description: String,
    val assignmentType: AssignmentType,
    val deadline: Long,
    val evaluationCriteria: List<String>,
    val targetObjectIds: List<String> = emptyList(),
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

enum class AssignmentType {
    ArScanningTask,
    ResearchTask,
    QuizAssignment,
    LearningChallenge,
}

data class AssignmentSubmission(
    val submissionId: String,
    val assignmentId: String,
    val studentId: String,
    val status: SubmissionStatus,
    val score: Int? = null,
    val feedback: String = "",
    val submittedAt: Long? = null,
)

enum class SubmissionStatus {
    Assigned,
    InProgress,
    Submitted,
    Reviewed,
    Returned,
}

data class ClassroomSession(
    val sessionId: String,
    val classId: String,
    val teacherId: String,
    val title: String,
    val sharedObjectIds: List<String>,
    val participantIds: List<String> = emptyList(),
    val status: ClassroomSessionStatus = ClassroomSessionStatus.Scheduled,
    val startedAt: Long? = null,
    val endedAt: Long? = null,
)

enum class ClassroomSessionStatus {
    Scheduled,
    Live,
    Completed,
    Cancelled,
}

data class TeachingPlan(
    val planId: String,
    val teacherId: String,
    val topic: String,
    val arObjectSuggestions: List<String>,
    val explanation: String,
    val quizPrompts: List<String>,
    val activities: List<String>,
    val materialIds: List<String>,
)

data class InstitutionAnalyticsReport(
    val reportId: String,
    val institutionId: String,
    val classId: String? = null,
    val completionRate: Int,
    val averageQuizScore: Int,
    val engagementScore: Int,
    val weakAreas: List<String>,
    val learningTrends: List<String>,
    val generatedAt: Long = System.currentTimeMillis(),
)

data class StudentReport(
    val reportId: String,
    val studentId: String,
    val classId: String,
    val progressPercent: Int,
    val completedLessons: Int,
    val averageQuizScore: Int,
    val achievements: List<String>,
    val recommendations: List<String>,
    val updatedAt: Long = System.currentTimeMillis(),
)

data class TeacherDashboard(
    val teacherId: String,
    val classes: List<ClassGroup>,
    val assignments: List<Assignment>,
    val pendingSubmissions: List<AssignmentSubmission>,
    val analytics: List<InstitutionAnalyticsReport>,
    val suggestedMaterials: List<LearningMaterial>,
)

data class StudentDashboard(
    val studentId: String,
    val assignments: List<Assignment>,
    val report: StudentReport?,
    val achievements: List<String>,
    val recommendations: List<String>,
)

data class ParentDashboard(
    val parentId: String,
    val childReports: List<StudentReport>,
    val recommendedActivities: List<String>,
)

data class LMSConnection(
    val connectionId: String,
    val institutionId: String,
    val provider: LMSProvider,
    val status: LMSConnectionStatus,
    val syncEnabled: Boolean = false,
    val lastSyncAt: Long? = null,
)

enum class LMSProvider {
    Moodle,
    GoogleClassroom,
    MicrosoftTeams,
    Canvas,
}

enum class LMSConnectionStatus {
    NotConfigured,
    Connected,
    Syncing,
    Error,
}

data class EnterpriseAuditLog(
    val auditId: String,
    val institutionId: String,
    val actorUserId: String,
    val action: String,
    val targetType: String,
    val targetId: String,
    val timestamp: Long = System.currentTimeMillis(),
)
