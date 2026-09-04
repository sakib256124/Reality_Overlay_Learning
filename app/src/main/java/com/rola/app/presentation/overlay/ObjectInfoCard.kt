package com.rola.app.presentation.overlay

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.ExpandLess
import androidx.compose.material.icons.rounded.ExpandMore
import androidx.compose.material.icons.rounded.Translate
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.rola.app.domain.model.ObjectInformation
import com.rola.app.presentation.translation.LanguageSelector

@Composable
fun ObjectInfoCard(
    uiState: OverlayUiState,
    onTap: () -> Unit,
    onClose: () -> Unit,
    onLanguageSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    AnimatedVisibility(
        visible = uiState.isVisible,
        enter = fadeIn() + scaleIn(initialScale = 0.88f),
        exit = fadeOut() + scaleOut(targetScale = 0.92f),
        modifier = modifier,
    ) {
        Surface(
            modifier = Modifier
                .widthIn(
                    min = if (uiState.isWearableOptimized) 220.dp else 240.dp,
                    max = if (uiState.isWearableOptimized) 280.dp else 320.dp,
                )
                .clip(RoundedCornerShape(8.dp))
                .clickable(onClick = onTap),
            color = Color.Black.copy(alpha = 0.68f),
            contentColor = Color.White,
            shape = RoundedCornerShape(8.dp),
            tonalElevation = 6.dp,
            shadowElevation = 6.dp,
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White.copy(alpha = 0.06f))
                    .padding(14.dp),
            ) {
                when (uiState.status) {
                    OverlayStatus.Loading -> LoadingContent()
                    OverlayStatus.Error,
                    OverlayStatus.TrackingLost,
                    -> MessageContent(uiState.errorMessage.orEmpty())
                    OverlayStatus.Visible -> uiState.objectInformation?.let { information ->
                        InformationContent(
                            information = information,
                            uiState = uiState,
                            isExpanded = uiState.isExpanded,
                            onClose = onClose,
                            onLanguageSelected = onLanguageSelected,
                        )
                    }
                    OverlayStatus.Hidden -> Unit
                }
            }
        }
    }
}

@Composable
private fun InformationContent(
    information: ObjectInformation,
    uiState: OverlayUiState,
    isExpanded: Boolean,
    onClose: () -> Unit,
    onLanguageSelected: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top,
        ) {
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                Text(
                    text = information.name,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = information.scientificName,
                    color = Color.White.copy(alpha = 0.78f),
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = if (isExpanded) 2 else 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
            IconButton(onClick = onClose) {
                Icon(imageVector = Icons.Rounded.Close, contentDescription = "Close overlay")
            }
        }

        if (!uiState.isWearableOptimized || isExpanded) {
            LanguageSelector(
                languages = uiState.languages,
                selectedLanguageCode = uiState.selectedLanguageCode,
                onLanguageSelected = onLanguageSelected,
                modifier = Modifier.fillMaxWidth(),
            )
        }
        if (uiState.isTranslationLoading) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.padding(2.dp))
                Icon(
                    imageVector = Icons.Rounded.Translate,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.72f),
                )
                Text(
                    text = "Translating...",
                    color = Color.White.copy(alpha = 0.86f),
                    style = MaterialTheme.typography.bodySmall,
                )
            }
        }

        if (!uiState.isWearableOptimized || isExpanded) {
            InfoSection(title = "Category", body = information.category)
        }
        InfoSection(
            title = "Description",
            body = information.description,
            maxLines = if (uiState.isWearableOptimized && !isExpanded) 1 else if (isExpanded) 4 else 2,
        )

        if (isExpanded) {
            InfoSection(title = "Common Uses", body = information.uses.joinToString(separator = "\n"))
            InfoSection(title = "Interesting Facts", body = information.facts.joinToString(separator = "\n"))
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = if (isExpanded) Icons.Rounded.ExpandLess else Icons.Rounded.ExpandMore,
                contentDescription = null,
                tint = Color.White.copy(alpha = 0.86f),
            )
        }
    }
}

@Composable
private fun LoadingContent() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircularProgressIndicator(color = Color.White)
        Text(text = "Loading object information...", style = MaterialTheme.typography.bodyLarge)
    }
}

@Composable
private fun MessageContent(message: String) {
    Text(
        text = message.ifBlank { "Overlay unavailable." },
        color = Color(0xFFFFD6D1),
        style = MaterialTheme.typography.bodyLarge,
    )
}

@Composable
private fun InfoSection(
    title: String,
    body: String,
    maxLines: Int = Int.MAX_VALUE,
) {
    Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
        Text(
            text = title,
            color = Color(0xFF8DE0B8),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = body.ifBlank { "Unavailable" },
            color = Color.White.copy(alpha = 0.9f),
            style = MaterialTheme.typography.bodyMedium,
            maxLines = maxLines,
            overflow = TextOverflow.Ellipsis,
        )
    }
}
