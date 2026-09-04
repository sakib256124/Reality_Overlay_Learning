package com.rola.app.presentation.neural_ai

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
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.School
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
fun NeuralLearningDashboardScreen(
    onBack: () -> Unit,
    viewModel: NeuralLearningDashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val dashboard = uiState.dashboard
    val cognitiveState = dashboard.cognitiveState
    val prediction = dashboard.prediction

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Neural Learning") },
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
            Button(onClick = viewModel::runSampleNeuralAnalysis, enabled = !uiState.loading) {
                Icon(Icons.Rounded.Psychology, contentDescription = null)
                Text("Run Neural Analysis")
            }
            if (uiState.loading) CircularProgressIndicator()
            uiState.message?.let { Text(text = it, color = MaterialTheme.colorScheme.primary) }
            uiState.errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(
                    onClick = {},
                    leadingIcon = { Icon(Icons.Rounded.AutoGraph, contentDescription = null) },
                    label = { Text("Attention ${cognitiveState?.attentionPercent ?: 0}%") },
                )
                AssistChip(
                    onClick = {},
                    leadingIcon = { Icon(Icons.Rounded.Lightbulb, contentDescription = null) },
                    label = { Text("Retention ${prediction?.knowledgeRetentionPercent ?: 0}%") },
                )
                AssistChip(
                    onClick = {},
                    leadingIcon = { Icon(Icons.Rounded.School, contentDescription = null) },
                    label = { Text(cognitiveState?.understandingLevel?.name ?: "Awaiting Signal") },
                )
            }

            NeuralMetricCard("Attention", cognitiveState?.attentionPercent ?: 0)
            NeuralMetricCard("Engagement", cognitiveState?.engagementPercent ?: 0)
            NeuralMetricCard("Mental Fatigue", cognitiveState?.mentalFatiguePercent ?: 0)

            NeuralTextCard(
                title = "Cognitive State",
                body = cognitiveState?.explanation ?: "Run a local simulated analysis to create the first cognitive state.",
            )
            NeuralTextCard(
                title = "Personalized Optimization",
                body = dashboard.learningState?.let {
                    "Difficulty ${it.recommendedDifficulty}%\nMethod ${it.recommendedMethod.name}\n${it.adaptationReason}"
                } ?: "Lesson optimization will appear after analysis.",
            )
            NeuralTextCard(
                title = "Prediction",
                body = prediction?.let {
                    "Success ${it.learningSuccessPercent}%\nSupport: ${it.requiredSupport.joinToString()}\n${it.longTermRoadmap.joinToString("\n")}"
                } ?: "Predictive roadmap is waiting for neural learning signals.",
            )
            NeuralTextCard(
                title = "AI Teaching Agent",
                body = uiState.latestTeachingInstruction.ifBlank { "No neural teaching instruction generated yet." },
            )
            NeuralTextCard(
                title = "Safety And Privacy",
                body = "Neural analysis is local-only in this foundation. It requires explicit consent and stores educational adaptation signals, not medical diagnoses.",
            )
        }
    }
}

@Composable
private fun NeuralMetricCard(title: String, value: Int) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            LinearProgressIndicator(progress = { value / 100f }, modifier = Modifier.fillMaxWidth())
            Text(text = "$value%", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun NeuralTextCard(title: String, body: String) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(text = body, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
