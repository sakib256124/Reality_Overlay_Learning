package com.rola.app.agi_network.agents

import com.rola.app.agi_network.intelligence.AGIAgentProfile
import com.rola.app.agi_network.intelligence.AGINetworkAgentRole
import com.rola.app.agi_network.intelligence.EducationNetworkSignal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MultiAgentEducationSystem @Inject constructor() {
    val availableAgents: List<AGIAgentProfile> = AGINetworkAgentRole.entries.map { role ->
        AGIAgentProfile(
            agentId = "agi-network-${role.name}",
            role = role,
            name = role.name.replace("Agent", " Agent"),
            capabilities = capabilitiesFor(role),
        )
    }

    fun selectAgents(signal: EducationNetworkSignal): List<AGINetworkAgentRole> = buildSet {
        add(AGINetworkAgentRole.AnalyticsAgent)
        if ((signal.learningOutcomeScore ?: 100) < 70) {
            add(AGINetworkAgentRole.AssessmentAgent)
            add(AGINetworkAgentRole.CognitiveLearningAgent)
            add(AGINetworkAgentRole.AITeacherAgent)
        }
        if (signal.activityType.contains("tutor", ignoreCase = true)) add(AGINetworkAgentRole.AITutorAgent)
        if (signal.activityType.contains("robot", ignoreCase = true)) add(AGINetworkAgentRole.RobotTeachingAgent)
        if (signal.researchEvidence.isNotEmpty() || signal.activityType.contains("research", ignoreCase = true)) {
            add(AGINetworkAgentRole.ResearchAgent)
            add(AGINetworkAgentRole.KnowledgeAgent)
        }
        if (size == 1) {
            add(AGINetworkAgentRole.AITeacherAgent)
            add(AGINetworkAgentRole.KnowledgeAgent)
        }
    }.toList()

    private fun capabilitiesFor(role: AGINetworkAgentRole): List<String> = when (role) {
        AGINetworkAgentRole.AITeacherAgent -> listOf("lesson strategy", "curriculum draft", "explanation planning")
        AGINetworkAgentRole.AITutorAgent -> listOf("dialog guidance", "hint planning", "question answering")
        AGINetworkAgentRole.ResearchAgent -> listOf("research scanning", "source comparison", "trend detection")
        AGINetworkAgentRole.KnowledgeAgent -> listOf("knowledge graph growth", "relationship validation")
        AGINetworkAgentRole.AssessmentAgent -> listOf("quiz generation", "mastery diagnosis")
        AGINetworkAgentRole.AnalyticsAgent -> listOf("platform metrics", "success prediction")
        AGINetworkAgentRole.RobotTeachingAgent -> listOf("robot classroom support", "embodied demonstrations")
        AGINetworkAgentRole.CognitiveLearningAgent -> listOf("cognitive adaptation", "learning state interpretation")
    }
}
