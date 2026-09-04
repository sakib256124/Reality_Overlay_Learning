package com.rola.app.domain.model

data class EducationalRobot(
    val robotId: String,
    val name: String,
    val robotType: EducationalRobotType,
    val capabilities: Set<RobotCapability>,
    val sensors: Set<RobotSensor>,
    val teachingModes: Set<RobotTeachingMode>,
    val batteryStatus: RobotBatteryStatus,
    val connectionStatus: RobotConnectionStatus,
    val learningEnvironment: String,
)

enum class EducationalRobotType {
    Humanoid,
    ClassroomRobot,
    SmartAssistant,
    MobileRobot,
    FuturePlatform,
}

enum class RobotCapability {
    VoiceConversation,
    GestureInteraction,
    ObjectRecognition,
    SceneUnderstanding,
    ExperimentGuidance,
    ARDemonstration,
    Translation,
    EmotionResponse,
}

enum class RobotSensor {
    Camera,
    Depth,
    Microphone,
    Motion,
    Touch,
    Lidar,
}

enum class RobotTeachingMode {
    Explain,
    Demonstrate,
    AskQuestions,
    GuideExperiment,
    GiveFeedback,
    ManageActivity,
}

data class RobotBatteryStatus(
    val percent: Int,
    val charging: Boolean,
) {
    val lowPowerMode: Boolean
        get() = percent < 20 && !charging
}

enum class RobotConnectionStatus {
    Offline,
    Connecting,
    Connected,
    CloudConnected,
}

data class RobotPerceptionFrame(
    val frameId: String,
    val robotId: String,
    val detectedStudents: List<String>,
    val detectedObjects: List<String>,
    val classroomEnvironment: String,
    val learningActivities: List<String>,
    val physicalInteractions: List<String>,
    val confidence: Float,
)

data class RobotVisionReport(
    val reportId: String,
    val recognizedObjects: List<String>,
    val sceneUnderstanding: String,
    val studentActivity: String,
    val classroomMonitoringNotes: List<String>,
    val confidence: Float,
)

data class RobotTeachingAction(
    val actionId: String,
    val robotId: String,
    val actionType: RobotTeachingActionType,
    val topic: String,
    val message: String,
    val demonstration: String,
    val adaptedDifficulty: SkillLevel,
)

enum class RobotTeachingActionType {
    Explain,
    AnswerQuestion,
    DemonstrateConcept,
    GuideExperiment,
    ProvideFeedback,
    ChangeStrategy,
    AskCheckQuestion,
}

data class RobotInteraction(
    val interactionId: String,
    val robotId: String,
    val studentId: String,
    val inputMode: RobotInputMode,
    val inputText: String,
    val responseText: String,
    val emotionResponse: String,
    val timestamp: Long = System.currentTimeMillis(),
)

enum class RobotInputMode {
    Voice,
    Text,
    Gesture,
    Touch,
}

data class RobotMemoryRecord(
    val memoryId: String,
    val robotId: String,
    val studentId: String,
    val topic: String,
    val previousQuestions: List<String>,
    val learningProgress: Int,
    val preferences: List<String>,
    val teachingHistory: List<String>,
    val updatedAt: Long = System.currentTimeMillis(),
)

data class RobotEmotionReport(
    val reportId: String,
    val studentId: String,
    val engagement: EngagementLevel,
    val confusionRisk: Int,
    val interestLevel: Int,
    val confidence: Int,
    val robotResponse: String,
)

data class RobotDecision(
    val decisionId: String,
    val robotId: String,
    val studentId: String,
    val decisionType: RobotDecisionType,
    val rationale: String,
    val teachingAction: RobotTeachingAction,
    val requiresTeacherApproval: Boolean,
)

enum class RobotDecisionType {
    ExplainNow,
    AskQuestion,
    ProvideExample,
    ChangeTeachingStrategy,
    StartExperiment,
    AlertTeacher,
}

data class RobotNavigationTarget(
    val targetId: String,
    val label: String,
    val coordinate: SpatialCoordinate,
    val safetyRadiusMeters: Float,
)

data class RobotNavigationPlan(
    val planId: String,
    val robotId: String,
    val target: RobotNavigationTarget,
    val pathSteps: List<String>,
    val safeToMove: Boolean,
)

data class RobotSession(
    val sessionId: String,
    val robotId: String,
    val classroomId: String,
    val teacherId: String,
    val status: RobotSessionStatus,
    val activeTopic: String,
    val startedAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)

enum class RobotSessionStatus {
    Preparing,
    Listening,
    Teaching,
    Demonstrating,
    Monitoring,
    Completed,
}

data class ClassroomRobotSupportPlan(
    val planId: String,
    val robotId: String,
    val teacherLesson: String,
    val assistanceSteps: List<String>,
    val studentInteractionPrompts: List<String>,
    val analyticsSignals: List<String>,
)

data class RobotNetworkState(
    val networkId: String,
    val institutionId: String,
    val robotIds: List<String>,
    val sharedKnowledgeTopics: List<String>,
    val centralManagementStatus: String,
)

data class RobotSafetyReport(
    val reportId: String,
    val robotId: String,
    val authenticated: Boolean,
    val communicationSecure: Boolean,
    val studentPrivacyProtected: Boolean,
    val physicalSafetyReady: Boolean,
    val permissionGranted: Boolean,
    val auditNotes: List<String>,
) {
    val safeToOperate: Boolean
        get() = authenticated && communicationSecure && studentPrivacyProtected && physicalSafetyReady && permissionGranted
}

data class RobotPerformanceProfile(
    val edgeProcessingEnabled: Boolean,
    val cloudSupportEnabled: Boolean,
    val lowLatencyMode: Boolean,
    val sensorOptimization: String,
    val batteryPlan: String,
    val offlineCapability: Boolean,
)

data class RobotSecurityContext(
    val userId: String,
    val institutionId: String,
    val permissions: Set<RobotPermission>,
)

enum class RobotPermission {
    RegisterRobot,
    StartRobotSession,
    ControlRobot,
    ViewRobotAnalytics,
    ManageRobotNetwork,
}

data class RobotDashboardState(
    val robots: List<EducationalRobot>,
    val activeSessions: List<RobotSession>,
    val recentInteractions: List<RobotInteraction>,
    val safetyReports: List<RobotSafetyReport>,
    val classroomPlans: List<ClassroomRobotSupportPlan>,
    val networkState: RobotNetworkState?,
)
