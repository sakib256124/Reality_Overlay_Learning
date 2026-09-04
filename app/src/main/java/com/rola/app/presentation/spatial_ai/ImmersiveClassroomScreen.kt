package com.rola.app.presentation.spatial_ai

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
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.Groups
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.ViewInAr
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
fun ImmersiveClassroomScreen(
    onBack: () -> Unit,
    viewModel: ImmersiveClassroomViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val world = uiState.activeWorld

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Immersive Classroom") },
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
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.Public, contentDescription = null) }, label = { Text("Worlds ${uiState.summary?.worldCount ?: 0}") })
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.ViewInAr, contentDescription = null) }, label = { Text("Twins ${uiState.summary?.twinCount ?: 0}") })
                AssistChip(onClick = {}, leadingIcon = { Icon(Icons.Rounded.Groups, contentDescription = null) }, label = { Text("Sessions ${uiState.summary?.activeSessionCount ?: 0}") })
            }

            SpatialCard("AI Generated Learning World") {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    OutlinedTextField(
                        value = uiState.topic,
                        onValueChange = viewModel::updateTopic,
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        label = { Text("Topic") },
                    )
                    OutlinedTextField(
                        value = uiState.objective,
                        onValueChange = viewModel::updateObjective,
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Learning objective") },
                    )
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = viewModel::generateWorld, enabled = !uiState.loading) {
                            Icon(Icons.Rounded.AutoAwesome, contentDescription = null)
                            Text("Generate World")
                        }
                        TextButton(onClick = viewModel::enterClassroom, enabled = !uiState.loading) {
                            Icon(Icons.Rounded.ViewInAr, contentDescription = null)
                            Text("Enter")
                        }
                    }
                    if (uiState.loading) CircularProgressIndicator()
                    uiState.message?.let { Text(text = it, color = MaterialTheme.colorScheme.primary) }
                    uiState.errorMessage?.let { Text(text = it, color = MaterialTheme.colorScheme.error) }
                }
            }

            world?.let {
                SpatialCard("Virtual Classroom") {
                    Text(text = "${it.classroom.title}\n${it.classroom.teacherPresence}", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                SpatialCard("3D Learning Objects") {
                    Text(text = it.objects.joinToString("\n") { obj -> "${obj.name} - ${obj.interactions.size} interactions" }, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                SpatialCard("Interactive Activities") {
                    Text(text = it.activities.joinToString("\n") { activity -> "${activity.title} - ${activity.estimatedMinutes} min" }, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                SpatialCard("Simulations") {
                    Text(text = it.simulations.joinToString("\n") { simulation -> "${simulation.title} - ${simulation.simulationType.name}" }, color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
                SpatialCard("AI Teacher Guidance") {
                    Text(text = it.aiTeacherGuidance.joinToString("\n"), color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            uiState.activeSession?.let {
                SpatialCard("Learning Progress") {
                    Text(text = "${it.status.name} - ${it.progressPercent}%", color = MaterialTheme.colorScheme.onSurfaceVariant)
                }
            }

            SpatialCard("XR Readiness") {
                Text(
                    text = uiState.xrReadiness.ifBlank { "Generate a world to inspect XR capability." },
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
    }
}

@Composable
private fun SpatialCard(
    title: String,
    content: @Composable () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            content()
        }
    }
}
