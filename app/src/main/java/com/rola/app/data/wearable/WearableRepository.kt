package com.rola.app.data.wearable

import android.content.Context
import com.rola.app.domain.model.DeviceConnectionType
import com.rola.app.domain.model.WearableAudioOutput
import com.rola.app.domain.model.WearableCommand
import com.rola.app.domain.model.WearableDevice
import com.rola.app.domain.model.WearableDisplayMode
import com.rola.app.domain.model.WearableInteractionMode
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

@Singleton
class WearableRepository @Inject constructor(
    @ApplicationContext context: Context,
    private val connector: WearableManager,
) {
    private val preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE)

    val discoveredDevices = connector.discoveredDevices
    val connectionState = connector.connectionState
    val connectedDevice = connector.connectedDevice

    private val _settings = MutableStateFlow(loadSettings())
    val settings: StateFlow<WearableSettings> = _settings.asStateFlow()

    suspend fun discoverDevices(): List<WearableDevice> =
        connector.discoverDevices(
            setOf(
                DeviceConnectionType.Bluetooth,
                DeviceConnectionType.WiFi,
            ),
        )

    suspend fun connect(device: WearableDevice): WearableConnectionResult =
        connector.connect(device)

    suspend fun disconnect(): WearableConnectionResult =
        connector.disconnect()

    suspend fun sendCommand(command: WearableCommand): WearableConnectionResult =
        connector.sendCommand(command)

    suspend fun sendLearningFrame(
        objectName: String,
        summary: String,
        languageCode: String,
    ): WearableConnectionResult =
        connector.sendLearningFrame(
            WearableLearningFrame(
                objectName = objectName.take(MAX_TITLE_LENGTH),
                summary = summary.take(summaryLimitFor(settings.value)),
                languageCode = languageCode,
                displayMode = settings.value.displayMode.name,
                shouldSpeak = settings.value.audioOutput != WearableAudioOutput.PhoneSpeaker,
            ),
        )

    fun updateDisplayMode(displayMode: WearableDisplayMode) {
        persist(settings.value.copy(displayMode = displayMode))
    }

    fun updateAudioOutput(audioOutput: WearableAudioOutput) {
        persist(settings.value.copy(audioOutput = audioOutput))
    }

    fun updateVoiceSensitivity(value: Float) {
        persist(settings.value.copy(voiceSensitivity = value.coerceIn(0f, 1f)))
    }

    fun updateInteractionMode(interactionMode: WearableInteractionMode) {
        persist(settings.value.copy(interactionMode = interactionMode))
    }

    fun updateLowPowerMode(enabled: Boolean) {
        persist(settings.value.copy(lowPowerMode = enabled))
    }

    fun adaptiveFrameIntervalMillis(): Long =
        if (settings.value.lowPowerMode || connectedDevice.value?.batteryLevel.orZero() < LOW_BATTERY_THRESHOLD) {
            LOW_POWER_FRAME_INTERVAL_MILLIS
        } else {
            DEFAULT_FRAME_INTERVAL_MILLIS
        }

    fun shouldUseCompactOverlay(): Boolean =
        settings.value.lowPowerMode ||
            settings.value.displayMode == WearableDisplayMode.CompactHeadsUp ||
            connectedDevice.value?.batteryLevel.orZero() < LOW_BATTERY_THRESHOLD

    private fun persist(settings: WearableSettings) {
        preferences.edit()
            .putString(KEY_DISPLAY_MODE, settings.displayMode.name)
            .putString(KEY_AUDIO_OUTPUT, settings.audioOutput.name)
            .putFloat(KEY_VOICE_SENSITIVITY, settings.voiceSensitivity)
            .putString(KEY_INTERACTION_MODE, settings.interactionMode.name)
            .putBoolean(KEY_LOW_POWER_MODE, settings.lowPowerMode)
            .apply()
        _settings.value = settings
    }

    private fun loadSettings(): WearableSettings = WearableSettings(
        displayMode = preferences.enumValue(KEY_DISPLAY_MODE, WearableDisplayMode.CompactHeadsUp),
        audioOutput = preferences.enumValue(KEY_AUDIO_OUTPUT, WearableAudioOutput.WearableSpeaker),
        voiceSensitivity = preferences.getFloat(KEY_VOICE_SENSITIVITY, DEFAULT_VOICE_SENSITIVITY),
        interactionMode = preferences.enumValue(KEY_INTERACTION_MODE, WearableInteractionMode.VoiceFirst),
        lowPowerMode = preferences.getBoolean(KEY_LOW_POWER_MODE, false),
    )

    private fun summaryLimitFor(settings: WearableSettings): Int =
        if (settings.lowPowerMode || settings.displayMode == WearableDisplayMode.CompactHeadsUp) {
            COMPACT_SUMMARY_LENGTH
        } else {
            EXTENDED_SUMMARY_LENGTH
        }

    private inline fun <reified T : Enum<T>> android.content.SharedPreferences.enumValue(
        key: String,
        fallback: T,
    ): T = runCatching {
        enumValueOf<T>(getString(key, fallback.name) ?: fallback.name)
    }.getOrDefault(fallback)

    private fun Int?.orZero(): Int = this ?: 0

    companion object {
        private const val PREFERENCES_NAME = "rola_wearable_preferences"
        private const val KEY_DISPLAY_MODE = "display_mode"
        private const val KEY_AUDIO_OUTPUT = "audio_output"
        private const val KEY_VOICE_SENSITIVITY = "voice_sensitivity"
        private const val KEY_INTERACTION_MODE = "interaction_mode"
        private const val KEY_LOW_POWER_MODE = "low_power_mode"
        private const val DEFAULT_VOICE_SENSITIVITY = 0.68f
        private const val LOW_BATTERY_THRESHOLD = 20
        private const val MAX_TITLE_LENGTH = 48
        private const val COMPACT_SUMMARY_LENGTH = 140
        private const val EXTENDED_SUMMARY_LENGTH = 320
        private const val DEFAULT_FRAME_INTERVAL_MILLIS = 500L
        private const val LOW_POWER_FRAME_INTERVAL_MILLIS = 1_500L
    }
}

data class WearableSettings(
    val displayMode: WearableDisplayMode,
    val audioOutput: WearableAudioOutput,
    val voiceSensitivity: Float,
    val interactionMode: WearableInteractionMode,
    val lowPowerMode: Boolean,
) {
    val processingProfile: String
        get() = if (lowPowerMode) "Adaptive low power" else "Balanced real-time"

    val overlayBudget: String
        get() = if (displayMode == WearableDisplayMode.SpatialAnchoredPanel) {
            "Spatial panel"
        } else {
            "Compact heads-up"
        }

    val connectionHealthLabel: String
        get() = when {
            lowPowerMode -> "Reduced camera, inference, and network cadence"
            else -> "Standard camera, inference, and rendering cadence"
        }
}
