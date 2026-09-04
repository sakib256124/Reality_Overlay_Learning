package com.rola.app.core.ai

import com.rola.app.domain.model.AIAgentType
import com.rola.app.domain.model.AIRequest
import com.rola.app.domain.model.UserLearningAction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AgentManager @Inject constructor() {
    fun agentsFor(request: AIRequest): List<AIAgentType> = when (request.action) {
        UserLearningAction.StartSession -> listOf(AIAgentType.LearningAgent, AIAgentType.AnalyticsAgent)
        UserLearningAction.DetectObject -> listOf(AIAgentType.VisionAgent, AIAgentType.KnowledgeAgent)
        UserLearningAction.RetrieveKnowledge -> listOf(AIAgentType.KnowledgeAgent, AIAgentType.ResearchAgent)
        UserLearningAction.ShowArVisualization -> listOf(AIAgentType.VisionAgent, AIAgentType.KnowledgeAgent)
        UserLearningAction.ExplainWithTutor -> listOf(AIAgentType.TutorAgent, AIAgentType.KnowledgeAgent, AIAgentType.ResearchAgent)
        UserLearningAction.TranslateContent -> listOf(AIAgentType.TranslationAgent)
        UserLearningAction.GenerateQuiz -> listOf(AIAgentType.QuizAgent, AIAgentType.LearningAgent)
        UserLearningAction.AssessProgress -> listOf(AIAgentType.LearningAgent, AIAgentType.AnalyticsAgent)
        UserLearningAction.RecommendNext -> listOf(AIAgentType.LearningAgent, AIAgentType.KnowledgeAgent)
        UserLearningAction.ResearchExpansion -> listOf(AIAgentType.ResearchAgent, AIAgentType.KnowledgeAgent)
        UserLearningAction.RecordAnalytics -> listOf(AIAgentType.AnalyticsAgent)
    }

    fun communicationPlan(request: AIRequest): String =
        agentsFor(request).joinToString(separator = " -> ") { it.name }
}
