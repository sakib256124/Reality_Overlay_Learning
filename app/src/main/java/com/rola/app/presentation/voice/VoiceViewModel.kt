package com.rola.app.presentation.voice

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.data.translation.TranslationRepository
import com.rola.app.data.voice.TextToSpeechResult
import com.rola.app.data.voice.VoiceLanguage
import com.rola.app.data.voice.VoicePlaybackListener
import com.rola.app.data.voice.VoiceRepository
import com.rola.app.domain.model.ObjectInformation
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class VoiceViewModel @Inject constructor(
    private val voiceRepository: VoiceRepository,
    private val translationRepository: TranslationRepository,
) : ViewModel(), VoicePlaybackListener {
    private val _uiState = MutableStateFlow(VoiceUiState())
    val uiState: StateFlow<VoiceUiState> = _uiState.asStateFlow()

    private var activeObjectId: String? = null
    private var activeExplanationText: String? = null
    private var speechJob: Job? = null

    init {
        voiceRepository.setPlaybackListener(this)
        initialize()
        observeLearningLanguage()
    }

    fun onObjectInformationChanged(objectInformation: ObjectInformation?) {
        if (objectInformation == null) {
            stop()
            activeObjectId = null
            activeExplanationText = null
            _uiState.update { it.copy(content = null, status = VoiceStatus.Idle, errorMessage = null) }
            return
        }

        val content = voiceRepository.createVoiceContent(objectInformation)
        if (
            activeObjectId == objectInformation.objectId &&
            activeExplanationText == content.explanationText
        ) {
            return
        }
        activeObjectId = objectInformation.objectId
        activeExplanationText = content.explanationText

        _uiState.update {
            it.copy(
                content = content,
                status = VoiceStatus.Ready,
                errorMessage = null,
            )
        }
        play()
    }

    fun play() {
        val content = _uiState.value.content ?: return showError("No object explanation is ready.")
        speechJob?.cancel()
        speechJob = viewModelScope.launch {
            _uiState.update { it.copy(status = VoiceStatus.Initializing, errorMessage = null) }
            handleResult(voiceRepository.speak(content.explanationText))
        }
    }

    fun pause() {
        speechJob?.cancel()
        speechJob = viewModelScope.launch {
            handleResult(voiceRepository.pause()) {
                _uiState.update { it.copy(status = VoiceStatus.Paused, errorMessage = null) }
            }
        }
    }

    fun resume() {
        speechJob?.cancel()
        speechJob = viewModelScope.launch {
            _uiState.update { it.copy(status = VoiceStatus.Initializing, errorMessage = null) }
            handleResult(voiceRepository.resume())
        }
    }

    fun stop() {
        speechJob?.cancel()
        speechJob = viewModelScope.launch {
            handleResult(voiceRepository.stop()) {
                _uiState.update { it.copy(status = VoiceStatus.Stopped, errorMessage = null) }
            }
        }
    }

    fun onSpeechRateChanged(rate: Float) {
        _uiState.update { it.copy(speechRate = rate.coerceIn(MIN_SPEECH_RATE, MAX_SPEECH_RATE)) }
        speechJob?.cancel()
        speechJob = viewModelScope.launch {
            handleResult(voiceRepository.setSpeechRate(_uiState.value.speechRate))
        }
    }

    fun onPitchChanged(pitch: Float) {
        _uiState.update { it.copy(pitch = pitch.coerceIn(MIN_PITCH, MAX_PITCH)) }
        speechJob?.cancel()
        speechJob = viewModelScope.launch {
            handleResult(voiceRepository.setPitch(_uiState.value.pitch))
        }
    }

    fun onLanguageSelected(language: VoiceLanguage) {
        _uiState.update { it.copy(selectedLanguage = language, status = VoiceStatus.Initializing, errorMessage = null) }
        speechJob?.cancel()
        speechJob = viewModelScope.launch {
            handleResult(voiceRepository.setLanguage(language)) {
                _uiState.update { it.copy(status = VoiceStatus.Ready, errorMessage = null) }
            }
        }
    }

    private fun observeLearningLanguage() {
        viewModelScope.launch {
            translationRepository.observeSelectedLanguageCode().collect { languageCode ->
                val voiceLanguage = translationRepository.voiceLanguageFor(languageCode)
                if (_uiState.value.selectedLanguage != voiceLanguage) {
                    onLanguageSelected(voiceLanguage)
                }
            }
        }
    }

    override fun onSpeechStarted() {
        _uiState.update { it.copy(status = VoiceStatus.Speaking, errorMessage = null) }
    }

    override fun onSpeechCompleted() {
        _uiState.update { it.copy(status = VoiceStatus.Stopped, errorMessage = null) }
    }

    override fun onSpeechError(message: String) {
        showError(message)
    }

    override fun onCleared() {
        speechJob?.cancel()
        voiceRepository.setPlaybackListener(null)
        voiceRepository.shutdown()
        super.onCleared()
    }

    private fun initialize() {
        speechJob = viewModelScope.launch {
            _uiState.update { it.copy(status = VoiceStatus.Initializing, errorMessage = null) }
            handleResult(voiceRepository.initialize(_uiState.value.selectedLanguage)) {
                _uiState.update { it.copy(status = VoiceStatus.Ready, errorMessage = null) }
            }
        }
    }

    private fun handleResult(
        result: TextToSpeechResult,
        onSuccess: (() -> Unit)? = null,
    ) {
        when (result) {
            TextToSpeechResult.Success -> onSuccess?.invoke()
            is TextToSpeechResult.Error -> showError(result.message)
        }
    }

    private fun showError(message: String) {
        _uiState.update {
            it.copy(
                status = VoiceStatus.Error,
                errorMessage = message,
            )
        }
    }

    private companion object {
        const val MIN_SPEECH_RATE = 0.5f
        const val MAX_SPEECH_RATE = 1.5f
        const val MIN_PITCH = 0.7f
        const val MAX_PITCH = 1.4f
    }
}
