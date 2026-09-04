package com.rola.app.core.ai

import com.rola.app.data.adaptive.UserProfileRepository
import com.rola.app.data.cloud.CloudAnalyticsService
import com.rola.app.data.quiz.QuizRepository
import com.rola.app.domain.model.AIRequest
import com.rola.app.domain.model.LearningSessionState
import com.rola.app.domain.model.LearningSessionStatus
import com.rola.app.domain.model.LearningSessionSummary
import com.rola.app.domain.model.UserLearningAction
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.first

@Singleton
class LearningExperienceManager @Inject constructor(
    private val aiOrchestrator: AIOrchestrator,
    private val contextManager: ContextManager,
    private val userProfileRepository: UserProfileRepository,
    private val quizRepository: QuizRepository,
    private val cloudAnalyticsService: CloudAnalyticsService,
) {
    suspend fun startSession(objective: String): LearningSessionState {
        val context = contextManager.buildContext(objective)
        val session = LearningSessionState(
            sessionId = "session-${UUID.randomUUID()}",
            userId = context.userId,
            objective = objective,
            context = context,
            completedSteps = listOf(UserLearningAction.StartSession),
        )
        cloudAnalyticsService.recordEvent("learning_session_started", mapOf("objective" to objective))
        aiOrchestrator.orchestrate(AIRequest(UserLearningAction.StartSession, objective, context = context))
        return session
    }

    suspend fun advanceSession(
        session: LearningSessionState,
        action: UserLearningAction,
    ): LearningSessionState {
        val response = aiOrchestrator.orchestrate(AIRequest(action = action, query = session.objective, context = session.context))
        val updatedSteps = (session.completedSteps + action).distinct()
        val status = when (action) {
            UserLearningAction.DetectObject -> LearningSessionStatus.Detecting
            UserLearningAction.GenerateQuiz,
            UserLearningAction.AssessProgress,
            -> LearningSessionStatus.Assessing
            UserLearningAction.RecordAnalytics,
            UserLearningAction.RecommendNext,
            -> LearningSessionStatus.Completed
            else -> LearningSessionStatus.Learning
        }
        val summary = if (status == LearningSessionStatus.Completed) generateSummary(session.copy(completedSteps = updatedSteps)) else null
        cloudAnalyticsService.recordEvent(
            eventType = "learning_session_step",
            metrics = mapOf("action" to action.name, "nextAction" to response.recommendedNextAction.name),
        )
        return session.copy(
            status = status,
            completedSteps = updatedSteps,
            summary = summary,
            updatedAt = System.currentTimeMillis(),
        )
    }

    suspend fun runFullLearningJourney(objective: String): LearningSessionState {
        var session = startSession(objective)
        learningFlow.drop(1).forEach { action ->
            session = advanceSession(session, action)
        }
        return session
    }

    suspend fun generateSummary(session: LearningSessionState): LearningSessionSummary {
        val profile = runCatching { userProfileRepository.refreshLearningProfile() }.getOrNull()
        val progress = runCatching { quizRepository.observeProgress().first() }.getOrNull()
        return LearningSessionSummary(
            sessionId = session.sessionId,
            learnedTopics = listOf(session.objective) + session.context.detectedObjects.map { it.label },
            masterySignals = listOfNotNull(
                profile?.learningLevel?.name?.let { "Current mastery level: $it" },
                progress?.let { "Quiz average: ${it.averageScore}% across ${it.totalQuizzesCompleted} attempts." },
                "Completed steps: ${session.completedSteps.size}",
            ),
            nextRecommendations = profile?.weakAreas?.take(3).orEmpty().ifEmpty {
                listOf("Continue exploring ${session.objective}", "Ask the AI tutor a follow-up question")
            },
            totalSteps = session.completedSteps.size,
            durationMillis = (System.currentTimeMillis() - session.startedAt).coerceAtLeast(0L),
        )
    }

    private companion object {
        val learningFlow = listOf(
            UserLearningAction.StartSession,
            UserLearningAction.DetectObject,
            UserLearningAction.RetrieveKnowledge,
            UserLearningAction.ShowArVisualization,
            UserLearningAction.ExplainWithTutor,
            UserLearningAction.GenerateQuiz,
            UserLearningAction.AssessProgress,
            UserLearningAction.RecommendNext,
            UserLearningAction.RecordAnalytics,
        )
    }
}
