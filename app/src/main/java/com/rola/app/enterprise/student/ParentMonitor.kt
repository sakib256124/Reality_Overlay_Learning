package com.rola.app.enterprise.student

import com.rola.app.domain.model.ParentDashboard
import com.rola.app.domain.model.StudentReport
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ParentMonitor @Inject constructor() {
    fun dashboard(
        parentId: String,
        childReports: List<StudentReport>,
    ): ParentDashboard = ParentDashboard(
        parentId = parentId,
        childReports = childReports,
        recommendedActivities = childReports.flatMap { report ->
            report.recommendations.ifEmpty { listOf("Review ${report.classId} progress with your child.") }
        }.distinct().take(5),
    )
}
