package com.rola.app.collective_ai.collaboration

import com.rola.app.collective_ai.intelligence_network.AgentAnalysis
import com.rola.app.collective_ai.intelligence_network.AgentTask
import com.rola.app.collective_ai.intelligence_network.CollectiveAIRequest
import javax.inject.Inject

class MultiAgentCoordinator @Inject constructor() {
    fun coordinate(request: CollectiveAIRequest, tasks: List<AgentTask>): List<AgentAnalysis> =
        tasks.map { task ->
            AgentAnalysis(
                agentId = task.agentId,
                perspective = task.taskType,
                evidence = request.learnerSignals + task.output,
                recommendation = recommendationFor(task.taskType, request.topic),
                confidencePercent = if (task.priority == 1) 92 else 84,
            )
        }

    private fun recommendationFor(taskType: String, topic: String): String = when (taskType) {
        "analyze-behavior" -> "Start with learner misconceptions and attention signals."
        "map-missing-concepts" -> "Repair prerequisite knowledge before advancing $topic."
        "create-explanation" -> "Use a concise explanation with one real-world example."
        "generate-practice" -> "Add short adaptive practice after the explanation."
        "plan-ar-visualization" -> "Use AR overlay to make abstract relationships visible."
        else -> "Blend personalized support into the $topic learning path."
    }
}
