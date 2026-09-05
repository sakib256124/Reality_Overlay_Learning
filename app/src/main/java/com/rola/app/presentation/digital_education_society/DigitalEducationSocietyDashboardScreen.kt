package com.rola.app.presentation.digital_education_society

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
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Hub
import androidx.compose.material.icons.rounded.Public
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
fun DigitalEducationSocietyDashboardScreen(
    onBack: () -> Unit,
    viewModel: DigitalEducationSocietyViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val dashboard = uiState.dashboard

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Digital Education Society") },
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
            Button(onClick = viewModel::runCivilizationCycle, enabled = !uiState.loading) {
                Icon(Icons.Rounded.Public, contentDescription = null)
                Text("Run Civilization Cycle")
            }
            if (uiState.loading) CircularProgressIndicator()
            uiState.message?.let { Text(text = it, color = MaterialTheme.colorScheme.primary) }
            uiState.errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.Groups, contentDescription = null) }, label = { Text("Participants ${dashboard.participantCount}") })
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.Hub, contentDescription = null) }, label = { Text("Topics ${dashboard.knowledgeTopics.size}") })
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.AdminPanelSettings, contentDescription = null) }, label = { Text("Policies ${dashboard.governanceStatus.size}") })
            }

            SocietyCard("Knowledge Network", dashboard.knowledgeTopics.ifEmpty { listOf("No global knowledge network yet.") }.joinToString("\n"))
            SocietyCard("AI Education Communities", dashboard.communityHighlights.ifEmpty { listOf("Communities appear after coordination.") }.joinToString("\n"))
            SocietyCard("Global Analytics", dashboard.globalTrends.ifEmpty { listOf("Global trend report will appear after analysis.") }.joinToString("\n"))
            SocietyCard("Digital Learning Avatar", dashboard.avatarSummary.ifBlank { "Avatar profile appears after the first civilization cycle." })
            SocietyCard("Innovation Economy", dashboard.innovationIdeas.ifEmpty { listOf("Innovation ideas require approved knowledge exchange.") }.joinToString("\n"))
            SocietyCard("Governance and Trust", dashboard.governanceStatus.ifEmpty { listOf("Human governance policies appear after review.") }.joinToString("\n"))
        }
    }
}

@Composable
private fun SocietyCard(title: String, body: String) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(text = body, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
