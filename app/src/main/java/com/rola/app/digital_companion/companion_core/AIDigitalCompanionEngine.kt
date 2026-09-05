package com.rola.app.digital_companion.companion_core

import com.rola.app.digital_companion.communication.CompanionCommunicationManager
import com.rola.app.digital_companion.decision.CompanionDecisionEngine
import com.rola.app.digital_companion.emotion.CompanionEmotionEngine
import com.rola.app.digital_companion.evolution.CompanionEvolutionEngine
import com.rola.app.digital_companion.learning.CompanionLearningManager
import com.rola.app.digital_companion.memory.CompanionMemoryManager
import com.rola.app.digital_companion.personality.CompanionPersonalityEngine
import com.rola.app.digital_companion.privacy.CompanionPrivacyManager
import com.rola.app.digital_companion.relationship.CompanionRelationshipManager
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIDigitalCompanionEngine @Inject constructor(
    private val coreManager: CompanionCoreManager,
    private val personalityEngine: CompanionPersonalityEngine,
    private val memoryManager: CompanionMemoryManager,
    private val emotionEngine: CompanionEmotionEngine,
    private val relationshipManager: CompanionRelationshipManager,
    private val learningManager: CompanionLearningManager,
    private val decisionEngine: CompanionDecisionEngine,
    private val communicationManager: CompanionCommunicationManager,
    private val evolutionEngine: CompanionEvolutionEngine,
    private val privacyManager: CompanionPrivacyManager,
) {
    fun supportLearner(context: CompanionLearningContext): DigitalCompanionResult {
        val personality = personalityEngine.profileFor(context)
        val companion = coreManager.createCompanion(context, personality)
        val memories = memoryManager.buildMemories(context)
        val emotion = emotionEngine.analyze(context)
        val relationship = relationshipManager.updateRelationship(context)
        val plan = learningManager.guideLearning(context, emotion)
        val decision = decisionEngine.decide(context, emotion)
        val response = communicationManager.respond(context, decision)
        return DigitalCompanionResult(
            resultId = "digital-companion-${UUID.randomUUID()}",
            companion = companion,
            memories = memories,
            personality = personality,
            emotionState = emotion,
            relationshipState = relationship,
            learningPlan = plan,
            decision = decision,
            conversationResponse = response,
            evolutionState = evolutionEngine.evolve(relationship, plan),
            privacyState = privacyManager.privacyState(memoryEnabled = companion.memoryControlEnabled),
        )
    }
}
