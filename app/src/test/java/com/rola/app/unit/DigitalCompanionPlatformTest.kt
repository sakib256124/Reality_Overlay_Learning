package com.rola.app.unit

import com.rola.app.digital_companion.communication.CompanionCommunicationManager
import com.rola.app.digital_companion.companion_core.AIDigitalCompanionEngine
import com.rola.app.digital_companion.companion_core.CompanionCoreManager
import com.rola.app.digital_companion.companion_core.CompanionLearningContext
import com.rola.app.digital_companion.companion_core.CompanionMemoryType
import com.rola.app.digital_companion.companion_core.CompanionModality
import com.rola.app.digital_companion.companion_core.CompanionTone
import com.rola.app.digital_companion.decision.CompanionDecisionEngine
import com.rola.app.digital_companion.emotion.CompanionEmotionEngine
import com.rola.app.digital_companion.evolution.CompanionEvolutionEngine
import com.rola.app.digital_companion.learning.CompanionLearningManager
import com.rola.app.digital_companion.learning.CompanionLearningPlanner
import com.rola.app.digital_companion.memory.CompanionMemoryManager
import com.rola.app.digital_companion.personality.CompanionPersonalityEngine
import com.rola.app.digital_companion.privacy.CompanionPrivacyManager
import com.rola.app.digital_companion.relationship.CompanionRelationshipManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class DigitalCompanionPlatformTest {
    private val engine = AIDigitalCompanionEngine(
        coreManager = CompanionCoreManager(),
        personalityEngine = CompanionPersonalityEngine(),
        memoryManager = CompanionMemoryManager(),
        emotionEngine = CompanionEmotionEngine(),
        relationshipManager = CompanionRelationshipManager(),
        learningManager = CompanionLearningManager(CompanionLearningPlanner()),
        decisionEngine = CompanionDecisionEngine(),
        communicationManager = CompanionCommunicationManager(),
        evolutionEngine = CompanionEvolutionEngine(),
        privacyManager = CompanionPrivacyManager(),
    )

    @Test
    fun companionCycle_createsPersonalCompanionWithBeginnerPersonality() {
        val result = engine.supportLearner(sampleContext())

        assertEquals("learner-1", result.companion.userId)
        assertEquals(CompanionTone.FriendlyTeacher, result.companion.personalityProfile)
        assertTrue(result.companion.preferences.contains(CompanionModality.ARGuidance.name))
    }

    @Test
    fun companionCycle_storesUserControlledMemoryAndRelationship() {
        val result = engine.supportLearner(sampleContext())

        assertTrue(result.memories.any { it.memoryType == CompanionMemoryType.CurrentConversation })
        assertTrue(result.memories.all { it.userControlled })
        assertEquals(CompanionModality.Text.name, result.relationshipState.learnsBestBy)
    }

    @Test
    fun companionCycle_generatesEmotionAwarePlanDecisionAndPrivacyControls() {
        val result = engine.supportLearner(sampleContext())

        assertTrue(result.emotionState.frustrationPercent > 25)
        assertTrue(result.learningPlan.practiceSchedule.any { it.contains("5-minute") })
        assertTrue(result.decision.transparentReason.contains("recent scores", ignoreCase = true))
        assertTrue(result.privacyState.deletionControlAvailable)
        assertTrue(result.privacyState.transparentDecisions)
    }

    private fun sampleContext(): CompanionLearningContext =
        CompanionLearningContext(
            userId = "learner-1",
            topic = "Electric Circuits",
            currentGoal = "Explain voltage and current with a real example",
            recentMessage = "I still confuse voltage with current.",
            skillLevel = "Beginner",
            recentScores = listOf(52, 64, 68),
            preferredModalities = listOf(CompanionModality.Text, CompanionModality.ARGuidance, CompanionModality.Voice),
        )
}

