package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "spatial_computing_environments", indices = [Index(value = ["environmentType"])])
data class SpatialComputingEnvironmentEntity(@PrimaryKey val envId: String, val environmentType: String, val roomStructure: String, val objects: List<String>, val locations: List<String>, val movementPatterns: List<String>, val permissionProtected: Boolean)
@Entity(tableName = "spatial_computing_objects", indices = [Index(value = ["envId"])])
data class SpatialComputingObjectEntity(@PrimaryKey val objectId: String, val envId: String, val name: String, val recognitionSignals: List<String>, val positionTracking: List<String>)
@Entity(tableName = "spatial_computing_immersive_sessions", indices = [Index(value = ["status"])])
data class SpatialComputingImmersiveSessionEntity(@PrimaryKey val sessionId: String, val envId: String, val virtualSpace: String, val status: String, val learningActivities: List<String>, val arVrAssets: List<String>)
@Entity(tableName = "spatial_computing_interactions", indices = [Index(value = ["interactionId"])])
data class SpatialComputingInteractionEntity(@PrimaryKey val interactionId: String, val modes: List<String>, val objectInteractions: List<String>, val gestures: List<String>, val collaborationTasks: List<String>)
@Entity(tableName = "spatial_computing_virtual_classrooms", indices = [Index(value = ["classroomId"])])
data class SpatialComputingVirtualClassroomEntity(@PrimaryKey val classroomId: String, val sharedSpaces: List<String>, val learners: List<String>, val teacherInteractions: List<String>, val groupExperiments: List<String>)
@Entity(tableName = "spatial_computing_analytics", indices = [Index(value = ["engagementScore"])])
data class SpatialComputingAnalyticsEntity(@PrimaryKey val analyticsId: String, val engagementScore: Int, val spatialUnderstandingScore: Int, val interactionPatterns: List<String>, val explorationBehavior: List<String>, val recommendations: List<String>)
@Entity(tableName = "spatial_computing_environment_models", indices = [Index(value = ["modelId"])])
data class SpatialComputingEnvironmentModelEntity(@PrimaryKey val modelId: String, val environmentMap: List<String>, val models3d: List<String>, val arOverlays: List<String>, val virtualSimulations: List<String>)
@Entity(tableName = "spatial_computing_learning_experiences", indices = [Index(value = ["environmentType"])])
data class SpatialComputingLearningExperienceEntity(@PrimaryKey val experienceId: String, val environmentType: String, val objectBasedLessons: List<String>, val realWorldExplanations: List<String>, val guidedExperiments: List<String>, val demonstrations: List<String>)
