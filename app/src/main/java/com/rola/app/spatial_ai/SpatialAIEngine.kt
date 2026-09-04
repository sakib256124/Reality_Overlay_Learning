package com.rola.app.spatial_ai

import com.rola.app.domain.model.DigitalTwin
import com.rola.app.domain.model.DigitalTwinType
import com.rola.app.domain.model.ImmersiveLearningReport
import com.rola.app.domain.model.SpatialEnvironmentMap
import com.rola.app.domain.model.SpatialInteractionEvent
import com.rola.app.domain.model.SpatialLearningWorld
import com.rola.app.domain.model.SpatialLearningWorldRequest
import com.rola.app.domain.model.SpatialPermission
import com.rola.app.domain.model.SpatialSecurityContext
import com.rola.app.domain.model.SpatialSimulation
import com.rola.app.domain.model.XRCapability
import com.rola.app.spatial_ai.digital_twin.DigitalTwinManager
import com.rola.app.spatial_ai.environment.EnvironmentUnderstanding
import com.rola.app.spatial_ai.interaction.XRDeviceManager
import com.rola.app.spatial_ai.simulation.SimulationEngine
import com.rola.app.spatial_ai.spatial_memory.SpatialMemoryManager
import com.rola.app.spatial_ai.virtual_world.WorldBuilder
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpatialAIEngine @Inject constructor(
    private val worldBuilder: WorldBuilder,
    private val environmentUnderstanding: EnvironmentUnderstanding,
    private val digitalTwinManager: DigitalTwinManager,
    private val simulationEngine: SimulationEngine,
    private val spatialSessionManager: SpatialSessionManager,
    private val spatialMemoryManager: SpatialMemoryManager,
    private val xrDeviceManager: XRDeviceManager,
) {
    fun generateWorld(
        request: SpatialLearningWorldRequest,
        securityContext: SpatialSecurityContext,
    ): SpatialLearningWorld {
        requirePermission(securityContext, SpatialPermission.CreateWorld)
        return worldBuilder.buildWorld(request)
    }

    fun understandEnvironment(
        detectedLabels: List<String>,
        depthQuality: Float,
    ): SpatialEnvironmentMap = environmentUnderstanding.analyzeRoom(detectedLabels, depthQuality)

    fun createDigitalTwin(
        sourceObjectId: String,
        name: String,
        type: DigitalTwinType,
        securityContext: SpatialSecurityContext,
    ): DigitalTwin {
        requirePermission(securityContext, SpatialPermission.ManageDigitalTwin)
        return digitalTwinManager.createTwin(sourceObjectId, name, type)
    }

    fun startLearningSession(
        learnerId: String,
        worldId: String,
        securityContext: SpatialSecurityContext,
    ) = requirePermission(securityContext, SpatialPermission.StartSpatialSession).let {
        spatialSessionManager.startSession(learnerId, worldId)
    }

    fun updateSimulation(
        simulation: SpatialSimulation,
        changedParameters: Map<String, Float>,
    ): SpatialSimulation = simulationEngine.updateSimulation(simulation, changedParameters)

    fun generateReport(
        sessionId: String,
        interactions: List<SpatialInteractionEvent>,
        simulationScore: Int,
        securityContext: SpatialSecurityContext,
    ): ImmersiveLearningReport {
        requirePermission(securityContext, SpatialPermission.ViewSpatialAnalytics)
        return spatialMemoryManager.immersiveReport(sessionId, interactions, simulationScore)
    }

    fun currentXRReadiness(): String {
        val device = xrDeviceManager.detectDefaultDevice()
        val ready = xrDeviceManager.supports(device, setOf(XRCapability.PlaneTracking, XRCapability.Depth))
        return "${device.name}: ${if (ready) "ready for spatial lessons" else "limited XR capability"} with ${xrDeviceManager.inputProfile(device)}."
    }

    private fun requirePermission(
        securityContext: SpatialSecurityContext,
        permission: SpatialPermission,
    ) {
        require(permission in securityContext.permissions) { "Missing spatial permission: ${permission.name}" }
    }
}
