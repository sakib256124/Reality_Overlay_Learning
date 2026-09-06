package com.rola.app.presentation.global_education_network

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
fun GlobalEducationDashboardScreen(onBack: () -> Unit, viewModel: GlobalEducationDashboardViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val dashboard = uiState.dashboard
    Scaffold(topBar = { TopAppBar(title = { Text("Global Education Network") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back") } }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = viewModel::connectGlobalNetwork, enabled = !uiState.loading) { Icon(Icons.Rounded.Public, contentDescription = null); Text("Connect Network") }
            if (uiState.loading) CircularProgressIndicator()
            uiState.message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
            uiState.errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            Metric("Global Knowledge Growth", dashboard.globalKnowledgeGrowth)
            Info("Global Learning Network", dashboard.globalLearningNetwork.ifEmpty { listOf("Global network status appears here.") }.joinToString("\n"))
            Info("Connected Institutions", dashboard.connectedInstitutions.ifEmpty { listOf("Institutions appear here.") }.joinToString("\n"))
            Info("Research Activities", dashboard.researchActivities.ifEmpty { listOf("Research activities appear here.") }.joinToString("\n"))
            Info("Learning Opportunities", dashboard.learningOpportunities.ifEmpty { listOf("Courses, scholarships, and projects appear here.") }.joinToString("\n"))
            Info("Knowledge Exchange", dashboard.knowledgeExchange.ifEmpty { listOf("Courses, research, and innovation exchange appear here.") }.joinToString("\n"))
            Info("Communication", dashboard.communication.ifEmpty { listOf("Translation and AI collaboration appear here.") }.joinToString("\n"))
            Info("Governance", dashboard.governanceStatus.ifBlank { "Identity, data protection, institutional auth, and policy status appears here." })
        }
    }
}

@Composable private fun Metric(title: String, value: Int) { Card(Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text(title, fontWeight = FontWeight.SemiBold); LinearProgressIndicator(progress = { value / 100f }, modifier = Modifier.fillMaxWidth()); Text("$value%", color = MaterialTheme.colorScheme.onSurfaceVariant) } } }
@Composable private fun Info(title: String, body: String) { Card(Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text(title, fontWeight = FontWeight.SemiBold); Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant) } } }
