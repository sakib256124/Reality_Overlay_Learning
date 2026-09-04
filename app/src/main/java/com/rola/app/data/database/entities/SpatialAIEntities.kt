package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rola.app.domain.model.DigitalTwinType
import com.rola.app.domain.model.SpatialEnvironmentType
import com.rola.app.domain.model.SpatialInteractionType
import com.rola.app.domain.model.SpatialSessionStatus
import com.rola.app.domain.model.SpatialSimulationType

@Entity(tableName = "spatial_worlds", indices = [Index(value = ["subject"]), Index(value = ["topic"])])
data class SpatialWorldEntity(
    @PrimaryKey val worldId: String,
    val title: String,
    val environmentType: SpatialEnvironmentType,
    val subject: String,
    val topic: String,
    val objective: String,
    val classroomId: String,
    val objectIds: List<String>,
    val activityIds: List<String>,
    val simulationIds: List<String>,
    val aiTeacherGuidance: List<String>,
    val createdAt: Long,
)

@Entity(tableName = "digital_twins", indices = [Index(value = ["sourceObjectId"]), Index(value = ["twinType"])])
data class DigitalTwinEntity(
    @PrimaryKey val twinId: String,
    val sourceObjectId: String,
    val name: String,
    val twinType: DigitalTwinType,
    val modelUri: String,
    val behaviorModel: String,
    val explanation: String,
    val manipulableProperties: List<String>,
)

@Entity(tableName = "virtual_classrooms", indices = [Index(value = ["teacherPresence"])])
data class VirtualClassroomEntity(
    @PrimaryKey val classroomId: String,
    val title: String,
    val teacherPresence: String,
    val sharedObjectIds: List<String>,
    val lessonFlow: List<String>,
)

@Entity(tableName = "virtual_lessons", indices = [Index(value = ["worldId"]), Index(value = ["topic"])])
data class VirtualLessonEntity(
    @PrimaryKey val lessonId: String,
    val worldId: String,
    val topic: String,
    val objective: String,
    val activityIds: List<String>,
    val assessmentPrompt: String,
)

@Entity(tableName = "simulations", indices = [Index(value = ["worldId"]), Index(value = ["simulationType"])])
data class SpatialSimulationEntity(
    @PrimaryKey val simulationId: String,
    val worldId: String,
    val title: String,
    val simulationType: SpatialSimulationType,
    val parameterNames: List<String>,
    val explanation: String,
)

@Entity(tableName = "spatial_sessions", indices = [Index(value = ["learnerId"]), Index(value = ["worldId"]), Index(value = ["status"])])
data class SpatialSessionEntity(
    @PrimaryKey val sessionId: String,
    val learnerId: String,
    val worldId: String,
    val status: SpatialSessionStatus,
    val progressPercent: Int,
    val startedAt: Long,
    val updatedAt: Long,
)

@Entity(tableName = "interaction_history", indices = [Index(value = ["sessionId"]), Index(value = ["objectId"]), Index(value = ["timestamp"])])
data class SpatialInteractionHistoryEntity(
    @PrimaryKey val eventId: String,
    val sessionId: String,
    val objectId: String,
    val interactionType: SpatialInteractionType,
    val durationMillis: Long,
    val successSignal: String,
    val timestamp: Long,
)

@Entity(tableName = "learning_environments", indices = [Index(value = ["worldId"]), Index(value = ["environmentType"])])
data class LearningEnvironmentEntity(
    @PrimaryKey val environmentId: String,
    val worldId: String,
    val environmentType: SpatialEnvironmentType,
    val roomStructure: String,
    val depthQuality: Float,
    val relationships: List<String>,
)

@Entity(tableName = "spatial_3d_assets", indices = [Index(value = ["assetType"]), Index(value = ["optimized"])])
data class Spatial3DAssetEntity(
    @PrimaryKey val assetId: String,
    val name: String,
    val uri: String,
    val assetType: String,
    val optimized: Boolean,
    val storagePath: String,
)
