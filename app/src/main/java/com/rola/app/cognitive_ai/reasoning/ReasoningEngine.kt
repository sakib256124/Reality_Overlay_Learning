package com.rola.app.cognitive_ai.reasoning

import com.rola.app.domain.model.CognitiveDecision
import com.rola.app.domain.model.LearnerCognitiveProfile
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ReasoningEngine @Inject constructor(
    private val learningReasoningEngine: LearningReasoningEngine,
) {
    fun explainDecision(
        profile: LearnerCognitiveProfile,
        topic: String,
    ): CognitiveDecision = learningReasoningEngine.strategyDecision(profile, topic)

    fun conceptConnections(
        topic: String,
        profile: LearnerCognitiveProfile,
    ): List<String> =
        (profile.knowledgeStrengths + profile.knowledgeWeaknesses)
            .distinct()
            .filterNot { it.equals(topic, ignoreCase = true) }
            .take(5)
            .map { "$topic connects to $it in the learner model." }
            .ifEmpty { listOf("$topic needs knowledge graph context before deeper personalization.") }
}
