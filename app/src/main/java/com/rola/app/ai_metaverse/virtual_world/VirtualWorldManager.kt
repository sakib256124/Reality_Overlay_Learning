package com.rola.app.ai_metaverse.virtual_world

import com.rola.app.ai_metaverse.intelligence.MetaverseLearningRequest
import com.rola.app.ai_metaverse.intelligence.MetaverseWorldType
import com.rola.app.ai_metaverse.intelligence.VirtualEducationWorld
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VirtualWorldManager @Inject constructor() {
    fun createWorld(request: MetaverseLearningRequest): VirtualEducationWorld =
        VirtualEducationWorld(
            worldId = "metaverse-world-${UUID.randomUUID()}",
            title = "${request.subject} ${request.topic} Campus",
            worldType = worldTypeFor(request.subject),
            subject = request.subject,
            topic = request.topic,
            persistent = true,
            spaces = listOf("Virtual classroom", "Interactive lab", "Research zone", "Assessment studio"),
        )

    private fun worldTypeFor(subject: String): MetaverseWorldType =
        when {
            subject.contains("history", ignoreCase = true) -> MetaverseWorldType.HistoricalEnvironment
            subject.contains("engineering", ignoreCase = true) -> MetaverseWorldType.EngineeringSimulation
            subject.contains("space", ignoreCase = true) -> MetaverseWorldType.SpaceExploration
            subject.contains("science", ignoreCase = true) -> MetaverseWorldType.ScienceLaboratory
            else -> MetaverseWorldType.Campus
        }
}

