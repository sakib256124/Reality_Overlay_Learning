package com.rola.app.unit

import com.rola.app.data.chatbot.LLMService
import com.rola.app.data.chatbot.PromptBuilder
import com.rola.app.domain.model.LearningContext
import com.rola.app.domain.model.LearningProgress
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertTrue
import org.junit.Test

class LLMServiceTest {
    private val service = LLMService(PromptBuilder())

    @Test
    fun returnsOfflineFallbackWhenNoObjectIsSelected() = runTest {
        val response = service.generateResponse(
            question = "What is this?",
            context = LearningContext(
                userId = "local_user",
                currentObject = null,
                recentObjects = emptyList(),
                progress = LearningProgress(
                    totalQuizzesCompleted = 0,
                    averageScore = 0,
                    totalPoints = 0,
                    currentLevel = 1,
                    learningStreak = 0,
                    strongTopics = emptyList(),
                    weakTopics = emptyList(),
                    badges = emptyList(),
                ),
                previousMessages = emptyList(),
            ),
        )

        assertTrue(response.contains("scan or search"))
    }
}
