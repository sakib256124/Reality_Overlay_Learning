package com.rola.app.spatial_ai.virtual_world

import com.rola.app.domain.model.SkillLevel
import com.rola.app.domain.model.SpatialActivity
import com.rola.app.domain.model.SpatialCoordinate
import com.rola.app.domain.model.SpatialEnvironmentType
import com.rola.app.domain.model.SpatialInteractionType
import com.rola.app.domain.model.SpatialLearningStyle
import com.rola.app.domain.model.SpatialLearningWorld
import com.rola.app.domain.model.SpatialLearningWorldRequest
import com.rola.app.domain.model.SpatialObject
import com.rola.app.domain.model.SpatialSimulation
import com.rola.app.domain.model.SpatialSimulationType
import com.rola.app.domain.model.VirtualClassroom
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorldBuilder @Inject constructor() {
    fun buildWorld(request: SpatialLearningWorldRequest): SpatialLearningWorld {
        val environmentType = environmentFor(request.subject, request.topic)
        val objects = objectsFor(request.topic, environmentType)
        return SpatialLearningWorld(
            worldId = "spatial-world-${UUID.randomUUID()}",
            title = "${request.topic} Immersive Learning World",
            environmentType = environmentType,
            subject = request.subject,
            topic = request.topic,
            objective = request.learningObjective,
            classroom = VirtualClassroom(
                classroomId = "virtual-classroom-${UUID.randomUUID()}",
                title = "${request.subject} Spatial Classroom",
                teacherPresence = teacherPresenceFor(request.studentLevel, request.learningStyle),
                sharedObjectIds = objects.map { it.objectId },
                lessonFlow = listOf("Enter classroom", "AI teacher introduction", "Lesson presentation", "Object interaction", "Assessment", "Learning report"),
            ),
            objects = objects,
            activities = activitiesFor(request, objects),
            simulations = listOf(simulationFor(request.subject, request.topic)),
            aiTeacherGuidance = guidanceFor(request),
        )
    }

    private fun environmentFor(subject: String, topic: String): SpatialEnvironmentType = when {
        subject.contains("biology", ignoreCase = true) || topic.contains("body", ignoreCase = true) -> SpatialEnvironmentType.HumanBodyLab
        subject.contains("astronomy", ignoreCase = true) || topic.contains("planet", ignoreCase = true) -> SpatialEnvironmentType.PlanetExploration
        subject.contains("chemistry", ignoreCase = true) -> SpatialEnvironmentType.ChemistryLab
        subject.contains("physics", ignoreCase = true) -> SpatialEnvironmentType.PhysicsSimulationRoom
        subject.contains("engineering", ignoreCase = true) -> SpatialEnvironmentType.EngineeringStudio
        else -> SpatialEnvironmentType.VirtualClassroom
    }

    private fun objectsFor(topic: String, environmentType: SpatialEnvironmentType): List<SpatialObject> {
        val names = when (environmentType) {
            SpatialEnvironmentType.HumanBodyLab -> listOf("Heart", "Lung", "Cell", "Blood Flow")
            SpatialEnvironmentType.PlanetExploration -> listOf("Planet", "Orbit", "Moon", "Solar System")
            SpatialEnvironmentType.ChemistryLab -> listOf("Molecule", "Beaker", "Reaction Chamber", "Periodic Element")
            SpatialEnvironmentType.PhysicsSimulationRoom -> listOf("Force Vector", "Energy Meter", "Circuit Board", "Motion Track")
            SpatialEnvironmentType.EngineeringStudio -> listOf("Bridge Truss", "Gear", "Load Sensor", "Blueprint")
            SpatialEnvironmentType.VirtualClassroom -> listOf(topic, "$topic Model", "$topic Evidence", "$topic Assessment")
        }
        return names.mapIndexed { index, name ->
            SpatialObject(
                objectId = "spatial-object-${name.slug()}-$index",
                name = name,
                modelUri = "models/spatial/${name.slug()}.glb",
                position = SpatialCoordinate(x = -1f + index * 0.65f, y = 0.2f + index * 0.05f, z = -1.5f),
                scale = 0.6f,
                interactions = listOf(SpatialInteractionType.Select, SpatialInteractionType.Rotate, SpatialInteractionType.Scale, SpatialInteractionType.Annotate, SpatialInteractionType.Simulate),
            )
        }
    }

    private fun activitiesFor(
        request: SpatialLearningWorldRequest,
        objects: List<SpatialObject>,
    ): List<SpatialActivity> = listOf(
        SpatialActivity(
            activityId = "spatial-activity-${UUID.randomUUID()}",
            title = "Explore ${request.topic}",
            instructions = listOf("Select each object.", "Rotate the model.", "Add one annotation.", "Explain how it supports ${request.learningObjective}."),
            objectIds = objects.map { it.objectId },
            estimatedMinutes = (request.durationMinutes / 3).coerceAtLeast(8),
        ),
        SpatialActivity(
            activityId = "spatial-activity-${UUID.randomUUID()}",
            title = "Collaborative challenge",
            instructions = listOf("Assign roles.", "Run the simulation.", "Discuss evidence.", "Submit a group explanation."),
            objectIds = objects.take(2).map { it.objectId },
            estimatedMinutes = (request.durationMinutes / 3).coerceAtLeast(8),
        ),
    )

    private fun simulationFor(subject: String, topic: String): SpatialSimulation = SpatialSimulation(
        simulationId = "spatial-simulation-${UUID.randomUUID()}",
        title = "$topic Simulation",
        simulationType = when {
            subject.contains("chemistry", ignoreCase = true) -> SpatialSimulationType.Chemistry
            subject.contains("biology", ignoreCase = true) -> SpatialSimulationType.Biology
            subject.contains("engineering", ignoreCase = true) -> SpatialSimulationType.Engineering
            else -> SpatialSimulationType.Physics
        },
        parameters = mapOf("intensity" to 1f, "timeScale" to 1f, "complexity" to 0.5f),
        explanation = "Students manipulate variables and observe how $topic changes in the immersive environment.",
    )

    private fun teacherPresenceFor(level: SkillLevel, style: SpatialLearningStyle): String =
        "AI teacher uses ${level.name.lowercase()} explanations with ${style.name} spatial guidance."

    private fun guidanceFor(request: SpatialLearningWorldRequest): List<String> = listOf(
        "Welcome learners into the ${request.topic} environment.",
        "Connect each 3D object to ${request.learningObjective}.",
        "Ask students to predict before manipulating a simulation.",
        "End with an immersive reflection and quick assessment.",
    )

    private fun String.slug(): String = lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-')
}
