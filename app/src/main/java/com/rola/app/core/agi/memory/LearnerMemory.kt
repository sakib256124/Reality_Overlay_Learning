package com.rola.app.core.agi.memory

import com.rola.app.domain.model.AGILearningEvent
import com.rola.app.domain.model.LearningSpeed
import com.rola.app.domain.model.SkillGraphNode
import com.rola.app.domain.model.SkillLevel
import com.rola.app.domain.model.LearningGoal

data class LearnerMemory(
    val learnerId: String,
    val learningHistory: List<AGILearningEvent>,
    val skills: List<SkillGraphNode>,
    val knowledgeLevel: SkillLevel,
    val interests: List<String>,
    val goals: List<LearningGoal>,
    val previousMistakes: List<String>,
    val learningPatterns: List<String>,
    val updatedAt: Long = System.currentTimeMillis(),
) {
    val learningSpeed: LearningSpeed
        get() {
            val recentScores = learningHistory.mapNotNull { it.score }.takeLast(5)
            val average = recentScores.average().takeIf { !it.isNaN() } ?: 0.0
            return when {
                average >= 85 && previousMistakes.size <= 1 -> LearningSpeed.Fast
                average < 60 || previousMistakes.size >= 4 -> LearningSpeed.SlowAndSteady
                else -> LearningSpeed.Balanced
            }
        }

    val retentionScore: Int
        get() {
            val mastery = skills.map { it.mastery }.average().takeIf { !it.isNaN() } ?: 40.0
            val mistakePenalty = previousMistakes.size * 4
            return (mastery.toInt() - mistakePenalty).coerceIn(0, 100)
        }

    fun remember(event: AGILearningEvent): LearnerMemory {
        val newMistakes = if ((event.score ?: 100) < 60) previousMistakes + event.topic else previousMistakes
        val newInterests = if (event.topic.isNotBlank()) (interests + event.topic).distinct().takeLast(12) else interests
        return copy(
            learningHistory = (learningHistory + event).takeLast(200),
            interests = newInterests,
            previousMistakes = newMistakes.distinct().takeLast(20),
            updatedAt = System.currentTimeMillis(),
        )
    }
}
