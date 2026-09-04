package com.rola.app.spatial_ai.interaction

import com.rola.app.domain.model.SpatialInteractionEvent
import com.rola.app.domain.model.SpatialInteractionType
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InteractionController @Inject constructor() {
    fun recordInteraction(
        sessionId: String,
        objectId: String,
        interactionType: SpatialInteractionType,
        durationMillis: Long,
        successSignal: String,
    ): SpatialInteractionEvent = SpatialInteractionEvent(
        eventId = "spatial-interaction-${UUID.randomUUID()}",
        sessionId = sessionId,
        objectId = objectId,
        interactionType = interactionType,
        durationMillis = durationMillis.coerceAtLeast(0),
        successSignal = successSignal.ifBlank { "completed" },
    )

    fun instructionFor(interactionType: SpatialInteractionType): String = when (interactionType) {
        SpatialInteractionType.Select -> "Select the object and listen to the AI explanation."
        SpatialInteractionType.Rotate -> "Rotate the object to inspect hidden structure."
        SpatialInteractionType.Scale -> "Scale the object to compare parts and proportions."
        SpatialInteractionType.Explode -> "Separate layers to understand internal relationships."
        SpatialInteractionType.Annotate -> "Place a label on the evidence you found."
        SpatialInteractionType.Simulate -> "Change a variable and predict the result."
    }
}
