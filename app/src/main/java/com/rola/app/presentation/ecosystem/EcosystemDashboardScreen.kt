package com.rola.app.presentation.ecosystem

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
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
fun EcosystemDashboardScreen(
    onBack: () -> Unit,
    viewModel: EcosystemDashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "ROLA Ecosystem") },
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
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.small,
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
            ) {
                Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    OutlinedTextField(
                        value = uiState.objectiveInput,
                        onValueChange = viewModel::updateObjective,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text(text = "Learning objective") },
                    )
                    Button(onClick = viewModel::runIntegratedSession, enabled = !uiState.loading) {
                        Icon(Icons.Rounded.PlayArrow, contentDescription = null)
                        Text(text = "Run Session")
                    }
                    if (uiState.loading) CircularProgressIndicator()
                    uiState.message?.let { Text(text = it, color = MaterialTheme.colorScheme.primary) }
                    uiState.errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
                }
            }

            val dashboard = uiState.dashboard
            if (dashboard != null) {
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    AssistChip(onClick = {}, label = { Text(text = dashboard.profile?.learningLevel?.name ?: "Beginner") })
                    AssistChip(onClick = {}, label = { Text(text = "Progress ${dashboard.profile?.progressPercent ?: 0}%") })
                    AssistChip(onClick = {}, label = { Text(text = "Quizzes ${dashboard.progress?.totalQuizzesCompleted ?: 0}") })
                    AssistChip(onClick = {}, label = { Text(text = "Recommendations ${dashboard.recommendations.size}") })
                }

                DashboardCard("Knowledge Map", dashboard.knowledgeMapSummary)
                DashboardCard("Achievements", dashboard.achievements.ifEmpty { listOf("Start a session") }.joinToString())
                DashboardCard("Recommended Learning Path", dashboard.recommendations.take(3).joinToString("\n") { it.title }.ifBlank { "Explore an object, ask the tutor, then take a quiz." })
                dashboard.report?.let { report ->
                    DashboardCard("AI Insights", report.aiInsights.ifEmpty { listOf(report.learningGrowth) }.joinToString("\n"))
                    DashboardCard("Performance Trends", report.performanceTrends.joinToString("\n"))
                }
            }

            uiState.activeSession?.summary?.let { summary ->
                DashboardCard(
                    title = "Session Summary",
                    body = listOf(
                        "Topics: ${summary.learnedTopics.joinToString()}",
                        "Steps: ${summary.totalSteps}",
                        "Next: ${summary.nextRecommendations.joinToString()}",
                    ).joinToString("\n"),
                )
            }
        }
    }
}

@Composable
private fun DashboardCard(
    title: String,
    body: String,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
    ) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(text = body, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
