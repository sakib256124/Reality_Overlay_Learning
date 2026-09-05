package com.rola.app.creative_ai.creativity_engine

import javax.inject.Inject

class CreativeKnowledgeGenerator @Inject constructor() {
    fun generate(request: CreativeAIRequest): CreativeKnowledge =
        CreativeKnowledge(
            knowledgeId = "creative-knowledge-${request.topic.lowercase().replace(" ", "-")}",
            explanations = listOf("Explain ${request.topic} using a simple-to-deep ladder.", "Offer an alternative story-based explanation."),
            analogies = listOf("${request.topic} as a city traffic system", "${request.topic} as a team workflow"),
            conceptConnections = listOf("connect ${request.topic} to prior knowledge", "link concept to future research"),
        )
}
