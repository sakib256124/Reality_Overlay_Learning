package com.rola.app.data.quiz

import com.rola.app.data.database.QuizDao
import com.rola.app.data.database.QuizResultDao
import com.rola.app.data.database.UserDao
import com.rola.app.data.database.entities.QuizResultEntity
import com.rola.app.data.database.entities.UserEntity
import com.rola.app.data.database.entities.toEntity
import com.rola.app.data.firestore.FirestoreService
import com.rola.app.data.firestore.SyncManager
import com.rola.app.domain.model.LearningProgress
import com.rola.app.domain.model.ObjectInformation
import com.rola.app.domain.model.Quiz
import com.rola.app.domain.model.QuizDifficulty
import com.rola.app.domain.model.QuizResult
import com.rola.app.domain.model.toObjectInformation
import com.rola.app.domain.repository.LearningRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class QuizRepository @Inject constructor(
    private val learningRepository: LearningRepository,
    private val userDao: UserDao,
    private val quizDao: QuizDao,
    private val quizResultDao: QuizResultDao,
    private val quizGenerator: QuizGenerator,
    private val firestoreService: FirestoreService,
    private val syncManager: SyncManager,
) {
    val currentUserId: String
        get() = firestoreService.currentUserId ?: LOCAL_USER_ID

    suspend fun loadQuiz(
        objectId: String,
        difficulty: QuizDifficulty,
    ): Quiz {
        quizDao.getQuizForObject(objectId, difficulty.name)?.let { return it.toDomain() }

        val information = loadObjectInformation(objectId)
            ?: throw IllegalStateException("Missing object information for quiz generation.")
        val quiz = quizGenerator.generateQuiz(information, difficulty)
        quizDao.insertQuiz(quiz.toEntity())
        return quiz
    }

    suspend fun saveResult(
        quiz: Quiz,
        score: Int,
        completionTime: Long,
    ): QuizResult {
        ensureLocalUser()
        val percentage = if (quiz.questions.isEmpty()) 0 else (score * 100f / quiz.questions.size).toInt()
        val result = QuizResult(
            resultId = UUID.randomUUID().toString(),
            userId = currentUserId,
            quizId = quiz.quizId,
            score = score,
            totalQuestions = quiz.questions.size,
            percentage = percentage.coerceIn(0, 100),
            completionTime = completionTime,
            timestamp = System.currentTimeMillis(),
        )
        quizResultDao.insertResult(result.toEntity(isSynced = false))
        if (firestoreService.currentUserId != null) {
            runCatching { syncManager.sync(currentUserId) }
        }
        return result
    }

    fun observeProgress(): Flow<LearningProgress> =
        quizResultDao.observeResults(currentUserId).map { results ->
            val completed = results.size
            val average = results.map { it.percentage }.average().takeIf { !it.isNaN() }?.toInt() ?: 0
            val points = results.sumOf { it.score * POINTS_PER_CORRECT_ANSWER }
            LearningProgress(
                totalQuizzesCompleted = completed,
                averageScore = average,
                totalPoints = points,
                currentLevel = (points / POINTS_PER_LEVEL) + 1,
                learningStreak = calculateStreak(results.map { it.timestamp }),
                strongTopics = if (average >= 80) listOf("Object recognition", "Scientific facts") else emptyList(),
                weakTopics = if (average in 1..59) listOf("Review material properties") else emptyList(),
                badges = buildBadges(completed, average, points),
            )
        }

    private suspend fun loadObjectInformation(objectId: String): ObjectInformation? =
        learningRepository.getObjectById(objectId)?.toObjectInformation()
            ?: learningRepository.getObjectByName(objectId)?.toObjectInformation()

    private suspend fun ensureLocalUser() {
        val userId = currentUserId
        if (userDao.getUser(userId) != null) return

        userDao.insertUser(
            UserEntity(
                userId = userId,
                name = if (userId == LOCAL_USER_ID) "Offline Learner" else "ROLA Learner",
                email = "",
                profileImage = "",
                isSynced = false,
            ),
        )
    }

    private fun calculateStreak(timestamps: List<Long>): Int {
        val days = timestamps.map { timestamp -> timestamp / MILLIS_PER_DAY }.toSet()
        val today = System.currentTimeMillis() / MILLIS_PER_DAY

        var streak = 0
        var cursor = today
        while (days.contains(cursor)) {
            streak++
            cursor--
        }
        return streak
    }

    private fun buildBadges(
        completed: Int,
        average: Int,
        points: Int,
    ): List<String> = buildList {
        if (completed >= 1) add("First Quiz")
        if (completed >= 10) add("Knowledge Builder")
        if (completed >= 20) add("Explorer")
        if (average >= 90) add("Sharp Scholar")
        if (points >= 500) add("Level Climber")
    }

    private companion object {
        const val LOCAL_USER_ID = "local_user"
        const val POINTS_PER_CORRECT_ANSWER = 10
        const val POINTS_PER_LEVEL = 100
        const val MILLIS_PER_DAY = 86_400_000L
    }
}
