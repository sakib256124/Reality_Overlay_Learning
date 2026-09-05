package com.rola.app.ai_metaverse.digital_spaces

import com.rola.app.ai_metaverse.intelligence.DigitalLearningSpace
import com.rola.app.ai_metaverse.intelligence.VirtualEducationWorldState
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DigitalSpaceManager @Inject constructor() {
    fun spacesFor(world: VirtualEducationWorldState): List<DigitalLearningSpace> =
        world.spaces.map { name ->
            DigitalLearningSpace(
                spaceId = "digital-space-${UUID.randomUUID()}",
                worldId = world.worldId,
                name = name,
                spaceType = world.worldType,
                interactiveObjects = listOf("${world.topic} 3D model", "Shared board", "Assessment console"),
                learningActivities = listOf("Explore ${world.topic}", "Collaborate with avatars", "Reflect with AI teacher"),
            )
        }
}
