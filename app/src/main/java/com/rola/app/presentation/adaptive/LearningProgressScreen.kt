package com.rola.app.presentation.adaptive

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun LearningProgressScreen(
    onBack: () -> Unit,
    viewModel: AdaptiveLearningViewModel = hiltViewModel(),
) {
    LearningDashboard(onBack = onBack, viewModel = viewModel)
}
