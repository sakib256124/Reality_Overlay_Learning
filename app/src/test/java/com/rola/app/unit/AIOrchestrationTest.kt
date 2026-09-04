package com.rola.app.unit

import com.rola.app.core.ai.AgentManager
import com.rola.app.core.ai.DecisionEngine
import com.rola.app.domain.model.AIAgentType
import com.rola.app.domain.model.AIRequest
import com.rola.app.domain.model.LearningPreferences
import com.rola.app.domain.model.SkillLevel
import com.rola.app.domain.model.UnifiedLearningContext
import com.rola.app.domain.model.UserLearningAction
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AIOrchestrationTest {
    private val agentManager = AgentManager()
    private val decisionEngine = DecisionEngine()

    @Test
    fun explainWithTutorSelectsTutorKnowledgeAndResearchAgents() {
        val agents = agentManager.agentsFor(AIRequest(UserLearningAction.ExplainWithTutor, "Why copper conducts?"))

        assertTrue(AIAgentType.TutorAgent in agents)
        assertTrue(AIAgentType.KnowledgeAgent in agents)
        assertTrue(AIAgentType.ResearchAgent in agents)
    }

    @Test
    fun learningFlowMovesFromAssessmentToRecommendations() {
        val next = decisionEngine.nextAction(UserLearningAction.AssessProgress, context())

        assertEquals(UserLearningAction.RecommendNext, next)
    }

    @Test
    fun groundedContextProducesHighConfidenceResponse() {
        val response = decisionEngine.responseFor(
            agents = listOf(AIAgentType.KnowledgeAgent, AIAgentType.TutorAgent),
            currentAction = UserLearningAction.ExplainWithTutor,
            context = context(knowledgeGraphContext = "Copper -> conductivity -> wiring"),
            insights = listOf("Research-enhanced explanation is available."),
        )

        assertTrue(response.confidence >= 0.78f)
        assertEquals(UserLearningAction.GenerateQuiz, response.recommendedNextAction)
    }

    private fun context(knowledgeGraphContext: String = ""): UnifiedLearningContext = UnifiedLearningContext(
        userId = "user-1",
        learningLevel = SkillLevel.Beginner,
        previousKnowledge = listOf("Materials"),
        preferences = LearningPreferences(),
        language = "en",
        currentObjective = "Learn copper",
        knowledgeGraphContext = knowledgeGraphContext,
    )
}
