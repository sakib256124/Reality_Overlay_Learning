package com.rola.app.spatial_computing_ai

import com.rola.app.data.database.SpatialComputingAIDao
import com.rola.app.data.database.entities.SpatialComputingAnalyticsEntity
import com.rola.app.data.database.entities.SpatialComputingEnvironmentEntity
import com.rola.app.data.database.entities.SpatialComputingEnvironmentModelEntity
import com.rola.app.data.database.entities.SpatialComputingImmersiveSessionEntity
import com.rola.app.data.database.entities.SpatialComputingInteractionEntity
import com.rola.app.data.database.entities.SpatialComputingLearningExperienceEntity
import com.rola.app.data.database.entities.SpatialComputingObjectEntity
import com.rola.app.data.database.entities.SpatialComputingVirtualClassroomEntity
import com.rola.app.spatial_computing_ai.spatial_core.SpatialComputingResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class SpatialComputingAIRepository @Inject constructor(private val dao: SpatialComputingAIDao) {
    fun observeDashboard(): Flow<SpatialComputingDashboardState> =
        combine(
            dao.observeEnvironment(),
            dao.observeObject(),
            dao.observeSession(),
            dao.observeInteraction(),
            dao.observeClassroom(),
            dao.observeAnalytics(),
            dao.observeModel(),
            dao.observeLearningExperience(),
        ) { values ->
            val environment = values[0] as SpatialComputingEnvironmentEntity?
            val spatialObject = values[1] as SpatialComputingObjectEntity?
            val session = values[2] as SpatialComputingImmersiveSessionEntity?
            val interaction = values[3] as SpatialComputingInteractionEntity?
            val classroom = values[4] as SpatialComputingVirtualClassroomEntity?
            val analytics = values[5] as SpatialComputingAnalyticsEntity?
            val model = values[6] as SpatialComputingEnvironmentModelEntity?
            val experience = values[7] as SpatialComputingLearningExperienceEntity?
            SpatialComputingDashboardState(
                activeEnvironments = listOfNotNull(environment?.roomStructure, environment?.environmentType, spatialObject?.name),
                learningInteractions = interaction?.objectInteractions.orEmpty() + interaction?.gestures.orEmpty(),
                immersiveSessions = listOfNotNull(session?.virtualSpace, session?.status) + session?.learningActivities.orEmpty(),
                progressAnalytics = analytics?.engagementScore ?: 0,
                spatialUnderstandingScore = analytics?.spatialUnderstandingScore ?: 0,
                aiRecommendations = analytics?.recommendations.orEmpty() + model?.arOverlays.orEmpty(),
                learningExperiences = experience?.guidedExperiments.orEmpty() + experience?.demonstrations.orEmpty() + classroom?.groupExperiments.orEmpty(),
                safetyStatus = if (environment?.permissionProtected == true) "Permission protected, edge-ready spatial processing enabled." else "",
            )
        }

    suspend fun save(result: SpatialComputingResult) {
        dao.upsertEnvironment(SpatialComputingEnvironmentEntity(result.environment.environmentId, result.experience.environmentType.name, result.environment.roomStructure, result.environment.objects, result.environment.locations, result.environment.movementPatterns, result.environment.permissionProtected))
        dao.upsertObject(SpatialComputingObjectEntity("object-${result.environment.environmentId}", result.environment.environmentId, result.perception.recognizedObjects.firstOrNull().orEmpty(), result.perception.recognizedObjects, result.perception.positionTracking))
        dao.upsertSession(SpatialComputingImmersiveSessionEntity(result.experience.experienceId, result.environment.environmentId, result.experience.virtualSpace, result.status.name, result.experience.learningActivities, result.experience.arVrAssets))
        dao.upsertInteraction(SpatialComputingInteractionEntity(result.interaction.interactionId, result.interaction.modes.map { it.name }, result.interaction.objectInteractions, result.interaction.gestures, result.interaction.collaborationTasks))
        dao.upsertClassroom(SpatialComputingVirtualClassroomEntity(result.collaboration.collaborationId, result.collaboration.sharedSpaces, result.collaboration.learners, result.collaboration.teacherInteractions, result.collaboration.groupExperiments))
        dao.upsertAnalytics(SpatialComputingAnalyticsEntity(result.analytics.analyticsId, result.analytics.engagementScore, result.analytics.spatialUnderstandingScore, result.analytics.interactionPatterns, result.analytics.explorationBehavior, result.analytics.recommendations))
        dao.upsertModel(SpatialComputingEnvironmentModelEntity(result.visualization.visualizationId, result.perception.environmentMap, result.visualization.models3d, result.visualization.arOverlays, result.visualization.virtualSimulations))
        dao.upsertLearningExperience(SpatialComputingLearningExperienceEntity(result.teaching.teachingId, result.experience.environmentType.name, result.teaching.objectBasedLessons, result.teaching.realWorldExplanations, result.teaching.guidedExperiments, result.teaching.demonstrations))
    }
}

data class SpatialComputingDashboardState(
    val activeEnvironments: List<String> = emptyList(),
    val learningInteractions: List<String> = emptyList(),
    val immersiveSessions: List<String> = emptyList(),
    val progressAnalytics: Int = 0,
    val spatialUnderstandingScore: Int = 0,
    val aiRecommendations: List<String> = emptyList(),
    val learningExperiences: List<String> = emptyList(),
    val safetyStatus: String = "",
)
