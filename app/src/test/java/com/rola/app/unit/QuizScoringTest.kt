package com.rola.app.unit

import com.rola.app.domain.model.Answer
import com.rola.app.domain.model.Question
import com.rola.app.domain.model.Quiz
import com.rola.app.domain.model.QuizDifficulty
import com.rola.app.presentation.quiz.QuizUiState
import org.junit.Assert.assertEquals
import org.junit.Test

class QuizScoringTest {
    @Test
    fun scoreCountsOnlyCorrectSelectedAnswers() {
        val correct = Answer("a1", "Silica")
        val incorrect = Answer("a2", "Wood")
        val question = Question(
            questionId = "q1",
            questionText = "Which material is used to make glass?",
            options = listOf(correct, incorrect),
            correctAnswer = correct,
            explanation = "Glass is primarily produced from silica sand.",
        )
        val state = QuizUiState(
            quiz = Quiz(
                quizId = "quiz",
                objectId = "glass",
                title = "Glass Quiz",
                difficulty = QuizDifficulty.Easy,
                questions = listOf(question),
                createdDate = 1L,
            ),
            selectedAnswers = mapOf("q1" to correct),
        )

        assertEquals(1, state.score)
    }
}
