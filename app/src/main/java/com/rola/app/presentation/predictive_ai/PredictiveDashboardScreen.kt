package com.rola.app.presentation.predictive_ai

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Psychology
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
fun PredictiveDashboardScreen(onBack: () -> Unit, viewModel: PredictiveDashboardViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val d = uiState.dashboard
    Scaffold(topBar = { TopAppBar(title = { Text("Predictive AI") }, navigationIcon = { IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back") } }) }) { padding ->
        Column(Modifier.fillMaxSize().padding(padding).verticalScroll(rememberScrollState()).padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            Button(onClick = viewModel::runPrediction, enabled = !uiState.loading) { Icon(Icons.Rounded.Psychology, contentDescription = null); Text("Predict Future") }
            if (uiState.loading) CircularProgressIndicator()
            uiState.message?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
            uiState.errorMessage?.let { Text(it, color = MaterialTheme.colorScheme.error) }
            Metric("Prediction Confidence", d.confidenceScore)
            Metric("Growth Score", d.growthScore)
            Info("Learning Forecast", d.learningForecast.ifBlank { "Run prediction to generate a future learning model." })
            Info("Potential Analysis", d.potential.ifBlank { "Potential profile appears here." })
            Info("Future Skills", d.futureSkills.ifEmpty { listOf("Future skill prediction appears here.") }.joinToString("\n"))
            Info("Career Roadmap", d.careerRoadmap.ifEmpty { listOf("Career roadmap appears here.") }.joinToString("\n"))
            Info("Growth Recommendations", d.growthRecommendations.ifEmpty { listOf("Optimization recommendations appear here.") }.joinToString("\n"))
        }
    }
}

@Composable private fun Metric(title: String, value: Int) { Card(Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text(title, fontWeight = FontWeight.SemiBold); LinearProgressIndicator(progress = { value / 100f }, modifier = Modifier.fillMaxWidth()); Text("$value%", color = MaterialTheme.colorScheme.onSurfaceVariant) } } }
@Composable private fun Info(title: String, body: String) { Card(Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) { Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) { Text(title, fontWeight = FontWeight.SemiBold); Text(body, color = MaterialTheme.colorScheme.onSurfaceVariant) } } }
