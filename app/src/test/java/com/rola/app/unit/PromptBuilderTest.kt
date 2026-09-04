package com.rola.app.unit

import com.rola.app.data.chatbot.PromptBuilder
import com.rola.app.domain.model.LearningContext
import com.rola.app.domain.model.LearningLevel
import com.rola.app.domain.model.LearningProgress
import com.rola.app.domain.model.ObjectInformation
import org.junit.Assert.assertTrue
import org.junit.Test

class PromptBuilderTest {
    private val promptBuilder = PromptBuilder()

    @Test
    fun promptContainsGroundingContext() {
        val prompt = promptBuilder.buildPrompt(
            userQuestion = "What is this made of?",
            context = LearningContext(
                userId = "local_user",
                currentObject = ObjectInformation(
                    objectId = "bottle",
                    name = "Bottle",
                    scientificName = "Polyethylene terephthalate container",
                    category = "Plastic Object",
                    description = "A lightweight container.",
                    uses = listOf("Storage"),
                    facts = listOf("PET is recyclable."),
                    imageUrl = "",
                ),
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
                learningLevel = LearningLevel.Beginner,
            ),
        )

        assertTrue(prompt.contains("Bottle"))
        assertTrue(prompt.contains("Polyethylene terephthalate"))
    }
}
