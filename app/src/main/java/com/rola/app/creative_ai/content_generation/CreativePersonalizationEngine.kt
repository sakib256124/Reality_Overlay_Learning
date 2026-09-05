package com.rola.app.creative_ai.content_generation

import com.rola.app.creative_ai.creativity_engine.CreativeAIRequest
import com.rola.app.creative_ai.creativity_engine.CreativePersonalization
import javax.inject.Inject

class CreativePersonalizationEngine @Inject constructor() {
    fun personalize(request: CreativeAIRequest): CreativePersonalization =
        CreativePersonalization(
            personalizationId = "creative-personal-${request.userId}",
            examples = listOf("example using learner interests", "emotion-aware simplified example"),
            projects = listOf("custom ${request.topic} mini project", "prediction-informed challenge"),
            challenges = listOf("creative assignment", "personal research prompt"),
        )
}
