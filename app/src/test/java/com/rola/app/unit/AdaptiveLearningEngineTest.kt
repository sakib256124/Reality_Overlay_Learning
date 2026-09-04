package com.rola.app.unit

import com.rola.app.data.adaptive.LearningAnalyzer
import com.rola.app.data.adaptive.RecommendationEngine
import com.rola.app.domain.model.LearningStatus
import com.rola.app.domain.model.QuizResult
import com.rola.app.domain.model.RecommendationType
import com.rola.app.domain.model.ScanHistory
import com.rola.app.domain.model.SkillLevel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class AdaptiveLearningEngineTest {
    private val analyzer = LearningAnalyzer()
    private val recommendationEngine = RecommendationEngine()

    @Test
    fun buildProfile_detectsFavoriteCategoriesAndBeginnerLevel() = runTest {
        val profile = analyzer.buildProfile(
            userId = "user-1",
            scans = listOf(
                scan("apple", "Plants"),
                scan("leaf", "Plants"),
                scan("bottle", "Materials"),
            ),
            quizResults = listOf(quizResult(45)),
            messages = emptyList(),
            preferredLanguage = "en",
        )

        assertEquals("Plants", profile.favoriteCategories.first())
        assertEquals(SkillLevel.Beginner, profile.learningLevel)
        assertTrue("Foundational concepts" in profile.weakAreas)
    }

    @Test
    fun generateRecommendations_prioritizesWeakAreaReviewForLowScores() = runTest {
        val profile = analyzer.buildProfile(
            userId = "user-1",
            scans = listOf(scan("bottle", "Materials")),
            quizResults = listOf(quizResult(40)),
            messages = emptyList(),
            preferredLanguage = "en",
        )
        val pattern = analyzer.detectPattern(
            scans = listOf(scan("bottle", "Materials")),
            quizResults = listOf(quizResult(40)),
            messages = emptyList(),
        )

        val recommendations = recommendationEngine.generateRecommendations(profile, pattern)

        assertTrue(recommendations.any { it.type == RecommendationType.ReviewWeakArea })
        assertEquals(SkillLevel.Beginner, recommendationEngine.adaptiveQuizDifficulty(profile))
    }

    private fun scan(objectId: String, category: String): ScanHistory = ScanHistory(
        scanId = "$objectId-scan",
        userId = "user-1",
        objectId = objectId,
        objectName = objectId,
        category = category,
        timestamp = System.currentTimeMillis(),
        confidenceScore = 0.9f,
        learningStatus = LearningStatus.Scanned,
        imageUrl = "",
    )

    private fun quizResult(score: Int): QuizResult = QuizResult(
        resultId = "result-$score",
        userId = "user-1",
        quizId = "quiz-$score",
        score = score / 10,
        totalQuestions = 10,
        percentage = score,
        completionTime = 120_000L,
        timestamp = System.currentTimeMillis(),
    )
}
