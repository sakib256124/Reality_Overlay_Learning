package com.rola.app.unit

import com.rola.app.spatial_computing_ai.analytics.SpatialAnalyticsManager
import com.rola.app.spatial_computing_ai.environment.SpatialEnvironmentManager
import com.rola.app.spatial_computing_ai.interaction.SpatialInteractionManager
import com.rola.app.spatial_computing_ai.learning.ImmersiveLearningManager
import com.rola.app.spatial_computing_ai.learning.SpatialCollaborationManager
import com.rola.app.spatial_computing_ai.learning.SpatialTeacherAgent
import com.rola.app.spatial_computing_ai.perception.SpatialPerceptionEngine
import com.rola.app.spatial_computing_ai.spatial_core.AISpatialIntelligenceEngine
import com.rola.app.spatial_computing_ai.spatial_core.SpatialInteractionMode
import com.rola.app.spatial_computing_ai.spatial_core.SpatialLearningRequest
import com.rola.app.spatial_computing_ai.spatial_core.SpatialSessionStatus
import com.rola.app.spatial_computing_ai.visualization.SpatialVisualizationEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SpatialComputingAIPlatformTest {
    private val engine = AISpatialIntelligenceEngine(
        SpatialEnvironmentManager(),
        SpatialPerceptionEngine(),
        SpatialInteractionManager(),
        ImmersiveLearningManager(),
        SpatialVisualizationEngine(),
        SpatialTeacherAgent(),
        SpatialCollaborationManager(),
        SpatialAnalyticsManager(),
    )

    @Test
    fun spatialComputing_createsImmersiveEnvironmentInteractionAndCollaboration() {
        val result = engine.createExperience(
            SpatialLearningRequest(
                learnerId = "spatial-learner",
                topic = "engineering physics",
                studentLevel = "intermediate",
                roomStructure = "lab room with open floor",
                detectedObjects = listOf("human heart model", "gravity ramp"),
                depthSignals = listOf("depth plane stable", "object anchor locked"),
                voiceCommand = "start gravity experiment",
            ),
        )

        assertEquals(SpatialSessionStatus.Active, result.status)
        assertTrue(result.environment.permissionProtected)
        assertTrue(result.perception.recognizedObjects.contains("gravity ramp"))
        assertTrue(result.experience.arVrAssets.contains("AR overlay"))
        assertTrue(result.interaction.modes.contains(SpatialInteractionMode.Gesture))
        assertTrue(result.visualization.models3d.contains("digital twin object"))
        assertTrue(result.teaching.guidedExperiments.isNotEmpty())
        assertTrue(result.collaboration.sharedSpaces.contains("AR laboratory"))
        assertTrue(result.analytics.engagementScore >= 90)
    }
}
