package com.rola.app.data.research

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResearchTutorBridge @Inject constructor(
    private val researchRepository: ResearchRepository,
    private val researchAgent: ResearchAgent,
) {
    suspend fun buildResearchEnhancedContext(question: String): String {
        val materials = researchRepository.materialsForTutor(question)
        val generatedContext = materials.take(3).joinToString(separator = "\n") { material ->
            "${material.title}: ${material.summary}"
        }
        val graphContext = researchAgent.tutorResearchContext(question)
        return listOf(graphContext, generatedContext)
            .filter { it.isNotBlank() }
            .joinToString(separator = "\n")
    }
}
