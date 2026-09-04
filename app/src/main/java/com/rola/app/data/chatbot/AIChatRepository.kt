package com.rola.app.data.chatbot

import com.rola.app.data.database.ChatMessageDao
import com.rola.app.data.database.entities.toEntity
import com.rola.app.data.adaptive.UserProfileRepository
import com.rola.app.data.translation.TranslationRepository
import com.rola.app.data.translation.TranslationService
import com.rola.app.domain.model.ChatMessage
import com.rola.app.domain.model.ChatRole
import com.rola.app.domain.model.TutorResponse
import com.rola.app.domain.repository.LearningHistoryRepository
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@Singleton
class AIChatRepository @Inject constructor(
    private val chatMessageDao: ChatMessageDao,
    private val knowledgeRetriever: KnowledgeRetriever,
    private val llmService: LLMService,
    private val learningHistoryRepository: LearningHistoryRepository,
    private val translationRepository: TranslationRepository,
    private val userProfileRepository: UserProfileRepository,
) {
    val currentUserId: String
        get() = learningHistoryRepository.currentUserId

    fun observeMessages(objectId: String?): Flow<List<ChatMessage>> =
        chatMessageDao.observeMessages(currentUserId, objectId)
            .map { messages -> messages.map { it.toDomain() } }

    suspend fun askTutor(
        question: String,
        objectId: String?,
    ): TutorResponse {
        require(question.isNotBlank()) { "Ask a question before sending." }

        val userMessage = ChatMessage(
            messageId = UUID.randomUUID().toString(),
            userId = currentUserId,
            objectId = objectId,
            role = ChatRole.User,
            content = question.trim(),
            timestamp = System.currentTimeMillis(),
        )
        chatMessageDao.insertMessage(userMessage.toEntity())

        val recentMessages = chatMessageDao.getRecentMessages(
            userId = currentUserId,
            objectId = objectId,
            limit = CONTEXT_MESSAGE_LIMIT,
        ).map { it.toDomain() }.reversed()

        val objectInformation = knowledgeRetriever.retrieveObjectInformation(objectId, question)
        val context = knowledgeRetriever.buildLearningContext(
            userId = currentUserId,
            objectInformation = objectInformation,
            previousMessages = recentMessages,
        )
        val questionLanguage = translationRepository.detectLanguage(question)
        val targetLanguage = questionLanguage
            .takeUnless { it == TranslationService.UNDETERMINED_LANGUAGE || it == TranslationService.DEFAULT_SOURCE_LANGUAGE }
            ?: translationRepository.selectedLanguageCode()
        val personalizationPrefix = userProfileRepository.personalizedTutorPrefix()
        val personalizedQuestion = listOf(personalizationPrefix, question)
            .filter { it.isNotBlank() }
            .joinToString(separator = " ")
        val answer = llmService.generateResponse(personalizedQuestion, context)
        val learnerAnswer = translationRepository.translateTutorResponse(
            responseText = answer,
            targetLanguage = targetLanguage,
            sourceLanguage = TranslationService.DEFAULT_SOURCE_LANGUAGE,
        ).translatedText
        val assistantMessage = ChatMessage(
            messageId = UUID.randomUUID().toString(),
            userId = currentUserId,
            objectId = objectId,
            role = ChatRole.Assistant,
            content = learnerAnswer,
            timestamp = System.currentTimeMillis(),
            isGrounded = objectInformation != null,
        )
        chatMessageDao.insertMessage(assistantMessage.toEntity())

        return TutorResponse(
            message = assistantMessage,
            sources = objectInformation?.let { listOf(it.name, it.category) }.orEmpty(),
            confidence = if (objectInformation == null) 0.45f else 0.86f,
            suggestedQuestions = suggestedQuestions(objectInformation?.name).map { suggestion ->
                translationRepository.translateTutorResponse(
                    responseText = suggestion,
                    targetLanguage = targetLanguage,
                    sourceLanguage = TranslationService.DEFAULT_SOURCE_LANGUAGE,
                ).translatedText
            },
        )
    }

    suspend fun clearChat(objectId: String?) {
        chatMessageDao.clearMessages(currentUserId, objectId)
    }

    private fun suggestedQuestions(objectName: String?): List<String> =
        if (objectName == null) {
            defaultSuggestions
        } else {
            listOf(
                "What is $objectName made of?",
                "Explain $objectName like I am a beginner.",
                "How is $objectName used in real life?",
                "Help me prepare for a quiz.",
            )
        }

    companion object {
        val defaultSuggestions = listOf(
            "What is this?",
            "How does it work?",
            "Give examples",
            "Explain scientifically",
        )
        private const val CONTEXT_MESSAGE_LIMIT = 8
    }
}
