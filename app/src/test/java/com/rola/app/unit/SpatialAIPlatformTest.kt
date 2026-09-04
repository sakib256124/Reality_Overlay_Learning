package com.rola.app.unit

import com.rola.app.domain.model.DigitalTwinType
import com.rola.app.domain.model.SkillLevel
import com.rola.app.domain.model.SpatialEnvironmentType
import com.rola.app.domain.model.SpatialInteractionType
import com.rola.app.domain.model.SpatialLearningStyle
import com.rola.app.domain.model.SpatialLearningWorldRequest
import com.rola.app.domain.model.SpatialPermission
import com.rola.app.domain.model.SpatialSecurityContext
import com.rola.app.domain.model.SpatialSimulation
import com.rola.app.domain.model.SpatialSimulationType
import com.rola.app.domain.model.XRCapability
import com.rola.app.spatial_ai.SpatialAIEngine
import com.rola.app.spatial_ai.SpatialSessionManager
import com.rola.app.spatial_ai.digital_twin.DigitalTwinLearningState
import com.rola.app.spatial_ai.digital_twin.DigitalTwinManager
import com.rola.app.spatial_ai.environment.EnvironmentUnderstanding
import com.rola.app.spatial_ai.interaction.InteractionController
import com.rola.app.spatial_ai.interaction.XRDeviceManager
import com.rola.app.spatial_ai.simulation.SimulationEngine
import com.rola.app.spatial_ai.spatial_memory.SpatialMemoryManager
import com.rola.app.spatial_ai.virtual_world.WorldBuilder
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SpatialAIPlatformTest {
    private val worldBuilder = WorldBuilder()
    private val environmentUnderstanding = EnvironmentUnderstanding()
    private val digitalTwinManager = DigitalTwinManager()
    private val simulationEngine = SimulationEngine()
    private val sessionManager = SpatialSessionManager()
    private val memoryManager = SpatialMemoryManager()
    private val xrDeviceManager = XRDeviceManager()
    private val interactionController = InteractionController()
    private val engine = SpatialAIEngine(
        worldBuilder = worldBuilder,
        environmentUnderstanding = environmentUnderstanding,
        digitalTwinManager = digitalTwinManager,
        simulationEngine = simulationEngine,
        spatialSessionManager = sessionManager,
        spatialMemoryManager = memoryManager,
        xrDeviceManager = xrDeviceManager,
    )

    @Test
    fun worldBuilderCreatesPhysicsSimulationRoomWithObjectsActivitiesAndGuidance() {
        val world = worldBuilder.buildWorld(
            SpatialLearningWorldRequest(
                subject = "Physics",
                topic = "Electricity",
                studentLevel = SkillLevel.Intermediate,
                learningObjective = "Build and explain a circuit.",
                learningStyle = SpatialLearningStyle.Kinesthetic,
            ),
        )

        assertEquals(SpatialEnvironmentType.PhysicsSimulationRoom, world.environmentType)
        assertTrue(world.objects.isNotEmpty())
        assertTrue(world.activities.isNotEmpty())
        assertTrue(world.aiTeacherGuidance.isNotEmpty())
    }

    @Test
    fun digitalTwinSupportsInteractiveManipulation() {
        val twin = digitalTwinManager.createTwin("heart-real", "Human Heart", DigitalTwinType.HumanAnatomy)
        val state = DigitalTwinLearningState(twin, "bloodFlow", "increase flow", "Blood flow shows system function.")

        assertTrue(twin.modelUri.endsWith(".glb"))
        assertTrue(state.readyForInteraction)
    }

    @Test
    fun environmentUnderstandingTracksSurfacesObjectsDepthAndRelationships() {
        val map = environmentUnderstanding.analyzeRoom(listOf("Desk", "Battery", "Wire"), depthQuality = 0.82f)

        assertTrue(map.surfaces.isNotEmpty())
        assertEquals(3, map.objectPositions.size)
        assertTrue(map.relationships.isNotEmpty())
        assertTrue(map.depthQuality >= 0.8f)
    }

    @Test
    fun simulationEngineCalculatesGravityExperimentAndUpdatesExplanation() {
        val simulation = SpatialSimulation(
            simulationId = "sim-1",
            title = "Gravity Experiment",
            simulationType = SpatialSimulationType.Physics,
            parameters = mapOf("gravity" to 9.8f),
            explanation = "Initial state",
        )

        val updated = simulationEngine.updateSimulation(simulation, mapOf("gravity" to 1.6f))
        val fallTime = simulationEngine.gravityExperiment(heightMeters = 10f, gravity = 9.8f)

        assertEquals(1.6f, updated.parameters["gravity"] ?: 0f, 0.01f)
        assertTrue(fallTime > 1f)
    }

    @Test
    fun spatialSessionFollowsImmersiveClassroomFlow() {
        val session = sessionManager.startSession("learner-1", "world-1")
        val advanced = sessionManager.advance(session)

        assertEquals(15, advanced.progressPercent)
        assertTrue(advanced.status.name.contains("Teacher"))
    }

    @Test
    fun immersiveReportCapturesInteractionTimeAndDifficultConcepts() {
        val event = interactionController.recordInteraction(
            sessionId = "session-1",
            objectId = "battery",
            interactionType = SpatialInteractionType.Simulate,
            durationMillis = 90_000,
            successSignal = "retry confused",
        )
        val report = memoryManager.immersiveReport("session-1", listOf(event), simulationScore = 62)

        assertEquals("session-1", report.sessionId)
        assertTrue(report.difficultConcepts.contains("battery"))
        assertTrue(report.recommendations.isNotEmpty())
    }

    @Test
    fun xrDeviceManagerChecksCapabilities() {
        val device = xrDeviceManager.detectDefaultDevice()

        assertTrue(xrDeviceManager.supports(device, setOf(XRCapability.PlaneTracking, XRCapability.Depth)))
        assertTrue(xrDeviceManager.inputProfile(device).contains("touch"))
    }

    @Test
    fun spatialEngineRequiresPermissionForWorldCreation() {
        val world = engine.generateWorld(
            request = SpatialLearningWorldRequest(
                subject = "Biology",
                topic = "Human Body",
                studentLevel = SkillLevel.Beginner,
                learningObjective = "Explore organ systems.",
                learningStyle = SpatialLearningStyle.Visual,
            ),
            securityContext = SpatialSecurityContext(
                userId = "teacher-1",
                institutionId = "institution-1",
                permissions = setOf(SpatialPermission.CreateWorld),
            ),
        )

        assertEquals(SpatialEnvironmentType.HumanBodyLab, world.environmentType)
    }
}
