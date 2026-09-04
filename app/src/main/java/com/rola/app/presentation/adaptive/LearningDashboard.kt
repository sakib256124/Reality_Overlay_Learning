package com.rola.app.presentation.adaptive

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
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

@OptIn(
    androidx.compose.material3.ExperimentalMaterial3Api::class,
    androidx.compose.foundation.layout.ExperimentalLayoutApi::class,
)
@Composable
fun LearningDashboard(
    onBack: () -> Unit,
    viewModel: AdaptiveLearningViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Adaptive Learning") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = viewModel::refresh) {
                        Icon(Icons.Rounded.Refresh, contentDescription = "Refresh adaptive profile")
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
            if (uiState.status == AdaptiveStatus.Loading) {
                CircularProgressIndicator()
            }

            ProgressSummary(uiState)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(text = "Personalization", fontWeight = FontWeight.SemiBold)
                Switch(
                    checked = uiState.profile.personalizationEnabled,
                    onCheckedChange = viewModel::setPersonalizationEnabled,
                )
            }

            TopicSection(title = "Strong Areas", values = uiState.pattern.strongTopics.ifEmpty { uiState.profile.favoriteCategories })
            TopicSection(title = "Improve", values = uiState.profile.weakAreas.ifEmpty { uiState.pattern.knowledgeGaps })

            Text(text = "Recommended Next", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            uiState.recommendations.ifEmpty {
                listOf(
                    com.rola.app.domain.model.Recommendation(
                        recommendationId = "starter",
                        userId = uiState.profile.userId,
                        title = "Scan one object",
                        description = "Start with a real object so ROLA can learn your interests.",
                        topic = "Getting started",
                        type = com.rola.app.domain.model.RecommendationType.ExploreTopic,
                        priority = com.rola.app.domain.model.RecommendationPriority.Medium,
                        targetSkillLevel = uiState.profile.learningLevel,
                    ),
                )
            }.forEach { recommendation ->
                RecommendationCard(
                    recommendation = recommendation,
                    onCompleted = viewModel::markRecommendationCompleted,
                )
            }

            LearningRoadmap(uiState.roadmap)

            uiState.errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
                Button(onClick = viewModel::refresh) {
                    Text(text = "Retry")
                }
            }
        }
    }
}

@Composable
private fun ProgressSummary(uiState: AdaptiveUiState) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Text(text = "Learning Progress", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            LinearProgressIndicator(
                progress = { uiState.progressPercent / 100f },
                modifier = Modifier.fillMaxWidth(),
            )
            Text(text = "${uiState.progressPercent}%")
            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(onClick = {}, label = { Text(text = "Objects ${uiState.profile.totalObjectsLearned}") })
                AssistChip(onClick = {}, label = { Text(text = "Quiz ${uiState.profile.averageQuizScore}%") })
                AssistChip(onClick = {}, label = { Text(text = uiState.profile.learningLevel.name) })
                AssistChip(onClick = {}, label = { Text(text = "Streak ${uiState.profile.learningStreak}") })
            }
            Text(
                text = "Next: ${uiState.nextTopic}",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
            )
            Text(
                text = uiState.predictedTrend,
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Composable
@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
private fun TopicSection(
    title: String,
    values: List<String>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            values.ifEmpty { listOf("Keep learning") }.forEach { value ->
                AssistChip(onClick = {}, label = { Text(text = value) })
            }
        }
    }
}

@Composable
private fun LearningRoadmap(roadmap: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(text = "Learning Path", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
        roadmap.ifEmpty { listOf("Scan object", "Practice", "Review", "Set next goal") }.forEachIndexed { index, step ->
            Text(text = "${index + 1}. $step", color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
