package com.rola.app.data.voice

import com.rola.app.domain.model.ObjectInformation
import com.rola.app.domain.model.VoiceContent
import com.rola.app.domain.model.toVoiceContent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class VoiceRepository @Inject constructor(
    private val textToSpeechManager: TextToSpeechManager,
) {
    fun createVoiceContent(objectInformation: ObjectInformation): VoiceContent =
        objectInformation.toVoiceContent()

    suspend fun initialize(language: VoiceLanguage): TextToSpeechResult =
        textToSpeechManager.initialize(language)

    suspend fun speak(text: String): TextToSpeechResult =
        textToSpeechManager.speak(text)

    suspend fun pause(): TextToSpeechResult = textToSpeechManager.pause()

    suspend fun resume(): TextToSpeechResult = textToSpeechManager.resume()

    suspend fun stop(): TextToSpeechResult = textToSpeechManager.stop()

    suspend fun setSpeechRate(rate: Float): TextToSpeechResult =
        textToSpeechManager.setSpeechRate(rate)

    suspend fun setPitch(pitch: Float): TextToSpeechResult =
        textToSpeechManager.setPitch(pitch)

    suspend fun setLanguage(language: VoiceLanguage): TextToSpeechResult =
        textToSpeechManager.setLanguage(language)

    fun setPlaybackListener(listener: VoicePlaybackListener?) {
        textToSpeechManager.setPlaybackListener(listener)
    }

    fun shutdown() {
        textToSpeechManager.shutdown()
    }
}
