package com.rola.app.presentation.voice

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rola.app.domain.model.ObjectInformation

@Composable
fun VoiceExplanationScreen(
    objectInformation: ObjectInformation?,
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: VoiceViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    DisposableEffect(objectInformation) {
        viewModel.onObjectInformationChanged(objectInformation)
        onDispose { viewModel.stop() }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
    ) {
        VoiceControlPanel(
            uiState = uiState,
            onPlay = viewModel::play,
            onPause = viewModel::pause,
            onResume = viewModel::resume,
            onStop = viewModel::stop,
            onSpeechRateChanged = viewModel::onSpeechRateChanged,
            onPitchChanged = viewModel::onPitchChanged,
            onLanguageSelected = viewModel::onLanguageSelected,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(20.dp),
        )
    }
}
