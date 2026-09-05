package com.rola.app.presentation.digital_companion

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.material.icons.rounded.Route
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CompanionDashboardScreen(
    onBack: () -> Unit,
    viewModel: CompanionDashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val dashboard = uiState.dashboard

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Companion") },
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
            Button(onClick = viewModel::runCompanionCycle, enabled = !uiState.loading) {
                Icon(Icons.Rounded.AutoAwesome, contentDescription = null)
                Text("Update Companion")
            }
            if (uiState.loading) CircularProgressIndicator()
            uiState.message?.let { Text(text = it, color = MaterialTheme.colorScheme.primary) }
            uiState.errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.Favorite, contentDescription = null) }, label = { Text(if (dashboard.companionStatus.isBlank()) "No companion" else "Companion ready") })
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.Memory, contentDescription = null) }, label = { Text("Memories ${dashboard.memories.size}") })
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.Route, contentDescription = null) }, label = { Text("Roadmap ${dashboard.roadmap.size}") })
            }

            CompanionMetricCard("Engagement", dashboard.engagementPercent)
            CompanionCard("Status", dashboard.companionStatus.ifBlank { "Run a companion cycle to create the learner's personal AI companion." })
            CompanionCard("Goals", dashboard.goals.ifEmpty { listOf("Learning goals appear after companion setup.") }.joinToString("\n"))
            CompanionCard("Memory", dashboard.memories.ifEmpty { listOf("User-controlled memory appears here.") }.joinToString("\n"))
            CompanionCard("Relationship", dashboard.learnsBestBy.ifBlank { "The companion will learn how this learner learns best." })
            CompanionCard("Recommendations", dashboard.recommendations.ifEmpty { listOf("Recommendations appear after emotion-aware planning.") }.joinToString("\n"))
            CompanionCard("Achievements", dashboard.achievements.ifEmpty { listOf("Achievements appear after progress checkpoints.") }.joinToString("\n"))
            CompanionCard("Future Roadmap", dashboard.roadmap.ifEmpty { listOf("Long-term learning roadmap appears after companion evolution.") }.joinToString("\n"))
        }
    }
}

@Composable
private fun CompanionMetricCard(title: String, value: Int) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            LinearProgressIndicator(progress = { value / 100f }, modifier = Modifier.fillMaxWidth())
            Text(text = "$value%", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun CompanionCard(title: String, body: String) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(text = body, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
