package com.rola.app.presentation.quiz

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Check
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.rola.app.domain.model.Answer

@Composable
fun AnswerOption(
    answer: Answer,
    isSelected: Boolean,
    isCorrect: Boolean,
    showFeedback: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val borderColor = when {
        showFeedback && isCorrect -> Color(0xFF2E7D32)
        showFeedback && isSelected -> MaterialTheme.colorScheme.error
        else -> MaterialTheme.colorScheme.outline
    }

    OutlinedButton(
        onClick = onClick,
        enabled = !showFeedback,
        modifier = modifier.fillMaxWidth(),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
    ) {
        Text(
            text = answer.text,
            modifier = Modifier
                .weight(1f)
                .padding(vertical = 6.dp),
            textAlign = TextAlign.Start,
        )
        if (showFeedback && (isCorrect || isSelected)) {
            Icon(
                imageVector = if (isCorrect) Icons.Rounded.Check else Icons.Rounded.Close,
                contentDescription = null,
                tint = borderColor,
            )
        }
    }
}
