package com.rola.app.presentation.chatbot

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.VolumeOff
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.AssistChip
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.rola.app.presentation.translation.LanguageSelector

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun ChatbotScreen(
    onBack: () -> Unit,
    viewModel: ChatbotViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "ROLA Tutor") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            if (uiState.status == ChatbotStatus.Speaking) {
                                viewModel.stopSpeaking()
                            } else {
                                viewModel.replayLastAnswer()
                            }
                        },
                    ) {
                        Icon(
                            imageVector = if (uiState.status == ChatbotStatus.Speaking) {
                                Icons.Rounded.VolumeOff
                            } else {
                                Icons.Rounded.VolumeUp
                            },
                            contentDescription = "Voice answer playback",
                        )
                    }
                    IconButton(onClick = viewModel::clearChat) {
                        Icon(Icons.Rounded.Delete, contentDescription = "Clear chat")
                    }
                },
            )
        },
        bottomBar = {
            Surface(tonalElevation = 3.dp) {
                ChatInputField(
                    value = uiState.inputText,
                    canSend = uiState.canSend,
                    onValueChanged = viewModel::onInputChanged,
                    onSend = viewModel::sendMessage,
                )
            }
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            TutorHeader(uiState = uiState)
            LanguageSelector(
                languages = uiState.languages,
                selectedLanguageCode = uiState.selectedLanguageCode,
                onLanguageSelected = viewModel::onLanguageSelected,
            )
            SuggestedQuestions(
                suggestions = uiState.suggestedQuestions,
                onClick = viewModel::sendSuggestedQuestion,
            )
            if (uiState.status == ChatbotStatus.Thinking) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
            }
            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                items(uiState.messages, key = { it.messageId }) { message ->
                    ChatMessageCard(message = message)
                }
            }
            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Composable
private fun TutorHeader(uiState: ChatbotUiState) {
    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
        Text(
            text = if (uiState.objectId == null) "Ask about your learning" else "Ask about ${uiState.objectId}",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Grounded answers use saved object knowledge, history, and quiz progress.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}

@Composable
private fun SuggestedQuestions(
    suggestions: List<String>,
    onClick: (String) -> Unit,
) {
    FlowRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        suggestions.forEach { suggestion ->
            AssistChip(
                onClick = { onClick(suggestion) },
                label = { Text(text = suggestion) },
            )
        }
    }
}
