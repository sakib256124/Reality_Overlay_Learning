package com.rola.app.presentation.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Search
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LoginScreen(onContinue: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = "ROLA Login", style = MaterialTheme.typography.headlineLarge)
        Text(
            text = "Authentication will connect here in the Firebase phase.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        )
        Button(onClick = onContinue, modifier = Modifier.padding(top = 20.dp)) {
            Text(text = "Continue")
        }
    }
}

@Composable
fun ObjectDetailScreen(
    objectId: String,
    onBack: () -> Unit,
    onQuiz: (String) -> Unit = {},
    onTutor: (String) -> Unit = {},
    onView3D: (String) -> Unit = {},
    onTranslate: (String) -> Unit = {},
    viewModel: ObjectDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = uiState.objectModel?.name ?: "Object Detail") },
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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            when {
                uiState.isLoading -> CircularProgressIndicator()
                uiState.objectModel != null -> {
                    val model = uiState.objectModel
                    Text(text = model.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                    Text(text = model.scientificName, color = MaterialTheme.colorScheme.primary)
                    Text(text = model.category, style = MaterialTheme.typography.titleMedium)
                    Text(text = model.description, style = MaterialTheme.typography.bodyLarge)
                    DetailSection(title = "Uses", values = model.uses)
                    DetailSection(title = "Facts", values = model.facts)
                    FlowRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(onClick = { onQuiz(model.objectId) }) {
                            Text(text = "Take Quiz")
                        }
                        Button(onClick = { onTutor(model.objectId) }) {
                            Text(text = "Ask Tutor")
                        }
                        Button(onClick = { onView3D(model.objectId) }) {
                            Text(text = "View 3D")
                        }
                        Button(onClick = { onTranslate(model.objectId) }) {
                            Text(text = "Translate")
                        }
                    }
                }
                else -> Text(
                    text = uiState.errorMessage ?: "Selected object: $objectId",
                    color = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}

@Composable
fun HistoryScreen(onBack: () -> Unit) {
    PlaceholderScreen(
        title = "Learning History",
        body = "Scanned objects and completed lessons will appear here.",
        onBack = onBack,
    )
}

@Composable
fun ProfileScreen(onBack: () -> Unit) {
    PlaceholderScreen(
        title = "Profile",
        body = "Learner profile, secure session settings, achievements, and sync status are connected through the dashboard and history modules.",
        onBack = onBack,
    )
}

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun SearchScreen(
    onBack: () -> Unit,
    onObjectSelected: (String) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Search Objects") },
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
                .padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
        ) {
            OutlinedTextField(
                value = uiState.query,
                onValueChange = viewModel::onQueryChanged,
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = { Icon(Icons.Rounded.Search, contentDescription = null) },
                label = { Text(text = "Search by name, category, or science") },
                singleLine = true,
            )
            uiState.errorMessage?.let {
                Text(text = it, color = MaterialTheme.colorScheme.error)
            }
            if (uiState.isLoading) {
                CircularProgressIndicator()
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(uiState.results, key = { it.objectId }) { model ->
                        Card(
                            onClick = { onObjectSelected(model.objectId) },
                            shape = MaterialTheme.shapes.small,
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(14.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = model.name, fontWeight = FontWeight.SemiBold)
                                    Text(
                                        text = model.category,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
private fun DetailSection(
    title: String,
    values: List<String>,
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = title, fontWeight = FontWeight.SemiBold)
        FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            values.ifEmpty { listOf("Unavailable") }.forEach { value ->
                AssistChip(onClick = {}, label = { Text(text = value) })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlaceholderScreen(
    title: String,
    body: String,
    onBack: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = title) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "Back",
                        )
                    }
                },
            )
        },
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = body, style = MaterialTheme.typography.bodyLarge)
        }
    }
}
