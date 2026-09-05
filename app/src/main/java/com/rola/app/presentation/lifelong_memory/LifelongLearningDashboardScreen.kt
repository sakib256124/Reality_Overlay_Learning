package com.rola.app.presentation.lifelong_memory

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Memory
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
fun LifelongLearningDashboardScreen(onBack: () -> Unit, viewModel: LifelongLearningDashboardViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val d = uiState.dashboard
    Scaffold(topBar = { TopAppBar(title = { Text("Lifelong Memory") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back") } }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = viewModel::updateMemory, enabled = !uiState.loading) { Icon(Icons.Rounded.Memory, contentDescription = null); Text("Update Memory") }
            if (uiState.loading) CircularProgressIndicator()
            uiState.message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
            uiState.errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            Metric("Knowledge Growth", d.knowledgeGrowthScore)
            Metric("Personalization", d.personalizationScore)
            Info("Expertise Level", d.expertiseLevel.ifBlank { "Run a memory update to build expertise profile." })
            Info("Knowledge Growth", d.knowledgeGrowth.ifEmpty { listOf("Personal knowledge graph appears here.") }.joinToString("\n"))
            Info("Skill Timeline", d.skillTimeline.ifEmpty { listOf("Learning timeline appears here.") }.joinToString("\n"))
            Info("Achievements", d.achievements.ifEmpty { listOf("Achievements appear here.") }.joinToString("\n"))
            Info("Recalled Memory", d.recalledMemory.ifEmpty { listOf("Long-term memory recall appears here.") }.joinToString("\n"))
            Info("Future Goals", d.futureGoals.ifEmpty { listOf("Lifelong mentor goals appear here.") }.joinToString("\n"))
        }
    }
}

@Composable private fun Metric(title: String, value: Int) { Card(Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text(title, fontWeight = FontWeight.SemiBold); LinearProgressIndicator(progress = { value / 100f }, modifier = Modifier.fillMaxWidth()); Text("$value%", color = MaterialTheme.colorScheme.onSurfaceVariant) } } }
@Composable private fun Info(title: String, body: String) { Card(Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text(title, fontWeight = FontWeight.SemiBold); Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant) } } }
