package com.rola.app.ai_metaverse

import com.rola.app.ai_metaverse.intelligence.MetaverseEducationResult
import com.rola.app.data.database.AIMetaverseDao
import com.rola.app.data.database.entities.MetaverseAnalyticsEntity
import com.rola.app.data.database.entities.MetaverseAvatarInteractionEntity
import com.rola.app.data.database.entities.MetaverseCommunitySpaceEntity
import com.rola.app.data.database.entities.MetaverseDigitalSpaceEntity
import com.rola.app.data.database.entities.MetaverseLearningAvatarEntity
import com.rola.app.data.database.entities.MetaverseSessionEntity
import com.rola.app.data.database.entities.MetaverseVirtualClassroomEntity
import com.rola.app.data.database.entities.MetaverseVirtualExperimentEntity
import com.rola.app.data.database.entities.MetaverseVirtualWorldEntity
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class MetaverseRepository @Inject constructor(
    private val dao: AIMetaverseDao,
) {
    fun observeDashboard(institutionId: String, learnerId: String): Flow<MetaverseDashboardState> =
        combine(
            dao.observeLatestWorld(institutionId),
            dao.observeAvatar(learnerId),
            dao.observeClassrooms(),
            dao.observeCommunities(),
            dao.observeAnalytics(learnerId),
        ) { world, avatar, classrooms, communities, analytics ->
            MetaverseDashboardState(
                worldTitle = world?.title.orEmpty(),
                worldSpaces = world?.spaces.orEmpty(),
                avatarSummary = avatar?.let { "${it.displayName}: ${it.knowledgeLevel} - ${it.skills.joinToString()}" }.orEmpty(),
                classroomSummaries = classrooms.map { "${it.title}: ${it.participants.size} participants" },
                communitySpaces = communities.flatMap { it.discussionSpaces }.take(6),
                engagementScore = analytics?.engagementScore ?: 0,
                performanceSummary = analytics?.performanceSummary.orEmpty(),
                governanceDecision = analytics?.governanceDecision.orEmpty(),
            )
        }

    suspend fun saveResult(result: MetaverseEducationResult) {
        val world = result.buildPlan.world
        dao.upsertWorld(
            MetaverseVirtualWorldEntity(
                worldId = world.worldId,
                institutionId = result.request.institutionId,
                title = world.title,
                worldType = world.worldType.name,
                subject = world.subject,
                topic = world.topic,
                persistent = world.persistent,
                spaces = world.spaces,
            ),
        )
        dao.upsertSpaces(
            world.spaces.map { name ->
                MetaverseDigitalSpaceEntity(
                    spaceId = "metaverse-space-${UUID.randomUUID()}",
                    worldId = world.worldId,
                    name = name,
                    spaceType = world.worldType.name,
                    interactiveObjects = result.buildPlan.objects3d,
                    learningActivities = result.buildPlan.activities,
                )
            },
        )
        dao.upsertAvatar(
            MetaverseLearningAvatarEntity(
                avatarId = result.avatar.avatarId,
                learnerId = result.avatar.learnerId,
                displayName = result.avatar.displayName,
                role = result.avatar.role.name,
                learningHistory = result.avatar.learningHistory,
                skills = result.avatar.skills,
                achievements = result.avatar.achievements,
                knowledgeLevel = result.avatar.knowledgeLevel,
                personalityProfile = result.avatar.personalityProfile,
                learningGoals = result.avatar.learningGoals,
            ),
        )
        dao.upsertClassroom(
            MetaverseVirtualClassroomEntity(
                classroomId = result.classroomSession.classroomId,
                worldId = result.classroomSession.worldId,
                title = result.classroomSession.title,
                participants = result.classroomSession.participants,
                sharedObjects = result.classroomSession.sharedObjects,
                lessonFlow = result.classroomSession.lessonFlow,
                analyticsSignals = result.classroomSession.analyticsSignals,
            ),
        )
        dao.upsertSession(
            MetaverseSessionEntity(
                sessionId = result.resultId,
                learnerId = result.request.learnerId,
                worldId = world.worldId,
                classroomId = result.classroomSession.classroomId,
                status = "Active",
                startedAt = System.currentTimeMillis(),
            ),
        )
        dao.upsertInteraction(
            MetaverseAvatarInteractionEntity(
                interactionId = result.worldDecision.decisionId,
                avatarId = result.avatar.avatarId,
                observedAction = result.worldDecision.observedAction,
                environmentResponse = result.worldDecision.environmentResponse,
                learningImprovement = result.worldDecision.learningImprovement,
                timestamp = System.currentTimeMillis(),
            ),
        )
        dao.upsertExperiment(
            MetaverseVirtualExperimentEntity(
                experimentId = result.digitalTwinPlan.twinId,
                worldId = world.worldId,
                topic = world.topic,
                manipulableSystems = result.digitalTwinPlan.manipulableSystems,
                assessment = result.buildPlan.assessment,
                simulationAccuracy = result.digitalTwinPlan.simulationAccuracy,
            ),
        )
        dao.upsertCommunity(
            MetaverseCommunitySpaceEntity(
                communityId = result.community.communityId,
                discussionSpaces = result.community.discussionSpaces,
                collaborativeProjects = result.community.collaborativeProjects,
                sharedKnowledge = result.community.sharedKnowledge,
            ),
        )
        dao.upsertAnalytics(
            MetaverseAnalyticsEntity(
                reportId = result.analyticsReport.reportId,
                learnerId = result.analyticsReport.learnerId,
                explorationScore = result.analyticsReport.explorationScore,
                collaborationScore = result.analyticsReport.collaborationScore,
                engagementScore = result.analyticsReport.engagementScore,
                performanceSummary = result.analyticsReport.performanceSummary,
                governanceDecision = result.governanceRecord.decision.name,
            ),
        )
    }
}

data class MetaverseDashboardState(
    val worldTitle: String = "",
    val worldSpaces: List<String> = emptyList(),
    val avatarSummary: String = "",
    val classroomSummaries: List<String> = emptyList(),
    val communitySpaces: List<String> = emptyList(),
    val engagementScore: Int = 0,
    val performanceSummary: String = "",
    val governanceDecision: String = "",
)
