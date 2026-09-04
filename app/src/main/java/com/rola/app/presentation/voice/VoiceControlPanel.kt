package com.rola.app.presentation.voice

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material.icons.rounded.VolumeOff
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilledTonalIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rola.app.data.voice.VoiceLanguage

@Composable
fun VoiceControlPanel(
    uiState: VoiceUiState,
    onPlay: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onStop: () -> Unit,
    onSpeechRateChanged: (Float) -> Unit,
    onPitchChanged: (Float) -> Unit,
    onLanguageSelected: (VoiceLanguage) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = uiState.content != null || uiState.errorMessage != null,
        enter = fadeIn(),
        exit = fadeOut(),
        modifier = modifier,
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            color = Color.Black.copy(alpha = 0.66f),
            contentColor = Color.White,
            shape = RoundedCornerShape(8.dp),
            tonalElevation = 6.dp,
            shadowElevation = 6.dp,
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Header(uiState = uiState)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    FilledTonalIconButton(
                        onClick = when {
                            uiState.isSpeaking -> onPause
                            uiState.isPaused -> onResume
                            else -> onPlay
                        },
                        enabled = uiState.content != null,
                    ) {
                        Icon(
                            imageVector = when {
                                uiState.isSpeaking -> Icons.Rounded.Pause
                                else -> Icons.Rounded.PlayArrow
                            },
                            contentDescription = if (uiState.isSpeaking) "Pause explanation" else "Play explanation",
                        )
                    }
                    IconButton(
                        onClick = onStop,
                        enabled = uiState.isVolumeActive,
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Stop,
                            contentDescription = "Stop explanation",
                            tint = Color.White,
                        )
                    }
                    Icon(
                        imageVector = if (uiState.isVolumeActive) Icons.Rounded.VolumeUp else Icons.Rounded.VolumeOff,
                        contentDescription = null,
                        tint = if (uiState.isSpeaking) Color(0xFF8DE0B8) else Color.White.copy(alpha = 0.72f),
                    )
                    Text(
                        text = uiState.status.name.lowercase().replaceFirstChar { it.uppercase() },
                        color = Color.White.copy(alpha = 0.82f),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    LanguageMenu(
                        selectedLanguage = uiState.selectedLanguage,
                        languages = uiState.supportedLanguages,
                        onLanguageSelected = onLanguageSelected,
                    )
                }

                VoiceSlider(
                    label = "Speed",
                    value = uiState.speechRate,
                    valueRange = 0.5f..1.5f,
                    onValueChanged = onSpeechRateChanged,
                )
                VoiceSlider(
                    label = "Pitch",
                    value = uiState.pitch,
                    valueRange = 0.7f..1.4f,
                    onValueChanged = onPitchChanged,
                )

                uiState.errorMessage?.let { message ->
                    Text(
                        text = message,
                        color = Color(0xFFFFD6D1),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
            }
        }
    }
}

@Composable
private fun Header(uiState: VoiceUiState) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        VoiceIndicator(isActive = uiState.isSpeaking)
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = uiState.content?.let { "Explaining: ${it.title}" } ?: "Voice explanation",
                fontWeight = FontWeight.SemiBold,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                text = uiState.content?.let { "Estimated ${it.explanationDuration / 1000}s" }.orEmpty(),
                color = Color.White.copy(alpha = 0.72f),
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
}

@Composable
private fun VoiceIndicator(isActive: Boolean) {
    Spacer(
        modifier = Modifier
            .size(10.dp)
            .clip(CircleShape)
            .background(if (isActive) Color(0xFF8DE0B8) else Color.White.copy(alpha = 0.42f)),
    )
}

@Composable
private fun VoiceSlider(
    label: String,
    value: Float,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChanged: (Float) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = "$label ${"%.2f".format(value)}",
            color = Color.White.copy(alpha = 0.82f),
            style = MaterialTheme.typography.labelMedium,
        )
        Slider(
            value = value,
            onValueChange = onValueChanged,
            valueRange = valueRange,
        )
    }
}

@Composable
private fun LanguageMenu(
    selectedLanguage: VoiceLanguage,
    languages: List<VoiceLanguage>,
    onLanguageSelected: (VoiceLanguage) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }

    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = selectedLanguage.displayName,
            color = Color.White,
            style = MaterialTheme.typography.bodyMedium,
        )
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Rounded.KeyboardArrowDown,
                contentDescription = "Select voice language",
                tint = Color.White,
            )
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            languages.forEach { language ->
                DropdownMenuItem(
                    text = { Text(language.displayName) },
                    onClick = {
                        expanded = false
                        onLanguageSelected(language)
                    },
                )
            }
        }
    }
}
