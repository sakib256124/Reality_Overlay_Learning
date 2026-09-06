package com.rola.app.virtual_campus_ai.collaboration

import com.rola.app.virtual_campus_ai.campus_core.CampusCollaborationPlan
import com.rola.app.virtual_campus_ai.campus_core.VirtualCampusRequest
import javax.inject.Inject

class CollaborationManager @Inject constructor() {
    fun coordinate(request: VirtualCampusRequest): CampusCollaborationPlan =
        CampusCollaborationPlan(
            collaborationId = "campus-collab-${request.learnerId}",
            participants = listOf(request.learnerId) + request.collaborators,
            sharedVirtualObjects = listOf("shared 3D model", "lab instrument twin", "research board"),
            communicationChannels = listOf("voice room", "gesture annotation", "teacher moderated chat"),
            collaborativeTasks = listOf("group experiment", "project teamwork", "research review"),
        )
}
