package com.rola.app.lifelong_memory.knowledge_storage

import com.rola.app.lifelong_memory.memory_core.LifelongMemoryState
import javax.inject.Inject

class KnowledgeStorageManager @Inject constructor() {
    fun index(memory: LifelongMemoryState): List<String> =
        (memory.shortTermMemory + memory.longTermMemory).distinct().map { "semantic-index:$it" }
}
