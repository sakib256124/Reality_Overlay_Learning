package com.rola.app.reasoning_ai

import com.rola.app.data.database.ReasoningAIDao
import com.rola.app.data.database.entities.AIReasoningHistoryEntity
import com.rola.app.data.database.entities.CriticalThinkingAnalyticsEntity
import com.rola.app.data.database.entities.ExplanationRecordEntity
import com.rola.app.data.database.entities.InferenceRecordEntity
import com.rola.app.data.database.entities.ProblemSolutionEntity
import com.rola.app.data.database.entities.ReasoningImprovementEntity
import com.rola.app.data.database.entities.ReasoningProfileEntity
import com.rola.app.reasoning_ai.reasoning_core.ReasoningResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class ReasoningAIRepository @Inject constructor(private val dao: ReasoningAIDao) {
    fun observeDashboard(): Flow<ReasoningDashboardState> =
        combine(dao.observeProfile(), dao.observeSolution(), dao.observeExplanation(), dao.observeCritical(), dao.observeImprovement()) { profile, solution, explanation, critical, improvement ->
            ReasoningDashboardState(
                reasoningAbility = profile?.logicalAbility ?: 0,
                problemSolvingProgress = solution?.answer.orEmpty(),
                criticalThinkingScore = critical?.logicalThinkingScore ?: 0,
                learningImprovements = critical?.recommendations.orEmpty(),
                recommendations = listOfNotNull(improvement?.finalRecommendation, explanation?.beginnerExplanation),
            )
        }

    suspend fun save(result: ReasoningResult) {
        dao.upsertProfile(ReasoningProfileEntity(result.profile.profileId, result.profile.problemSolvingStyle, result.profile.logicalAbility, result.profile.learningMistakes, result.profile.improvementPlan))
        dao.upsertHistory(AIReasoningHistoryEntity(result.trace.traceId, result.trace.conceptUnderstanding, result.trace.logicalSteps, result.trace.knowledgeConnections))
        dao.upsertSolution(ProblemSolutionEntity(result.solution.solutionId, result.solution.answer, result.solution.method, result.solution.confidence.name))
        dao.upsertInference(InferenceRecordEntity(result.inference.inferenceId, result.inference.hiddenDiscoveries, result.inference.patterns, result.inference.conclusions))
        dao.upsertExplanation(ExplanationRecordEntity(result.explanation.explanationId, result.explanation.steps, result.explanation.beginnerExplanation, result.explanation.expertExplanation, result.explanation.realWorldExample))
        dao.upsertCritical(CriticalThinkingAnalyticsEntity(result.criticalThinking.reportId, result.criticalThinking.analysisScore, result.criticalThinking.logicalThinkingScore, result.criticalThinking.decisionScore, result.criticalThinking.recommendations))
        dao.upsertImprovement(ReasoningImprovementEntity(result.decision.decisionId, result.decision.finalRecommendation, result.decision.verified, result.decision.humanReviewSupported))
    }
}

data class ReasoningDashboardState(
    val reasoningAbility: Int = 0,
    val problemSolvingProgress: String = "",
    val criticalThinkingScore: Int = 0,
    val learningImprovements: List<String> = emptyList(),
    val recommendations: List<String> = emptyList(),
)
