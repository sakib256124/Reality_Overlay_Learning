package com.rola.app.agi_network.decision

import com.rola.app.agi_network.intelligence.AgentCollaborationPlan
import com.rola.app.agi_network.intelligence.AGIEducationalDecision
import com.rola.app.agi_network.intelligence.AGINetworkAgentRole
import com.rola.app.agi_network.intelligence.EducationNetworkSignal
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AGIDecisionEngine @Inject constructor() {
    fun decide(
        signal: EducationNetworkSignal,
        collaborationPlan: AgentCollaborationPlan,
        reasoningTrace: List<String>,
    ): AGIEducationalDecision {
        val struggling = (signal.learningOutcomeScore ?: 100) < 70
        val hasResearchRoute = AGINetworkAgentRole.ResearchAgent in collaborationPlan.selectedAgents
        return AGIEducationalDecision(
            decisionId = "agi-network-decision-${UUID.randomUUID()}",
            learnerId = signal.learnerId,
            topic = signal.topic,
            teachingApproach = if (struggling) "Simplified reteaching with guided practice" else "Adaptive enrichment",
            requiredContent = buildList {
                add("Teacher-reviewed explanation for ${signal.topic}")
                if (struggling) add("Diagnostic practice set")
                if (hasResearchRoute) add("Verified research update draft")
            },
            difficultyAdjustment = if (struggling) "Decrease difficulty and add scaffolding" else "Maintain or increase challenge gradually",
            learningEnvironment = if (struggling) "Guided AR lesson" else "Exploratory AR and knowledge graph pathway",
            assessmentStrategy = if (struggling) "Short diagnostic quiz after reteaching" else "Project-style mastery check",
            explanation = reasoningTrace.joinToString(" "),
        )
    }
}
