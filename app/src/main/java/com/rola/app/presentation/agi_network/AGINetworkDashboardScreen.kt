package com.rola.app.presentation.agi_network

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
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material.icons.rounded.Hub
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
fun AGINetworkDashboardScreen(
    onBack: () -> Unit,
    viewModel: AGINetworkDashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val dashboard = uiState.dashboard

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AGI Network") },
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
            Button(onClick = viewModel::runNetworkCycle, enabled = !uiState.loading) {
                Icon(Icons.Rounded.Hub, contentDescription = null)
                Text("Run AGI Network Cycle")
            }
            if (uiState.loading) CircularProgressIndicator()
            uiState.message?.let { Text(text = it, color = MaterialTheme.colorScheme.primary) }
            uiState.errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.AutoGraph, contentDescription = null) }, label = { Text("AI ${dashboard.aiPerformancePercent}%") })
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.Hub, contentDescription = null) }, label = { Text("Tasks ${dashboard.activeTasks.size}") })
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.AdminPanelSettings, contentDescription = null) }, label = { Text("Governance ${dashboard.governanceAlerts.size}") })
            }

            AGINetworkMetricCard("Student Success", dashboard.studentSuccessPercent)
            AGINetworkMetricCard("AI Performance", dashboard.aiPerformancePercent)
            AGINetworkTextCard("Agent Tasks", dashboard.activeTasks.ifEmpty { listOf("No AGI agent tasks yet.") }.joinToString("\n"))
            AGINetworkTextCard("Recent Decisions", dashboard.recentDecisions.ifEmpty { listOf("Run a network cycle to generate supervised draft decisions.") }.joinToString("\n"))
            AGINetworkTextCard("Curriculum Evolution", dashboard.curriculumDrafts.ifEmpty { listOf("Curriculum drafts wait for research and governance review.") }.joinToString("\n"))
            AGINetworkTextCard("Governance", dashboard.governanceAlerts.ifEmpty { listOf("No governance records yet.") }.joinToString("\n"))
        }
    }
}

@Composable
private fun AGINetworkMetricCard(title: String, value: Int) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            LinearProgressIndicator(progress = { value / 100f }, modifier = Modifier.fillMaxWidth())
            Text(text = "$value%", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun AGINetworkTextCard(title: String, body: String) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(text = body, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
