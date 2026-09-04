package com.rola.app.presentation.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.rola.app.domain.model.LearningProgress
import com.rola.app.domain.model.QuizResult

@OptIn(androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun ResultScreen(
    result: QuizResult,
    progress: LearningProgress,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "Score: ${result.score}/${result.totalQuestions}",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )
        Text(
            text = "Accuracy: ${result.percentage}%",
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )
        Text(
            text = recommendationFor(result.percentage),
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyLarge,
        )

        Surface(
            color = MaterialTheme.colorScheme.surfaceContainer,
            shape = MaterialTheme.shapes.small,
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(text = "Points", fontWeight = FontWeight.SemiBold)
                    Text(text = progress.totalPoints.toString())
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(text = "Level", fontWeight = FontWeight.SemiBold)
                    Text(text = progress.currentLevel.toString())
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(text = "Streak", fontWeight = FontWeight.SemiBold)
                    Text(text = "${progress.learningStreak} days")
                }
                Text(text = "Badges", fontWeight = FontWeight.SemiBold)
                FlowRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    progress.badges.ifEmpty { listOf("Keep Learning") }.forEach { badge ->
                        AssistChip(onClick = {}, label = { Text(text = badge) })
                    }
                }
            }
        }

        Button(onClick = onRetry) {
            Text(text = "Try Again")
        }
    }
}

private fun recommendationFor(percentage: Int): String = when {
    percentage >= 90 -> "Excellent work. Try a harder quiz next."
    percentage >= 70 -> "Good progress. Review one or two scientific details."
    else -> "Learning recommendation: Review material properties and interesting facts."
}
