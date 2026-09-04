package com.rola.app.spatial_ai.collaboration

import com.rola.app.domain.model.CollaborativeLearningRoom
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CollaborativeLearningRoomManager @Inject constructor() {
    fun createRoom(
        teacherId: String,
        studentIds: List<String>,
        sharedWorldId: String,
        sharedObjectIds: List<String>,
        topic: String,
    ): CollaborativeLearningRoom = CollaborativeLearningRoom(
        roomId = "collaborative-room-${UUID.randomUUID()}",
        teacherId = teacherId,
        studentIds = studentIds.distinct(),
        sharedWorldId = sharedWorldId,
        sharedObjectIds = sharedObjectIds.distinct(),
        discussionPrompts = listOf(
            "What did your group observe about $topic?",
            "Which object changed your explanation?",
            "What evidence should the class compare?",
        ),
        activeExperiment = "$topic group simulation",
    )
}
