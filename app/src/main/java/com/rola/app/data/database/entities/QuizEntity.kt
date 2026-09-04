package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.rola.app.domain.model.Question
import com.rola.app.domain.model.Quiz
import com.rola.app.domain.model.QuizDifficulty

@Entity(
    tableName = "quizzes",
    indices = [Index(value = ["objectId"])],
)
data class QuizEntity(
    @PrimaryKey val quizId: String,
    val objectId: String,
    val title: String,
    val difficulty: QuizDifficulty,
    val questionsJson: String,
    val createdDate: Long,
) {
    fun toDomain(): Quiz = Quiz(
        quizId = quizId,
        objectId = objectId,
        title = title,
        difficulty = difficulty,
        questions = decodeQuestions(questionsJson),
        createdDate = createdDate,
    )

    private fun decodeQuestions(value: String): List<Question> =
        if (value.isBlank()) {
            emptyList()
        } else {
            Gson().fromJson(value, object : TypeToken<List<Question>>() {}.type)
        }
}

fun Quiz.toEntity(): QuizEntity = QuizEntity(
    quizId = quizId,
    objectId = objectId,
    title = title,
    difficulty = difficulty,
    questionsJson = Gson().toJson(questions),
    createdDate = createdDate,
)
