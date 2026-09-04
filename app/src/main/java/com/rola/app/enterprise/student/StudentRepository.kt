package com.rola.app.enterprise.student

import com.rola.app.data.database.EnterpriseDao
import com.rola.app.domain.model.StudentDashboard
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest

@Singleton
class StudentRepository @Inject constructor(
    private val enterpriseDao: EnterpriseDao,
) {
    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun observeStudentDashboard(studentId: String): Flow<StudentDashboard> =
        enterpriseDao.observeStudentClasses(studentId).flatMapLatest { classes ->
            val classIds = classes.map { it.classId }
            combine(
                enterpriseDao.observeAssignmentsForClasses(classIds.ifEmpty { listOf("__none__") }),
                enterpriseDao.observeStudentReport(studentId),
            ) { assignments, report ->
                StudentDashboard(
                    studentId = studentId,
                    assignments = assignments.map { it.toDomain() },
                    report = report?.toDomain(),
                    achievements = report?.achievements.orEmpty(),
                    recommendations = report?.recommendations.orEmpty().ifEmpty {
                        classIds.firstOrNull()?.let { listOf("Complete the next AR classroom activity.") }.orEmpty()
                    },
                )
            }
        }
}
