package com.rola.app.presentation.chatbot

import com.rola.app.data.chatbot.AIChatRepository
import com.rola.app.domain.model.ChatMessage
import com.rola.app.domain.model.Language

enum class ChatbotStatus {
    Ready,
    Thinking,
    Speaking,
    Error,
}

data class ChatbotUiState(
    val objectId: String? = null,
    val messages: List<ChatMessage> = emptyList(),
    val inputText: String = "",
    val languages: List<Language> = emptyList(),
    val selectedLanguageCode: String = "en",
    val status: ChatbotStatus = ChatbotStatus.Ready,
    val suggestedQuestions: List<String> = AIChatRepository.defaultSuggestions,
    val errorMessage: String? = null,
) {
    val canSend: Boolean
        get() = inputText.isNotBlank() && status != ChatbotStatus.Thinking
}
