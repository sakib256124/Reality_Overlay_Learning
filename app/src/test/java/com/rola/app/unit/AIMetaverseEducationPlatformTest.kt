package com.rola.app.unit

import com.rola.app.ai_metaverse.analytics.MetaverseAnalyticsEngine
import com.rola.app.ai_metaverse.avatars.LearningAvatarManager
import com.rola.app.ai_metaverse.classrooms.MetaverseTeacherAgent
import com.rola.app.ai_metaverse.classrooms.VirtualClassroomManager
import com.rola.app.ai_metaverse.collaboration.VirtualLearningCommunityManager
import com.rola.app.ai_metaverse.digital_spaces.AIWorldBuilder
import com.rola.app.ai_metaverse.digital_spaces.DigitalSpaceManager
import com.rola.app.ai_metaverse.digital_twin.MetaverseDigitalTwinManager
import com.rola.app.ai_metaverse.economy.MetaverseLearningEconomyManager
import com.rola.app.ai_metaverse.governance.MetaverseGovernanceManager
import com.rola.app.ai_metaverse.intelligence.AIWorldController
import com.rola.app.ai_metaverse.intelligence.MetaverseEducationEngine
import com.rola.app.ai_metaverse.intelligence.MetaverseGovernanceDecision
import com.rola.app.ai_metaverse.intelligence.MetaverseLearningRequest
import com.rola.app.ai_metaverse.intelligence.MetaversePermission
import com.rola.app.ai_metaverse.virtual_world.VirtualWorldManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AIMetaverseEducationPlatformTest {
    private val virtualWorldManager = VirtualWorldManager()
    private val engine = MetaverseEducationEngine(
        worldBuilder = AIWorldBuilder(virtualWorldManager, DigitalSpaceManager()),
        avatarManager = LearningAvatarManager(),
        classroomManager = VirtualClassroomManager(),
        teacherAgent = MetaverseTeacherAgent(),
        digitalTwinManager = MetaverseDigitalTwinManager(),
        communityManager = VirtualLearningCommunityManager(),
        economyManager = MetaverseLearningEconomyManager(),
        worldController = AIWorldController(),
        analyticsEngine = MetaverseAnalyticsEngine(),
        governanceManager = MetaverseGovernanceManager(),
    )

    @Test
    fun metaverseCycle_generatesPersistentWorldAvatarAndClassroom() {
        val result = engine.createLearningUniverse(sampleRequest())

        assertTrue(result.buildPlan.world.persistent)
        assertTrue(result.buildPlan.objects3d.any { it.contains("3D") })
        assertEquals("local-learner", result.avatar.learnerId)
        assertTrue(result.classroomSession.lessonFlow.any { it.contains("experiment", ignoreCase = true) })
    }

    @Test
    fun metaverseCycle_supportsTeacherDigitalTwinAndCommunity() {
        val result = engine.createLearningUniverse(sampleRequest())

        assertTrue(result.teacherAction.demonstration.contains("Manipulate"))
        assertTrue(result.digitalTwinPlan.manipulableSystems.any { it.contains("scientific") })
        assertTrue(result.community.collaborativeProjects.any { it.contains("shared") })
        assertTrue(result.economyPlan.accessPolicy.contains("approved", ignoreCase = true))
    }

    @Test
    fun metaverseCycle_tracksAnalyticsAndHumanGovernedGlobalAccess() {
        val result = engine.createLearningUniverse(sampleRequest())

        assertTrue(result.analyticsReport.engagementScore >= 70)
        assertEquals(MetaverseGovernanceDecision.HumanReviewRequired, result.governanceRecord.decision)
        assertTrue(result.governanceRecord.permissions.contains(MetaversePermission.EnterWorld))
        assertTrue(result.governanceRecord.identityProtected)
    }

    private fun sampleRequest(): MetaverseLearningRequest =
        MetaverseLearningRequest(
            requestId = "metaverse-test",
            learnerId = "local-learner",
            institutionId = "local-institution",
            subject = "Science",
            topic = "Solar System",
            studentLevel = "Beginner",
            learningObjective = "Explain orbit, gravity, and scale through a virtual space lab",
            collaborationMode = "global classroom",
        )
}
