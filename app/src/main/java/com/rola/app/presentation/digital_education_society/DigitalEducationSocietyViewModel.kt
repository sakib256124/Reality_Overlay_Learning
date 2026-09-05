package com.rola.app.presentation.digital_education_society

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rola.app.di.DefaultDispatcher
import com.rola.app.di.IoDispatcher
import com.rola.app.digital_education_society.DigitalEducationSocietyDashboardState
import com.rola.app.digital_education_society.DigitalEducationSocietyRepository
import com.rola.app.digital_education_society.civilization_core.DigitalEducationCivilizationEngine
import com.rola.app.digital_education_society.civilization_core.GlobalEducationChallenge
import com.rola.app.digital_education_society.civilization_core.SocietyParticipantType
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@HiltViewModel
class DigitalEducationSocietyViewModel @Inject constructor(
    private val engine: DigitalEducationCivilizationEngine,
    private val repository: DigitalEducationSocietyRepository,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val institutionId: String = savedStateHandle["institutionId"] ?: "local-institution"
    private val learnerId: String = savedStateHandle["learnerId"] ?: "global-learner-$institutionId"
    private val localState = MutableStateFlow(DigitalEducationSocietyUiState())

    val uiState = combine(
        localState,
        repository.observeDashboard(institutionId, learnerId),
    ) { state, dashboard -> state.copy(dashboard = dashboard) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = DigitalEducationSocietyUiState(),
        )

    fun runCivilizationCycle() {
        viewModelScope.launch {
            localState.update { it.copy(loading = true, message = "Coordinating global education society...", errorMessage = null) }
            runCatching {
                val challenge = GlobalEducationChallenge(
                    challengeId = "digital-society-${UUID.randomUUID()}",
                    institutionId = institutionId,
                    region = "Global South learning network",
                    topic = "Climate Science",
                    knowledgeNeed = "Localized experiments and multilingual explanations",
                    participants = listOf(
                        SocietyParticipantType.School,
                        SocietyParticipantType.University,
                        SocietyParticipantType.Teacher,
                        SocietyParticipantType.Student,
                        SocietyParticipantType.Researcher,
                        SocietyParticipantType.EducationalRobot,
                    ),
                    learningTrends = listOf("AR science labs", "AI-assisted tutoring", "community research projects"),
                    resourceNeeds = listOf("offline lessons", "simulation labs", "teacher-reviewed AR activities"),
                    languages = listOf("English", "Bangla"),
                )
                val result = withContext(defaultDispatcher) { engine.buildCivilization(challenge) }
                withContext(ioDispatcher) { repository.saveResult(result) }
                result.governancePolicy.decision.name
            }.onSuccess { decision ->
                localState.update { it.copy(loading = false, message = "Digital education society saved as $decision.") }
            }.onFailure { error ->
                localState.update {
                    it.copy(
                        loading = false,
                        message = null,
                        errorMessage = error.message ?: "Digital education society cycle failed.",
                    )
                }
            }
        }
    }
}

data class DigitalEducationSocietyUiState(
    val loading: Boolean = false,
    val message: String? = null,
    val errorMessage: String? = null,
    val dashboard: DigitalEducationSocietyDashboardState = DigitalEducationSocietyDashboardState(),
)

