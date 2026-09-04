package com.rola.app.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rola.app.data.database.entities.QuizEntity

@Dao
interface QuizDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertQuiz(quiz: QuizEntity)

    @Query("SELECT * FROM quizzes WHERE quizId = :quizId LIMIT 1")
    suspend fun getQuizById(quizId: String): QuizEntity?

    @Query("SELECT * FROM quizzes WHERE objectId = :objectId AND difficulty = :difficulty LIMIT 1")
    suspend fun getQuizForObject(objectId: String, difficulty: String): QuizEntity?
}
