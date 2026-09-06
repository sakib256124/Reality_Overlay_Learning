package com.rola.app.spatial_computing_ai.interaction

import com.rola.app.spatial_computing_ai.spatial_core.SpatialInteractionMode
import com.rola.app.spatial_computing_ai.spatial_core.SpatialInteractionRecord
import com.rola.app.spatial_computing_ai.spatial_core.SpatialLearningRequest
import javax.inject.Inject

class SpatialInteractionManager @Inject constructor() {
    fun capture(request: SpatialLearningRequest): SpatialInteractionRecord =
        SpatialInteractionRecord(
            interactionId = "interaction-${request.learnerId}",
            modes = listOf(SpatialInteractionMode.ObjectTouch, SpatialInteractionMode.Gesture, SpatialInteractionMode.Voice, SpatialInteractionMode.VirtualManipulation, SpatialInteractionMode.Collaboration),
            objectInteractions = request.detectedObjects.map { "learner interacts with $it" },
            gestures = listOf("rotate", "zoom", "point", "place"),
            collaborationTasks = listOf("shared object annotation", "group experiment"),
        )
}
