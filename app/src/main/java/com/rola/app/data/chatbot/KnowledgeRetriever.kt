package com.rola.app.data.chatbot

import com.rola.app.data.quiz.QuizRepository
import com.rola.app.domain.model.LearningContext
import com.rola.app.domain.model.ObjectInformation
import com.rola.app.domain.model.toObjectInformation
import com.rola.app.domain.repository.LearningHistoryRepository
import com.rola.app.domain.repository.LearningRepository
import javax.inject.Inject
import kotlinx.coroutines.flow.first

class KnowledgeRetriever @Inject constructor(
    private val learningRepository: LearningRepository,
    private val learningHistoryRepository: LearningHistoryRepository,
    private val quizRepository: QuizRepository,
) {
    suspend fun retrieveObjectInformation(
        objectId: String?,
        userQuestion: String,
    ): ObjectInformation? {
        objectId?.let { id ->
            learningRepository.getObjectById(id)?.toObjectInformation()?.let { return it }
        }

        val query = userQuestion.extractSearchQuery()
        return learningRepository.searchObjects(query)
            .first()
            .firstOrNull()
            ?.toObjectInformation()
    }

    suspend fun buildLearningContext(
        userId: String,
        objectInformation: ObjectInformation?,
        previousMessages: List<com.rola.app.domain.model.ChatMessage>,
    ): LearningContext {
        val recentObjects = learningHistoryRepository.observeUserHistory()
            .first()
            .take(5)
        val progress = quizRepository.observeProgress().first()
        val learningLevel = when {
            progress.averageScore >= 85 && progress.totalQuizzesCompleted >= 5 ->
                com.rola.app.domain.model.LearningLevel.Advanced
            progress.totalQuizzesCompleted >= 2 ->
                com.rola.app.domain.model.LearningLevel.Intermediate
            else ->
                com.rola.app.domain.model.LearningLevel.Beginner
        }

        return LearningContext(
            userId = userId,
            currentObject = objectInformation,
            recentObjects = recentObjects,
            progress = progress,
            previousMessages = previousMessages,
            learningLevel = learningLevel,
        )
    }

    private fun String.extractSearchQuery(): String {
        val normalized = lowercase()
            .replace(Regex("[^a-z0-9\\s]"), " ")
            .split(Regex("\\s+"))
            .filterNot { token -> token in stopWords }
        return normalized.firstOrNull().orEmpty()
    }

    private companion object {
        val stopWords = setOf(
            "what",
            "is",
            "this",
            "the",
            "a",
            "an",
            "how",
            "does",
            "it",
            "work",
            "explain",
            "give",
            "examples",
        )
    }
}
