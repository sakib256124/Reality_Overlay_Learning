package com.rola.app.cognitive_ai.memory

import com.rola.app.domain.model.CognitiveMemoryRecord
import com.rola.app.domain.model.CognitiveMemoryType
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemorySystem @Inject constructor(
    private val adaptiveMemoryManager: AdaptiveMemoryManager,
) {
    fun encode(records: List<CognitiveMemoryRecord>): String =
        records.joinToString(separator = "\n") { "${it.memoryType.name}:${it.topic}:${it.summary}" }

    fun shortTerm(records: List<CognitiveMemoryRecord>): List<CognitiveMemoryRecord> =
        records.filter { it.memoryType == CognitiveMemoryType.ShortTermObjective || it.memoryType == CognitiveMemoryType.RecentMistake }

    fun longTerm(records: List<CognitiveMemoryRecord>): List<CognitiveMemoryRecord> =
        records.filter {
            it.memoryType == CognitiveMemoryType.LearnedConcept ||
                it.memoryType == CognitiveMemoryType.Achievement ||
                it.memoryType == CognitiveMemoryType.KnowledgeGap ||
                it.memoryType == CognitiveMemoryType.LearningPattern
        }

    fun knownConcepts(records: List<CognitiveMemoryRecord>): List<String> =
        adaptiveMemoryManager.knows(records)

    fun strugglingConcepts(records: List<CognitiveMemoryRecord>): List<String> =
        adaptiveMemoryManager.strugglesWith(records)
}
