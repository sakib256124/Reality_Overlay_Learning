package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rola.app.domain.model.EducationalRobotType
import com.rola.app.domain.model.RobotConnectionStatus
import com.rola.app.domain.model.RobotDecisionType
import com.rola.app.domain.model.RobotInputMode
import com.rola.app.domain.model.RobotSessionStatus
import com.rola.app.domain.model.RobotTeachingActionType
import com.rola.app.domain.model.SkillLevel

@Entity(tableName = "robot_profiles", indices = [Index(value = ["robotType"]), Index(value = ["connectionStatus"])])
data class RobotProfileEntity(
    @PrimaryKey val robotId: String,
    val name: String,
    val robotType: EducationalRobotType,
    val capabilities: List<String>,
    val sensors: List<String>,
    val teachingModes: List<String>,
    val batteryPercent: Int,
    val charging: Boolean,
    val connectionStatus: RobotConnectionStatus,
    val learningEnvironment: String,
)

@Entity(tableName = "robot_sessions", indices = [Index(value = ["robotId"]), Index(value = ["classroomId"]), Index(value = ["status"])])
data class RobotSessionEntity(
    @PrimaryKey val sessionId: String,
    val robotId: String,
    val classroomId: String,
    val teacherId: String,
    val status: RobotSessionStatus,
    val activeTopic: String,
    val startedAt: Long,
    val updatedAt: Long,
)

@Entity(tableName = "robot_interactions", indices = [Index(value = ["robotId"]), Index(value = ["studentId"]), Index(value = ["timestamp"])])
data class RobotInteractionEntity(
    @PrimaryKey val interactionId: String,
    val robotId: String,
    val studentId: String,
    val inputMode: RobotInputMode,
    val inputText: String,
    val responseText: String,
    val emotionResponse: String,
    val timestamp: Long,
)

@Entity(tableName = "robot_memory", indices = [Index(value = ["robotId"]), Index(value = ["studentId"]), Index(value = ["topic"])])
data class RobotMemoryEntity(
    @PrimaryKey val memoryId: String,
    val robotId: String,
    val studentId: String,
    val topic: String,
    val previousQuestions: List<String>,
    val learningProgress: Int,
    val preferences: List<String>,
    val teachingHistory: List<String>,
    val updatedAt: Long,
)

@Entity(tableName = "robot_teaching_activities", indices = [Index(value = ["robotId"]), Index(value = ["topic"]), Index(value = ["actionType"])])
data class RobotTeachingActivityEntity(
    @PrimaryKey val actionId: String,
    val robotId: String,
    val actionType: RobotTeachingActionType,
    val topic: String,
    val message: String,
    val demonstration: String,
    val adaptedDifficulty: SkillLevel,
)

@Entity(tableName = "robot_classroom_sessions", indices = [Index(value = ["robotId"]), Index(value = ["teacherId"])])
data class RobotClassroomSessionEntity(
    @PrimaryKey val planId: String,
    val robotId: String,
    val teacherLesson: String,
    val assistanceSteps: List<String>,
    val studentInteractionPrompts: List<String>,
    val analyticsSignals: List<String>,
    val teacherId: String,
)

@Entity(tableName = "robot_analytics", indices = [Index(value = ["robotId"]), Index(value = ["studentId"])])
data class RobotAnalyticsEntity(
    @PrimaryKey val reportId: String,
    val robotId: String,
    val studentId: String,
    val engagement: String,
    val confusionRisk: Int,
    val interestLevel: Int,
    val confidence: Int,
    val robotResponse: String,
)

@Entity(tableName = "student_robot_history", indices = [Index(value = ["studentId"]), Index(value = ["robotId"])])
data class StudentRobotHistoryEntity(
    @PrimaryKey val historyId: String,
    val studentId: String,
    val robotId: String,
    val topics: List<String>,
    val decisions: List<RobotDecisionType>,
    val lastInteractionAt: Long,
)
