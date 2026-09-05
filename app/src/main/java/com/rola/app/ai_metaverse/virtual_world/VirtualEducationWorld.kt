package com.rola.app.ai_metaverse.virtual_world

import com.rola.app.ai_metaverse.intelligence.MetaverseLearningRequest
import com.rola.app.ai_metaverse.intelligence.VirtualEducationWorld
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VirtualEducationWorldSystem @Inject constructor(
    private val virtualWorldManager: VirtualWorldManager,
) {
    fun enter(request: MetaverseLearningRequest): VirtualEducationWorld =
        virtualWorldManager.createWorld(request)
}

