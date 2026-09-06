package com.rola.app.unit

import com.rola.app.virtual_campus_ai.analytics.CampusAnalyticsManager
import com.rola.app.virtual_campus_ai.avatars.AIAvatarManager
import com.rola.app.virtual_campus_ai.campus_core.CampusAvatarRole
import com.rola.app.virtual_campus_ai.campus_core.CampusSessionStatus
import com.rola.app.virtual_campus_ai.campus_core.CampusSpaceType
import com.rola.app.virtual_campus_ai.campus_core.DigitalCampusManager
import com.rola.app.virtual_campus_ai.campus_core.VirtualCampusAIEngine
import com.rola.app.virtual_campus_ai.campus_core.VirtualCampusRequest
import com.rola.app.virtual_campus_ai.classrooms.VirtualClassroomManager
import com.rola.app.virtual_campus_ai.collaboration.CollaborationManager
import com.rola.app.virtual_campus_ai.collaboration.VirtualCollaborationManager
import com.rola.app.virtual_campus_ai.intelligence.CampusAssistantAgent
import com.rola.app.virtual_campus_ai.intelligence.CampusIntelligenceEngine
import com.rola.app.virtual_campus_ai.intelligence.VirtualTeacherAgent
import com.rola.app.virtual_campus_ai.management.VirtualLaboratoryEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class VirtualCampusAIPlatformTest {
    private val engine = VirtualCampusAIEngine(
        DigitalCampusManager(),
        VirtualClassroomManager(),
        AIAvatarManager(),
        VirtualLaboratoryEngine(),
        CollaborationManager(),
        VirtualCollaborationManager(),
        VirtualTeacherAgent(),
        CampusAssistantAgent(),
        CampusIntelligenceEngine(),
        CampusAnalyticsManager(),
    )

    @Test
    fun virtualCampus_createsCampusClassroomAvatarsLabCollaborationAndSecurity() {
        val result = engine.openCampus(
            VirtualCampusRequest(
                learnerId = "campus-learner",
                courseTopic = "robotics engineering",
                learningGoal = "study robotics through virtual labs",
                preferredSpace = CampusSpaceType.Laboratory,
                collaborators = listOf("teacher-avatar", "research-peer"),
                accessLevel = "verified-student",
            ),
        )

        assertEquals(CampusSessionStatus.Active, result.status)
        assertTrue(result.campus.accessManaged)
        assertTrue(result.classroom.realTimeInteraction)
        assertTrue(result.avatars.any { it.role == CampusAvatarRole.Teacher && it.voiceEnabled })
        assertTrue(result.lab.digitalTwinIntegrated)
        assertTrue(result.lab.spatialAIIntegrated)
        assertTrue(result.collaboration.sharedVirtualObjects.contains("shared 3D model"))
        assertTrue(result.teacherSession.classesConducted.contains("Virtual Physics Lab"))
        assertTrue(result.assistant.navigationGuidance.any { it.contains("robotics simulation lab") })
        assertTrue(result.intelligence.studentEngagement >= 90)
        assertTrue(result.intelligence.recommendations.any { it.contains("privacy") })
    }
}
