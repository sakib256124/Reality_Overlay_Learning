package com.rola.app.presentation.collective_ai

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
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Hub
import androidx.compose.material.icons.rounded.Psychology
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
fun CollectiveAIDashboardScreen(
    onBack: () -> Unit,
    viewModel: CollectiveAIDashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val dashboard = uiState.dashboard

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Collective AI") },
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
            Button(onClick = viewModel::runCollectiveCycle, enabled = !uiState.loading) {
                Icon(Icons.Rounded.Hub, contentDescription = null)
                Text("Run Collaboration")
            }
            if (uiState.loading) CircularProgressIndicator()
            uiState.message?.let { Text(text = it, color = MaterialTheme.colorScheme.primary) }
            uiState.errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.Groups, contentDescription = null) }, label = { Text("Agents ${dashboard.activeAgents}") })
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.Hub, contentDescription = null) }, label = { Text(if (dashboard.consensus.isBlank()) "No consensus" else "Consensus ready") })
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.Psychology, contentDescription = null) }, label = { Text("Exchange ${dashboard.knowledgeExchange.size}") })
            }

            CollectiveMetricCard("Consensus Score", dashboard.consensusScore)
            CollectiveMetricCard("Improvement Score", dashboard.improvementScore)
            CollectiveCard("Collaboration Status", dashboard.collaborationStatus.ifBlank { "Run a collaboration cycle to activate the collective AI society." })
            CollectiveCard("Active Agents", dashboard.agentNames.ifEmpty { listOf("Agent roster appears after activation.") }.joinToString("\n"))
            CollectiveCard("Knowledge Exchange", dashboard.knowledgeExchange.ifEmpty { listOf("Knowledge sharing appears after agents coordinate.") }.joinToString("\n"))
            CollectiveCard("Consensus", dashboard.consensus.ifBlank { "Consensus decision appears after debate and ranking." })
            CollectiveCard("Collective Decisions", dashboard.decisions.ifEmpty { listOf("Ranked strategies appear after consensus.") }.joinToString("\n"))
            CollectiveCard("Education Improvements", dashboard.improvements.ifEmpty { listOf("Optimization results appear after feedback integration.") }.joinToString("\n"))
            CollectiveCard("Curriculum Updates", dashboard.curriculumUpdates.ifEmpty { listOf("Curriculum improvements appear after collective learning.") }.joinToString("\n"))
        }
    }
}

@Composable
private fun CollectiveMetricCard(title: String, value: Int) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            LinearProgressIndicator(progress = { value / 100f }, modifier = Modifier.fillMaxWidth())
            Text(text = "$value%", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun CollectiveCard(title: String, body: String) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(text = body, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
