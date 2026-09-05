package com.rola.app.collective_ai.agent_society

import com.rola.app.collective_ai.intelligence_network.AIAgentNode
import com.rola.app.collective_ai.intelligence_network.AgentRelationship
import com.rola.app.collective_ai.intelligence_network.AgentTask
import com.rola.app.collective_ai.intelligence_network.CollectiveAgentStatus
import com.rola.app.collective_ai.intelligence_network.CollectiveAgentType
import com.rola.app.collective_ai.intelligence_network.CollectiveAIRequest
import javax.inject.Inject

class AgentSocietyManager @Inject constructor() {
    fun activateAgents(): List<AIAgentNode> =
        CollectiveAgentType.values().map { type ->
            AIAgentNode(
                agentId = "collective-${type.name.lowercase()}",
                name = type.displayName(),
                agentType = type,
                capability = type.capability(),
                status = CollectiveAgentStatus.Active,
            )
        }

    fun buildRelationships(agents: List<AIAgentNode>): List<AgentRelationship> =
        agents.flatMap { source ->
            agents.filter { it.agentId != source.agentId }.take(2).map { target ->
                AgentRelationship(
                    relationshipId = "${source.agentId}-${target.agentId}",
                    fromAgentId = source.agentId,
                    toAgentId = target.agentId,
                    relationshipType = "knowledge-partner",
                    trustScore = 88,
                )
            }
        }

    fun assignTasks(request: CollectiveAIRequest, agents: List<AIAgentNode>): List<AgentTask> =
        agents.mapIndexed { index, agent ->
            AgentTask(
                taskId = "task-${request.topic.lowercase().replace(" ", "-")}-$index",
                agentId = agent.agentId,
                taskType = agent.agentType.taskType(),
                priority = if (agent.agentType in criticalAgents) 1 else 2,
                output = "${agent.name} contributes ${agent.capability} for ${request.problem}.",
            )
        }

    private fun CollectiveAgentType.displayName(): String = when (this) {
        CollectiveAgentType.AITeacher -> "AI Teacher Agent"
        CollectiveAgentType.AITutor -> "AI Tutor Agent"
        CollectiveAgentType.Research -> "Research Agent"
        CollectiveAgentType.Knowledge -> "Knowledge Agent"
        CollectiveAgentType.Assessment -> "Assessment Agent"
        CollectiveAgentType.Cognitive -> "Cognitive Agent"
        CollectiveAgentType.RobotTeaching -> "Robot Teaching Agent"
        CollectiveAgentType.SpatialLearning -> "Spatial Learning Agent"
        CollectiveAgentType.CompanionAI -> "Companion AI Agent"
    }

    private fun CollectiveAgentType.capability(): String = when (this) {
        CollectiveAgentType.AITeacher -> "lesson explanation"
        CollectiveAgentType.AITutor -> "adaptive guidance"
        CollectiveAgentType.Research -> "scientific verification"
        CollectiveAgentType.Knowledge -> "concept gap detection"
        CollectiveAgentType.Assessment -> "practice and evaluation"
        CollectiveAgentType.Cognitive -> "learning behavior analysis"
        CollectiveAgentType.RobotTeaching -> "embodied classroom support"
        CollectiveAgentType.SpatialLearning -> "immersive learning design"
        CollectiveAgentType.CompanionAI -> "personal memory and motivation"
    }

    private fun CollectiveAgentType.taskType(): String = when (this) {
        CollectiveAgentType.AITeacher -> "create-explanation"
        CollectiveAgentType.AITutor -> "personalize-support"
        CollectiveAgentType.Research -> "verify-accuracy"
        CollectiveAgentType.Knowledge -> "map-missing-concepts"
        CollectiveAgentType.Assessment -> "generate-practice"
        CollectiveAgentType.Cognitive -> "analyze-behavior"
        CollectiveAgentType.RobotTeaching -> "plan-embodied-demo"
        CollectiveAgentType.SpatialLearning -> "plan-ar-visualization"
        CollectiveAgentType.CompanionAI -> "align-with-memory"
    }

    private companion object {
        val criticalAgents = setOf(
            CollectiveAgentType.Cognitive,
            CollectiveAgentType.Knowledge,
            CollectiveAgentType.AITeacher,
            CollectiveAgentType.Assessment,
        )
    }
}
