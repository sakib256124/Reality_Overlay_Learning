package com.rola.app.enterprise.analytics

import com.rola.app.domain.model.AssignmentSubmission
import com.rola.app.domain.model.InstitutionAnalyticsReport
import com.rola.app.domain.model.StudentReport
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstitutionAnalyticsEngine @Inject constructor() {
    fun classReport(
        institutionId: String,
        classId: String,
        studentReports: List<StudentReport>,
        submissions: List<AssignmentSubmission>,
    ): InstitutionAnalyticsReport {
        val averageQuiz = studentReports.map { it.averageQuizScore }.average().takeIf { !it.isNaN() }?.toInt() ?: 0
        val completionRate = if (submissions.isEmpty()) {
            0
        } else {
            (submissions.count { it.submittedAt != null } * 100f / submissions.size).toInt()
        }
        return InstitutionAnalyticsReport(
            reportId = "institution-report-${UUID.randomUUID()}",
            institutionId = institutionId,
            classId = classId,
            completionRate = completionRate.coerceIn(0, 100),
            averageQuizScore = averageQuiz.coerceIn(0, 100),
            engagementScore = ((completionRate + averageQuiz) / 2).coerceIn(0, 100),
            weakAreas = studentReports.flatMap { it.recommendations }.distinct().take(5),
            learningTrends = listOf(
                "Completion rate: $completionRate%",
                "Average quiz score: $averageQuiz%",
                "Active reports: ${studentReports.size}",
            ),
        )
    }
}
