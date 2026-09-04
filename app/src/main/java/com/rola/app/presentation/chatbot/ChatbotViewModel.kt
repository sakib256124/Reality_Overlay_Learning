package com.rola.app.presentation.chatbot

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.data.chatbot.AIChatRepository
import com.rola.app.data.translation.TranslationRepository
import com.rola.app.data.voice.TextToSpeechResult
import com.rola.app.data.voice.VoicePlaybackListener
import com.rola.app.data.voice.VoiceRepository
import com.rola.app.domain.model.ChatRole
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class ChatbotViewModel @Inject constructor(
    private val aiChatRepository: AIChatRepository,
    private val translationRepository: TranslationRepository,
    private val voiceRepository: VoiceRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), VoicePlaybackListener {
    private val objectId: String? = savedStateHandle["objectId"]
        ?.takeUnless { it.isBlank() || it == NO_OBJECT_ID }

    private val _uiState = MutableStateFlow(ChatbotUiState(objectId = objectId))
    val uiState: StateFlow<ChatbotUiState> = _uiState.asStateFlow()
    private var sendJob: Job? = null
    private var speechJob: Job? = null

    init {
        voiceRepository.setPlaybackListener(this)
        observeLanguages()
        observeSelectedLanguage()
        observeMessages()
    }

    fun onInputChanged(value: String) {
        _uiState.update { it.copy(inputText = value, errorMessage = null) }
    }

    fun sendMessage() {
        sendQuestion(_uiState.value.inputText)
    }

    fun sendSuggestedQuestion(question: String) {
        sendQuestion(question)
    }

    fun replayLastAnswer() {
        val answer = _uiState.value.messages.lastOrNull {
            it.role == ChatRole.Assistant
        }?.content ?: return
        speakAnswer(answer)
    }

    fun stopSpeaking() {
        speechJob?.cancel()
        speechJob = viewModelScope.launch {
            voiceRepository.stop()
            _uiState.update { it.copy(status = ChatbotStatus.Ready) }
        }
    }

    fun clearChat() {
        viewModelScope.launch {
            runCatching { aiChatRepository.clearChat(objectId) }
                .onFailure { throwable -> showError(throwable.message ?: "Unable to clear chat.") }
        }
    }

    fun onLanguageSelected(languageCode: String) {
        translationRepository.setSelectedLanguage(languageCode)
    }

    override fun onCleared() {
        sendJob?.cancel()
        speechJob?.cancel()
        voiceRepository.shutdown()
        super.onCleared()
    }

    override fun onSpeechStarted() {
        _uiState.update { it.copy(status = ChatbotStatus.Speaking, errorMessage = null) }
    }

    override fun onSpeechCompleted() {
        _uiState.update { it.copy(status = ChatbotStatus.Ready, errorMessage = null) }
    }

    override fun onSpeechError(message: String) {
        showError(message)
    }

    private fun observeMessages() {
        viewModelScope.launch {
            aiChatRepository.observeMessages(objectId)
                .catch { throwable -> showError(throwable.message ?: "Unable to load chat history.") }
                .collect { messages ->
                    _uiState.update { it.copy(messages = messages) }
                }
        }
    }

    private fun observeLanguages() {
        viewModelScope.launch {
            runCatching { translationRepository.ensureLanguagesSeeded() }
            translationRepository.observeLanguages()
                .catch { throwable -> showError(throwable.message ?: "Unable to load languages.") }
                .collect { languages ->
                    _uiState.update { it.copy(languages = languages) }
                }
        }
    }

    private fun observeSelectedLanguage() {
        viewModelScope.launch {
            translationRepository.observeSelectedLanguageCode().collect { languageCode ->
                _uiState.update { it.copy(selectedLanguageCode = languageCode) }
                val voiceLanguage = translationRepository.voiceLanguageFor(languageCode)
                when (val result = voiceRepository.setLanguage(voiceLanguage)) {
                    TextToSpeechResult.Success -> Unit
                    is TextToSpeechResult.Error -> showError(result.message)
                }
            }
        }
    }

    private fun sendQuestion(question: String) {
        if (question.isBlank()) return
        sendJob?.cancel()
        sendJob = viewModelScope.launch {
            _uiState.update {
                it.copy(
                    inputText = "",
                    status = ChatbotStatus.Thinking,
                    errorMessage = null,
                )
            }
            runCatching { aiChatRepository.askTutor(question, objectId) }
                .onSuccess { response ->
                    _uiState.update {
                        it.copy(
                            status = ChatbotStatus.Ready,
                            suggestedQuestions = response.suggestedQuestions,
                            errorMessage = null,
                        )
                    }
                    speakAnswer(response.message.content)
                }
                .onFailure { throwable ->
                    showError(throwable.message ?: "Tutor response failed.")
                }
        }
    }

    private fun speakAnswer(text: String) {
        speechJob?.cancel()
        speechJob = viewModelScope.launch {
            _uiState.update { it.copy(status = ChatbotStatus.Speaking, errorMessage = null) }
            when (val result = voiceRepository.speak(text)) {
                TextToSpeechResult.Success -> Unit
                is TextToSpeechResult.Error -> showError(result.message)
            }
        }
    }

    private fun showError(message: String) {
        _uiState.update {
            it.copy(
                status = ChatbotStatus.Error,
                errorMessage = message,
            )
        }
    }

    companion object {
        const val NO_OBJECT_ID = "none"
    }
}
