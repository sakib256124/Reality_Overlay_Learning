package com.rola.app.presentation.overlay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.data.ar.ARInformationNode
import com.rola.app.data.translation.TranslationRepository
import com.rola.app.data.wearable.WearableRepository
import com.rola.app.domain.model.ObjectInformation
import com.rola.app.domain.model.toObjectInformation
import com.rola.app.domain.repository.LearningRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OverlayViewModel @Inject constructor(
    private val learningRepository: LearningRepository,
    private val translationRepository: TranslationRepository,
    private val wearableRepository: WearableRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(OverlayUiState())
    val uiState: StateFlow<OverlayUiState> = _uiState.asStateFlow()

    private var activeObjectId: String? = null
    private var loadingJob: Job? = null
    private var translationJob: Job? = null

    init {
        observeLanguages()
        observeSelectedLanguage()
        observeWearableSettings()
    }

    fun onNodeUpdated(node: ARInformationNode) {
        if (activeObjectId != node.objectId) {
            activeObjectId = node.objectId
            loadObjectInformation(node)
        }

        _uiState.update {
            it.copy(
                screenTransform = node.screenTransform,
                errorMessage = null,
            )
        }
    }

    fun onTrackingLost(message: String) {
        loadingJob?.cancel()
        _uiState.update {
            it.copy(
                status = OverlayStatus.TrackingLost,
                screenTransform = null,
                errorMessage = message,
            )
        }
    }

    fun onPlacementError(message: String) {
        _uiState.update {
            it.copy(
                status = OverlayStatus.Error,
                errorMessage = message,
            )
        }
    }

    fun onPanelTapped() {
        _uiState.update { it.copy(isExpanded = !it.isExpanded) }
    }

    fun closeOverlay() {
        loadingJob?.cancel()
        translationJob?.cancel()
        activeObjectId = null
        _uiState.value = OverlayUiState()
    }

    fun prepareForRescan() {
        loadingJob?.cancel()
        translationJob?.cancel()
        activeObjectId = null
        _uiState.value = OverlayUiState(status = OverlayStatus.Hidden)
    }

    fun onLanguageSelected(languageCode: String) {
        translationRepository.setSelectedLanguage(languageCode)
    }

    private fun loadObjectInformation(node: ARInformationNode) {
        loadingJob?.cancel()
        _uiState.update {
            it.copy(
                status = OverlayStatus.Loading,
                originalObjectInformation = null,
                objectInformation = null,
                isTranslationLoading = false,
                translationFromCache = false,
                isExpanded = false,
                errorMessage = null,
            )
        }

        loadingJob = viewModelScope.launch {
            runCatching {
                learningRepository.getObjectById(node.objectId)
                    ?.toObjectInformation()
                    ?: learningRepository.getObjectByName(node.objectName)
                        ?.toObjectInformation()
                    ?: fallbackInformation[node.objectId]
                    ?: fallbackInformation[node.objectName.normalizedObjectId()]
            }.fold(
                onSuccess = { information ->
                    if (information == null) {
                        _uiState.update {
                            it.copy(
                                status = OverlayStatus.Error,
                                errorMessage = "Information unavailable for ${node.objectName}.",
                            )
                        }
                    } else {
                        val shouldShowOriginal = translationRepository.selectedLanguageCode() == DEFAULT_SOURCE_LANGUAGE
                        _uiState.update {
                            it.copy(
                                status = if (shouldShowOriginal) OverlayStatus.Visible else OverlayStatus.Loading,
                                originalObjectInformation = information,
                                objectInformation = if (shouldShowOriginal) information else null,
                                errorMessage = null,
                            )
                        }
                        translateOverlayInformation(information)
                    }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(
                            status = OverlayStatus.Error,
                            errorMessage = throwable.message ?: "Unable to load object information.",
                        )
                    }
                },
            )
        }
    }

    private fun observeLanguages() {
        viewModelScope.launch {
            runCatching { translationRepository.ensureLanguagesSeeded() }
            translationRepository.observeLanguages().collect { languages ->
                _uiState.update { it.copy(languages = languages) }
            }
        }
    }

    private fun observeSelectedLanguage() {
        viewModelScope.launch {
            translationRepository.observeSelectedLanguageCode().collect { languageCode ->
                _uiState.update { it.copy(selectedLanguageCode = languageCode) }
                _uiState.value.originalObjectInformation?.let(::translateOverlayInformation)
            }
        }
    }

    private fun observeWearableSettings() {
        viewModelScope.launch {
            wearableRepository.settings.collect { settings ->
                _uiState.update { it.copy(displayMode = settings.displayMode) }
            }
        }
    }

    private fun translateOverlayInformation(information: ObjectInformation) {
        translationJob?.cancel()
        val targetLanguage = translationRepository.selectedLanguageCode()
        if (targetLanguage == DEFAULT_SOURCE_LANGUAGE) {
            _uiState.update {
                it.copy(
                    status = OverlayStatus.Visible,
                    objectInformation = information,
                    isTranslationLoading = false,
                    translationFromCache = true,
                    errorMessage = null,
                )
            }
            return
        }

        translationJob = viewModelScope.launch {
            _uiState.update { it.copy(isTranslationLoading = true, errorMessage = null) }
            runCatching {
                translationRepository.translateObjectInformation(
                    information = information,
                    targetLanguage = targetLanguage,
                    sourceLanguage = DEFAULT_SOURCE_LANGUAGE,
                )
            }.fold(
                onSuccess = { translated ->
                    _uiState.update {
                        it.copy(
                            status = OverlayStatus.Visible,
                            objectInformation = translated.translated,
                            isTranslationLoading = false,
                            translationFromCache = translated.fromCache,
                            errorMessage = null,
                        )
                    }
                },
                onFailure = { throwable ->
                    _uiState.update {
                        it.copy(
                            status = OverlayStatus.Visible,
                            objectInformation = information,
                            isTranslationLoading = false,
                            errorMessage = throwable.message ?: "Unable to translate overlay content.",
                        )
                    }
                },
            )
        }
    }

    private fun String.normalizedObjectId(): String = trim()
        .lowercase()
        .replace(Regex("[^a-z0-9]+"), "_")
        .trim('_')
        .ifBlank { "unknown" }

    private companion object {
        const val DEFAULT_SOURCE_LANGUAGE = "en"
        val fallbackInformation = listOf(
            ObjectInformation(
                objectId = "bottle",
                name = "Bottle",
                scientificName = "Polyethylene terephthalate container",
                category = "Plastic Object",
                description = "A lightweight container commonly used to store and transport liquids.",
                uses = listOf("Storage", "Transportation", "Measured dispensing"),
                facts = listOf("PET is recyclable.", "Clear bottles are often made from PET polymer."),
                imageUrl = "",
            ),
            ObjectInformation(
                objectId = "book",
                name = "Book",
                scientificName = "Printed cellulose fiber medium",
                category = "Learning Object",
                description = "A bound collection of pages used to preserve and share information.",
                uses = listOf("Reading", "Reference", "Education"),
                facts = listOf("Paper is mostly cellulose fiber.", "Books can last for centuries when stored carefully."),
                imageUrl = "",
            ),
            ObjectInformation(
                objectId = "cup",
                name = "Cup",
                scientificName = "Drinking vessel",
                category = "Household Object",
                description = "A small open container designed for holding drinks.",
                uses = listOf("Drinking", "Measuring small amounts", "Serving"),
                facts = listOf("Cups may be ceramic, glass, plastic, metal, or paper."),
                imageUrl = "",
            ),
        ).associateBy { it.objectId }
    }
}
