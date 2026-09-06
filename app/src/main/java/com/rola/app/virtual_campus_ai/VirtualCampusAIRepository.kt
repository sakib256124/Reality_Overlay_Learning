package com.rola.app.virtual_campus_ai

import com.rola.app.data.database.VirtualCampusAIDao
import com.rola.app.data.database.entities.VirtualCampusAIAvatarEntity
import com.rola.app.data.database.entities.VirtualCampusAnalyticsEntity
import com.rola.app.data.database.entities.VirtualCampusClassroomEntity
import com.rola.app.data.database.entities.VirtualCampusCollaborationSessionEntity
import com.rola.app.data.database.entities.VirtualCampusEntity
import com.rola.app.data.database.entities.VirtualCampusLabEntity
import com.rola.app.data.database.entities.VirtualCampusLearningActivityEntity
import com.rola.app.data.database.entities.VirtualCampusUserEntity
import com.rola.app.virtual_campus_ai.campus_core.VirtualCampusResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class VirtualCampusAIRepository @Inject constructor(private val dao: VirtualCampusAIDao) {
    fun observeDashboard(): Flow<VirtualCampusDashboardState> =
        combine(
            dao.observeCampus(),
            dao.observeClassroom(),
            dao.observeAvatar(),
            dao.observeUser(),
            dao.observeLab(),
            dao.observeCollaboration(),
            dao.observeAnalytics(),
            dao.observeActivity(),
        ) { values ->
            val campus = values[0] as VirtualCampusEntity?
            val classroom = values[1] as VirtualCampusClassroomEntity?
            val avatar = values[2] as VirtualCampusAIAvatarEntity?
            val user = values[3] as VirtualCampusUserEntity?
            val lab = values[4] as VirtualCampusLabEntity?
            val collaboration = values[5] as VirtualCampusCollaborationSessionEntity?
            val analytics = values[6] as VirtualCampusAnalyticsEntity?
            val activity = values[7] as VirtualCampusLearningActivityEntity?
            VirtualCampusDashboardState(
                activeClassrooms = listOfNotNull(classroom?.title) + campus?.classrooms.orEmpty(),
                aiTeachers = listOfNotNull(avatar?.role, avatar?.communicationStyle),
                studentActivities = activity?.classesConducted.orEmpty() + activity?.conceptExplanations.orEmpty(),
                virtualLabs = lab?.experiments.orEmpty() + lab?.engineeringSimulations.orEmpty(),
                collaboration = collaboration?.collaborativeTasks.orEmpty() + collaboration?.communicationChannels.orEmpty(),
                learningAnalytics = analytics?.studentEngagement ?: 0,
                educationQuality = analytics?.educationQuality ?: 0,
                campusResources = campus?.libraries.orEmpty() + campus?.researchCenters.orEmpty(),
                securityStatus = if (user?.privacyProtected == true && campus?.accessManaged == true) "Virtual identity, avatar security, access control, and privacy active." else "",
            )
        }

    suspend fun save(result: VirtualCampusResult, requestAccessLevel: String) {
        dao.upsertCampus(VirtualCampusEntity(result.campus.campusId, result.campus.buildings, result.campus.classrooms, result.campus.laboratories, result.campus.libraries, result.campus.researchCenters, result.campus.accessManaged))
        dao.upsertClassroom(VirtualCampusClassroomEntity(result.classroom.classroomId, result.classroom.title, result.classroom.interactiveLessons, result.classroom.discussionSpaces, result.classroom.sharedLearningObjects, result.classroom.realTimeInteraction))
        result.avatars.forEach { dao.upsertAvatar(VirtualCampusAIAvatarEntity(it.avatarId, it.role.name, it.voiceEnabled, it.expressionModel, it.communicationStyle, it.personalizedBehavior)) }
        dao.upsertUser(VirtualCampusUserEntity(result.collaboration.participants.first(), requestAccessLevel, "verified campus identity", "signed avatar session", privacyProtected = true))
        dao.upsertLab(VirtualCampusLabEntity(result.lab.labId, result.lab.experiments, result.lab.engineeringSimulations, result.lab.medicalTraining, result.lab.industrialLearning, result.lab.digitalTwinIntegrated, result.lab.spatialAIIntegrated))
        dao.upsertCollaboration(VirtualCampusCollaborationSessionEntity(result.collaboration.collaborationId, result.collaboration.participants, result.collaboration.sharedVirtualObjects, result.collaboration.communicationChannels, result.collaboration.collaborativeTasks))
        dao.upsertAnalytics(VirtualCampusAnalyticsEntity(result.intelligence.reportId, result.intelligence.learningActivities, result.intelligence.campusUsage, result.intelligence.studentEngagement, result.intelligence.educationQuality, result.intelligence.recommendations))
        dao.upsertActivity(VirtualCampusLearningActivityEntity("activity-${result.teacherSession.teacherSessionId}", result.teacherSession.teacherSessionId, result.teacherSession.classesConducted, result.teacherSession.conceptExplanations, result.teacherSession.learnerEvaluations, result.status.name))
    }
}

data class VirtualCampusDashboardState(
    val activeClassrooms: List<String> = emptyList(),
    val aiTeachers: List<String> = emptyList(),
    val studentActivities: List<String> = emptyList(),
    val virtualLabs: List<String> = emptyList(),
    val collaboration: List<String> = emptyList(),
    val learningAnalytics: Int = 0,
    val educationQuality: Int = 0,
    val campusResources: List<String> = emptyList(),
    val securityStatus: String = "",
)
