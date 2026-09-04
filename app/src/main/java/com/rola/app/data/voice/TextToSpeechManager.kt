package com.rola.app.data.voice

import android.content.Context
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

enum class VoiceLanguage(
    val displayName: String,
    val locale: Locale,
) {
    English("English", Locale.US),
    Bengali("Bengali", Locale("bn", "BD")),
    Spanish("Spanish", Locale("es", "ES")),
    French("French", Locale.FRANCE),
    German("German", Locale.GERMANY),
    Chinese("Chinese", Locale.SIMPLIFIED_CHINESE),
    Arabic("Arabic", Locale("ar")),
}

sealed class TextToSpeechResult {
    data object Success : TextToSpeechResult()
    data class Error(val message: String) : TextToSpeechResult()
}

interface VoicePlaybackListener {
    fun onSpeechStarted()
    fun onSpeechCompleted()
    fun onSpeechError(message: String)
}

@Singleton
class TextToSpeechManager @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    private var textToSpeech: TextToSpeech? = null
    private var initializeDeferred: CompletableDeferred<TextToSpeechResult>? = null
    private var isInitialized: Boolean = false
    private var playbackListener: VoicePlaybackListener? = null
    private var currentText: String = ""
    private var pausedCharacterOffset: Int = 0
    private var speechRate: Float = DEFAULT_SPEECH_RATE
    private var pitch: Float = DEFAULT_PITCH
    private var language: VoiceLanguage = VoiceLanguage.English

    suspend fun initialize(selectedLanguage: VoiceLanguage = language): TextToSpeechResult =
        withContext(Dispatchers.Main) {
            language = selectedLanguage
            initializeDeferred?.takeUnless { it.isCompleted }?.let { deferred ->
                return@withContext when (val result = deferred.await()) {
                    TextToSpeechResult.Success -> textToSpeech
                        ?.let { configureEngine(it, selectedLanguage) }
                        ?: TextToSpeechResult.Error("Text-to-Speech engine is unavailable.")
                    is TextToSpeechResult.Error -> result
                }
            }

            textToSpeech?.let { engine ->
                return@withContext if (isInitialized) {
                    configureEngine(engine, selectedLanguage)
                } else {
                    TextToSpeechResult.Error("Text-to-Speech engine is still initializing.")
                }
            }

            val deferred = CompletableDeferred<TextToSpeechResult>()
            initializeDeferred = deferred

            textToSpeech = TextToSpeech(context) { status ->
                val engine = textToSpeech
                val result = if (status == TextToSpeech.SUCCESS && engine != null) {
                    isInitialized = true
                    configureEngine(engine, selectedLanguage)
                } else {
                    isInitialized = false
                    TextToSpeechResult.Error("Text-to-Speech engine is unavailable.")
                }
                initializeDeferred?.complete(result)
            }

            deferred.await()
        }

    suspend fun speak(text: String): TextToSpeechResult = withContext(Dispatchers.Main) {
        if (text.isBlank()) {
            return@withContext TextToSpeechResult.Error("Explanation text is empty.")
        }

        val initResult = initialize(language)
        if (initResult is TextToSpeechResult.Error) return@withContext initResult

        currentText = text
        pausedCharacterOffset = 0
        speakFromOffset(0)
    }

    suspend fun pause(): TextToSpeechResult = withContext(Dispatchers.Main) {
        val engine = textToSpeech ?: return@withContext TextToSpeechResult.Error("Text-to-Speech is not initialized.")
        engine.stop()
        TextToSpeechResult.Success
    }

    suspend fun resume(): TextToSpeechResult = withContext(Dispatchers.Main) {
        if (currentText.isBlank()) {
            return@withContext TextToSpeechResult.Error("No explanation is ready to resume.")
        }
        val initResult = initialize(language)
        if (initResult is TextToSpeechResult.Error) return@withContext initResult
        speakFromOffset(pausedCharacterOffset.coerceIn(0, currentText.lastIndex.coerceAtLeast(0)))
    }

    suspend fun stop(): TextToSpeechResult = withContext(Dispatchers.Main) {
        textToSpeech?.stop()
        pausedCharacterOffset = 0
        TextToSpeechResult.Success
    }

    suspend fun setSpeechRate(rate: Float): TextToSpeechResult = withContext(Dispatchers.Main) {
        speechRate = rate.coerceIn(MIN_SPEECH_RATE, MAX_SPEECH_RATE)
        textToSpeech?.setSpeechRate(speechRate)
        TextToSpeechResult.Success
    }

    suspend fun setPitch(value: Float): TextToSpeechResult = withContext(Dispatchers.Main) {
        pitch = value.coerceIn(MIN_PITCH, MAX_PITCH)
        textToSpeech?.setPitch(pitch)
        TextToSpeechResult.Success
    }

    suspend fun setLanguage(selectedLanguage: VoiceLanguage): TextToSpeechResult = withContext(Dispatchers.Main) {
        language = selectedLanguage
        val initResult = initialize(selectedLanguage)
        if (initResult is TextToSpeechResult.Error) return@withContext initResult
        TextToSpeechResult.Success
    }

    fun setPlaybackListener(listener: VoicePlaybackListener?) {
        playbackListener = listener
    }

    fun shutdown() {
        playbackListener = null
        textToSpeech?.stop()
        textToSpeech?.shutdown()
        textToSpeech = null
        initializeDeferred = null
        isInitialized = false
        currentText = ""
        pausedCharacterOffset = 0
    }

    private fun configureEngine(
        engine: TextToSpeech,
        selectedLanguage: VoiceLanguage,
    ): TextToSpeechResult {
        val languageResult = engine.setLanguage(selectedLanguage.locale)
        if (
            languageResult == TextToSpeech.LANG_MISSING_DATA ||
            languageResult == TextToSpeech.LANG_NOT_SUPPORTED
        ) {
            val fallbackResult = engine.setLanguage(VoiceLanguage.English.locale)
            if (
                fallbackResult == TextToSpeech.LANG_MISSING_DATA ||
                fallbackResult == TextToSpeech.LANG_NOT_SUPPORTED
            ) {
                return TextToSpeechResult.Error("${selectedLanguage.displayName} voice is not supported on this device.")
            }
            language = VoiceLanguage.English
        }

        engine.setSpeechRate(speechRate)
        engine.setPitch(pitch)
        engine.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
            override fun onStart(utteranceId: String?) {
                playbackListener?.onSpeechStarted()
            }

            override fun onDone(utteranceId: String?) {
                pausedCharacterOffset = 0
                playbackListener?.onSpeechCompleted()
            }

            @Deprecated("Deprecated by Android framework; required for compatibility.")
            override fun onError(utteranceId: String?) {
                playbackListener?.onSpeechError("Text-to-Speech playback failed.")
            }

            override fun onError(utteranceId: String?, errorCode: Int) {
                playbackListener?.onSpeechError("Text-to-Speech playback failed with code $errorCode.")
            }

            override fun onRangeStart(
                utteranceId: String?,
                start: Int,
                end: Int,
                frame: Int,
            ) {
                pausedCharacterOffset = start
            }
        })
        return TextToSpeechResult.Success
    }

    private fun speakFromOffset(characterOffset: Int): TextToSpeechResult {
        val engine = textToSpeech ?: return TextToSpeechResult.Error("Text-to-Speech is not initialized.")
        val text = currentText.drop(characterOffset).trim()
        if (text.isBlank()) return TextToSpeechResult.Error("Explanation text is empty.")

        val params = Bundle().apply {
            putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, UUID.randomUUID().toString())
        }
        val result = engine.speak(text, TextToSpeech.QUEUE_FLUSH, params, UUID.randomUUID().toString())
        return if (result == TextToSpeech.SUCCESS) {
            TextToSpeechResult.Success
        } else {
            TextToSpeechResult.Error("Unable to start Text-to-Speech playback.")
        }
    }

    private companion object {
        const val DEFAULT_SPEECH_RATE = 0.95f
        const val DEFAULT_PITCH = 1f
        const val MIN_SPEECH_RATE = 0.5f
        const val MAX_SPEECH_RATE = 1.5f
        const val MIN_PITCH = 0.7f
        const val MAX_PITCH = 1.4f
    }
}
