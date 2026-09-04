package com.rola.app.data.quiz

import com.rola.app.domain.model.Answer
import javax.inject.Inject

class QuizDataSource @Inject constructor() {
    fun commonDistractors(correctAnswer: String): List<Answer> =
        listOf("Wood", "Silica", "Iron", "Plastic", "Water", "Carbon", "Cellulose", "Metal")
            .filterNot { it.equals(correctAnswer, ignoreCase = true) }
            .take(6)
            .mapIndexed { index, value -> Answer(answerId = "distractor_$index", text = value) }

    fun categoryDistractors(correctCategory: String): List<Answer> =
        listOf("Plant", "Animal", "Mineral", "Plastic Object", "Household Object", "Electronic Object")
            .filterNot { it.equals(correctCategory, ignoreCase = true) }
            .take(6)
            .mapIndexed { index, value -> Answer(answerId = "category_$index", text = value) }
}
