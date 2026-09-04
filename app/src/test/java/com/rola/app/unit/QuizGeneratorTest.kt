package com.rola.app.unit

import com.rola.app.data.quiz.QuizDataSource
import com.rola.app.data.quiz.QuizGenerator
import com.rola.app.domain.model.ObjectInformation
import com.rola.app.domain.model.QuizDifficulty
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class QuizGeneratorTest {
    private val generator = QuizGenerator(QuizDataSource())

    @Test
    fun generatesEasyQuizFromObjectInformation() {
        val quiz = generator.generateQuiz(sampleBottle, QuizDifficulty.Easy)

        assertEquals("bottle_easy", quiz.quizId)
        assertTrue(quiz.questions.isNotEmpty())
        assertTrue(quiz.questions.all { question ->
            question.options.any { option -> option.text == question.correctAnswer.text }
        })
    }

    @Test
    fun hardQuizContainsMoreQuestionsThanEasyQuiz() {
        val easy = generator.generateQuiz(sampleBottle, QuizDifficulty.Easy)
        val hard = generator.generateQuiz(sampleBottle, QuizDifficulty.Hard)

        assertTrue(hard.questions.size > easy.questions.size)
    }

    private val sampleBottle = ObjectInformation(
        objectId = "bottle",
        name = "Bottle",
        scientificName = "Polyethylene terephthalate container",
        category = "Plastic Object",
        description = "A lightweight container used to store liquids.",
        uses = listOf("Storage", "Transportation"),
        facts = listOf("PET is recyclable."),
        imageUrl = "",
    )
}
