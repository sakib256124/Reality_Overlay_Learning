package com.rola.app.core.ai

import com.rola.app.domain.model.AIAgentType
import com.rola.app.domain.model.AIResponse
import com.rola.app.domain.model.UnifiedLearningContext
import com.rola.app.domain.model.UserLearningAction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DecisionEngine @Inject constructor() {
    fun nextAction(
        currentAction: UserLearningAction,
        context: UnifiedLearningContext?,
    ): UserLearningAction = when (currentAction) {
        UserLearningAction.StartSession -> UserLearningAction.DetectObject
        UserLearningAction.DetectObject -> if (context?.detectedObjects.isNullOrEmpty()) {
            UserLearningAction.DetectObject
        } else {
            UserLearningAction.RetrieveKnowledge
        }
        UserLearningAction.RetrieveKnowledge -> UserLearningAction.ShowArVisualization
        UserLearningAction.ShowArVisualization -> UserLearningAction.ExplainWithTutor
        UserLearningAction.ExplainWithTutor -> UserLearningAction.GenerateQuiz
        UserLearningAction.TranslateContent -> UserLearningAction.ExplainWithTutor
        UserLearningAction.GenerateQuiz -> UserLearningAction.AssessProgress
        UserLearningAction.AssessProgress -> UserLearningAction.RecommendNext
        UserLearningAction.RecommendNext -> UserLearningAction.RecordAnalytics
        UserLearningAction.ResearchExpansion -> UserLearningAction.RetrieveKnowledge
        UserLearningAction.RecordAnalytics -> UserLearningAction.RecommendNext
    }

    fun responseFor(
        agents: List<AIAgentType>,
        currentAction: UserLearningAction,
        context: UnifiedLearningContext?,
        insights: List<String>,
    ): AIResponse {
        val next = nextAction(currentAction, context)
        val confidence = when {
            context == null -> 0.45f
            context.detectedObjects.isNotEmpty() && context.knowledgeGraphContext.isNotBlank() -> 0.9f
            context.knowledgeGraphContext.isNotBlank() || context.researchContext.isNotBlank() -> 0.78f
            else -> 0.62f
        }
        return AIResponse(
            selectedAgents = agents,
            message = "Coordinated ${agents.joinToString { it.name }} for ${currentAction.name}.",
            confidence = confidence,
            recommendedNextAction = next,
            insights = insights,
        )
    }
}
