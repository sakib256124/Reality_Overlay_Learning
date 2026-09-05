package com.rola.app.ai_metaverse.intelligence

enum class MetaverseWorldType {
    Campus,
    Classroom,
    ScienceLaboratory,
    HistoricalEnvironment,
    EngineeringSimulation,
    SpaceExploration,
}

enum class AvatarRole {
    Student,
    Teacher,
    Researcher,
    AITeacher,
    RobotGuide,
}

enum class MetaversePermission {
    EnterWorld,
    JoinClassroom,
    UseVoice,
    ManipulateObjects,
    RunExperiment,
    ShareKnowledge,
}

enum class MetaverseGovernanceDecision {
    Allowed,
    HumanReviewRequired,
    Restricted,
}

data class MetaverseLearningRequest(
    val requestId: String,
    val learnerId: String,
    val institutionId: String,
    val subject: String,
    val topic: String,
    val studentLevel: String,
    val learningObjective: String,
    val collaborationMode: String,
)

data class VirtualEducationWorld(
    val worldId: String,
    val title: String,
    val worldType: MetaverseWorldType,
    val subject: String,
    val topic: String,
    val persistent: Boolean,
    val spaces: List<String>,
)

data class DigitalLearningSpace(
    val spaceId: String,
    val worldId: String,
    val name: String,
    val spaceType: MetaverseWorldType,
    val interactiveObjects: List<String>,
    val learningActivities: List<String>,
)

data class AIWorldBuildPlan(
    val planId: String,
    val world: VirtualEducationWorld,
    val objects3d: List<String>,
    val activities: List<String>,
    val assessment: String,
)

data class LearningAvatar(
    val avatarId: String,
    val learnerId: String,
    val displayName: String,
    val role: AvatarRole,
    val learningHistory: List<String>,
    val skills: List<String>,
    val achievements: List<String>,
    val knowledgeLevel: String,
    val personalityProfile: String,
    val learningGoals: List<String>,
)

data class MetaverseTeacherAction(
    val actionId: String,
    val teacherAgentId: String,
    val topic: String,
    val message: String,
    val demonstration: String,
    val adaptedMethod: String,
)

data class VirtualClassroomSession(
    val classroomId: String,
    val worldId: String,
    val title: String,
    val participants: List<String>,
    val sharedObjects: List<String>,
    val lessonFlow: List<String>,
    val analyticsSignals: List<String>,
)

data class MetaverseDigitalTwinPlan(
    val twinId: String,
    val worldId: String,
    val modelType: String,
    val manipulableSystems: List<String>,
    val simulationAccuracy: String,
)

data class VirtualLearningCommunity(
    val communityId: String,
    val discussionSpaces: List<String>,
    val collaborativeProjects: List<String>,
    val sharedKnowledge: List<String>,
)

data class MetaverseLearningEconomyPlan(
    val economyId: String,
    val approvedAssetExchange: List<String>,
    val creatorCredits: List<String>,
    val accessPolicy: String,
)

data class AIWorldDecision(
    val decisionId: String,
    val observedAction: String,
    val environmentResponse: String,
    val learningImprovement: String,
    val explanation: String,
)

data class MetaverseAnalyticsReport(
    val reportId: String,
    val learnerId: String,
    val explorationScore: Int,
    val collaborationScore: Int,
    val engagementScore: Int,
    val performanceSummary: String,
)

data class MetaverseGovernanceRecord(
    val recordId: String,
    val decision: MetaverseGovernanceDecision,
    val permissions: Set<MetaversePermission>,
    val identityProtected: Boolean,
    val secureCommunication: Boolean,
    val auditNotes: List<String>,
)

data class MetaverseEducationResult(
    val resultId: String,
    val request: MetaverseLearningRequest,
    val buildPlan: AIWorldBuildPlan,
    val avatar: LearningAvatar,
    val teacherAction: MetaverseTeacherAction,
    val classroomSession: VirtualClassroomSession,
    val digitalTwinPlan: MetaverseDigitalTwinPlan,
    val community: VirtualLearningCommunity,
    val economyPlan: MetaverseLearningEconomyPlan,
    val worldDecision: AIWorldDecision,
    val analyticsReport: MetaverseAnalyticsReport,
    val governanceRecord: MetaverseGovernanceRecord,
)
