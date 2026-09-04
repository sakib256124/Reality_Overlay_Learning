package com.rola.app.core.ai

import com.rola.app.data.adaptive.UserProfileRepository
import com.rola.app.data.knowledgegraph.KnowledgeGraphRepository
import com.rola.app.data.quiz.QuizRepository
import com.rola.app.domain.model.EducationDashboardState
import com.rola.app.domain.model.LearningProgress
import com.rola.app.domain.model.LearningProfile
import com.rola.app.domain.model.PersonalLearningReport
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class EcosystemAnalyticsManager @Inject constructor(
    private val userProfileRepository: UserProfileRepository,
    private val quizRepository: QuizRepository,
    private val knowledgeGraphRepository: KnowledgeGraphRepository,
) {
    fun observeEducationDashboard(): Flow<EducationDashboardState> =
        combine(
            userProfileRepository.observeProfile(),
            quizRepository.observeProgress(),
            userProfileRepository.observeRecommendations(),
            knowledgeGraphRepository.observeConceptGraph(),
        ) { profile, progress, recommendations, graph ->
            EducationDashboardState(
                profile = profile,
                progress = progress,
                knowledgeMapSummary = "${graph.nodes.size} concepts and ${graph.relations.size} relationships available.",
                achievements = achievements(progress, profile),
                recommendations = recommendations,
                report = profile?.let { buildPersonalLearningReport(it, progress) },
            )
        }

    fun buildPersonalLearningReport(
        profile: LearningProfile,
        progress: LearningProgress,
    ): PersonalLearningReport = PersonalLearningReport(
        userId = profile.userId,
        learningGrowth = when {
            profile.totalObjectsLearned >= 50 -> "Strong exploratory growth across multiple object categories."
            profile.totalObjectsLearned >= 10 -> "Steady growth with enough scans for personalized guidance."
            else -> "Early learning stage; more AR sessions will improve personalization."
        },
        knowledgeMastery = ((profile.averageQuizScore + profile.progressPercent + progress.averageScore) / 3).coerceIn(0, 100),
        interestAreas = (profile.favoriteCategories + profile.frequentlySearchedTopics).distinct().take(6),
        learningPatterns = listOf(
            "Learning speed: ${profile.learningSpeed.name}",
            "Learning streak: ${profile.learningStreak}",
            "Quiz average: ${progress.averageScore}%",
        ),
        performanceTrends = listOf(
            if (progress.averageScore >= 80) "Quiz performance is trending strong." else "Review sessions should focus on weak areas.",
            if (profile.totalLearningTimeMillis > 0) "Learning time is being tracked for progress reports." else "Learning time data is still sparse.",
        ),
        aiInsights = buildList {
            if (profile.weakAreas.isNotEmpty()) add("Recommended review: ${profile.weakAreas.take(3).joinToString()}")
            if (progress.badges.isNotEmpty()) add("Achievements earned: ${progress.badges.take(3).joinToString()}")
            if (profile.personalizationEnabled) add("Personalized AI tutoring is enabled.")
        },
    )

    private fun achievements(
        progress: LearningProgress,
        profile: LearningProfile?,
    ): List<String> = buildList {
        addAll(progress.badges)
        if ((profile?.totalObjectsLearned ?: 0) >= 10) add("AR Explorer")
        if ((profile?.learningStreak ?: 0) >= 7) add("Weekly Learner")
        if (progress.averageScore >= 90) add("Mastery Signal")
    }.distinct()
}
