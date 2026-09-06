package com.rola.app.presentation.education_economy_ai

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.AutoAwesome
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
fun EducationEconomyDashboardScreen(onBack: () -> Unit, viewModel: EducationEconomyDashboardViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val dashboard = uiState.dashboard
    Scaffold(topBar = { TopAppBar(title = { Text("Education Economy AI") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back") } }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = viewModel::buildEconomy, enabled = !uiState.loading) { Icon(Icons.Rounded.AutoAwesome, contentDescription = null); Text("Build Economy") }
            if (uiState.loading) CircularProgressIndicator()
            uiState.message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
            uiState.errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            Metric("Learning Value", dashboard.learningValue)
            Metric("Economy Score", dashboard.economyScore)
            Info("Digital Assets", dashboard.digitalAssets.ifEmpty { listOf("Digital learning assets appear here.") }.joinToString("\n"))
            Info("Creator Activity", dashboard.creatorActivity.ifEmpty { listOf("Creator economy activity appears here.") }.joinToString("\n"))
            Info("Innovation Trends", dashboard.innovationTrends.ifEmpty { listOf("Innovation trends appear here.") }.joinToString("\n"))
            Info("Certifications", dashboard.certifications.ifEmpty { listOf("Skill certifications appear here.") }.joinToString("\n"))
            Info("Reputation", dashboard.reputation.ifEmpty { listOf("Reputation records appear here.") }.joinToString("\n"))
            Info("Resource Performance", dashboard.resourcePerformance.ifEmpty { listOf("Resource performance appears here.") }.joinToString("\n"))
            Info("Governance", listOf(dashboard.governanceStatus, dashboard.transactionStatus).filter { it.isNotBlank() }.ifEmpty { listOf("Governance and transaction status appears here.") }.joinToString("\n"))
        }
    }
}

@Composable private fun Metric(title: String, value: Int) { Card(Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text(title, fontWeight = FontWeight.SemiBold); LinearProgressIndicator(progress = { value / 100f }, modifier = Modifier.fillMaxWidth()); Text("$value%", color = MaterialTheme.colorScheme.onSurfaceVariant) } } }
@Composable private fun Info(title: String, body: String) { Card(Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text(title, fontWeight = FontWeight.SemiBold); Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant) } } }
