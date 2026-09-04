package com.rola.app.spatial_ai

import com.rola.app.data.database.SpatialAIDao
import com.rola.app.data.database.entities.DigitalTwinEntity
import com.rola.app.data.database.entities.SpatialInteractionHistoryEntity
import com.rola.app.data.database.entities.SpatialSessionEntity
import com.rola.app.data.database.entities.SpatialSimulationEntity
import com.rola.app.data.database.entities.SpatialWorldEntity
import com.rola.app.data.database.entities.VirtualClassroomEntity
import com.rola.app.data.database.entities.VirtualLessonEntity
import com.rola.app.domain.model.DigitalTwin
import com.rola.app.domain.model.SpatialInteractionEvent
import com.rola.app.domain.model.SpatialLearningWorld
import com.rola.app.domain.model.SpatialSession
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class SpatialAIRepository @Inject constructor(
    private val spatialAIDao: SpatialAIDao,
) {
    fun observeSummary(learnerId: String): Flow<SpatialAISummary> =
        combine(
            spatialAIDao.observeSpatialWorlds(),
            spatialAIDao.observeDigitalTwins(),
            spatialAIDao.observeSessions(learnerId),
        ) { worlds, twins, sessions ->
            SpatialAISummary(
                worldCount = worlds.size,
                twinCount = twins.size,
                activeSessionCount = sessions.count { it.progressPercent < 100 },
                recentWorldTitles = worlds.take(5).map { it.title },
            )
        }

    suspend fun saveWorld(world: SpatialLearningWorld) {
        spatialAIDao.upsertWorld(
            SpatialWorldEntity(
                worldId = world.worldId,
                title = world.title,
                environmentType = world.environmentType,
                subject = world.subject,
                topic = world.topic,
                objective = world.objective,
                classroomId = world.classroom.classroomId,
                objectIds = world.objects.map { it.objectId },
                activityIds = world.activities.map { it.activityId },
                simulationIds = world.simulations.map { it.simulationId },
                aiTeacherGuidance = world.aiTeacherGuidance,
                createdAt = world.createdAt,
            ),
        )
        spatialAIDao.upsertVirtualClassroom(
            VirtualClassroomEntity(
                classroomId = world.classroom.classroomId,
                title = world.classroom.title,
                teacherPresence = world.classroom.teacherPresence,
                sharedObjectIds = world.classroom.sharedObjectIds,
                lessonFlow = world.classroom.lessonFlow,
            ),
        )
        spatialAIDao.upsertVirtualLessons(
            world.activities.map {
                VirtualLessonEntity(
                    lessonId = "virtual-lesson-${it.activityId}",
                    worldId = world.worldId,
                    topic = world.topic,
                    objective = world.objective,
                    activityIds = listOf(it.activityId),
                    assessmentPrompt = "Explain ${world.topic} using evidence from ${it.title}.",
                )
            },
        )
        spatialAIDao.upsertSimulations(
            world.simulations.map {
                SpatialSimulationEntity(
                    simulationId = it.simulationId,
                    worldId = world.worldId,
                    title = it.title,
                    simulationType = it.simulationType,
                    parameterNames = it.parameters.keys.toList(),
                    explanation = it.explanation,
                )
            },
        )
    }

    suspend fun saveDigitalTwin(twin: DigitalTwin) {
        spatialAIDao.upsertDigitalTwin(
            DigitalTwinEntity(
                twinId = twin.twinId,
                sourceObjectId = twin.sourceObjectId,
                name = twin.name,
                twinType = twin.twinType,
                modelUri = twin.modelUri,
                behaviorModel = twin.behaviorModel,
                explanation = twin.explanation,
                manipulableProperties = twin.manipulableProperties,
            ),
        )
    }

    suspend fun saveSession(session: SpatialSession) {
        spatialAIDao.upsertSession(
            SpatialSessionEntity(
                sessionId = session.sessionId,
                learnerId = session.learnerId,
                worldId = session.worldId,
                status = session.status,
                progressPercent = session.progressPercent,
                startedAt = session.startedAt,
                updatedAt = session.updatedAt,
            ),
        )
    }

    suspend fun saveInteraction(event: SpatialInteractionEvent) {
        spatialAIDao.upsertInteraction(
            SpatialInteractionHistoryEntity(
                eventId = event.eventId,
                sessionId = event.sessionId,
                objectId = event.objectId,
                interactionType = event.interactionType,
                durationMillis = event.durationMillis,
                successSignal = event.successSignal,
                timestamp = event.timestamp,
            ),
        )
    }
}

data class SpatialAISummary(
    val worldCount: Int,
    val twinCount: Int,
    val activeSessionCount: Int,
    val recentWorldTitles: List<String>,
)
