package com.rola.app.ai_metaverse.intelligence

import com.rola.app.ai_metaverse.analytics.MetaverseAnalyticsEngine
import com.rola.app.ai_metaverse.avatars.LearningAvatarManager
import com.rola.app.ai_metaverse.classrooms.MetaverseTeacherAgent
import com.rola.app.ai_metaverse.classrooms.VirtualClassroomManager
import com.rola.app.ai_metaverse.collaboration.VirtualLearningCommunityManager
import com.rola.app.ai_metaverse.digital_spaces.AIWorldBuilder
import com.rola.app.ai_metaverse.digital_twin.MetaverseDigitalTwinManager
import com.rola.app.ai_metaverse.economy.MetaverseLearningEconomyManager
import com.rola.app.ai_metaverse.governance.MetaverseGovernanceManager
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetaverseEducationEngine @Inject constructor(
    private val worldBuilder: AIWorldBuilder,
    private val avatarManager: LearningAvatarManager,
    private val classroomManager: VirtualClassroomManager,
    private val teacherAgent: MetaverseTeacherAgent,
    private val digitalTwinManager: MetaverseDigitalTwinManager,
    private val communityManager: VirtualLearningCommunityManager,
    private val economyManager: MetaverseLearningEconomyManager,
    private val worldController: AIWorldController,
    private val analyticsEngine: MetaverseAnalyticsEngine,
    private val governanceManager: MetaverseGovernanceManager,
) {
    fun createLearningUniverse(request: MetaverseLearningRequest): MetaverseEducationResult {
        val buildPlan = worldBuilder.build(request)
        val avatar = avatarManager.createAvatar(request)
        val classroom = classroomManager.openClassroom(buildPlan, avatar)
        return MetaverseEducationResult(
            resultId = "metaverse-result-${UUID.randomUUID()}",
            request = request,
            buildPlan = buildPlan,
            avatar = avatar,
            teacherAction = teacherAgent.teach(avatar, classroom),
            classroomSession = classroom,
            digitalTwinPlan = digitalTwinManager.prepareTwin(buildPlan),
            community = communityManager.createCommunity(request),
            economyPlan = economyManager.planAssetExchange(buildPlan),
            worldDecision = worldController.respondToAction(request, "avatar explored shared 3D object"),
            analyticsReport = analyticsEngine.report(avatar, classroom),
            governanceRecord = governanceManager.review(request),
        )
    }
}
