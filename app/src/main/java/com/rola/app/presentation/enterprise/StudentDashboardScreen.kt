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
fun StudentDashboardScreen(
    onBack: () -> Unit,
    viewModel: StudentDashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val dashboard = uiState.dashboard
    val report = dashboard?.report

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Student Dashboard") },
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
                AssistChip(onClick = {}, label = { Text(text = "Progress ${report?.progressPercent ?: 0}%") })
                AssistChip(onClick = {}, label = { Text(text = "Lessons ${report?.completedLessons ?: 0}") })
                AssistChip(onClick = {}, label = { Text(text = "Quiz ${report?.averageQuizScore ?: 0}%") })
                AssistChip(onClick = {}, label = { Text(text = "Assignments ${dashboard?.assignments?.size ?: 0}") })
            }
            EnterpriseCard("Assigned Activities", dashboard?.assignments?.joinToString("\n") { it.title }.orEmpty().ifBlank { "Assigned AR activities and quizzes appear here." })
            EnterpriseCard("Achievements", dashboard?.achievements?.joinToString().orEmpty().ifBlank { "Achievements appear after lessons and quizzes." })
            EnterpriseCard("Recommendations", dashboard?.recommendations?.joinToString("\n").orEmpty().ifBlank { "Personal recommendations appear after classroom activity." })
        }
    }
}
