package com.rola.app.presentation.quantum_ai

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
import androidx.compose.material.icons.rounded.Hub
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Tune
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
fun QuantumAIDashboardScreen(
    onBack: () -> Unit,
    viewModel: QuantumAIDashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val dashboard = uiState.dashboard

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quantum AI") },
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
            Button(onClick = viewModel::runQuantumOptimization, enabled = !uiState.loading) {
                Icon(Icons.Rounded.Tune, contentDescription = null)
                Text("Run Quantum Optimization")
            }
            if (uiState.loading) CircularProgressIndicator()
            uiState.message?.let { Text(text = it, color = MaterialTheme.colorScheme.primary) }
            uiState.errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.AutoGraph, contentDescription = null) }, label = { Text("Optimization ${dashboard.learningOptimizationScore}%") })
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.Lightbulb, contentDescription = null) }, label = { Text("Prediction ${dashboard.predictionAccuracyPercent}%") })
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.Hub, contentDescription = null) }, label = { Text("Discovery ${dashboard.knowledgeDiscovery.size}") })
            }

            QuantumMetricCard("Learning Optimization", dashboard.learningOptimizationScore)
            QuantumMetricCard("AI Improvement", dashboard.aiImprovementPercent)
            QuantumMetricCard("Future Performance", dashboard.futurePerformancePercent)
            QuantumTextCard("Quantum Decisions", dashboard.decisions.ifEmpty { listOf("No quantum decisions yet.") }.joinToString("\n"))
            QuantumTextCard("Knowledge Discovery", dashboard.knowledgeDiscovery.ifEmpty { listOf("Discovery records appear after optimization.") }.joinToString("\n"))
            QuantumTextCard("System Intelligence Growth", dashboard.intelligenceGrowth.ifEmpty { listOf("Growth signals are waiting for learning data.") }.joinToString("\n"))
            QuantumTextCard("Responsible AI", "Quantum AI runs as quantum-inspired classical optimization now. Student-facing publication requires human control and transparent audit records.")
        }
    }
}

@Composable
private fun QuantumMetricCard(title: String, value: Int) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            LinearProgressIndicator(progress = { value / 100f }, modifier = Modifier.fillMaxWidth())
            Text(text = "$value%", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun QuantumTextCard(title: String, body: String) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(text = body, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
