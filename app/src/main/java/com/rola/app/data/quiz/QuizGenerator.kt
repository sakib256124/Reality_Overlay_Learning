package com.rola.app.data.quiz

import com.rola.app.domain.model.Answer
import com.rola.app.domain.model.ObjectInformation
import com.rola.app.domain.model.Question
import com.rola.app.domain.model.Quiz
import com.rola.app.domain.model.QuizDifficulty
import java.util.UUID
import javax.inject.Inject

class QuizGenerator @Inject constructor(
    private val quizDataSource: QuizDataSource,
) {
    fun generateQuiz(
        objectInformation: ObjectInformation,
        difficulty: QuizDifficulty,
    ): Quiz {
        val questions = when (difficulty) {
            QuizDifficulty.Easy -> easyQuestions(objectInformation)
            QuizDifficulty.Medium -> easyQuestions(objectInformation) + scienceQuestions(objectInformation)
            QuizDifficulty.Hard -> easyQuestions(objectInformation) +
                scienceQuestions(objectInformation) +
                reasoningQuestions(objectInformation)
        }.distinctBy { it.questionText }.take(MAX_QUESTIONS)

        require(questions.isNotEmpty()) { "Unable to generate quiz from missing object information." }

        return Quiz(
            quizId = "${objectInformation.objectId}_${difficulty.name.lowercase()}",
            objectId = objectInformation.objectId,
            title = "${objectInformation.name} Quiz",
            difficulty = difficulty,
            questions = questions,
            createdDate = System.currentTimeMillis(),
        )
    }

    private fun easyQuestions(info: ObjectInformation): List<Question> = listOf(
        multipleChoiceQuestion(
            text = "What object did you just learn about?",
            correct = info.name,
            distractors = listOf("Leaf", "Smartphone", "Flower", "Chair"),
            explanation = "The AR scanner identified this object as ${info.name}.",
        ),
        multipleChoiceQuestion(
            text = "Which category does ${info.name} belong to?",
            correct = info.category,
            distractors = quizDataSource.categoryDistractors(info.category).map { it.text },
            explanation = "${info.name} is categorized as ${info.category}.",
        ),
    )

    private fun scienceQuestions(info: ObjectInformation): List<Question> = listOf(
        multipleChoiceQuestion(
            text = "What scientific name or material is connected to ${info.name}?",
            correct = info.scientificName.ifBlank { info.category },
            distractors = quizDataSource.commonDistractors(info.scientificName).map { it.text },
            explanation = "The scientific information shown for ${info.name} is ${info.scientificName.ifBlank { info.category }}.",
        ),
        multipleChoiceQuestion(
            text = "Which statement best describes ${info.name}?",
            correct = info.description,
            distractors = listOf(
                "It is mainly used to generate electricity.",
                "It is only found underwater.",
                "It is a type of musical instrument.",
            ),
            explanation = info.description,
        ),
    )

    private fun reasoningQuestions(info: ObjectInformation): List<Question> = listOf(
        multipleChoiceQuestion(
            text = "What is one common use of ${info.name}?",
            correct = info.uses.firstOrNull().orEmpty().ifBlank { "Learning and observation" },
            distractors = listOf("Navigation in space", "Weather forecasting", "Deep sea drilling"),
            explanation = "${info.name} is commonly used for ${info.uses.joinToString().ifBlank { "learning and observation" }}.",
        ),
        multipleChoiceQuestion(
            text = "Which fact about ${info.name} is correct?",
            correct = info.facts.firstOrNull().orEmpty().ifBlank { "${info.name} can be studied by observing its properties." },
            distractors = listOf(
                "It has no observable properties.",
                "It cannot be classified.",
                "It is unrelated to science.",
            ),
            explanation = info.facts.firstOrNull().orEmpty().ifBlank {
                "Objects can be studied by observing their structure, category, and uses."
            },
        ),
    )

    private fun multipleChoiceQuestion(
        text: String,
        correct: String,
        distractors: List<String>,
        explanation: String,
    ): Question {
        val correctAnswer = Answer(answerId = UUID.randomUUID().toString(), text = correct)
        val options = (listOf(correctAnswer) + distractors
            .filter { it.isNotBlank() && !it.equals(correct, ignoreCase = true) }
            .distinct()
            .take(3)
            .map { distractor -> Answer(answerId = UUID.randomUUID().toString(), text = distractor) })
            .shuffled()

        return Question(
            questionId = UUID.randomUUID().toString(),
            questionText = text,
            options = options,
            correctAnswer = correctAnswer,
            explanation = explanation,
        )
    }

    private companion object {
        const val MAX_QUESTIONS = 6
    }
}
