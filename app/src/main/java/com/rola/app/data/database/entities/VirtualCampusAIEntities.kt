package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "virtual_campus_campuses", indices = [Index(value = ["accessManaged"])])
data class VirtualCampusEntity(@PrimaryKey val campusId: String, val buildings: List<String>, val classrooms: List<String>, val laboratories: List<String>, val libraries: List<String>, val researchCenters: List<String>, val accessManaged: Boolean)
@Entity(tableName = "virtual_campus_classrooms", indices = [Index(value = ["realTimeInteraction"])])
data class VirtualCampusClassroomEntity(@PrimaryKey val classroomId: String, val title: String, val interactiveLessons: List<String>, val discussionSpaces: List<String>, val sharedLearningObjects: List<String>, val realTimeInteraction: Boolean)
@Entity(tableName = "virtual_campus_ai_avatars", indices = [Index(value = ["role"])])
data class VirtualCampusAIAvatarEntity(@PrimaryKey val avatarId: String, val role: String, val voiceEnabled: Boolean, val expressionModel: String, val communicationStyle: String, val personalizedBehavior: String)
@Entity(tableName = "virtual_campus_users", indices = [Index(value = ["accessLevel"])])
data class VirtualCampusUserEntity(@PrimaryKey val userId: String, val accessLevel: String, val virtualIdentity: String, val avatarSecurity: String, val privacyProtected: Boolean)
@Entity(tableName = "virtual_campus_labs", indices = [Index(value = ["digitalTwinIntegrated"]), Index(value = ["spatialAIIntegrated"])])
data class VirtualCampusLabEntity(@PrimaryKey val labId: String, val experiments: List<String>, val engineeringSimulations: List<String>, val medicalTraining: List<String>, val industrialLearning: List<String>, val digitalTwinIntegrated: Boolean, val spatialAIIntegrated: Boolean)
@Entity(tableName = "virtual_campus_collaboration_sessions", indices = [Index(value = ["collaborationId"])])
data class VirtualCampusCollaborationSessionEntity(@PrimaryKey val collaborationId: String, val participants: List<String>, val sharedVirtualObjects: List<String>, val communicationChannels: List<String>, val collaborativeTasks: List<String>)
@Entity(tableName = "virtual_campus_analytics", indices = [Index(value = ["studentEngagement"]), Index(value = ["educationQuality"])])
data class VirtualCampusAnalyticsEntity(@PrimaryKey val reportId: String, val learningActivities: List<String>, val campusUsage: List<String>, val studentEngagement: Int, val educationQuality: Int, val recommendations: List<String>)
@Entity(tableName = "virtual_campus_learning_activities", indices = [Index(value = ["status"])])
data class VirtualCampusLearningActivityEntity(@PrimaryKey val activityId: String, val teacherSessionId: String, val classesConducted: List<String>, val conceptExplanations: List<String>, val learnerEvaluations: List<String>, val status: String)
