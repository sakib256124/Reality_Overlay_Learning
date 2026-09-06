package com.rola.app.spatial_computing_ai.learning

import com.rola.app.spatial_computing_ai.spatial_core.ImmersiveLearningExperience
import com.rola.app.spatial_computing_ai.spatial_core.SpatialEnvironmentProfile
import com.rola.app.spatial_computing_ai.spatial_core.SpatialEnvironmentType
import com.rola.app.spatial_computing_ai.spatial_core.SpatialLearningRequest
import javax.inject.Inject

class ImmersiveLearningManager @Inject constructor() {
    fun generate(request: SpatialLearningRequest, environment: SpatialEnvironmentProfile): ImmersiveLearningExperience =
        ImmersiveLearningExperience(
            experienceId = "immersive-${request.learnerId}",
            environmentType = if (request.topic.contains("engineering", true)) SpatialEnvironmentType.EngineeringSpace else SpatialEnvironmentType.Laboratory,
            virtualSpace = "AR/VR ${request.topic} space for ${request.studentLevel} learner",
            learningActivities = environment.objects.map { "inspect and manipulate $it" } + "complete spatial reflection",
            arVrAssets = listOf("interactive 3D model", "AR overlay", "virtual simulation"),
        )
}
