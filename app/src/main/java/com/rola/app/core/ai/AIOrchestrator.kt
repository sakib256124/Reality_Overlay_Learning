package com.rola.app.core.ai

import com.rola.app.data.adaptive.UserProfileRepository
import com.rola.app.data.cloud.CloudAnalyticsService
import com.rola.app.domain.model.AIRequest
import com.rola.app.domain.model.AIResponse
import com.rola.app.domain.model.DetectedObject
import com.rola.app.domain.model.SceneContext
import com.rola.app.domain.model.UserLearningAction
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AIOrchestrator @Inject constructor(
    private val agentManager: AgentManager,
    private val contextManager: ContextManager,
    private val decisionEngine: DecisionEngine,
    private val userProfileRepository: UserProfileRepository,
    private val cloudAnalyticsService: CloudAnalyticsService,
) {
    suspend fun orchestrate(request: AIRequest): AIResponse {
        val context = request.context ?: contextManager.buildContext(
            objective = request.query.ifBlank { request.action.name },
        )
        val agents = agentManager.agentsFor(request.copy(context = context))
        val insights = generateInsights(context)
        val response = decisionEngine.responseFor(agents, request.action, context, insights)
        cloudAnalyticsService.recordEvent(
            eventType = "ai_orchestration",
            metrics = mapOf(
                "action" to request.action.name,
                "agentCount" to agents.size,
                "confidence" to response.confidence,
                "nextAction" to response.recommendedNextAction.name,
            ),
        )
        return response
    }

    suspend fun orchestrateLearningAction(
        action: UserLearningAction,
        objective: String,
        detectedObjects: List<DetectedObject> = emptyList(),
        sceneContext: SceneContext? = null,
    ): AIResponse {
        val context = contextManager.buildContext(
            objective = objective,
            detectedObjects = detectedObjects,
            sceneContext = sceneContext,
        )
        return orchestrate(AIRequest(action = action, query = objective, context = context))
    }

    private suspend fun generateInsights(context: com.rola.app.domain.model.UnifiedLearningContext): List<String> {
        val pattern = runCatching { userProfileRepository.detectPattern() }.getOrNull()
        return buildList {
            add("Learning level: ${context.learningLevel.name}")
            if (context.detectedObjects.isNotEmpty()) {
                add("Detected focus: ${context.detectedObjects.first().label}")
            }
            if (context.preferences.interestAreas.isNotEmpty()) {
                add("Interest areas: ${context.preferences.interestAreas.joinToString()}")
            }
            pattern?.knowledgeGaps?.takeIf { it.isNotEmpty() }?.let { gaps ->
                add("Knowledge gaps: ${gaps.take(3).joinToString()}")
            }
            if (context.researchContext.isNotBlank()) {
                add("Research-enhanced explanation is available.")
            }
        }
    }
}
