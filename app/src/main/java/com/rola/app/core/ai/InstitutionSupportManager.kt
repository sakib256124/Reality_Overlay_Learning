package com.rola.app.core.ai

import com.rola.app.data.research.ResearchRepository
import com.rola.app.domain.model.InstitutionClassroom
import com.rola.app.domain.model.TeacherDashboardState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InstitutionSupportManager @Inject constructor(
    private val researchRepository: ResearchRepository,
) {
    suspend fun teacherDashboard(
        teacherId: String,
        classrooms: List<InstitutionClassroom>,
    ): TeacherDashboardState {
        val domainTopics = classrooms.map { it.domain }.distinct()
        val materials = domainTopics.flatMap { topic -> researchRepository.materialsForTutor(topic) }.take(12)
        return TeacherDashboardState(
            teacherId = teacherId,
            classrooms = classrooms,
            studentProgressSummary = classrooms.associate { classroom ->
                classroom.classroomId to classroom.studentIds.size
            },
            recommendedMaterials = materials,
            analyticsHighlights = listOf(
                "Classrooms managed: ${classrooms.size}",
                "Students enrolled: ${classrooms.sumOf { it.studentIds.size }}",
                "Domains covered: ${domainTopics.joinToString()}",
            ),
        )
    }
}
