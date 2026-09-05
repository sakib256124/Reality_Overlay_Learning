package com.rola.app.digital_companion.companion_core

import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanionCoreManager @Inject constructor() {
    fun createCompanion(
        context: CompanionLearningContext,
        personality: CompanionPersonalityProfile,
    ): PersonalLearningCompanion =
        PersonalLearningCompanion(
            companionId = "personal-companion-${UUID.randomUUID()}",
            userId = context.userId,
            personalityProfile = personality.tone,
            learningHistory = listOf("AR learning", "AI tutor support", "Adaptive recommendations"),
            knowledgeUnderstanding = listOf(context.topic, context.currentGoal),
            communicationStyle = personality.explanationPreference,
            learningGoals = listOf(context.currentGoal),
            preferences = context.preferredModalities.map { it.name },
            memoryControlEnabled = true,
        )
}

