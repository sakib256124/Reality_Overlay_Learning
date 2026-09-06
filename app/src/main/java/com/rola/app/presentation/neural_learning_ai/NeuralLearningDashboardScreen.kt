package com.rola.app.presentation.neural_learning_ai

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Psychology
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
fun NeuralKnowledgeDashboardScreen(onBack: () -> Unit, viewModel: NeuralLearningDashboardViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val dashboard = uiState.dashboard
    Scaffold(topBar = { TopAppBar(title = { Text("Neural Knowledge AI") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back") } }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = viewModel::personalizeLearning, enabled = !uiState.loading) { Icon(Icons.Rounded.Psychology, contentDescription = null); Text("Personalize Learning") }
            if (uiState.loading) CircularProgressIndicator()
            uiState.message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
            uiState.errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            Metric("Knowledge Growth", dashboard.knowledgeGrowth)
            Metric("Cognitive Improvement", dashboard.cognitiveImprovement)
            Info("Cognitive Profile", dashboard.cognitiveProfile.ifEmpty { listOf("Cognitive profile appears here.") }.joinToString("\n"))
            Info("Learning Pathways", dashboard.learningPathways.ifEmpty { listOf("Knowledge pathways appear here.") }.joinToString("\n"))
            Info("Memory Improvement", dashboard.memoryImprovement.ifEmpty { listOf("Retention and reinforcement plans appear here.") }.joinToString("\n"))
            Info("Knowledge Connections", dashboard.knowledgeConnections.ifEmpty { listOf("Hidden relationships appear here.") }.joinToString("\n"))
            Info("Adaptation", dashboard.adaptationHistory.ifEmpty { listOf("Difficulty, pace, and practice adaptation appears here.") }.joinToString("\n"))
            Info("AI Recommendations", dashboard.aiRecommendations.ifEmpty { listOf("Neural learning recommendations appear here.") }.joinToString("\n"))
            Info("Ethics", dashboard.ethicalStatus.ifBlank { "Privacy, control, transparency, and secure storage status appears here." })
        }
    }
}

@Composable private fun Metric(title: String, value: Int) { Card(Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text(title, fontWeight = FontWeight.SemiBold); LinearProgressIndicator(progress = { value / 100f }, modifier = Modifier.fillMaxWidth()); Text("$value%", color = MaterialTheme.colorScheme.onSurfaceVariant) } } }
@Composable private fun Info(title: String, body: String) { Card(Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text(title, fontWeight = FontWeight.SemiBold); Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant) } } }
