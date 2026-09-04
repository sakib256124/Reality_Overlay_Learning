package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.rola.app.domain.model.QuizResult

@Entity(
    tableName = "quiz_results",
    foreignKeys = [
        ForeignKey(
            entity = UserEntity::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE,
        ),
    ],
    indices = [
        Index(value = ["userId"]),
        Index(value = ["quizId"]),
        Index(value = ["timestamp"]),
        Index(value = ["isSynced"]),
    ],
)
data class QuizResultEntity(
    @PrimaryKey val resultId: String,
    val userId: String,
    val quizId: String,
    val score: Int,
    val totalQuestions: Int,
    val percentage: Int,
    val completionTime: Long,
    val timestamp: Long,
    val isSynced: Boolean = false,
    val updatedAt: Long = System.currentTimeMillis(),
) {
    fun toDomain(): QuizResult = QuizResult(
        resultId = resultId,
        userId = userId,
        quizId = quizId,
        score = score,
        totalQuestions = totalQuestions,
        percentage = percentage,
        completionTime = completionTime,
        timestamp = timestamp,
    )
}

fun QuizResult.toEntity(isSynced: Boolean = false): QuizResultEntity = QuizResultEntity(
    resultId = resultId,
    userId = userId,
    quizId = quizId,
    score = score,
    totalQuestions = totalQuestions,
    percentage = percentage,
    completionTime = completionTime,
    timestamp = timestamp,
    isSynced = isSynced,
)
