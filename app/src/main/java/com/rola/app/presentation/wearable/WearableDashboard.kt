package com.rola.app.presentation.wearable

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Bolt
import androidx.compose.material.icons.rounded.CenterFocusStrong
import androidx.compose.material.icons.rounded.GraphicEq
import androidx.compose.material.icons.rounded.Hearing
import androidx.compose.material.icons.rounded.PanTool
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.RecordVoiceOver
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rola.app.domain.model.WearableAudioOutput
import com.rola.app.domain.model.WearableCommand
import com.rola.app.domain.model.WearableDisplayMode
import com.rola.app.domain.model.WearableInteractionMode

@OptIn(
    androidx.compose.material3.ExperimentalMaterial3Api::class,
    androidx.compose.foundation.layout.ExperimentalLayoutApi::class,
)
@Composable
fun WearableDashboard(
    onBack: () -> Unit,
    viewModel: WearableViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Wearable XR") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(18.dp),
        ) {
            DeviceConnectionScreen(
                uiState = uiState,
                onDiscover = viewModel::discoverDevices,
                onConnect = viewModel::connect,
                onDisconnect = viewModel::disconnect,
            )

            VoiceCommandPanel(
                enabled = uiState.isConnected,
                onCommand = viewModel::sendCommand,
                onSampleFrame = viewModel::sendSampleLearningFrame,
            )

            WearableSettingsPanel(
                uiState = uiState,
                onDisplayModeChanged = viewModel::updateDisplayMode,
                onAudioOutputChanged = viewModel::updateAudioOutput,
                onInteractionModeChanged = viewModel::updateInteractionMode,
                onVoiceSensitivityChanged = viewModel::updateVoiceSensitivity,
                onLowPowerModeChanged = viewModel::updateLowPowerMode,
            )

            StatusPanel(uiState = uiState)
        }
    }
}

@Composable
private fun VoiceCommandPanel(
    enabled: Boolean,
    onCommand: (WearableCommand) -> Unit,
    onSampleFrame: () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Text(text = "Hands-Free", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            CommandChip("Scan object", Icons.Rounded.CenterFocusStrong, enabled) { onCommand(WearableCommand.ScanObject) }
            CommandChip("Explain this", Icons.Rounded.RecordVoiceOver, enabled) { onCommand(WearableCommand.ExplainThis) }
            CommandChip("Start quiz", Icons.Rounded.Quiz, enabled) { onCommand(WearableCommand.StartQuiz) }
            CommandChip("Translate", Icons.Rounded.Translate, enabled) { onCommand(WearableCommand.Translate) }
        }
        Button(onClick = onSampleFrame, enabled = enabled, modifier = Modifier.fillMaxWidth()) {
            Icon(Icons.Rounded.GraphicEq, contentDescription = null)
            Text(text = "Send Learning Frame")
        }
    }
}

@Composable
private fun CommandChip(
    label: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    enabled: Boolean,
    onClick: () -> Unit,
) {
    AssistChip(
        onClick = onClick,
        enabled = enabled,
        leadingIcon = { Icon(icon, contentDescription = null) },
        label = { Text(text = label) },
    )
}

@Composable
@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
private fun WearableSettingsPanel(
    uiState: WearableUiState,
    onDisplayModeChanged: (WearableDisplayMode) -> Unit,
    onAudioOutputChanged: (WearableAudioOutput) -> Unit,
    onInteractionModeChanged: (WearableInteractionMode) -> Unit,
    onVoiceSensitivityChanged: (Float) -> Unit,
    onLowPowerModeChanged: (Boolean) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        Text(text = "Device Settings", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)

        SettingGroup(title = "Display") {
            WearableDisplayMode.entries.forEach { mode ->
                FilterChip(
                    selected = uiState.displayMode == mode,
                    onClick = { onDisplayModeChanged(mode) },
                    label = { Text(text = mode.name) },
                )
            }
        }

        SettingGroup(title = "Audio") {
            WearableAudioOutput.entries.forEach { output ->
                FilterChip(
                    selected = uiState.audioOutput == output,
                    onClick = { onAudioOutputChanged(output) },
                    leadingIcon = if (uiState.audioOutput == output) {
                        { Icon(Icons.Rounded.Hearing, contentDescription = null) }
                    } else {
                        null
                    },
                    label = { Text(text = output.name) },
                )
            }
        }

        SettingGroup(title = "Interaction") {
            WearableInteractionMode.entries.forEach { mode ->
                FilterChip(
                    selected = uiState.interactionMode == mode,
                    onClick = { onInteractionModeChanged(mode) },
                    leadingIcon = if (mode == WearableInteractionMode.HandTracking) {
                        { Icon(Icons.Rounded.PanTool, contentDescription = null) }
                    } else {
                        null
                    },
                    label = { Text(text = mode.name) },
                )
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(text = "Voice Sensitivity ${(uiState.voiceSensitivity * 100).toInt()}%")
            Slider(
                value = uiState.voiceSensitivity,
                onValueChange = onVoiceSensitivityChanged,
                valueRange = 0f..1f,
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.Bolt, contentDescription = null)
                Text(text = "Low Power Mode")
            }
            Switch(checked = uiState.lowPowerMode, onCheckedChange = onLowPowerModeChanged)
        }
    }
}

@Composable
private fun SettingGroup(
    title: String,
    content: @Composable () -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = title, color = MaterialTheme.colorScheme.onSurfaceVariant)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            content()
        }
    }
}

@Composable
private fun StatusPanel(uiState: WearableUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "Runtime", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        Text(text = "Frame interval: ${uiState.adaptiveFrameIntervalMillis} ms")
        Text(text = "Overlay: ${if (uiState.usesCompactOverlay) "Compact" else "Spatial"}")
        uiState.lastCommand?.let { Text(text = "Last command: $it") }
        uiState.lastTransferSummary?.let { Text(text = it, color = MaterialTheme.colorScheme.onSurfaceVariant) }
        uiState.errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
    }
}
