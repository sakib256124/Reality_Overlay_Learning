package com.rola.app.presentation.translation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.data.database.ObjectDao
import com.rola.app.data.translation.TranslationRepository
import com.rola.app.data.voice.TextToSpeechResult
import com.rola.app.data.voice.VoicePlaybackListener
import com.rola.app.data.voice.VoiceRepository
import com.rola.app.domain.model.ObjectInformation
import com.rola.app.domain.model.toObjectInformation
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
class TranslationViewModel @Inject constructor(
    private val translationRepository: TranslationRepository,
    private val objectDao: ObjectDao,
    private val voiceRepository: VoiceRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel(), VoicePlaybackListener {
    private val objectId: String? = savedStateHandle["objectId"]
        ?.takeUnless { it.isBlank() || it == NO_OBJECT_ID }

    private val _uiState = MutableStateFlow(TranslationUiState())
    val uiState: StateFlow<TranslationUiState> = _uiState.asStateFlow()
    private var translationJob: Job? = null
    private var speechJob: Job? = null

    init {
        voiceRepository.setPlaybackListener(this)
        observeLanguages()
        observeSelectedLanguage()
        loadObject()
    }

    fun onLanguageSelected(languageCode: String) {
        val selectedLanguage = translationRepository.setSelectedLanguage(languageCode)
        _uiState.update {
            it.copy(
                selectedLanguageCode = selectedLanguage.languageCode,
                translatedInformation = null,
                translatedText = "",
                errorMessage = null,
            )
        }
        translate()
    }

    fun onSourceTextChanged(value: String) {
        _uiState.update {
            it.copy(
                sourceText = value,
                translatedText = "",
                errorMessage = null,
            )
        }
    }

    fun translate() {
        val state = _uiState.value
        if (!state.canTranslate) {
            showError("Add text or open an object before translating.")
            return
        }

        translationJob?.cancel()
        translationJob = viewModelScope.launch {
            _uiState.update { it.copy(status = TranslationStatus.Loading, errorMessage = null) }
            runCatching {
                state.objectInformation?.let { objectInformation ->
                    translationRepository.translateObjectInformation(
                        information = objectInformation,
                        targetLanguage = _uiState.value.selectedLanguageCode,
                    )
                }
            }.onSuccess { translatedObject ->
                if (translatedObject != null) {
                    _uiState.update {
                        it.copy(
                            status = TranslationStatus.Ready,
                            translatedInformation = translatedObject.translated,
                            detectedLanguage = translatedObject.sourceLanguage,
                            fromCache = translatedObject.fromCache,
                            errorMessage = null,
                        )
                    }
                    return@launch
                }

                runCatching {
                    translationRepository.translateText(
                        text = _uiState.value.sourceText,
                        targetLanguage = _uiState.value.selectedLanguageCode,
                    )
                }.onSuccess { result ->
                    _uiState.update {
                        it.copy(
                            status = TranslationStatus.Ready,
                            translatedText = result.translatedText,
                            detectedLanguage = result.sourceLanguage,
                            fromCache = result.fromCache,
                            errorMessage = null,
                        )
                    }
                }.onFailure { throwable ->
                    showError(throwable.message ?: "Translation failed.")
                }
            }.onFailure { throwable ->
                showError(throwable.message ?: "Object translation failed.")
            }
        }
    }

    fun speakTranslatedContent() {
        val text = _uiState.value.translatedInformation?.toSpeechText()
            ?: _uiState.value.translatedText
        if (text.isBlank()) {
            showError("No translated content is ready for speech.")
            return
        }

        speechJob?.cancel()
        speechJob = viewModelScope.launch {
            val language = translationRepository.voiceLanguageFor(_uiState.value.selectedLanguageCode)
            when (val languageResult = voiceRepository.setLanguage(language)) {
                TextToSpeechResult.Success -> Unit
                is TextToSpeechResult.Error -> {
                    showError(languageResult.message)
                    return@launch
                }
            }
            when (val speechResult = voiceRepository.speak(text)) {
                TextToSpeechResult.Success -> Unit
                is TextToSpeechResult.Error -> showError(speechResult.message)
            }
        }
    }

    fun stopSpeech() {
        speechJob?.cancel()
        speechJob = viewModelScope.launch {
            voiceRepository.stop()
            _uiState.update { it.copy(status = TranslationStatus.Ready) }
        }
    }

    override fun onSpeechStarted() {
        _uiState.update { it.copy(status = TranslationStatus.Speaking, errorMessage = null) }
    }

    override fun onSpeechCompleted() {
        _uiState.update { it.copy(status = TranslationStatus.Ready, errorMessage = null) }
    }

    override fun onSpeechError(message: String) {
        showError(message)
    }

    override fun onCleared() {
        translationJob?.cancel()
        speechJob?.cancel()
        voiceRepository.setPlaybackListener(null)
        voiceRepository.shutdown()
        super.onCleared()
    }

    private fun observeLanguages() {
        viewModelScope.launch {
            runCatching { translationRepository.ensureLanguagesSeeded() }
                .onFailure { throwable -> showError(throwable.message ?: "Unable to seed languages.") }
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
            }
        }
    }

    private fun loadObject() {
        val id = objectId ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(status = TranslationStatus.Loading, errorMessage = null) }
            runCatching { objectDao.getObjectById(id)?.toObjectModel()?.toObjectInformation() }
                .onSuccess { information ->
                    _uiState.update {
                        it.copy(
                            status = TranslationStatus.Idle,
                            objectInformation = information,
                            errorMessage = if (information == null) "Object information is not available." else null,
                        )
                    }
                    if (information != null) translate()
                }
                .onFailure { throwable ->
                    showError(throwable.message ?: "Unable to load object information.")
                }
        }
    }

    private fun ObjectInformation.toSpeechText(): String = buildString {
        append(name)
        append(". ")
        append(description)
        if (uses.isNotEmpty()) append(" Uses: ${uses.joinToString()}.")
        if (facts.isNotEmpty()) append(" Facts: ${facts.joinToString()}.")
    }

    private fun showError(message: String) {
        _uiState.update {
            it.copy(
                status = TranslationStatus.Error,
                errorMessage = message,
            )
        }
    }

    companion object {
        const val NO_OBJECT_ID = "none"
    }
}
