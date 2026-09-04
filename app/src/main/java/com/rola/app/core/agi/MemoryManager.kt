package com.rola.app.core.agi

import com.rola.app.core.agi.memory.LearnerMemory
import com.rola.app.domain.model.AGILearningEvent
import com.rola.app.domain.model.LearningGoal
import com.rola.app.domain.model.SkillGraphNode
import com.rola.app.domain.model.SkillLevel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MemoryManager @Inject constructor() {
    fun buildMemory(
        learnerId: String,
        history: List<AGILearningEvent>,
        goals: List<LearningGoal> = emptyList(),
    ): LearnerMemory {
        val mistakes = history.filter { (it.score ?: 100) < 60 }.map { it.topic }.distinct()
        val interests = history.groupingBy { it.topic }
            .eachCount()
            .entries
            .sortedByDescending { it.value }
            .map { it.key }
            .take(8)
        val averageScore = history.mapNotNull { it.score }.average().takeIf { !it.isNaN() } ?: 50.0
        val skills = interests.map { topic ->
            SkillGraphNode(
                skillId = "skill-${topic.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-')}",
                name = topic,
                mastery = history.filter { it.topic == topic }.mapNotNull { it.score }.average().takeIf { !it.isNaN() }?.toInt()
                    ?: averageScore.toInt(),
            )
        }
        return LearnerMemory(
            learnerId = learnerId,
            learningHistory = history.takeLast(200),
            skills = skills,
            knowledgeLevel = when {
                averageScore >= 85 -> SkillLevel.Advanced
                averageScore >= 65 -> SkillLevel.Intermediate
                else -> SkillLevel.Beginner
            },
            interests = interests,
            goals = goals,
            previousMistakes = mistakes,
            learningPatterns = patternsFor(history, mistakes),
        )
    }

    private fun patternsFor(
        history: List<AGILearningEvent>,
        mistakes: List<String>,
    ): List<String> = buildList {
        if (history.any { it.activityType.name.contains("AR") || it.activityType.name.contains("Object") }) {
            add("Responds to spatial and object-based learning.")
        }
        if (mistakes.isNotEmpty()) add("Needs spaced retrieval for ${mistakes.take(3).joinToString()}.")
        if (history.size >= 5) add("Has enough activity for personalized sequencing.")
        if (isEmpty()) add("Needs more activity data for stable personalization.")
    }
}
