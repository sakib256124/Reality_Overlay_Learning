package com.rola.app.ai_metaverse.virtual_world

import com.rola.app.ai_metaverse.intelligence.MetaverseLearningRequest
import com.rola.app.ai_metaverse.intelligence.VirtualEducationWorldState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VirtualEducationWorld @Inject constructor(
    private val virtualWorldManager: VirtualWorldManager,
) {
    fun enter(request: MetaverseLearningRequest): VirtualEducationWorldState =
        virtualWorldManager.createWorld(request)
}
