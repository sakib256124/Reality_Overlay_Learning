package com.rola.app.presentation.voice

import com.rola.app.data.voice.VoiceLanguage
import com.rola.app.domain.model.VoiceContent

enum class VoiceStatus {
    Idle,
    Initializing,
    Ready,
    Speaking,
    Paused,
    Stopped,
    Error,
}

data class VoiceUiState(
    val status: VoiceStatus = VoiceStatus.Idle,
    val content: VoiceContent? = null,
    val speechRate: Float = 0.95f,
    val pitch: Float = 1f,
    val selectedLanguage: VoiceLanguage = VoiceLanguage.English,
    val supportedLanguages: List<VoiceLanguage> = VoiceLanguage.entries,
    val errorMessage: String? = null,
) {
    val isSpeaking: Boolean
        get() = status == VoiceStatus.Speaking

    val isPaused: Boolean
        get() = status == VoiceStatus.Paused

    val isVolumeActive: Boolean
        get() = status == VoiceStatus.Speaking || status == VoiceStatus.Paused
}
