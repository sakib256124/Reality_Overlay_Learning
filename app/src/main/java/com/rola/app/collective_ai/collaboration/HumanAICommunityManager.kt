package com.rola.app.collective_ai.collaboration

import com.rola.app.collective_ai.intelligence_network.CollectiveAIRequest
import com.rola.app.collective_ai.intelligence_network.HumanFeedbackSignal
import javax.inject.Inject

class HumanAICommunityManager @Inject constructor() {
    fun integrateFeedback(request: CollectiveAIRequest): HumanFeedbackSignal =
        HumanFeedbackSignal(
            feedbackId = "feedback-${request.userId}-${request.topic.lowercase().replace(" ", "-")}",
            userId = request.userId,
            role = if (request.humanFeedback.isNullOrBlank()) "learner" else "teacher-or-expert",
            feedback = request.humanFeedback ?: "Use a clearer explanation with more practice.",
            validationScore = if (request.humanFeedback.isNullOrBlank()) 78 else 90,
        )
}
