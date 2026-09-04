package com.rola.app.presentation.vision

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun SceneInformationCard(
    uiState: VisionUiState,
    modifier: Modifier = Modifier,
) {
    val scene = uiState.sceneContext
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(
                text = scene?.sceneType ?: "Scene Understanding",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = scene?.description ?: uiState.statusMessage,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
            )
            Text(text = "Inference ${uiState.inferenceTimeMillis} ms")

            FlowSection("Objects", uiState.trackedObjects.map { "${it.label} ${(it.confidence * 100).toInt()}%" })
            FlowSection("Topics", scene?.recommendedTopics.orEmpty())
            FlowSection("Safety", scene?.safetyInformation.orEmpty())
            FlowSection("Science", scene?.scientificExplanations.orEmpty())
            FlowSection("Relationships", uiState.relationships.map { it.description })
        }
    }
}

@Composable
@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
private fun FlowSection(
    title: String,
    values: List<String>,
) {
    if (values.isEmpty()) return
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = title, fontWeight = FontWeight.SemiBold)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            values.forEach { value ->
                AssistChip(onClick = {}, label = { Text(text = value) })
            }
        }
    }
}
