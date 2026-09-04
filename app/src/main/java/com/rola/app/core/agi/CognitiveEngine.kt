package com.rola.app.core.agi

import com.rola.app.core.agi.memory.LearnerMemory
import com.rola.app.domain.model.CognitiveLearningReport
import com.rola.app.domain.model.SkillGraphNode
import com.rola.app.domain.model.SkillLevel
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CognitiveEngine @Inject constructor() {
    fun analyze(memory: LearnerMemory): CognitiveLearningReport {
        val averageMastery = memory.skills.map { it.mastery }.average().takeIf { !it.isNaN() } ?: memory.retentionScore.toDouble()
        return CognitiveLearningReport(
            reportId = "cognitive-report-${UUID.randomUUID()}",
            learnerId = memory.learnerId,
            learningSpeed = memory.learningSpeed,
            memoryRetentionScore = memory.retentionScore,
            conceptDifficulty = when {
                averageMastery >= 85 -> SkillLevel.Advanced
                averageMastery >= 65 -> SkillLevel.Intermediate
                else -> SkillLevel.Beginner
            },
            engagementPattern = engagementPattern(memory),
            skillMap = memory.skills.ifEmpty { starterSkillMap(memory) },
            futurePrediction = predictionFor(memory, averageMastery.toInt()),
        )
    }

    fun strategyFor(memory: LearnerMemory): String = when {
        memory.retentionScore < 50 -> "Use retrieval practice, short explanations, and repeated AR evidence."
        memory.learningSpeed.name == "Fast" -> "Use challenge tasks, research extensions, and learner-designed simulations."
        memory.previousMistakes.isNotEmpty() -> "Use targeted reteaching for ${memory.previousMistakes.take(3).joinToString()}."
        else -> "Use balanced teaching, practice, simulation, and reflection."
    }

    private fun engagementPattern(memory: LearnerMemory): String = when {
        memory.learningHistory.any { it.activityType.name.contains("AR") } -> "Spatial engagement is strong."
        memory.learningHistory.any { it.activityType.name.contains("Tutor") } -> "Conversational learning is active."
        memory.learningHistory.size >= 6 -> "Consistent multi-session engagement."
        else -> "Engagement pattern is still emerging."
    }

    private fun starterSkillMap(memory: LearnerMemory): List<SkillGraphNode> =
        memory.interests.take(3).map {
            SkillGraphNode(
                skillId = "starter-${it.lowercase().replace(Regex("[^a-z0-9]+"), "-").trim('-')}",
                name = it,
                mastery = memory.retentionScore,
            )
        }

    private fun predictionFor(
        memory: LearnerMemory,
        averageMastery: Int,
    ): String = when {
        memory.retentionScore < 55 -> "Learner may struggle with future topics unless prerequisite gaps are retaught."
        averageMastery >= 85 -> "Learner is ready for advanced projects, simulations, and research tasks."
        memory.previousMistakes.isNotEmpty() -> "Learner should improve with spaced practice on prior mistakes."
        else -> "Learner is likely to progress steadily with adaptive practice."
    }
}
