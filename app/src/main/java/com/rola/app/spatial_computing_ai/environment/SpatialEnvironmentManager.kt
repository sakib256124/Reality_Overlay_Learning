package com.rola.app.spatial_computing_ai.environment

import com.rola.app.spatial_computing_ai.spatial_core.SpatialEnvironmentProfile
import com.rola.app.spatial_computing_ai.spatial_core.SpatialLearningRequest
import javax.inject.Inject

class SpatialEnvironmentManager @Inject constructor() {
    fun analyze(request: SpatialLearningRequest): SpatialEnvironmentProfile =
        SpatialEnvironmentProfile(
            environmentId = "spatial-env-${request.learnerId}",
            roomStructure = request.roomStructure,
            objects = request.detectedObjects,
            locations = request.detectedObjects.mapIndexed { index, item -> "$item at learning-zone-${index + 1}" },
            movementPatterns = listOf("learner scan path", "object approach", "gesture focus"),
            permissionProtected = true,
        )
}
