package com.rola.app.presentation.agi

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
import androidx.compose.material.icons.rounded.AccountTree
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.Timeline
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AGIDashboardScreen(
    onBack: () -> Unit,
    viewModel: AGIDashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val dashboard = uiState.dashboard
    val result = uiState.latestResult

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "AGI Education Intelligence") },
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = uiState.objective,
                        onValueChange = viewModel::updateObjective,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Learning objective") },
                    )
                    OutlinedTextField(
                        value = uiState.topic,
                        onValueChange = viewModel::updateTopic,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Topic") },
                    )
                    Button(onClick = viewModel::runAGICycle, enabled = !uiState.loading) {
                        Icon(Icons.Rounded.Psychology, contentDescription = null)
                        Text("Run AGI Cycle")
                    }
                    if (uiState.loading) CircularProgressIndicator()
                    uiState.message?.let { Text(text = it, color = MaterialTheme.colorScheme.primary) }
                    uiState.errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
                }
            }

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.AccountTree, contentDescription = null) }, label = { Text("Agents ${dashboard?.activeAgents?.size ?: 0}") })
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.Timeline, contentDescription = null) }, label = { Text("Evolution ${dashboard?.curriculumEvolution?.size ?: 0}") })
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.Security, contentDescription = null) }, label = { Text("Safety ${dashboard?.safetyAlerts?.size ?: 0}") })
            }

            result?.let {
                AGICard("Latest Decision", "${it.learningAction.title}\n${it.learningAction.description}")
                AGICard("Cognitive Report", listOf(
                    "Speed: ${it.cognitiveReport.learningSpeed.name}",
                    "Retention: ${it.cognitiveReport.memoryRetentionScore}%",
                    "Difficulty: ${it.cognitiveReport.conceptDifficulty.name}",
                    it.cognitiveReport.futurePrediction,
                ).joinToString("\n"))
                AGICard("Reasoning Trace", it.reasoningTrace.take(6).joinToString("\n"))
            }

            AGICard(
                title = "AI Activity Monitoring",
                body = dashboard?.activeAgents
                    ?.joinToString("\n") { it.name }
                    .orEmpty()
                    .ifBlank { "Autonomous agents will appear after an AGI cycle runs." },
            )
            AGICard(
                title = "Curriculum Evolution",
                body = dashboard?.curriculumEvolution
                    ?.joinToString("\n") { "${it.topic}: ${it.missingConcepts.joinToString()}" }
                    .orEmpty()
                    .ifBlank { "Teacher-reviewed curriculum evolution proposals will appear here." },
            )
            AGICard(
                title = "Knowledge Growth",
                body = dashboard?.knowledgeGrowth
                    ?.joinToString("\n")
                    .orEmpty()
                    .ifBlank { "Knowledge intelligence growth signals will appear here." },
            )
            AGICard(
                title = "Safety Alerts",
                body = dashboard?.safetyAlerts
                    ?.joinToString("\n")
                    .orEmpty()
                    .ifBlank { "No AGI safety alerts." },
            )
        }
    }
}

@Composable
private fun AGICard(
    title: String,
    body: String,
) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(text = body, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
