package com.rola.app.lifelong_memory.retrieval

import com.rola.app.lifelong_memory.memory_core.LifelongMemoryContext
import com.rola.app.lifelong_memory.memory_core.LifelongMemoryState
import com.rola.app.lifelong_memory.memory_core.MemoryRetrievalResult
import javax.inject.Inject

class MemoryRetrievalEngine @Inject constructor() {
    fun retrieve(context: LifelongMemoryContext, memory: LifelongMemoryState): MemoryRetrievalResult =
        MemoryRetrievalResult(
            retrievalId = "retrieval-${context.userId}",
            recalledLessons = memory.longTermMemory.take(3),
            pastMistakes = context.recentInteractions.filter { it.contains("confuse", ignoreCase = true) || it.contains("mistake", ignoreCase = true) },
            learningPreferences = listOf("visual explanation", "short practice", "personal examples"),
            personalizedExplanation = "Connect ${context.currentLesson} to prior projects and the active goal: ${context.activeGoal}.",
        )
}
