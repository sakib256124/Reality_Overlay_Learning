package com.rola.app.presentation.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccountCircle
import androidx.compose.material.icons.rounded.AutoAwesome
import androidx.compose.material.icons.rounded.AdminPanelSettings
import androidx.compose.material.icons.rounded.CameraAlt
import androidx.compose.material.icons.rounded.CloudQueue
import androidx.compose.material.icons.rounded.Explore
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.History
import androidx.compose.material.icons.rounded.Hub
import androidx.compose.material.icons.rounded.Memory
import androidx.compose.material.icons.rounded.Psychology
import androidx.compose.material.icons.rounded.Public
import androidx.compose.material.icons.rounded.Quiz
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Science
import androidx.compose.material.icons.rounded.SettingsSuggest
import androidx.compose.material.icons.rounded.Tune
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material.icons.rounded.ViewInAr
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rola.app.presentation.components.LearningActionCard
import com.rola.app.presentation.viewmodel.HomeViewModel
import kotlinx.coroutines.delay
import java.text.DateFormat
import java.util.Date

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun HomeScreen(
    onStartArLearning: () -> Unit,
    onAdvancedVision: () -> Unit = {},
    onHistory: () -> Unit,
    onExploreObjects: () -> Unit,
    onTakeQuiz: () -> Unit = onHistory,
    onTutor: () -> Unit = {},
    onView3D: () -> Unit = onExploreObjects,
    onTranslate: () -> Unit = {},
    onWearable: () -> Unit = {},
    onAdaptiveLearning: () -> Unit = {},
    onNeuralLearning: () -> Unit = {},
    onAGINetwork: () -> Unit = {},
    onQuantumAI: () -> Unit = {},
    onASICore: () -> Unit = {},
    onDigitalEducationSociety: () -> Unit = {},
    onAIMetaverse: () -> Unit = {},
    onAIInfrastructure: () -> Unit = {},
    onAIEducationOS: () -> Unit = {},
    onDigitalCompanion: () -> Unit = {},
    onCollectiveAI: () -> Unit = {},
    onEducationSingularity: () -> Unit = {},
    onAICivilization: () -> Unit = {},
    onLifelongMemory: () -> Unit = {},
    onPredictiveAI: () -> Unit = {},
    onEmotionalAI: () -> Unit = {},
    onCreativeAI: () -> Unit = {},
    onAIResearchScientist: () -> Unit = {},
    onKnowledgeEngineering: () -> Unit = {},
    onReasoningAI: () -> Unit = {},
    onPlanningAI: () -> Unit = {},
    onMasteryAI: () -> Unit = {},
    onPersonalAgent: () -> Unit = {},
    onEducationOrchestration: () -> Unit = {},
    onSelfEvolvingAI: () -> Unit = {},
    onProfile: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    var showCards by remember { mutableStateOf(false) }
    val dashboardState by viewModel.dashboardState.collectAsState()

    LaunchedEffect(Unit) {
        delay(220)
        showCards = true
    }

    Scaffold { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            item {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text(
                        text = "ROLA Dashboard",
                        style = MaterialTheme.typography.headlineLarge,
                    )
                    Text(
                        text = "Scan real objects, hear explanations, review progress, and strengthen learning with quizzes.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }

            item {
                DashboardStats(
                    totalQuizzes = dashboardState.progress.totalQuizzesCompleted,
                    averageScore = dashboardState.progress.averageScore,
                    streak = dashboardState.progress.learningStreak,
                    level = dashboardState.progress.currentLevel,
                )
            }

            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Start AR Learning",
                        description = "Open the scanner and identify real-world objects.",
                        icon = Icons.Rounded.CameraAlt,
                        onClick = onStartArLearning,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Scene Understanding",
                        description = "Detect multiple objects and learn from their environment.",
                        icon = Icons.Rounded.Hub,
                        onClick = onAdvancedVision,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Search Object",
                        description = "Find saved knowledge and object details.",
                        icon = Icons.Rounded.Explore,
                        onClick = onExploreObjects,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Learning History",
                        description = "Review recent scans and saved learning moments.",
                        icon = Icons.Rounded.History,
                        onClick = onHistory,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Take Quiz",
                        description = "Practice with quizzes from recently learned objects.",
                        icon = Icons.Rounded.Quiz,
                        onClick = onTakeQuiz,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "AI Tutor",
                        description = "Ask grounded questions about objects and quiz prep.",
                        icon = Icons.Rounded.Psychology,
                        onClick = onTutor,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "3D Visualization",
                        description = "Open an object and place its interactive model in AR.",
                        icon = Icons.Rounded.ViewInAr,
                        onClick = onView3D,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Translate Learning",
                        description = "Read and hear object knowledge in multiple languages.",
                        icon = Icons.Rounded.Translate,
                        onClick = onTranslate,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Wearable XR",
                        description = "Prepare hands-free learning for AR glasses and XR devices.",
                        icon = Icons.Rounded.ViewInAr,
                        onClick = onWearable,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Personal Learning",
                        description = "View adaptive recommendations, weak areas, and next goals.",
                        icon = Icons.Rounded.School,
                        onClick = onAdaptiveLearning,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Neural Learning",
                        description = "Analyze future cognitive signals for adaptive education.",
                        icon = Icons.Rounded.Psychology,
                        onClick = onNeuralLearning,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "AGI Network",
                        description = "Coordinate supervised AI agents for platform improvement.",
                        icon = Icons.Rounded.Hub,
                        onClick = onAGINetwork,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Quantum AI",
                        description = "Optimize learning paths with quantum-inspired intelligence.",
                        icon = Icons.Rounded.Tune,
                        onClick = onQuantumAI,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "ASI Education",
                        description = "Review supervised advanced intelligence learning strategies.",
                        icon = Icons.Rounded.AdminPanelSettings,
                        onClick = onASICore,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Education Society",
                        description = "Coordinate global AI knowledge communities and governance.",
                        icon = Icons.Rounded.Public,
                        onClick = onDigitalEducationSociety,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "AI Metaverse",
                        description = "Enter persistent virtual classrooms, labs, and learning worlds.",
                        icon = Icons.Rounded.ViewInAr,
                        onClick = onAIMetaverse,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "AI Infrastructure",
                        description = "Optimize distributed cloud, edge, service mesh, and model systems.",
                        icon = Icons.Rounded.CloudQueue,
                        onClick = onAIInfrastructure,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "AI Education OS",
                        description = "Run the unified kernel for services, agents, workflows, memory, and security.",
                        icon = Icons.Rounded.SettingsSuggest,
                        onClick = onAIEducationOS,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "AI Companion",
                        description = "Open the learner's persistent personal education companion.",
                        icon = Icons.Rounded.AutoAwesome,
                        onClick = onDigitalCompanion,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Collective AI",
                        description = "Coordinate multi-agent debate, knowledge exchange, and consensus decisions.",
                        icon = Icons.Rounded.Hub,
                        onClick = onCollectiveAI,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Education Singularity",
                        description = "Fuse all ROLA intelligence into a universal learning system.",
                        icon = Icons.Rounded.AutoAwesome,
                        onClick = onEducationSingularity,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "AI Civilization",
                        description = "Evolve global knowledge, learning systems, innovations, and future plans.",
                        icon = Icons.Rounded.Public,
                        onClick = onAICivilization,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Lifelong Memory",
                        description = "Build a user-owned lifelong knowledge graph, timeline, and mentor plan.",
                        icon = Icons.Rounded.Memory,
                        onClick = onLifelongMemory,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Predictive AI",
                        description = "Forecast future skills, hidden potential, learning paths, and growth strategy.",
                        icon = Icons.Rounded.Psychology,
                        onClick = onPredictiveAI,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Emotional AI",
                        description = "Adapt learning with motivation, confidence, engagement, and support signals.",
                        icon = Icons.Rounded.Favorite,
                        onClick = onEmotionalAI,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Creative AI",
                        description = "Generate lessons, simulations, research ideas, and human-approved innovations.",
                        icon = Icons.Rounded.AutoAwesome,
                        onClick = onCreativeAI,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "AI Research Scientist",
                        description = "Discover gaps, generate hypotheses, design experiments, and validate knowledge.",
                        icon = Icons.Rounded.Science,
                        onClick = onAIResearchScientist,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Knowledge Engineering",
                        description = "Extract, validate, reason over, and deliver structured educational knowledge.",
                        icon = Icons.Rounded.Hub,
                        onClick = onKnowledgeEngineering,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Reasoning AI",
                        description = "Solve complex problems with inference, logic, explanation, and critical thinking.",
                        icon = Icons.Rounded.Psychology,
                        onClick = onReasoningAI,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Planning AI",
                        description = "Generate goals, schedules, optimized strategies, and adaptive learning roadmaps.",
                        icon = Icons.Rounded.Tune,
                        onClick = onPlanningAI,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Mastery AI",
                        description = "Measure skill mastery, detect gaps, adapt practice, and prove competency.",
                        icon = Icons.Rounded.School,
                        onClick = onMasteryAI,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Personal Agent",
                        description = "Unify teaching, planning, mentoring, memory, research, and lifelong guidance.",
                        icon = Icons.Rounded.AutoAwesome,
                        onClick = onPersonalAgent,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Education Orchestration",
                        description = "Coordinate AI services, workflows, decisions, quality, and governance.",
                        icon = Icons.Rounded.Hub,
                        onClick = onEducationOrchestration,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Self-Evolving AI",
                        description = "Analyze performance, learn from feedback, evolve models, and improve ROLA.",
                        icon = Icons.Rounded.AutoAwesome,
                        onClick = onSelfEvolvingAI,
                    )
                }
            }
            item {
                AnimatedVisibility(visible = showCards) {
                    LearningActionCard(
                        title = "Profile",
                        description = "Manage learner details and progress preferences.",
                        icon = Icons.Rounded.AccountCircle,
                        onClick = onProfile,
                    )
                }
            }

            if (dashboardState.recentHistory.isNotEmpty()) {
                item {
                    Text(
                        text = "Recently Learned",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                }
                dashboardState.recentHistory.forEach { scan ->
                    item {
                        Card(
                            shape = MaterialTheme.shapes.small,
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceContainer,
                            ),
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(4.dp),
                            ) {
                                Text(text = scan.objectName, fontWeight = FontWeight.SemiBold)
                                Text(
                                    text = DateFormat.getDateInstance(DateFormat.MEDIUM)
                                        .format(Date(scan.timestamp)),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    style = MaterialTheme.typography.bodyMedium,
                                )
                            }
                        }
                    }
                }
            }

            if (dashboardState.progress.badges.isNotEmpty()) {
                item {
                    Text(
                        text = "Achievements",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.SemiBold,
                    )
                    FlowRow(
                        modifier = Modifier.padding(top = 8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                    ) {
                        dashboardState.progress.badges.forEach { badge ->
                            AssistChip(onClick = {}, label = { Text(text = badge) })
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DashboardStats(
    totalQuizzes: Int,
    averageScore: Int,
    streak: Int,
    level: Int,
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
    ) {
        StatCard(label = "Quizzes", value = totalQuizzes.toString(), modifier = Modifier.weight(1f))
        StatCard(label = "Accuracy", value = "$averageScore%", modifier = Modifier.weight(1f))
        StatCard(label = "Streak", value = streak.toString(), modifier = Modifier.weight(1f))
        StatCard(label = "Level", value = level.toString(), modifier = Modifier.weight(1f))
    }
}

@Composable
private fun StatCard(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(text = value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Text(text = label, style = MaterialTheme.typography.labelMedium)
        }
    }
}
