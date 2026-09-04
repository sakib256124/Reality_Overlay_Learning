package com.rola.app.unit

import com.rola.app.enterprise.analytics.InstitutionAnalyticsEngine
import com.rola.app.enterprise.student.ParentMonitor
import com.rola.app.domain.model.AssignmentSubmission
import com.rola.app.domain.model.LMSProvider
import com.rola.app.domain.model.Permission
import com.rola.app.domain.model.StudentReport
import com.rola.app.domain.model.SubmissionStatus
import com.rola.app.domain.model.UserRole
import com.rola.app.domain.model.defaultPermissions
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class EnterprisePlatformTest {
    private val analyticsEngine = InstitutionAnalyticsEngine()
    private val parentMonitor = ParentMonitor()

    @Test
    fun teacherRoleCanCreateAssignmentsAndViewAnalytics() {
        val permissions = UserRole.Teacher.defaultPermissions

        assertTrue(Permission.CreateAssignments in permissions)
        assertTrue(Permission.ViewClassAnalytics in permissions)
        assertTrue(Permission.ReviewSubmissions in permissions)
    }

    @Test
    fun lmsProviderListCoversRequiredPlatforms() {
        val providers = LMSProvider.entries

        assertTrue(LMSProvider.Moodle in providers)
        assertTrue(LMSProvider.GoogleClassroom in providers)
        assertTrue(LMSProvider.MicrosoftTeams in providers)
        assertTrue(LMSProvider.Canvas in providers)
    }

    @Test
    fun analyticsReportCalculatesCompletionAndQuizAverage() {
        val report = analyticsEngine.classReport(
            institutionId = "school-1",
            classId = "science-101",
            studentReports = listOf(studentReport("student-1", 80), studentReport("student-2", 60)),
            submissions = listOf(
                AssignmentSubmission("submission-1", "assignment-1", "student-1", SubmissionStatus.Submitted, submittedAt = 1L),
                AssignmentSubmission("submission-2", "assignment-1", "student-2", SubmissionStatus.Assigned),
            ),
        )

        assertEquals(50, report.completionRate)
        assertEquals(70, report.averageQuizScore)
        assertEquals(60, report.engagementScore)
    }

    @Test
    fun parentDashboardAggregatesChildRecommendations() {
        val dashboard = parentMonitor.dashboard(
            parentId = "parent-1",
            childReports = listOf(studentReport("student-1", 85), studentReport("student-2", 45)),
        )

        assertEquals(2, dashboard.childReports.size)
        assertTrue(dashboard.recommendedActivities.isNotEmpty())
    }

    private fun studentReport(studentId: String, quizAverage: Int): StudentReport = StudentReport(
        reportId = "report-$studentId",
        studentId = studentId,
        classId = "science-101",
        progressPercent = quizAverage,
        completedLessons = 3,
        averageQuizScore = quizAverage,
        achievements = listOf("Class Explorer"),
        recommendations = listOf("Review AR science activity"),
    )
}
