package com.rola.app.digital_companion.memory

import com.rola.app.digital_companion.companion_core.CompanionLearningContext
import com.rola.app.digital_companion.companion_core.CompanionMemoryRecord
import com.rola.app.digital_companion.companion_core.CompanionMemoryType
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanionMemoryManager @Inject constructor() {
    fun buildMemories(context: CompanionLearningContext): List<CompanionMemoryRecord> =
        listOf(
            CompanionMemoryRecord("companion-memory-${UUID.randomUUID()}", context.userId, CompanionMemoryType.CurrentConversation, context.topic, context.recentMessage, true),
            CompanionMemoryRecord("companion-memory-${UUID.randomUUID()}", context.userId, CompanionMemoryType.CurrentObjective, context.topic, context.currentGoal, true),
            CompanionMemoryRecord("companion-memory-${UUID.randomUUID()}", context.userId, CompanionMemoryType.LearningJourney, context.topic, "Scores: ${context.recentScores.joinToString()}", true),
            CompanionMemoryRecord("companion-memory-${UUID.randomUUID()}", context.userId, CompanionMemoryType.DifficultTopic, context.topic, difficultTopicSummary(context), true),
            CompanionMemoryRecord("companion-memory-${UUID.randomUUID()}", context.userId, CompanionMemoryType.Preference, context.topic, "Preferred modalities: ${context.preferredModalities.joinToString()}", true),
        )

    private fun difficultTopicSummary(context: CompanionLearningContext): String =
        if (context.recentScores.average().takeIf { !it.isNaN() }?.let { it < 70 } == true) {
            "${context.topic} needs simpler examples and practice."
        } else {
            "${context.topic} is ready for deeper exploration."
        }
}

