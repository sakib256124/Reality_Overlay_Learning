package com.rola.app.presentation.ai_civilization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Public
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CivilizationDashboardScreen(onBack: () -> Unit, viewModel: CivilizationDashboardViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val d = uiState.dashboard
    Scaffold(topBar = { TopAppBar(title = { Text("AI Civilization") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back") } }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = viewModel::runCivilizationCycle, enabled = !uiState.loading) { Icon(Icons.Rounded.Public, contentDescription = null); Text("Evolve Civilization") }
            if (uiState.loading) CircularProgressIndicator()
            uiState.message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
            uiState.errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            Metric("Intelligence", d.intelligenceScore)
            Metric("Evolution", d.evolutionScore)
            Info("Global Education Intelligence", d.intelligence.ifBlank { "Run a cycle to initialize civilization intelligence." })
            Info("Knowledge Evolution", d.knowledgeEvolution.ifEmpty { listOf("Knowledge expansion appears here.") }.joinToString("\n"))
            Info("Learning Improvement", d.learningImprovement.ifEmpty { listOf("Learning improvements appear here.") }.joinToString("\n"))
            Info("AI Innovation", d.innovations.ifEmpty { listOf("Innovation proposals appear here.") }.joinToString("\n"))
            Info("Future Predictions", d.futurePredictions.ifEmpty { listOf("Future predictions appear here.") }.joinToString("\n"))
            Info("Roadmap", d.roadmap.ifEmpty { listOf("Future roadmap appears here.") }.joinToString("\n"))
        }
    }
}

@Composable private fun Metric(title: String, value: Int) { Card(Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text(title, fontWeight = FontWeight.SemiBold); LinearProgressIndicator(progress = { value / 100f }, modifier = Modifier.fillMaxWidth()); Text("$value%", color = MaterialTheme.colorScheme.onSurfaceVariant) } } }
@Composable private fun Info(title: String, body: String) { Card(Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text(title, fontWeight = FontWeight.SemiBold); Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant) } } }
