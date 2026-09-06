package com.rola.app.spatial_computing_ai.spatial_core

import com.rola.app.spatial_computing_ai.analytics.SpatialAnalyticsManager
import com.rola.app.spatial_computing_ai.environment.SpatialEnvironmentManager
import com.rola.app.spatial_computing_ai.interaction.SpatialInteractionManager
import com.rola.app.spatial_computing_ai.learning.ImmersiveLearningManager
import com.rola.app.spatial_computing_ai.learning.SpatialCollaborationManager
import com.rola.app.spatial_computing_ai.learning.SpatialTeacherAgent
import com.rola.app.spatial_computing_ai.perception.SpatialPerceptionEngine
import com.rola.app.spatial_computing_ai.visualization.SpatialVisualizationEngine
import javax.inject.Inject

class AISpatialIntelligenceEngine @Inject constructor(
    private val environmentManager: SpatialEnvironmentManager,
    private val perceptionEngine: SpatialPerceptionEngine,
    private val interactionManager: SpatialInteractionManager,
    private val immersiveLearningManager: ImmersiveLearningManager,
    private val visualizationEngine: SpatialVisualizationEngine,
    private val teacherAgent: SpatialTeacherAgent,
    private val collaborationManager: SpatialCollaborationManager,
    private val analyticsManager: SpatialAnalyticsManager,
) {
    fun createExperience(request: SpatialLearningRequest): SpatialComputingResult {
        val environment = environmentManager.analyze(request)
        val experience = immersiveLearningManager.generate(request, environment)
        val interaction = interactionManager.capture(request)
        val visualization = visualizationEngine.visualize(experience)
        return SpatialComputingResult(
            resultId = "spatial-computing-${request.learnerId}",
            environment = environment,
            perception = perceptionEngine.perceive(request),
            experience = experience,
            interaction = interaction,
            visualization = visualization,
            teaching = teacherAgent.teach(request, visualization),
            collaboration = collaborationManager.collaborate(request),
            analytics = analyticsManager.analyze(interaction),
            status = SpatialSessionStatus.Active,
        )
    }
}
