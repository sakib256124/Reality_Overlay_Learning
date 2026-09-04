package com.rola.app.presentation.overlay

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun ARInformationOverlay(
    uiState: OverlayUiState,
    onPanelTapped: () -> Unit,
    onClose: () -> Unit,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val transform = uiState.screenTransform
    val density = LocalDensity.current
    val panelWidthPx = with(density) { if (uiState.isWearableOptimized) 260.dp.toPx() else 300.dp.toPx() }
    val panelHeightGuessPx = with(density) {
        when {
            uiState.isWearableOptimized -> 180.dp.toPx()
            uiState.isExpanded -> 360.dp.toPx()
            else -> 220.dp.toPx()
        }
    }
    val fallbackX = with(density) { 24.dp.toPx() }
    val fallbackY = with(density) { 132.dp.toPx() }
    val animatedScale by animateFloatAsState(
        targetValue = transform?.scale ?: 0.92f,
        animationSpec = spring(dampingRatio = 0.78f, stiffness = 360f),
        label = "ar-overlay-scale",
    )
    val animatedAlpha by animateFloatAsState(
        targetValue = if (transform?.isVisible == true) 1f else 0f,
        label = "ar-overlay-alpha",
    )

    Box(
        modifier = modifier.fillMaxSize(),
    ) {
        if (uiState.isVisible && transform != null) {
            ObjectInfoCard(
                uiState = uiState,
                onTap = onPanelTapped,
                onClose = onClose,
                onLanguageSelected = onLanguageSelected,
                modifier = Modifier
                    .graphicsLayer {
                        translationX = transform.x - (panelWidthPx / 2f)
                        translationY = transform.y - panelHeightGuessPx
                        scaleX = animatedScale
                        scaleY = animatedScale
                    }
                    .alpha(animatedAlpha),
            )
        } else if (uiState.isVisible) {
            ObjectInfoCard(
                uiState = uiState,
                onTap = onPanelTapped,
                onClose = onClose,
                onLanguageSelected = onLanguageSelected,
                modifier = Modifier
                    .graphicsLayer {
                        translationX = fallbackX
                        translationY = fallbackY
                    },
            )
        }
    }
}
