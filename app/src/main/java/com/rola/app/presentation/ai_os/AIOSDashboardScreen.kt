package com.rola.app.presentation.ai_os

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
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.material.icons.rounded.Security
import androidx.compose.material.icons.rounded.SettingsSuggest
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
fun AIOSDashboardScreen(
    onBack: () -> Unit,
    viewModel: AIOSDashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val dashboard = uiState.dashboard

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Education OS") },
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
            Button(onClick = viewModel::runAIOS, enabled = !uiState.loading) {
                Icon(Icons.Rounded.SettingsSuggest, contentDescription = null)
                Text("Run AI OS Workflow")
            }
            if (uiState.loading) CircularProgressIndicator()
            uiState.message?.let { Text(text = it, color = MaterialTheme.colorScheme.primary) }
            uiState.errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.SettingsSuggest, contentDescription = null) }, label = { Text("Services ${dashboard.startedServices.size}") })
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.AccountTree, contentDescription = null) }, label = { Text("Agents ${dashboard.agents.size}") })
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.Security, contentDescription = null) }, label = { Text(if (dashboard.securitySummary.isBlank()) "No audit" else "Secured") })
            }

            AIOSCard("Kernel", dashboard.executionPlan.ifBlank { "Run AI OS to boot the education kernel." })
            AIOSCard("Services", dashboard.services.ifEmpty { listOf("Service registry appears after OS run.") }.joinToString("\n"))
            AIOSCard("Agent Runtime", dashboard.agents.ifEmpty { listOf("Agent tasks appear after OS run.") }.joinToString("\n"))
            AIOSCard("Learning Workflow", dashboard.workflowStages.ifEmpty { listOf("Workflow stages appear after orchestration.") }.joinToString("\n"))
            AIOSCard("Memory Core", dashboard.memorySummary.ifEmpty { listOf("Memory snapshot appears after OS run.") }.joinToString("\n"))
            AIOSCard("Security", dashboard.securitySummary.ifBlank { "Identity, permissions, data protection, and override status appear after review." })
            AIOSCard("System Events", dashboard.systemEvents.ifEmpty { listOf("Monitoring events appear after OS run.") }.joinToString("\n"))
        }
    }
}

@Composable
private fun AIOSCard(title: String, body: String) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(text = body, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
