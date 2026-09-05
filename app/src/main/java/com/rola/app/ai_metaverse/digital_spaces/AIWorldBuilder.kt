package com.rola.app.ai_metaverse.digital_spaces

import com.rola.app.ai_metaverse.intelligence.AIWorldBuildPlan
import com.rola.app.ai_metaverse.intelligence.MetaverseLearningRequest
import com.rola.app.ai_metaverse.virtual_world.VirtualWorldManager
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIWorldBuilder @Inject constructor(
    private val virtualWorldManager: VirtualWorldManager,
    private val digitalSpaceManager: DigitalSpaceManager,
) {
    fun build(request: MetaverseLearningRequest): AIWorldBuildPlan {
        val world = virtualWorldManager.createWorld(request)
        val spaces = digitalSpaceManager.spacesFor(world)
        return AIWorldBuildPlan(
            planId = "ai-world-build-${UUID.randomUUID()}",
            world = world,
            objects3d = spaces.flatMap { it.interactiveObjects }.distinct(),
            activities = spaces.flatMap { it.learningActivities }.distinct(),
            assessment = "Avatar explains ${request.learningObjective} using evidence from the virtual world.",
        )
    }
}
