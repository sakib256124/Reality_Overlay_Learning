package com.rola.app.presentation.visualization

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Animation
import androidx.compose.material.icons.rounded.CenterFocusStrong
import androidx.compose.material.icons.rounded.Highlight
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.OpenWith
import androidx.compose.material.icons.rounded.RestartAlt
import androidx.compose.material.icons.rounded.Rotate90DegreesCcw
import androidx.compose.material.icons.rounded.ZoomIn
import androidx.compose.material.icons.rounded.ZoomOut
import androidx.compose.material3.AssistChip
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

@Composable
fun ModelControlPanel(
    uiState: VisualizationUiState,
    onPlace: () -> Unit,
    onRotate: () -> Unit,
    onZoomIn: () -> Unit,
    onZoomOut: () -> Unit,
    onReset: () -> Unit,
    onAnimate: () -> Unit,
    onExplode: () -> Unit,
    onHighlight: () -> Unit,
    onInfo: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = Color.Black.copy(alpha = 0.66f),
        contentColor = Color.White,
        shape = RoundedCornerShape(8.dp),
        tonalElevation = 6.dp,
        shadowElevation = 6.dp,
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp),
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = uiState.modelAsset?.model?.modelName ?: "3D Model",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = if (uiState.isTracking) "AR tracking active" else "Move slowly to find a surface",
                    color = Color.White.copy(alpha = 0.76f),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                FilledTonalButton(
                    onClick = onPlace,
                    enabled = uiState.hasModel && uiState.isTracking,
                ) {
                    Icon(Icons.Rounded.CenterFocusStrong, contentDescription = null)
                    Text(text = "Place")
                }
                IconButton(onClick = onRotate, enabled = uiState.hasModel) {
                    Icon(Icons.Rounded.Rotate90DegreesCcw, contentDescription = "Rotate model")
                }
                IconButton(onClick = onZoomIn, enabled = uiState.hasModel) {
                    Icon(Icons.Rounded.ZoomIn, contentDescription = "Zoom in")
                }
                IconButton(onClick = onZoomOut, enabled = uiState.hasModel) {
                    Icon(Icons.Rounded.ZoomOut, contentDescription = "Zoom out")
                }
                IconButton(onClick = onReset, enabled = uiState.hasModel) {
                    Icon(Icons.Rounded.RestartAlt, contentDescription = "Reset model")
                }
            }

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                AssistChip(
                    onClick = onAnimate,
                    label = { Text(text = "Animate") },
                    leadingIcon = { Icon(Icons.Rounded.Animation, contentDescription = null) },
                )
                AssistChip(
                    onClick = onExplode,
                    label = { Text(text = "Explode") },
                    leadingIcon = { Icon(Icons.Rounded.OpenWith, contentDescription = null) },
                )
                AssistChip(
                    onClick = onHighlight,
                    label = { Text(text = "Highlight") },
                    leadingIcon = { Icon(Icons.Rounded.Highlight, contentDescription = null) },
                )
                AssistChip(
                    onClick = onInfo,
                    label = { Text(text = "Info") },
                    leadingIcon = { Icon(Icons.Rounded.Info, contentDescription = null) },
                )
            }

            uiState.errorMessage?.let { message ->
                Text(
                    text = message,
                    color = Color(0xFFFFD6D1),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
