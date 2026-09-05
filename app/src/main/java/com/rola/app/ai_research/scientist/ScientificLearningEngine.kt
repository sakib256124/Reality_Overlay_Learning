package com.rola.app.ai_research.scientist

import javax.inject.Inject

class ScientificLearningEngine @Inject constructor() {
    fun createPackage(context: ResearchContext): ScientificLearningPackage =
        ScientificLearningPackage("scientific-learning-${context.userId}", listOf("research-based ${context.domain} lesson"), listOf("advanced tutorial for ${context.question}"), listOf("scientific simulation"), listOf("practical research project"))
}
