package com.rola.app.presentation.research

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.TaskAlt
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rola.app.domain.model.KnowledgeUpdate
import com.rola.app.domain.model.LearningMaterial
import com.rola.app.domain.model.ResearchDashboardState
import com.rola.app.domain.model.ResearchTask

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ResearchDashboardScreen(
    onBack: () -> Unit,
    viewModel: ResearchDashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Research Assistant") },
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
            ResearchControls(
                uiState = uiState,
                onTopicChanged = viewModel::updateTopic,
                onAnalyzeGaps = viewModel::analyzeGaps,
                onCreateTask = viewModel::createResearchTask,
                onRunTask = viewModel::runNextPendingTask,
                onApprove = viewModel::approveFirstUpdate,
            )
            StatisticsPanel(uiState.dashboard)
            TaskQueue(uiState.dashboard.pendingTasks)
            UpdateQueue(uiState.dashboard.pendingUpdates)
            MaterialsPanel(uiState.dashboard.learningMaterials)
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun ResearchControls(
    uiState: ResearchDashboardUiState,
    onTopicChanged: (String) -> Unit,
    onAnalyzeGaps: () -> Unit,
    onCreateTask: () -> Unit,
    onRunTask: () -> Unit,
    onApprove: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            OutlinedTextField(
                value = uiState.topicInput,
                onValueChange = onTopicChanged,
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                label = { Text(text = "Research topic") },
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(onClick = onAnalyzeGaps, enabled = !uiState.loading) {
                    Icon(Icons.Rounded.Search, contentDescription = null)
                    Text(text = "Analyze")
                }
                Button(onClick = onCreateTask, enabled = !uiState.loading) {
                    Icon(Icons.Rounded.TaskAlt, contentDescription = null)
                    Text(text = "Create")
                }
                Button(onClick = onRunTask, enabled = !uiState.loading) {
                    Icon(Icons.Rounded.PlayArrow, contentDescription = null)
                    Text(text = "Run")
                }
                Button(onClick = onApprove, enabled = !uiState.loading) {
                    Icon(Icons.Rounded.CheckCircle, contentDescription = null)
                    Text(text = "Approve")
                }
            }
            if (uiState.loading) CircularProgressIndicator()
            uiState.message?.let { Text(text = it, color = MaterialTheme.colorScheme.primary) }
            uiState.errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
        }
    }
}

@Composable
@OptIn(ExperimentalLayoutApi::class)
private fun StatisticsPanel(dashboard: ResearchDashboardState) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AssistChip(onClick = {}, label = { Text(text = "Tasks ${dashboard.statistics.pendingTaskCount}") })
        AssistChip(onClick = {}, label = { Text(text = "Reviews ${dashboard.statistics.approvalQueueCount}") })
        AssistChip(onClick = {}, label = { Text(text = "Sources ${dashboard.statistics.trustedSourceCount}") })
        AssistChip(onClick = {}, label = { Text(text = "Materials ${dashboard.statistics.generatedMaterialCount}") })
        AssistChip(onClick = {}, label = { Text(text = "Applied ${dashboard.statistics.appliedUpdateCount}") })
    }
}

@Composable
private fun TaskQueue(tasks: List<ResearchTask>) {
    DashboardSection(title = "Pending Research Tasks") {
        tasks.take(5).ifEmpty {
            listOf(ResearchTask("empty", "No queued tasks", "Create or analyze a topic to begin.", com.rola.app.domain.model.ResearchPriority.Low))
        }.forEach { task ->
            Text(text = task.topic, fontWeight = FontWeight.SemiBold)
            Text(text = task.reason, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun UpdateQueue(updates: List<KnowledgeUpdate>) {
    DashboardSection(title = "Approval Queue") {
        if (updates.isEmpty()) {
            Text(text = "No generated updates are awaiting approval.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            updates.take(5).forEach { update ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = update.topic, fontWeight = FontWeight.SemiBold)
                    Text(text = "${(update.verification.reliabilityScore * 100).toInt()}% reliable")
                }
                Text(text = update.summary, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun MaterialsPanel(materials: List<LearningMaterial>) {
    DashboardSection(title = "Generated Learning Materials") {
        if (materials.isEmpty()) {
            Text(text = "Generated articles, quiz banks, and study guides will appear here.", color = MaterialTheme.colorScheme.onSurfaceVariant)
        } else {
            materials.take(5).forEach { material ->
                Text(text = material.title, fontWeight = FontWeight.SemiBold)
                Text(text = material.summary, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun DashboardSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            content()
        }
    }
}
