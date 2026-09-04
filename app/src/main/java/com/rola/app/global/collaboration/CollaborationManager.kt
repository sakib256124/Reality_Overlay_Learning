package com.rola.app.global.collaboration

import com.rola.app.domain.model.CollaborationRoom
import com.rola.app.domain.model.CollaborationStatus
import com.rola.app.domain.model.ProjectWorkspace
import com.rola.app.domain.model.SharedLearningSession
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CollaborationManager @Inject constructor() {
    fun activateRoom(room: CollaborationRoom): CollaborationRoom =
        room.copy(status = CollaborationStatus.Active)

    fun createWorkspace(
        room: CollaborationRoom,
        title: String,
        memberIds: List<String>,
        resourceIds: List<String>,
    ): ProjectWorkspace = ProjectWorkspace(
        workspaceId = "workspace-${UUID.randomUUID()}",
        roomId = room.roomId,
        title = title,
        memberIds = memberIds.distinct().take(100),
        resourceIds = resourceIds.distinct().take(50),
        milestones = listOf("Plan", "Explore AR objects", "Share findings", "Publish reflection"),
    )

    fun sharedSession(
        room: CollaborationRoom,
        facilitatorId: String,
        topic: String,
        languageCodes: List<String>,
        objectIds: List<String>,
    ): SharedLearningSession = SharedLearningSession(
        sharedSessionId = "shared-session-${UUID.randomUUID()}",
        roomId = room.roomId,
        facilitatorId = facilitatorId,
        topic = topic,
        languageCodes = languageCodes.distinct(),
        sharedObjectIds = objectIds.distinct().take(20),
        live = room.status == CollaborationStatus.Active,
    )
}
