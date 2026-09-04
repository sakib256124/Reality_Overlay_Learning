package com.rola.app.presentation.quiz

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.AssistChip
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rola.app.domain.model.QuizDifficulty
import com.rola.app.presentation.translation.LanguageSelector

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.layout.ExperimentalLayoutApi::class)
@Composable
fun QuizScreen(
    onBack: () -> Unit,
    viewModel: QuizViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = uiState.quiz?.title ?: "Object Quiz") },
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
        when (uiState.status) {
            QuizStatus.Loading -> LoadingQuiz(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )
            QuizStatus.Error -> ErrorQuiz(
                message = uiState.errorMessage.orEmpty(),
                onRetry = viewModel::retry,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
            )
            QuizStatus.Complete -> uiState.quizResult?.let { result ->
                ResultScreen(
                    result = result,
                    progress = uiState.progress,
                    onRetry = viewModel::retry,
                    modifier = Modifier.padding(paddingValues),
                )
            }
            QuizStatus.Ready,
            QuizStatus.Answered,
            -> QuizContent(
                uiState = uiState,
                onDifficultySelected = viewModel::onDifficultySelected,
                onLanguageSelected = viewModel::onLanguageSelected,
                onAnswerSelected = viewModel::selectAnswer,
                onNext = viewModel::nextQuestion,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(20.dp),
            )
        }
    }
}

@Composable
private fun QuizContent(
    uiState: QuizUiState,
    onDifficultySelected: (QuizDifficulty) -> Unit,
    onLanguageSelected: (String) -> Unit,
    onAnswerSelected: (com.rola.app.domain.model.Answer) -> Unit,
    onNext: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val question = uiState.currentQuestion ?: return
    val selectedAnswer = uiState.selectedAnswer
    val showFeedback = selectedAnswer != null

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        Text(
            text = uiState.quiz?.title.orEmpty(),
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
        )
        DifficultySelector(
            selectedDifficulty = uiState.selectedDifficulty,
            onDifficultySelected = onDifficultySelected,
        )
        LanguageSelector(
            languages = uiState.languages,
            selectedLanguageCode = uiState.selectedLanguageCode,
            onLanguageSelected = onLanguageSelected,
        )
        if (uiState.isTranslating) {
            Text(
                text = "Translating quiz...",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
        QuestionCard(
            question = question,
            questionNumber = uiState.currentQuestionIndex + 1,
            totalQuestions = uiState.questionCount,
        )
        question.options.forEach { answer ->
            AnswerOption(
                answer = answer,
                isSelected = selectedAnswer?.answerId == answer.answerId,
                isCorrect = answer.answerId == question.correctAnswer.answerId,
                showFeedback = showFeedback,
                onClick = { onAnswerSelected(answer) },
            )
        }
        if (showFeedback) {
            Text(
                text = if (selectedAnswer?.answerId == question.correctAnswer.answerId) "Correct" else "Incorrect",
                color = if (selectedAnswer?.answerId == question.correctAnswer.answerId) {
                    ColorCorrect
                } else {
                    MaterialTheme.colorScheme.error
                },
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
            )
            Text(
                text = question.explanation,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
            )
            Button(
                onClick = onNext,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(text = if (uiState.currentQuestionIndex == uiState.questionCount - 1) "Finish Quiz" else "Next Question")
            }
        }
    }
}

@Composable
private fun DifficultySelector(
    selectedDifficulty: QuizDifficulty,
    onDifficultySelected: (QuizDifficulty) -> Unit,
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        QuizDifficulty.entries.forEach { difficulty ->
            AssistChip(
                onClick = { onDifficultySelected(difficulty) },
                label = { Text(text = difficulty.name) },
                leadingIcon = if (difficulty == selectedDifficulty) {
                    { Text(text = "*") }
                } else {
                    null
                },
            )
        }
    }
}

@Composable
private fun LoadingQuiz(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CircularProgressIndicator()
        Text(
            text = "Generating quiz...",
            modifier = Modifier.padding(top = 16.dp),
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}

@Composable
private fun ErrorQuiz(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = message.ifBlank { "Quiz generation failed." },
            color = MaterialTheme.colorScheme.error,
            style = MaterialTheme.typography.bodyLarge,
        )
        Button(
            onClick = onRetry,
            modifier = Modifier.padding(top = 16.dp),
        ) {
            Text(text = "Retry")
        }
    }
}

private val ColorCorrect = androidx.compose.ui.graphics.Color(0xFF2E7D32)
