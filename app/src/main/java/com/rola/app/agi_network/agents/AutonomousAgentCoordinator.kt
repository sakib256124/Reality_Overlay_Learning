package com.rola.app.agi_network.agents

import com.rola.app.agi_network.collaboration.AgentCommunicationManager
import com.rola.app.agi_network.intelligence.AgentCollaborationPlan
import com.rola.app.agi_network.intelligence.AgentTaskStatus
import com.rola.app.agi_network.intelligence.AGIAgentTask
import com.rola.app.agi_network.intelligence.EducationNetworkSignal
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AutonomousAgentCoordinator @Inject constructor(
    private val multiAgentEducationSystem: MultiAgentEducationSystem,
    private val agentCommunicationManager: AgentCommunicationManager,
) {
    fun coordinate(signal: EducationNetworkSignal): AgentCollaborationPlan {
        val agents = multiAgentEducationSystem.selectAgents(signal)
        val tasks = agents.mapIndexed { index, agent ->
            AGIAgentTask(
                taskId = "agent-task-${UUID.randomUUID()}",
                agentRole = agent,
                title = "Improve ${signal.topic} using ${agent.name}",
                priority = if (index == 0) 90 else 70,
                status = AgentTaskStatus.Queued,
                evidence = signal.researchEvidence + listOf("Activity ${signal.activityType}", "Outcome ${signal.learningOutcomeScore ?: "unknown"}"),
            )
        }
        val messages = agentCommunicationManager.messagesFor(signal, agents)
        return AgentCollaborationPlan(
            planId = "agent-collaboration-${UUID.randomUUID()}",
            selectedAgents = agents,
            tasks = tasks,
            messages = messages,
            conflictResolution = agentCommunicationManager.resolveConflicts(messages),
        )
    }
}
