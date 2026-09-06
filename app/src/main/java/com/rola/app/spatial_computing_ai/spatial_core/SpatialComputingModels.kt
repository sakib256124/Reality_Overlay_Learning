package com.rola.app.spatial_computing_ai.spatial_core

enum class SpatialEnvironmentType { Classroom, Laboratory, Museum, ScienceField, EngineeringSpace }
enum class SpatialInteractionMode { ObjectTouch, Gesture, Voice, VirtualManipulation, Collaboration }
enum class SpatialSessionStatus { Mapping, Active, Collaborative, Completed, NeedsPermission }

data class SpatialLearningRequest(
    val learnerId: String,
    val topic: String,
    val studentLevel: String,
    val roomStructure: String,
    val detectedObjects: List<String>,
    val depthSignals: List<String>,
    val voiceCommand: String?,
)

data class SpatialEnvironmentProfile(val environmentId: String, val roomStructure: String, val objects: List<String>, val locations: List<String>, val movementPatterns: List<String>, val permissionProtected: Boolean)
data class SpatialPerceptionReport(val perceptionId: String, val recognizedObjects: List<String>, val sceneUnderstanding: String, val depthAnalysis: String, val positionTracking: List<String>, val environmentMap: List<String>)
data class ImmersiveLearningExperience(val experienceId: String, val environmentType: SpatialEnvironmentType, val virtualSpace: String, val learningActivities: List<String>, val arVrAssets: List<String>)
data class SpatialInteractionRecord(val interactionId: String, val modes: List<SpatialInteractionMode>, val objectInteractions: List<String>, val gestures: List<String>, val collaborationTasks: List<String>)
data class SpatialVisualizationPlan(val visualizationId: String, val models3d: List<String>, val interactiveDiagrams: List<String>, val arOverlays: List<String>, val virtualSimulations: List<String>)
data class SpatialTeachingSession(val teachingId: String, val objectBasedLessons: List<String>, val realWorldExplanations: List<String>, val guidedExperiments: List<String>, val demonstrations: List<String>)
data class SpatialCollaborationSession(val collaborationId: String, val learners: List<String>, val teacherInteractions: List<String>, val sharedSpaces: List<String>, val groupExperiments: List<String>)
data class SpatialIntelligenceAnalytics(val analyticsId: String, val interactionPatterns: List<String>, val explorationBehavior: List<String>, val engagementScore: Int, val spatialUnderstandingScore: Int, val recommendations: List<String>)
data class SpatialComputingResult(
    val resultId: String,
    val environment: SpatialEnvironmentProfile,
    val perception: SpatialPerceptionReport,
    val experience: ImmersiveLearningExperience,
    val interaction: SpatialInteractionRecord,
    val visualization: SpatialVisualizationPlan,
    val teaching: SpatialTeachingSession,
    val collaboration: SpatialCollaborationSession,
    val analytics: SpatialIntelligenceAnalytics,
    val status: SpatialSessionStatus,
)
