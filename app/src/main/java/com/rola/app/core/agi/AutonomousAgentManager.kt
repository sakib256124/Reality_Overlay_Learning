package com.rola.app.core.agi

import com.rola.app.core.agi.agents.AssessmentAgent
import com.rola.app.core.agi.agents.AutonomousLearningAgent
import com.rola.app.core.agi.agents.KnowledgeExpansionAgent
import com.rola.app.core.agi.agents.PersonalMentorAgent
import com.rola.app.core.agi.agents.RecommendationAgent
import com.rola.app.core.agi.agents.ResearchAgent
import com.rola.app.core.agi.agents.TeachingAgent
import com.rola.app.core.agi.memory.LearnerMemory
import com.rola.app.domain.model.AGIAgentMessage
import com.rola.app.domain.model.AGIAgentRole
import com.rola.app.domain.model.AGILearningEvent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AutonomousAgentManager @Inject constructor(
    teachingAgent: TeachingAgent,
    researchAgent: ResearchAgent,
    assessmentAgent: AssessmentAgent,
    recommendationAgent: RecommendationAgent,
    knowledgeExpansionAgent: KnowledgeExpansionAgent,
    personalMentorAgent: PersonalMentorAgent,
) {
    private val agents: List<AutonomousLearningAgent> = listOf(
        teachingAgent,
        researchAgent,
        assessmentAgent,
        recommendationAgent,
        knowledgeExpansionAgent,
        personalMentorAgent,
    )

    fun selectAgents(event: AGILearningEvent): List<AGIAgentRole> =
        agents.filter { it.canHandle(event) }
            .map { it.role }
            .ifEmpty { listOf(AGIAgentRole.PersonalMentorAgent, AGIAgentRole.RecommendationAgent) }

    fun runAgents(
        event: AGILearningEvent,
        memory: LearnerMemory,
    ): List<AGIAgentMessage> =
        agents.filter { it.role in selectAgents(event) }
            .map { it.act(event, memory) }

    fun communicationPlan(event: AGILearningEvent): String =
        selectAgents(event).joinToString(separator = " -> ") { it.name }
}
