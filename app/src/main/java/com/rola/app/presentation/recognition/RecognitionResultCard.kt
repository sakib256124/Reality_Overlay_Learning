package com.rola.app.presentation.recognition

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun RecognitionResultCard(
    uiState: RecognitionUiState,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Black.copy(alpha = 0.58f),
        shape = RoundedCornerShape(8.dp),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp),
        ) {
            Text(
                text = uiState.statusMessage,
                color = Color.White,
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Start,
            )

            uiState.latestResult?.let { result ->
                Text(
                    text = "Name: ${result.name}",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = "Scientific information loading...",
                    color = Color.White.copy(alpha = 0.78f),
                    style = MaterialTheme.typography.bodyLarge,
                )
                Text(
                    text = "Confidence: ${result.confidencePercent}%",
                    color = Color(0xFF62D6A4),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = Color(0xFFFFC8C2),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}
