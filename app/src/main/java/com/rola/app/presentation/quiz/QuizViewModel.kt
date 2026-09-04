package com.rola.app.presentation.quiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.data.adaptive.UserProfileRepository
import com.rola.app.data.quiz.QuizRepository
import com.rola.app.data.translation.TranslationRepository
import com.rola.app.domain.model.Answer
import com.rola.app.domain.model.Quiz
import com.rola.app.domain.model.QuizDifficulty
import com.rola.app.domain.model.SkillLevel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val quizRepository: QuizRepository,
    private val translationRepository: TranslationRepository,
    private val userProfileRepository: UserProfileRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val objectId: String = savedStateHandle["objectId"] ?: ""
    private val _uiState = MutableStateFlow(QuizUiState())
    val uiState: StateFlow<QuizUiState> = _uiState.asStateFlow()

    init {
        observeLanguages()
        observeSelectedLanguage()
        loadAdaptiveQuiz()
        observeProgress()
    }

    fun onDifficultySelected(difficulty: QuizDifficulty) {
        _uiState.update { it.copy(selectedDifficulty = difficulty) }
        loadQuiz(difficulty)
    }

    fun selectAnswer(answer: Answer) {
        val question = _uiState.value.currentQuestion ?: return
        if (_uiState.value.selectedAnswers.containsKey(question.questionId)) return

        _uiState.update {
            it.copy(
                status = QuizStatus.Answered,
                selectedAnswers = it.selectedAnswers + (question.questionId to answer),
            )
        }
    }

    fun nextQuestion() {
        val state = _uiState.value
        if (state.currentQuestionIndex < state.questionCount - 1) {
            _uiState.update {
                it.copy(
                    status = QuizStatus.Ready,
                    currentQuestionIndex = it.currentQuestionIndex + 1,
                )
            }
        } else {
            finishQuiz()
        }
    }

    fun retry() {
        loadQuiz(_uiState.value.selectedDifficulty)
    }

    fun onLanguageSelected(languageCode: String) {
        translationRepository.setSelectedLanguage(languageCode)
    }

    private fun loadQuiz(difficulty: QuizDifficulty = _uiState.value.selectedDifficulty) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    status = QuizStatus.Loading,
                    selectedDifficulty = difficulty,
                    currentQuestionIndex = 0,
                    selectedAnswers = emptyMap(),
                    quizResult = null,
                    startedAt = System.currentTimeMillis(),
                    errorMessage = null,
                )
            }
            runCatching { quizRepository.loadQuiz(objectId, difficulty) }
                .onSuccess { quiz ->
                    _uiState.update {
                        it.copy(
                            status = QuizStatus.Ready,
                            baseQuiz = quiz,
                            quiz = quiz,
                            errorMessage = null,
                        )
                    }
                    translateQuizForSelectedLanguage()
                }
                .onFailure { throwable ->
                    _uiState.update {
                        it.copy(
                            status = QuizStatus.Error,
                            errorMessage = throwable.message ?: "Unable to load quiz.",
                        )
                    }
                }
        }
    }

    private fun loadAdaptiveQuiz() {
        viewModelScope.launch {
            val difficulty = runCatching {
                when (userProfileRepository.adaptiveQuizSkillLevel()) {
                    SkillLevel.Beginner -> QuizDifficulty.Easy
                    SkillLevel.Intermediate -> QuizDifficulty.Medium
                    SkillLevel.Advanced -> QuizDifficulty.Hard
                }
            }.getOrDefault(QuizDifficulty.Easy)
            loadQuiz(difficulty)
        }
    }

    private fun finishQuiz() {
        val state = _uiState.value
        val quiz: Quiz = state.quiz ?: return
        viewModelScope.launch {
            runCatching {
                quizRepository.saveResult(
                    quiz = quiz,
                    score = state.score,
                    completionTime = System.currentTimeMillis() - state.startedAt,
                )
            }.onSuccess { result ->
                _uiState.update {
                    it.copy(
                        status = QuizStatus.Complete,
                        quizResult = result,
                        errorMessage = null,
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        status = QuizStatus.Error,
                        errorMessage = throwable.message ?: "Unable to save quiz result.",
                    )
                }
            }
        }
    }

    private fun observeProgress() {
        viewModelScope.launch {
            quizRepository.observeProgress().collect { progress ->
                _uiState.update { it.copy(progress = progress) }
            }
        }
    }

    private fun observeLanguages() {
        viewModelScope.launch {
            runCatching { translationRepository.ensureLanguagesSeeded() }
            translationRepository.observeLanguages().collect { languages ->
                _uiState.update { it.copy(languages = languages) }
            }
        }
    }

    private fun observeSelectedLanguage() {
        viewModelScope.launch {
            translationRepository.observeSelectedLanguageCode().collect { languageCode ->
                _uiState.update { it.copy(selectedLanguageCode = languageCode) }
                translateQuizForSelectedLanguage()
            }
        }
    }

    private fun translateQuizForSelectedLanguage() {
        val baseQuiz = _uiState.value.baseQuiz ?: return
        viewModelScope.launch {
            _uiState.update { it.copy(isTranslating = true, errorMessage = null) }
            runCatching {
                translationRepository.translateQuiz(
                    quiz = baseQuiz,
                    targetLanguage = _uiState.value.selectedLanguageCode,
                )
            }.onSuccess { translatedQuiz ->
                _uiState.update {
                    it.copy(
                        quiz = translatedQuiz,
                        isTranslating = false,
                        errorMessage = null,
                    )
                }
            }.onFailure { throwable ->
                _uiState.update {
                    it.copy(
                        quiz = baseQuiz,
                        isTranslating = false,
                        errorMessage = throwable.message ?: "Unable to translate quiz.",
                    )
                }
            }
        }
    }
}
