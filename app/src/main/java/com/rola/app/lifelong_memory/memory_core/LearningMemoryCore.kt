package com.rola.app.lifelong_memory.memory_core

import javax.inject.Inject

class LearningMemoryCore @Inject constructor() {
    fun store(context: LifelongMemoryContext): LifelongMemoryState =
        LifelongMemoryState(
            memoryId = "lifelong-memory-${context.userId}",
            userId = context.userId,
            shortTermMemory = listOf(context.currentLesson, context.activeGoal) + context.recentInteractions,
            longTermMemory = context.completedProjects + context.achievements + listOf("complete education history", "learning patterns"),
            userOwned = true,
        )
}
