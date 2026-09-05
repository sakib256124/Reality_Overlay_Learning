package com.rola.app.digital_companion.communication

import com.rola.app.digital_companion.companion_core.CompanionConversationResponse
import com.rola.app.digital_companion.companion_core.CompanionDecision
import com.rola.app.digital_companion.companion_core.CompanionLearningContext
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CompanionCommunicationManager @Inject constructor() {
    fun respond(
        context: CompanionLearningContext,
        decision: CompanionDecision,
    ): CompanionConversationResponse =
        CompanionConversationResponse(
            responseId = "companion-response-${UUID.randomUUID()}",
            message = "Let's work on ${context.topic}. I will use ${decision.explanationMethod}, then guide you through ${decision.learningActivity}.",
            modalities = context.preferredModalities,
            knowledgeSources = listOf("Knowledge Graph", "AI Teacher", "Cognitive AI", "Neural AI", "Adaptive Learning"),
        )
}

