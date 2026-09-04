package com.rola.app.presentation.translation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TranslationScreen(
    onBack: () -> Unit,
    viewModel: TranslationViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Multilingual Learning") },
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
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Text(
                text = uiState.objectInformation?.name ?: "Translate educational content",
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = "Detected language: ${uiState.detectedLanguage}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )

            LanguageSelector(
                languages = uiState.languages,
                selectedLanguageCode = uiState.selectedLanguageCode,
                onLanguageSelected = viewModel::onLanguageSelected,
            )

            if (uiState.objectInformation == null) {
                OutlinedTextField(
                    value = uiState.sourceText,
                    onValueChange = viewModel::onSourceTextChanged,
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text(text = "Text to translate") },
                    minLines = 4,
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Button(
                    onClick = viewModel::translate,
                    enabled = uiState.status != TranslationStatus.Loading && uiState.canTranslate,
                ) {
                    Icon(Icons.Rounded.Translate, contentDescription = null)
                    Text(text = "Translate")
                }
                Button(
                    onClick = viewModel::speakTranslatedContent,
                    enabled = uiState.status == TranslationStatus.Ready,
                ) {
                    Icon(Icons.Rounded.VolumeUp, contentDescription = null)
                    Text(text = "Listen")
                }
                IconButton(
                    onClick = viewModel::stopSpeech,
                    enabled = uiState.status == TranslationStatus.Speaking,
                ) {
                    Icon(Icons.Rounded.Stop, contentDescription = "Stop speech")
                }
                if (uiState.status == TranslationStatus.Loading) {
                    CircularProgressIndicator()
                }
            }

            uiState.errorMessage?.let { message ->
                Text(text = message, color = MaterialTheme.colorScheme.error)
            }

            TranslatedContentCard(
                information = uiState.translatedInformation,
                translatedText = uiState.translatedText,
                fromCache = uiState.fromCache,
            )
        }
    }
}

