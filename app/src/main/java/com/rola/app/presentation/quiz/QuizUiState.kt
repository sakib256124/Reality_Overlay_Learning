package com.rola.app.presentation.quiz

import com.rola.app.domain.model.Answer
import com.rola.app.domain.model.Language
import com.rola.app.domain.model.LearningProgress
import com.rola.app.domain.model.Question
import com.rola.app.domain.model.Quiz
import com.rola.app.domain.model.QuizDifficulty
import com.rola.app.domain.model.QuizResult

enum class QuizStatus {
    Loading,
    Ready,
    Answered,
    Complete,
    Error,
}

data class QuizUiState(
    val status: QuizStatus = QuizStatus.Loading,
    val quiz: Quiz? = null,
    val baseQuiz: Quiz? = null,
    val languages: List<Language> = emptyList(),
    val selectedLanguageCode: String = "en",
    val isTranslating: Boolean = false,
    val selectedDifficulty: QuizDifficulty = QuizDifficulty.Easy,
    val currentQuestionIndex: Int = 0,
    val selectedAnswers: Map<String, Answer> = emptyMap(),
    val quizResult: QuizResult? = null,
    val progress: LearningProgress = LearningProgress(
        totalQuizzesCompleted = 0,
        averageScore = 0,
        totalPoints = 0,
        currentLevel = 1,
        learningStreak = 0,
        strongTopics = emptyList(),
        weakTopics = emptyList(),
        badges = emptyList(),
    ),
    val startedAt: Long = System.currentTimeMillis(),
    val errorMessage: String? = null,
) {
    val currentQuestion: Question?
        get() = quiz?.questions?.getOrNull(currentQuestionIndex)

    val questionCount: Int
        get() = quiz?.questions?.size ?: 0

    val score: Int
        get() = quiz?.questions.orEmpty().count { question ->
            selectedAnswers[question.questionId]?.answerId == question.correctAnswer.answerId
        }

    val selectedAnswer: Answer?
        get() = currentQuestion?.let { selectedAnswers[it.questionId] }
}
