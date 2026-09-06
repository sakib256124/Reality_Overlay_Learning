package com.rola.app.virtual_campus_ai.campus_core

import com.rola.app.virtual_campus_ai.analytics.CampusAnalyticsManager
import com.rola.app.virtual_campus_ai.avatars.AIAvatarManager
import com.rola.app.virtual_campus_ai.classrooms.VirtualClassroomManager
import com.rola.app.virtual_campus_ai.collaboration.CollaborationManager
import com.rola.app.virtual_campus_ai.collaboration.VirtualCollaborationManager
import com.rola.app.virtual_campus_ai.intelligence.CampusAssistantAgent
import com.rola.app.virtual_campus_ai.intelligence.CampusIntelligenceEngine
import com.rola.app.virtual_campus_ai.intelligence.VirtualTeacherAgent
import com.rola.app.virtual_campus_ai.management.VirtualLaboratoryEngine
import javax.inject.Inject

class VirtualCampusAIEngine @Inject constructor(
    private val digitalCampusManager: DigitalCampusManager,
    private val virtualClassroomManager: VirtualClassroomManager,
    private val aiAvatarManager: AIAvatarManager,
    private val virtualLaboratoryEngine: VirtualLaboratoryEngine,
    private val collaborationManager: CollaborationManager,
    private val virtualCollaborationManager: VirtualCollaborationManager,
    private val virtualTeacherAgent: VirtualTeacherAgent,
    private val campusAssistantAgent: CampusAssistantAgent,
    private val campusIntelligenceEngine: CampusIntelligenceEngine,
    private val campusAnalyticsManager: CampusAnalyticsManager,
) {
    fun openCampus(request: VirtualCampusRequest): VirtualCampusResult {
        val campus = digitalCampusManager.createCampus(request)
        val classroom = virtualClassroomManager.createClassroom(request, campus)
        val collaboration = virtualCollaborationManager.improveQuality(collaborationManager.coordinate(request))
        val report = campusIntelligenceEngine.analyze(campus, classroom, collaboration)
        return VirtualCampusResult(
            resultId = "virtual-campus-${request.learnerId}",
            campus = campus,
            classroom = classroom,
            avatars = aiAvatarManager.createAvatars(request),
            lab = virtualLaboratoryEngine.designLab(request),
            collaboration = collaboration,
            teacherSession = virtualTeacherAgent.conductClass(request, classroom),
            assistant = campusAssistantAgent.guide(request, campus),
            intelligence = campusAnalyticsManager.summarize(report),
            status = CampusSessionStatus.Active,
        )
    }
}
