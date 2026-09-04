package com.rola.app.presentation.translation

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
import com.rola.app.domain.model.ObjectInformation

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun TranslatedContentCard(
    information: ObjectInformation?,
    translatedText: String,
    fromCache: Boolean,
    modifier: Modifier = Modifier,
) {
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
                text = if (fromCache) "Translated Content (cached)" else "Translated Content",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )

            if (information != null) {
                Text(text = information.name, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                Text(text = information.scientificName, color = MaterialTheme.colorScheme.primary)
                TranslationSection(title = "Category", body = information.category)
                TranslationSection(title = "Description", body = information.description)
                FlowSection(title = "Uses", values = information.uses)
                FlowSection(title = "Facts", values = information.facts)
            } else {
                Text(
                    text = translatedText.ifBlank { "Choose a language and translate content." },
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }
    }
}

@Composable
private fun TranslationSection(
    title: String,
    body: String,
) {
    Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
        Text(text = title, fontWeight = FontWeight.SemiBold)
        Text(text = body.ifBlank { "Unavailable" }, color = MaterialTheme.colorScheme.onSurfaceVariant)
    }
}

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
private fun FlowSection(
    title: String,
    values: List<String>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = title, fontWeight = FontWeight.SemiBold)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            values.ifEmpty { listOf("Unavailable") }.forEach { value ->
                AssistChip(onClick = {}, label = { Text(text = value) })
            }
        }
    }
}

