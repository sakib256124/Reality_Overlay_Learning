package com.rola.app.embodied_ai.control

import com.rola.app.domain.model.RobotInteraction
import com.rola.app.domain.model.RobotMemoryRecord
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RobotMemoryManager @Inject constructor() {
    fun buildMemory(
        robotId: String,
        studentId: String,
        interactions: List<RobotInteraction>,
        progress: Int,
        preferences: List<String>,
    ): RobotMemoryRecord =
        RobotMemoryRecord(
            memoryId = "robot-memory-${UUID.randomUUID()}",
            robotId = robotId,
            studentId = studentId,
            topic = interactions.lastOrNull()?.inputText?.substringBefore("?")?.take(60) ?: "General learning",
            previousQuestions = interactions.map { it.inputText }.filter { it.contains("?") }.takeLast(8),
            learningProgress = progress.coerceIn(0, 100),
            preferences = preferences.distinct(),
            teachingHistory = interactions.map { it.responseText }.takeLast(12),
        )
}
