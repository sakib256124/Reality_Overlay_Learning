package com.rola.app.creative_ai.research_creation

import com.rola.app.creative_ai.creativity_engine.CreativeAIRequest
import com.rola.app.creative_ai.creativity_engine.ResearchIdeaSet
import javax.inject.Inject

class ResearchIdeaGenerator @Inject constructor() {
    fun generate(request: CreativeAIRequest): ResearchIdeaSet =
        ResearchIdeaSet(
            researchId = "research-ideas-${request.topic.lowercase().replace(" ", "-")}",
            topics = listOf("How learners form misconceptions in ${request.topic}", "Creative AR explanations for ${request.subject}"),
            hypotheses = listOf("Personalized analogies improve transfer.", "Simulation-first learning improves retention."),
            experiments = listOf("compare standard lesson with creative simulation", "measure confidence before and after analogy ladder"),
            futureDirections = listOf("Knowledge Graph integration", "Global Knowledge Network validation"),
        )
}
