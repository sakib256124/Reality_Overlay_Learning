package com.rola.app.agi_network.evolution

import com.rola.app.agi_network.intelligence.EducationNetworkSignal
import com.rola.app.agi_network.intelligence.SelfLearningEvaluation
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelfLearningManager @Inject constructor() {
    fun evaluate(signal: EducationNetworkSignal): SelfLearningEvaluation {
        val outcome = signal.learningOutcomeScore ?: 65
        val content = signal.contentQualityScore ?: 70
        return SelfLearningEvaluation(
            evaluationId = "self-learning-${UUID.randomUUID()}",
            teachingImprovement = if (outcome < 70) "Improve explanations and add prerequisite checks." else "Preserve strategy and add enrichment.",
            recommendationAccuracyPercent = ((outcome + content) / 2).coerceIn(0, 100),
            questionQualityPercent = if (outcome < 60) 58 else 82,
            contentQualityPercent = content.coerceIn(0, 100),
            improvementTargets = buildList {
                if (outcome < 70) add("Teaching strategy")
                if (content < 75) add("Content verification")
                if (signal.researchEvidence.isNotEmpty()) add("Knowledge graph update")
                if (isEmpty()) add("Advanced personalization")
            },
        )
    }
}
