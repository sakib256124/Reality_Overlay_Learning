package com.rola.app.domain.model

data class SpatialLearningWorldRequest(
    val subject: String,
    val topic: String,
    val studentLevel: SkillLevel,
    val learningObjective: String,
    val learningStyle: SpatialLearningStyle,
    val gradeLevel: String = "",
    val durationMinutes: Int = 45,
)

enum class SpatialLearningStyle {
    Visual,
    Kinesthetic,
    Collaborative,
    InquiryBased,
}

data class SpatialLearningWorld(
    val worldId: String,
    val title: String,
    val environmentType: SpatialEnvironmentType,
    val subject: String,
    val topic: String,
    val objective: String,
    val classroom: VirtualClassroom,
    val objects: List<SpatialObject>,
    val activities: List<SpatialActivity>,
    val simulations: List<SpatialSimulation>,
    val aiTeacherGuidance: List<String>,
    val createdAt: Long = System.currentTimeMillis(),
)

enum class SpatialEnvironmentType {
    VirtualClassroom,
    HumanBodyLab,
    PlanetExploration,
    ChemistryLab,
    PhysicsSimulationRoom,
    EngineeringStudio,
}

data class VirtualClassroom(
    val classroomId: String,
    val title: String,
    val teacherPresence: String,
    val sharedObjectIds: List<String>,
    val lessonFlow: List<String>,
)

data class SpatialObject(
    val objectId: String,
    val name: String,
    val modelUri: String,
    val position: SpatialCoordinate,
    val scale: Float,
    val interactions: List<SpatialInteractionType>,
)

data class SpatialCoordinate(
    val x: Float,
    val y: Float,
    val z: Float,
)

enum class SpatialInteractionType {
    Select,
    Rotate,
    Scale,
    Explode,
    Annotate,
    Simulate,
}

data class SpatialActivity(
    val activityId: String,
    val title: String,
    val instructions: List<String>,
    val objectIds: List<String>,
    val estimatedMinutes: Int,
)

data class DigitalTwin(
    val twinId: String,
    val sourceObjectId: String,
    val name: String,
    val twinType: DigitalTwinType,
    val modelUri: String,
    val behaviorModel: String,
    val explanation: String,
    val manipulableProperties: List<String>,
)

enum class DigitalTwinType {
    RealObject,
    Machine,
    HumanAnatomy,
    ScientificModel,
    IndustrialSystem,
    EngineeringStructure,
}

data class SpatialEnvironmentMap(
    val mapId: String,
    val roomStructure: String,
    val surfaces: List<SpatialSurface>,
    val objectPositions: List<SpatialObjectPosition>,
    val relationships: List<String>,
    val depthQuality: Float,
)

data class SpatialSurface(
    val surfaceId: String,
    val surfaceType: SpatialSurfaceType,
    val center: SpatialCoordinate,
    val widthMeters: Float,
    val heightMeters: Float,
)

enum class SpatialSurfaceType {
    Floor,
    Wall,
    Table,
    Ceiling,
    Unknown,
}

data class SpatialObjectPosition(
    val objectId: String,
    val label: String,
    val coordinate: SpatialCoordinate,
    val distanceMeters: Float,
)

data class SpatialSimulation(
    val simulationId: String,
    val title: String,
    val simulationType: SpatialSimulationType,
    val parameters: Map<String, Float>,
    val explanation: String,
)

enum class SpatialSimulationType {
    Physics,
    Chemistry,
    Biology,
    Engineering,
}

data class SpatialSession(
    val sessionId: String,
    val learnerId: String,
    val worldId: String,
    val status: SpatialSessionStatus,
    val progressPercent: Int,
    val startedAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

enum class SpatialSessionStatus {
    Entered,
    TeacherIntroduction,
    LessonPresentation,
    ObjectInteraction,
    Assessment,
    ReportReady,
}

data class SpatialInteractionEvent(
    val eventId: String,
    val sessionId: String,
    val objectId: String,
    val interactionType: SpatialInteractionType,
    val durationMillis: Long,
    val successSignal: String,
    val timestamp: Long = System.currentTimeMillis(),
)

data class ImmersiveLearningReport(
    val reportId: String,
    val sessionId: String,
    val objectInteractionTimeMillis: Long,
    val explorationPattern: String,
    val simulationPerformance: Int,
    val attentionPattern: String,
    val difficultConcepts: List<String>,
    val recommendations: List<String>,
)

data class CollaborativeLearningRoom(
    val roomId: String,
    val teacherId: String,
    val studentIds: List<String>,
    val sharedWorldId: String,
    val sharedObjectIds: List<String>,
    val discussionPrompts: List<String>,
    val activeExperiment: String,
)

data class XRDeviceProfile(
    val deviceId: String,
    val name: String,
    val deviceType: XRDeviceType,
    val connected: Boolean,
    val capabilities: Set<XRCapability>,
    val batteryLevel: Int? = null,
)

enum class XRDeviceType {
    PhoneAR,
    ARGlasses,
    VRHeadset,
    MixedRealityHeadset,
    SpatialComputer,
    Unknown,
}

enum class XRCapability {
    PlaneTracking,
    Depth,
    HandTracking,
    EyeTracking,
    SpatialAudio,
    SharedAnchors,
}

data class SpatialSecurityContext(
    val userId: String,
    val institutionId: String,
    val permissions: Set<SpatialPermission>,
    val environmentPrivacyEnabled: Boolean = true,
)

enum class SpatialPermission {
    CreateWorld,
    ManageDigitalTwin,
    StartSpatialSession,
    JoinCollaborativeRoom,
    ViewSpatialAnalytics,
}
