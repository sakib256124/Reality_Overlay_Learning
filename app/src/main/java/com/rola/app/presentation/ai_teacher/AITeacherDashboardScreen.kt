package com.rola.app.presentation.ai_teacher

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.rounded.CloudDone
import androidx.compose.material.icons.rounded.LibraryAdd
import androidx.compose.material.icons.rounded.MenuBook
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.School
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rola.app.domain.model.SkillLevel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AITeacherDashboardScreen(
    onBack: () -> Unit,
    viewModel: AITeacherDashboardViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()
    val dashboard = uiState.dashboard
    var subject by remember { mutableStateOf("Science") }
    var topic by remember { mutableStateOf("Energy") }
    var grade by remember { mutableStateOf("Grade 7") }
    var objective by remember { mutableStateOf("Explain energy transfer using AR observations.") }
    var durationText by remember { mutableStateOf("4") }
    var selectedLevel by remember { mutableIntStateOf(1) }
    val levels = listOf(SkillLevel.Beginner, SkillLevel.Intermediate, SkillLevel.Advanced)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "AI Teacher") },
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
            if (uiState.message.isNotBlank()) {
                Text(
                    text = uiState.message,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary,
                )
            }

            FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(
                    onClick = {},
                    leadingIcon = { Icon(Icons.Rounded.School, contentDescription = null) },
                    label = { Text(text = "Plans ${dashboard?.curriculumPlans?.size ?: 0}") },
                )
                AssistChip(
                    onClick = {},
                    leadingIcon = { Icon(Icons.Rounded.Psychology, contentDescription = null) },
                    label = { Text(text = "Lessons ${dashboard?.generatedLessons?.size ?: 0}") },
                )
                AssistChip(
                    onClick = {},
                    leadingIcon = { Icon(Icons.Rounded.CheckCircle, contentDescription = null) },
                    label = { Text(text = "Approvals ${dashboard?.pendingApprovals?.size ?: 0}") },
                )
                AssistChip(
                    onClick = {},
                    leadingIcon = { Icon(Icons.Rounded.Quiz, contentDescription = null) },
                    label = { Text(text = "Signals ${dashboard?.lessonAnalytics?.size ?: 0}") },
                )
            }

            AITeacherCard(title = "Generate Curriculum") {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = subject,
                            onValueChange = { subject = it },
                            label = { Text("Subject") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                        )
                        OutlinedTextField(
                            value = grade,
                            onValueChange = { grade = it },
                            label = { Text("Grade") },
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                        )
                    }
                    OutlinedTextField(
                        value = topic,
                        onValueChange = { topic = it },
                        label = { Text("Topic") },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                    )
                    OutlinedTextField(
                        value = objective,
                        onValueChange = { objective = it },
                        label = { Text("Learning objective") },
                        modifier = Modifier.fillMaxWidth(),
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = durationText,
                            onValueChange = { durationText = it.filter { char -> char.isDigit() }.take(1) },
                            label = { Text("Weeks") },
                            modifier = Modifier.weight(0.35f),
                            singleLine = true,
                        )
                        SingleChoiceSegmentedButtonRow(modifier = Modifier.weight(1f)) {
                            levels.forEachIndexed { index, level ->
                                SegmentedButton(
                                    selected = selectedLevel == index,
                                    onClick = { selectedLevel = index },
                                    shape = SegmentedButtonDefaults.itemShape(index = index, count = levels.size),
                                ) {
                                    Text(level.name)
                                }
                            }
                        }
                    }
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(
                            enabled = !uiState.isGenerating,
                            onClick = {
                                viewModel.generateStarterCurriculum(
                                    subject = subject,
                                    topic = topic,
                                    gradeLevel = grade,
                                    objective = objective,
                                    durationWeeks = durationText.toIntOrNull() ?: 4,
                                    level = levels[selectedLevel],
                                )
                            },
                        ) {
                            Icon(Icons.Rounded.LibraryAdd, contentDescription = null)
                            Text("Create Draft")
                        }
                        TextButton(onClick = viewModel::generateTeachingPlanPreview, enabled = !uiState.isGenerating) {
                            Icon(Icons.Rounded.MenuBook, contentDescription = null)
                            Text("Teaching Plan")
                        }
                        TextButton(onClick = viewModel::generateAdaptivePlan, enabled = !uiState.isGenerating) {
                            Icon(Icons.Rounded.Psychology, contentDescription = null)
                            Text("Adapt")
                        }
                    }
                }
            }

            AITeacherCard(
                title = "AI Generated Lessons",
                body = dashboard?.generatedLessons
                    ?.joinToString("\n") { "${it.title} - ${it.difficulty.name} - ${it.practiceQuestions.size} checks" }
                    .orEmpty()
                    .ifBlank { "Generated lessons will appear after a teacher creates a curriculum plan." },
            )
            AITeacherCard(
                title = "Curriculum Plans",
                body = dashboard?.curriculumPlans
                    ?.joinToString("\n") {
                        "${it.title} - ${it.approvalStatus.name} - quality ${(it.quality.objectiveCoverage * 100).toInt()}%"
                    }
                    .orEmpty()
                    .ifBlank { "Draft, review, approved, and distributed plans appear here." },
            )
            AITeacherCard(title = "Review Queue") {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = dashboard?.pendingApprovals
                            ?.joinToString("\n") { "${it.title} - ${it.modules.size} modules awaiting review" }
                            .orEmpty()
                            .ifBlank { "No pending AI generated content." },
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = { viewModel.approveFirstPending(distributeToStudents = false) }) {
                            Icon(Icons.Rounded.CheckCircle, contentDescription = null)
                            Text("Approve")
                        }
                        TextButton(onClick = { viewModel.approveFirstPending(distributeToStudents = true) }) {
                            Icon(Icons.Rounded.CloudDone, contentDescription = null)
                            Text("Distribute")
                        }
                    }
                }
            }
            AITeacherCard(
                title = "Student Insights",
                body = dashboard?.studentInsights
                    ?.joinToString("\n")
                    .orEmpty()
                    .ifBlank { "Adaptive insights appear after class activity and assessment data are available." },
            )
            AITeacherCard(
                title = "Suggested Improvements",
                body = dashboard?.suggestedImprovements
                    ?.joinToString("\n")
                    .orEmpty()
                    .ifBlank { "Lesson effectiveness suggestions appear after analytics are collected." },
            )
        }
    }
}

@Composable
private fun AITeacherCard(
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

@Composable
private fun AITeacherCard(
    title: String,
    content: @Composable () -> Unit,
) {
    Card(modifier = Modifier.fillMaxWidth(), shape = MaterialTheme.shapes.small) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
            Text(text = title, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold)
            content()
        }
    }
}
