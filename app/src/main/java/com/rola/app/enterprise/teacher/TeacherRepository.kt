package com.rola.app.enterprise.teacher

import com.rola.app.data.database.EnterpriseDao
import com.rola.app.data.research.ResearchRepository
import com.rola.app.domain.model.TeacherDashboard
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class TeacherRepository @Inject constructor(
    private val enterpriseDao: EnterpriseDao,
    private val researchRepository: ResearchRepository,
) {
    fun observeTeacherDashboard(
        institutionId: String,
        teacherId: String,
    ): Flow<TeacherDashboard> =
        combine(
            enterpriseDao.observeTeacherClasses(teacherId),
            enterpriseDao.observeTeacherAssignments(teacherId),
            enterpriseDao.observeInstitutionAnalytics(institutionId),
            researchRepository.observeDashboard(),
        ) { classes, assignments, analytics, research ->
            val assignmentIds = assignments.map { it.assignmentId }
            TeacherDashboard(
                teacherId = teacherId,
                classes = classes.map { it.toDomain() },
                assignments = assignments.map { it.toDomain() },
                pendingSubmissions = emptyList(),
                analytics = analytics.map { it.toDomain() },
                suggestedMaterials = research.learningMaterials.filter { material ->
                    assignmentIds.isEmpty() || assignments.any { assignment ->
                        material.topic.contains(assignment.title, ignoreCase = true) ||
                            assignment.title.contains(material.topic, ignoreCase = true)
                    }
                }.take(6),
            )
        }
}
