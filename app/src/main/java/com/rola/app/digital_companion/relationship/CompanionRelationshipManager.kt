package com.rola.app.digital_companion.relationship

import com.rola.app.digital_companion.companion_core.CompanionLearningContext
import com.rola.app.digital_companion.companion_core.CompanionRelationshipState
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanionRelationshipManager @Inject constructor() {
    fun updateRelationship(context: CompanionLearningContext): CompanionRelationshipState =
        CompanionRelationshipState(
            relationshipId = "companion-relationship-${UUID.randomUUID()}",
            userId = context.userId,
            progressSummary = "Learner is progressing through ${context.topic} with ${context.preferredModalities.joinToString()} support.",
            interactionHistory = listOf(context.recentMessage, "Reviewed goal: ${context.currentGoal}", "Updated learning strategy"),
            goalsAchieved = if ((context.recentScores.maxOrNull() ?: 0) >= 80) listOf("Strong checkpoint on ${context.topic}") else emptyList(),
            learnsBestBy = if (context.preferredModalities.isNotEmpty()) context.preferredModalities.first().name else "guided conversation",
        )
}
