package com.rola.app.cognitive_ai.memory

import com.rola.app.domain.model.CognitiveActivityType
import com.rola.app.domain.model.CognitiveLearningActivity
import com.rola.app.domain.model.CognitiveMemoryRecord
import com.rola.app.domain.model.CognitiveMemoryType
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AdaptiveMemoryManager @Inject constructor() {
    fun buildMemoryRecords(activities: List<CognitiveLearningActivity>): List<CognitiveMemoryRecord> {
        val recent = activities.takeLast(12)
        val recentMistakes = recent.mapNotNull { activity -> activity.mistake?.let { activity.topic to it } }
        val learnedConcepts = recent.filter { (it.score ?: 0) >= 75 }.map { it.topic }.distinct()
        val objectives = recent.lastOrNull()?.let { listOf(it.topic) }.orEmpty()
        return buildList {
            objectives.forEach {
                add(record(recent.last().userId, CognitiveMemoryType.ShortTermObjective, it, "Current objective is $it.", 70))
            }
            recentMistakes.forEach { (topic, mistake) ->
                add(record(recent.last().userId, CognitiveMemoryType.RecentMistake, topic, mistake, 85))
                add(record(recent.last().userId, CognitiveMemoryType.KnowledgeGap, topic, "Needs prerequisite support for $topic.", 80))
            }
            learnedConcepts.forEach {
                add(record(recent.last().userId, CognitiveMemoryType.LearnedConcept, it, "Learner demonstrated understanding of $it.", 75))
            }
            if (recent.any { it.activityType == CognitiveActivityType.ARObjectExploration }) {
                add(record(recent.last().userId, CognitiveMemoryType.LearningPattern, "Spatial learning", "Learner responds to AR and 3D exploration.", 78))
            }
        }
    }

    fun knows(records: List<CognitiveMemoryRecord>): List<String> =
        records.filter { it.memoryType == CognitiveMemoryType.LearnedConcept || it.memoryType == CognitiveMemoryType.Achievement }
            .map { it.topic }
            .distinct()

    fun strugglesWith(records: List<CognitiveMemoryRecord>): List<String> =
        records.filter { it.memoryType == CognitiveMemoryType.RecentMistake || it.memoryType == CognitiveMemoryType.KnowledgeGap }
            .map { it.topic }
            .distinct()

    private fun record(
        userId: String,
        type: CognitiveMemoryType,
        topic: String,
        summary: String,
        strength: Int,
    ): CognitiveMemoryRecord = CognitiveMemoryRecord(
        memoryId = "cognitive-memory-${UUID.randomUUID()}",
        userId = userId,
        memoryType = type,
        topic = topic,
        summary = summary,
        strength = strength.coerceIn(0, 100),
    )
}
