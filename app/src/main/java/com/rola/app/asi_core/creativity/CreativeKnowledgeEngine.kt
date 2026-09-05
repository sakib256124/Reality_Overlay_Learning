package com.rola.app.asi_core.creativity

import com.rola.app.asi_core.intelligence.CreativeKnowledgeOutput
import com.rola.app.asi_core.intelligence.UniversalKnowledgeMap
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CreativeKnowledgeEngine @Inject constructor() {
    fun generate(map: UniversalKnowledgeMap): CreativeKnowledgeOutput =
        CreativeKnowledgeOutput(
            outputId = "asi-creative-${UUID.randomUUID()}",
            topic = map.topic,
            educationalApproaches = listOf(
                "Analogy-first explanation for ${map.topic}",
                "Evidence-based AR exploration",
                "Student-generated question path",
            ),
            learningActivities = map.domainConnections.map { "Connect ${map.topic} with $it through a guided challenge." },
            simulations = listOf("Adaptive virtual lab for ${map.topic}", "Scenario comparison simulation"),
            researchDirections = listOf("Study misconception patterns for ${map.topic}", "Compare explanation formats across learners"),
        )
}
