package com.rola.app.ai_os.memory

import com.rola.app.ai_os.intelligence.AIOSRequest
import com.rola.app.ai_os.intelligence.EducationMemorySnapshot
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EducationMemoryCore @Inject constructor() {
    fun snapshot(request: AIOSRequest): EducationMemorySnapshot =
        EducationMemorySnapshot(
            memoryId = "education-memory-${UUID.randomUUID()}",
            shortTermMemory = listOf("Current session: ${request.activeTopic}", "Recent action: ${request.userNeed}"),
            longTermMemory = listOf("Knowledge history", "Skills", "Learning behavior", "Adaptive preferences"),
            achievementMemory = listOf("Completed AR learning", "Used AI tutor", "Entered unified AI OS workflow"),
        )
}
