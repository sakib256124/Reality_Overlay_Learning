package com.rola.app.presentation.enterprise

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
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
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
fun TeacherDashboardScreen(
    onBack: () -> Unit,
    viewModel: TeacherDashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val dashboard = uiState.dashboard

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Teacher Dashboard") },
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
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, label = { Text(text = "Classes ${dashboard?.classes?.size ?: 0}") })
                AssistChip(onClick = {}, label = { Text(text = "Assignments ${dashboard?.assignments?.size ?: 0}") })
                AssistChip(onClick = {}, label = { Text(text = "Reviews ${dashboard?.pendingSubmissions?.size ?: 0}") })
                AssistChip(onClick = {}, label = { Text(text = "Materials ${dashboard?.suggestedMaterials?.size ?: 0}") })
            }
            EnterpriseCard("Classes", dashboard?.classes?.joinToString("\n") { it.name }.orEmpty().ifBlank { "Create a class to begin classroom learning." })
            EnterpriseCard("Assignments", dashboard?.assignments?.joinToString("\n") { it.title }.orEmpty().ifBlank { "AR scanning tasks, quizzes, research tasks, and challenges appear here." })
            EnterpriseCard("Class Analytics", dashboard?.analytics?.joinToString("\n") { "${it.completionRate}% completion, ${it.averageQuizScore}% quiz average" }.orEmpty().ifBlank { "Class progress and weak areas appear after student activity." })
            EnterpriseCard("Suggested Materials", dashboard?.suggestedMaterials?.joinToString("\n") { it.title }.orEmpty().ifBlank { "AI-suggested materials appear after research content is available." })
        }
    }
}

@Composable
private fun EnterpriseCard(
    title: String,
    body: String,
) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(text = body, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
