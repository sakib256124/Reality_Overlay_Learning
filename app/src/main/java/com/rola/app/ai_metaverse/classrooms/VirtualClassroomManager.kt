package com.rola.app.ai_metaverse.classrooms

import com.rola.app.ai_metaverse.intelligence.AIWorldBuildPlan
import com.rola.app.ai_metaverse.intelligence.LearningAvatar
import com.rola.app.ai_metaverse.intelligence.VirtualClassroomSession
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VirtualClassroomManager @Inject constructor() {
    fun openClassroom(
        buildPlan: AIWorldBuildPlan,
        avatar: LearningAvatar,
    ): VirtualClassroomSession =
        VirtualClassroomSession(
            classroomId = "metaverse-classroom-${UUID.randomUUID()}",
            worldId = buildPlan.world.worldId,
            title = "${buildPlan.world.topic} Virtual Classroom",
            participants = listOf(avatar.avatarId, "ai-teacher", "teacher-host", "robot-guide"),
            sharedObjects = buildPlan.objects3d.take(4),
            lessonFlow = listOf("Voice check-in", "Shared 3D object demonstration", "Group activity", "Virtual experiment", "Assessment reflection"),
            analyticsSignals = listOf("exploration", "collaboration", "performance", "engagement"),
        )
}

