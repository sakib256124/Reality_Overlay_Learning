package com.rola.app.presentation.wearable

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.data.translation.TranslationRepository
import com.rola.app.data.wearable.WearableConnectionResult
import com.rola.app.data.wearable.WearableRepository
import com.rola.app.domain.model.WearableAudioOutput
import com.rola.app.domain.model.WearableCommand
import com.rola.app.domain.model.WearableDevice
import com.rola.app.domain.model.WearableDisplayMode
import com.rola.app.domain.model.WearableInteractionMode
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class WearableViewModel @Inject constructor(
    private val wearableRepository: WearableRepository,
    private val translationRepository: TranslationRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(WearableUiState())
    val uiState: StateFlow<WearableUiState> = _uiState.asStateFlow()

    init {
        observeDevices()
        observeSettings()
        discoverDevices()
    }

    fun discoverDevices() {
        viewModelScope.launch {
            _uiState.update { it.copy(status = WearableStatus.Discovering, errorMessage = null) }
            runCatching { wearableRepository.discoverDevices() }
                .onSuccess { devices ->
                    _uiState.update {
                        it.copy(
                            status = if (it.isConnected) WearableStatus.Connected else WearableStatus.Idle,
                            discoveredDevices = devices,
                            errorMessage = null,
                        )
                    }
                }
                .onFailure { throwable -> showError(throwable.message ?: "Unable to discover wearable devices.") }
        }
    }

    fun connect(device: WearableDevice) {
        viewModelScope.launch {
            _uiState.update { it.copy(status = WearableStatus.Connecting, errorMessage = null) }
            handleConnectionResult(
                result = wearableRepository.connect(device),
                successStatus = WearableStatus.Connected,
                commandLabel = "Secure pairing complete",
            )
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            handleConnectionResult(
                result = wearableRepository.disconnect(),
                successStatus = WearableStatus.Idle,
                commandLabel = "Disconnected",
            )
        }
    }

    fun sendVoiceCommand(commandPhrase: String) {
        val command = commandForPhrase(commandPhrase)
        sendCommand(command, commandPhrase)
    }

    fun sendCommand(command: WearableCommand, label: String = command.name) {
        viewModelScope.launch {
            _uiState.update { it.copy(status = WearableStatus.Sending, errorMessage = null) }
            handleConnectionResult(
                result = wearableRepository.sendCommand(command),
                successStatus = WearableStatus.Connected,
                commandLabel = label,
            )
        }
    }

    fun sendSampleLearningFrame() {
        viewModelScope.launch {
            _uiState.update { it.copy(status = WearableStatus.Sending, errorMessage = null) }
            val result = wearableRepository.sendLearningFrame(
                objectName = "Apple",
                summary = "A fruit containing vitamins, fiber, and natural sugars. Useful for learning nutrition and plant biology.",
                languageCode = translationRepository.selectedLanguageCode(),
            )
            handleConnectionResult(
                result = result,
                successStatus = WearableStatus.Connected,
                commandLabel = "Learning frame sent",
                transferSummary = "Apple overlay prepared for ${_uiState.value.displayMode.name}",
            )
        }
    }

    fun updateDisplayMode(displayMode: WearableDisplayMode) {
        wearableRepository.updateDisplayMode(displayMode)
    }

    fun updateAudioOutput(audioOutput: WearableAudioOutput) {
        wearableRepository.updateAudioOutput(audioOutput)
    }

    fun updateVoiceSensitivity(value: Float) {
        wearableRepository.updateVoiceSensitivity(value)
    }

    fun updateInteractionMode(interactionMode: WearableInteractionMode) {
        wearableRepository.updateInteractionMode(interactionMode)
    }

    fun updateLowPowerMode(enabled: Boolean) {
        wearableRepository.updateLowPowerMode(enabled)
    }

    private fun observeDevices() {
        viewModelScope.launch {
            wearableRepository.discoveredDevices.collect { devices ->
                _uiState.update { it.copy(discoveredDevices = devices) }
            }
        }
        viewModelScope.launch {
            wearableRepository.connectedDevice.collect { device ->
                _uiState.update {
                    it.copy(
                        connectedDevice = device,
                        status = if (device == null) WearableStatus.Idle else WearableStatus.Connected,
                    )
                }
            }
        }
        viewModelScope.launch {
            wearableRepository.connectionState.collect { state ->
                _uiState.update { it.copy(connectionState = state) }
            }
        }
    }

    private fun observeSettings() {
        viewModelScope.launch {
            wearableRepository.settings.collect { settings ->
                _uiState.update {
                    it.copy(
                        displayMode = settings.displayMode,
                        audioOutput = settings.audioOutput,
                        voiceSensitivity = settings.voiceSensitivity,
                        interactionMode = settings.interactionMode,
                        lowPowerMode = settings.lowPowerMode,
                        adaptiveFrameIntervalMillis = wearableRepository.adaptiveFrameIntervalMillis(),
                        usesCompactOverlay = wearableRepository.shouldUseCompactOverlay(),
                    )
                }
            }
        }
    }

    private fun handleConnectionResult(
        result: WearableConnectionResult,
        successStatus: WearableStatus,
        commandLabel: String,
        transferSummary: String? = null,
    ) {
        when (result) {
            WearableConnectionResult.Success -> _uiState.update {
                it.copy(
                    status = successStatus,
                    lastCommand = commandLabel,
                    lastTransferSummary = transferSummary ?: it.lastTransferSummary,
                    errorMessage = null,
                )
            }
            is WearableConnectionResult.Error -> showError(result.message)
        }
    }

    private fun commandForPhrase(commandPhrase: String): WearableCommand =
        when (commandPhrase.trim().lowercase()) {
            "scan object" -> WearableCommand.ScanObject
            "explain this" -> WearableCommand.ExplainThis
            "start quiz" -> WearableCommand.StartQuiz
            "translate" -> WearableCommand.Translate
            else -> WearableCommand.ExplainThis
        }

    private fun showError(message: String) {
        _uiState.update {
            it.copy(
                status = WearableStatus.Error,
                errorMessage = message,
            )
        }
    }
}
