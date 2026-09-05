package com.rola.app.presentation.ai_metaverse

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
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.ViewInAr
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
fun MetaverseDashboardScreen(
    onBack: () -> Unit,
    viewModel: MetaverseDashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val dashboard = uiState.dashboard

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Metaverse") },
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
            Button(onClick = viewModel::createLearningUniverse, enabled = !uiState.loading) {
                Icon(Icons.Rounded.ViewInAr, contentDescription = null)
                Text("Create Learning Universe")
            }
            if (uiState.loading) CircularProgressIndicator()
            uiState.message?.let { Text(text = it, color = MaterialTheme.colorScheme.primary) }
            uiState.errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.Public, contentDescription = null) }, label = { Text(if (dashboard.worldTitle.isBlank()) "No world" else "World ready") })
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.School, contentDescription = null) }, label = { Text("Spaces ${dashboard.worldSpaces.size}") })
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.Groups, contentDescription = null) }, label = { Text("Communities ${dashboard.communitySpaces.size}") })
            }

            MetaverseMetricCard("Engagement", dashboard.engagementScore)
            MetaverseCard("Virtual World", dashboard.worldTitle.ifBlank { "Create a learning universe to generate the persistent virtual campus." })
            MetaverseCard("Digital Spaces", dashboard.worldSpaces.ifEmpty { listOf("Classrooms, labs, research zones, and assessment spaces appear here.") }.joinToString("\n"))
            MetaverseCard("Learning Avatar", dashboard.avatarSummary.ifBlank { "Avatar profile appears after universe creation." })
            MetaverseCard("Virtual Classrooms", dashboard.classroomSummaries.ifEmpty { listOf("Classroom sessions appear after world creation.") }.joinToString("\n"))
            MetaverseCard("Communities", dashboard.communitySpaces.ifEmpty { listOf("Community learning spaces appear after collaboration setup.") }.joinToString("\n"))
            MetaverseCard("AI World Intelligence", dashboard.performanceSummary.ifBlank { "World controller feedback appears after interaction analysis." })
            MetaverseCard("Governance", dashboard.governanceDecision.ifBlank { "Metaverse permissions and identity checks appear after review." })
        }
    }
}

@Composable
private fun MetaverseMetricCard(title: String, value: Int) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            LinearProgressIndicator(progress = { value / 100f }, modifier = Modifier.fillMaxWidth())
            Text(text = "$value%", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun MetaverseCard(title: String, body: String) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            Text(text = body, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
