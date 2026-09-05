package com.rola.app.unit

import com.rola.app.collective_ai.agent_society.AgentSocietyManager
import com.rola.app.collective_ai.collaboration.AIDebateEngine
import com.rola.app.collective_ai.collaboration.CollectiveLearningOptimizer
import com.rola.app.collective_ai.collaboration.HumanAICommunityManager
import com.rola.app.collective_ai.collaboration.MultiAgentCoordinator
import com.rola.app.collective_ai.communication.AICommunityManager
import com.rola.app.collective_ai.decision.AIConsensusEngine
import com.rola.app.collective_ai.decision.CollectiveDecisionEngine
import com.rola.app.collective_ai.governance.CollectiveGovernanceManager
import com.rola.app.collective_ai.intelligence_network.CollectiveAIEngine
import com.rola.app.collective_ai.intelligence_network.CollectiveAIRequest
import com.rola.app.collective_ai.intelligence_network.CollectiveAgentType
import com.rola.app.collective_ai.intelligence_network.ConsensusOutcome
import com.rola.app.collective_ai.intelligence_network.GlobalAIResearchNetwork
import com.rola.app.collective_ai.knowledge_exchange.KnowledgeExchangeManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CollectiveAIPlatformTest {
    private val engine = CollectiveAIEngine(
        agentSocietyManager = AgentSocietyManager(),
        aiCommunityManager = AICommunityManager(),
        multiAgentCoordinator = MultiAgentCoordinator(),
        knowledgeExchangeManager = KnowledgeExchangeManager(),
        debateEngine = AIDebateEngine(),
        consensusEngine = AIConsensusEngine(),
        decisionEngine = CollectiveDecisionEngine(),
        humanAICommunityManager = HumanAICommunityManager(),
        learningOptimizer = CollectiveLearningOptimizer(),
        governanceManager = CollectiveGovernanceManager(),
        globalResearchNetwork = GlobalAIResearchNetwork(),
    )

    @Test
    fun collectiveCycle_activatesFullEducationAgentSociety() {
        val result = engine.solveEducationalProblem(sampleRequest())

        assertEquals(CollectiveAgentType.values().size, result.agents.size)
        assertTrue(result.agents.any { it.agentType == CollectiveAgentType.AITeacher })
        assertTrue(result.agents.any { it.agentType == CollectiveAgentType.CompanionAI })
        assertEquals(result.agents.size, result.tasks.size)
    }

    @Test
    fun collectiveCycle_exchangesKnowledgeDebatesAndReachesConsensus() {
        val result = engine.solveEducationalProblem(sampleRequest())

        assertFalse(result.knowledgeExchanges.isEmpty())
        assertTrue(result.knowledgeExchanges.all { it.sources.contains("Knowledge Graph") })
        assertEquals(ConsensusOutcome.PartialConsensus, result.consensus.outcome)
        assertTrue(result.consensus.rankedStrategies.isNotEmpty())
        assertTrue(result.debate.verifiedFacts.any { it.contains("scientific accuracy") })
    }

    @Test
    fun collectiveCycle_integratesHumanFeedbackGovernanceAndOptimization() {
        val result = engine.solveEducationalProblem(sampleRequest())

        assertEquals("teacher-or-expert", result.humanFeedback.role)
        assertTrue(result.governance.permissions.contains("human-control"))
        assertTrue(result.decision.reasoningTrace.any { it.contains("Human feedback included: true") })
        assertTrue(result.learningResult.assessmentImprovements.any { it.contains("micro-quiz") })
        assertTrue(result.analytics.improvementScore >= 80)
    }

    private fun sampleRequest(): CollectiveAIRequest =
        CollectiveAIRequest(
            userId = "learner-collective",
            problem = "Learner cannot connect voltage, current, and resistance.",
            topic = "Electric Circuits",
            learnerSignals = listOf("misconception detected", "needs AR visual", "low quiz confidence"),
            humanFeedback = "Teacher wants a more concrete example.",
        )
}
