package com.rola.app.presentation.cognitive_ai

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
import androidx.compose.material.icons.rounded.AutoGraph
import androidx.compose.material.icons.rounded.Lightbulb
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.Route
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
fun CognitiveDashboardScreen(
    onBack: () -> Unit,
    viewModel: CognitiveDashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val dashboard = uiState.dashboard

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Cognitive Intelligence") },
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
            Button(onClick = viewModel::runCognitiveAnalysis, enabled = !uiState.loading) {
                Icon(Icons.Rounded.Psychology, contentDescription = null)
                Text("Run Cognitive Analysis")
            }
            if (uiState.loading) CircularProgressIndicator()
            uiState.message?.let { Text(text = it, color = MaterialTheme.colorScheme.primary) }
            uiState.errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.AutoGraph, contentDescription = null) }, label = { Text("Score ${dashboard?.intelligenceScore ?: 0}") })
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.Lightbulb, contentDescription = null) }, label = { Text("Strengths ${dashboard?.strengthAreas?.size ?: 0}") })
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.Route, contentDescription = null) }, label = { Text("Predictions ${dashboard?.futurePredictions?.size ?: 0}") })
            }

            CognitiveCard(
                title = "Skill Map",
                body = dashboard?.skillMap
                    ?.joinToString("\n") { "${it.name} - ${it.mastery}% - ${it.growthTrend.name}" }
                    .orEmpty()
                    .ifBlank { "Run cognitive analysis to build the learner skill map." },
            )
            CognitiveCard(
                title = "Knowledge Growth",
                body = dashboard?.knowledgeGrowth?.joinToString("\n").orEmpty().ifBlank { "Growth signals will appear after learning activity." },
            )
            CognitiveCard(
                title = "Strength Areas",
                body = dashboard?.strengthAreas?.joinToString("\n").orEmpty().ifBlank { "No stable strengths detected yet." },
            )
            CognitiveCard(
                title = "Weak Areas",
                body = dashboard?.weakAreas?.joinToString("\n").orEmpty().ifBlank { "No cognitive weak areas detected yet." },
            )
            CognitiveCard(
                title = "Future Predictions",
                body = dashboard?.futurePredictions?.joinToString("\n").orEmpty().ifBlank { "Future roadmap appears after analysis." },
            )
            CognitiveCard(
                title = "Personalized Recommendations",
                body = dashboard?.personalizedRecommendations
                    ?.joinToString("\n") { "${it.nextLesson}: ${it.rationale}" }
                    .orEmpty()
                    .ifBlank { "Personalized recommendations will appear here." },
            )
            if (uiState.latestDecision.isNotBlank()) {
                CognitiveCard("Explainable AI Decision", uiState.latestDecision)
            }
        }
    }
}

@Composable
private fun CognitiveCard(
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
