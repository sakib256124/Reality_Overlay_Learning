package com.rola.app.virtual_campus_ai.campus_core

enum class CampusSpaceType { University, Classroom, Laboratory, Library, ResearchCenter }
enum class CampusAvatarRole { Teacher, StudentAssistant, ResearchAssistant, Mentor }
enum class CampusSessionStatus { Planning, Active, Collaborative, Completed, AccessRestricted }

data class VirtualCampusRequest(
    val learnerId: String,
    val courseTopic: String,
    val learningGoal: String,
    val preferredSpace: CampusSpaceType,
    val collaborators: List<String>,
    val accessLevel: String,
)

data class VirtualCampusProfile(val campusId: String, val buildings: List<String>, val classrooms: List<String>, val laboratories: List<String>, val libraries: List<String>, val researchCenters: List<String>, val accessManaged: Boolean)
data class VirtualClassroomPlan(val classroomId: String, val title: String, val interactiveLessons: List<String>, val discussionSpaces: List<String>, val sharedLearningObjects: List<String>, val realTimeInteraction: Boolean)
data class CampusAvatar(val avatarId: String, val role: CampusAvatarRole, val voiceEnabled: Boolean, val expressionModel: String, val communicationStyle: String, val personalizedBehavior: String)
data class VirtualLabPlan(val labId: String, val experiments: List<String>, val engineeringSimulations: List<String>, val medicalTraining: List<String>, val industrialLearning: List<String>, val digitalTwinIntegrated: Boolean, val spatialAIIntegrated: Boolean)
data class CampusCollaborationPlan(val collaborationId: String, val participants: List<String>, val sharedVirtualObjects: List<String>, val communicationChannels: List<String>, val collaborativeTasks: List<String>)
data class VirtualTeacherSession(val teacherSessionId: String, val classesConducted: List<String>, val conceptExplanations: List<String>, val discussionPrompts: List<String>, val learnerEvaluations: List<String>)
data class CampusAssistantResponse(val assistantId: String, val navigationGuidance: List<String>, val courseRecommendations: List<String>, val resourceDiscovery: List<String>, val transparentReasoning: String)
data class CampusIntelligenceReport(val reportId: String, val learningActivities: List<String>, val campusUsage: List<String>, val studentEngagement: Int, val educationQuality: Int, val recommendations: List<String>)
data class VirtualCampusResult(
    val resultId: String,
    val campus: VirtualCampusProfile,
    val classroom: VirtualClassroomPlan,
    val avatars: List<CampusAvatar>,
    val lab: VirtualLabPlan,
    val collaboration: CampusCollaborationPlan,
    val teacherSession: VirtualTeacherSession,
    val assistant: CampusAssistantResponse,
    val intelligence: CampusIntelligenceReport,
    val status: CampusSessionStatus,
)
