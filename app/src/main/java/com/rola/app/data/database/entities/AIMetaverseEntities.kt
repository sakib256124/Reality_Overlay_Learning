package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "metaverse_virtual_worlds", indices = [Index(value = ["institutionId"]), Index(value = ["topic"])])
data class MetaverseVirtualWorldEntity(
    @PrimaryKey val worldId: String,
    val institutionId: String,
    val title: String,
    val worldType: String,
    val subject: String,
    val topic: String,
    val persistent: Boolean,
    val spaces: List<String>,
)

@Entity(tableName = "metaverse_digital_spaces", indices = [Index(value = ["worldId"]), Index(value = ["spaceType"])])
data class MetaverseDigitalSpaceEntity(
    @PrimaryKey val spaceId: String,
    val worldId: String,
    val name: String,
    val spaceType: String,
    val interactiveObjects: List<String>,
    val learningActivities: List<String>,
)

@Entity(tableName = "metaverse_learning_avatars", indices = [Index(value = ["learnerId"]), Index(value = ["role"])])
data class MetaverseLearningAvatarEntity(
    @PrimaryKey val avatarId: String,
    val learnerId: String,
    val displayName: String,
    val role: String,
    val learningHistory: List<String>,
    val skills: List<String>,
    val achievements: List<String>,
    val knowledgeLevel: String,
    val personalityProfile: String,
    val learningGoals: List<String>,
)

@Entity(tableName = "metaverse_virtual_classrooms", indices = [Index(value = ["worldId"])])
data class MetaverseVirtualClassroomEntity(
    @PrimaryKey val classroomId: String,
    val worldId: String,
    val title: String,
    val participants: List<String>,
    val sharedObjects: List<String>,
    val lessonFlow: List<String>,
    val analyticsSignals: List<String>,
)

@Entity(tableName = "metaverse_sessions", indices = [Index(value = ["learnerId"]), Index(value = ["worldId"])])
data class MetaverseSessionEntity(
    @PrimaryKey val sessionId: String,
    val learnerId: String,
    val worldId: String,
    val classroomId: String,
    val status: String,
    val startedAt: Long,
)

@Entity(tableName = "metaverse_avatar_interactions", indices = [Index(value = ["avatarId"]), Index(value = ["timestamp"])])
data class MetaverseAvatarInteractionEntity(
    @PrimaryKey val interactionId: String,
    val avatarId: String,
    val observedAction: String,
    val environmentResponse: String,
    val learningImprovement: String,
    val timestamp: Long,
)

@Entity(tableName = "metaverse_virtual_experiments", indices = [Index(value = ["worldId"]), Index(value = ["topic"])])
data class MetaverseVirtualExperimentEntity(
    @PrimaryKey val experimentId: String,
    val worldId: String,
    val topic: String,
    val manipulableSystems: List<String>,
    val assessment: String,
    val simulationAccuracy: String,
)

@Entity(tableName = "metaverse_community_spaces", indices = [Index(value = ["communityId"])])
data class MetaverseCommunitySpaceEntity(
    @PrimaryKey val communityId: String,
    val discussionSpaces: List<String>,
    val collaborativeProjects: List<String>,
    val sharedKnowledge: List<String>,
)

@Entity(tableName = "metaverse_analytics", indices = [Index(value = ["learnerId"])])
data class MetaverseAnalyticsEntity(
    @PrimaryKey val reportId: String,
    val learnerId: String,
    val explorationScore: Int,
    val collaborationScore: Int,
    val engagementScore: Int,
    val performanceSummary: String,
    val governanceDecision: String,
)

