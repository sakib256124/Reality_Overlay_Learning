package com.rola.app.enterprise.teacher

import com.rola.app.data.knowledgegraph.KnowledgeGraphRepository
import com.rola.app.data.research.ResearchRepository
import com.rola.app.domain.model.TeachingPlan
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AITeachingAssistant @Inject constructor(
    private val knowledgeGraphRepository: KnowledgeGraphRepository,
    private val researchRepository: ResearchRepository,
) {
    suspend fun generateLessonPlan(
        teacherId: String,
        topic: String,
    ): TeachingPlan {
        val search = knowledgeGraphRepository.semanticSearch(topic)
        val materials = researchRepository.materialsForTutor(topic)
        val objectSuggestions = search.graph.nodes
            .filter { it.type == com.rola.app.domain.model.KnowledgeNodeType.Object || it.type == com.rola.app.domain.model.KnowledgeNodeType.Material }
            .map { it.nodeId }
            .take(5)
        return TeachingPlan(
            planId = "teaching-plan-${UUID.randomUUID()}",
            teacherId = teacherId,
            topic = topic,
            arObjectSuggestions = objectSuggestions,
            explanation = search.explanation.ifBlank { "Introduce $topic with a real object, a guided explanation, and a short reflection." },
            quizPrompts = listOf(
                "What is $topic?",
                "Which real object demonstrates $topic?",
                "How does $topic connect to daily life?",
            ),
            activities = listOf(
                "Scan a related object in AR.",
                "Discuss observed properties in small groups.",
                "Complete a short quiz and review misconceptions.",
            ),
            materialIds = materials.map { it.materialId }.take(5),
        )
    }
}
