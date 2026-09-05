package com.rola.app.presentation.ai_infrastructure

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
import androidx.compose.material.icons.rounded.CloudQueue
import androidx.compose.material.icons.rounded.DeviceHub
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.material.icons.rounded.Security
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
fun AIInfrastructureDashboardScreen(
    onBack: () -> Unit,
    viewModel: AIInfrastructureDashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val dashboard = uiState.dashboard

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Infrastructure") },
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
            Button(onClick = viewModel::optimizeInfrastructure, enabled = !uiState.loading) {
                Icon(Icons.Rounded.CloudQueue, contentDescription = null)
                Text("Optimize Infrastructure")
            }
            if (uiState.loading) CircularProgressIndicator()
            uiState.message?.let { Text(text = it, color = MaterialTheme.colorScheme.primary) }
            uiState.errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.DeviceHub, contentDescription = null) }, label = { Text("Users ${dashboard.activeUsers}") })
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.Memory, contentDescription = null) }, label = { Text("Scale ${dashboard.predictedScale}") })
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.Security, contentDescription = null) }, label = { Text(dashboard.serviceHealth.ifBlank { "No health" }) })
            }

            InfrastructureMetricCard("Response Time Target", dashboard.responseTimeMs)
            InfrastructureCard("Service Mesh", dashboard.requestedServices.ifEmpty { listOf("Run optimization to register AI services.") }.joinToString("\n"))
            InfrastructureCard("Cloud Services", dashboard.cloudServices.ifEmpty { listOf("Cloud service plan appears after optimization.") }.joinToString("\n"))
            InfrastructureCard("Global Clusters", dashboard.clusterRegions.ifEmpty { listOf("Cluster regions appear after optimization.") }.joinToString("\n"))
            InfrastructureCard("Reliability", dashboard.reliabilitySummary.ifBlank { "Monitoring report appears after infrastructure analysis." })
            InfrastructureCard("Scaling Events", dashboard.scalingEvents.ifEmpty { listOf("Self-healing and scaling events appear here.") }.joinToString("\n"))
        }
    }
}

@Composable
private fun InfrastructureMetricCard(title: String, value: Int) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            LinearProgressIndicator(progress = { (1000 - value).coerceIn(0, 1000) / 1000f }, modifier = Modifier.fillMaxWidth())
            Text(text = "$value ms", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun InfrastructureCard(title: String, body: String) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(text = body, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
