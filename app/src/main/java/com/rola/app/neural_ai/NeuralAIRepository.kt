package com.rola.app.neural_ai

import com.rola.app.data.database.NeuralAIDao
import com.rola.app.data.database.entities.AttentionRecordEntity
import com.rola.app.data.database.entities.BrainSignalEntity
import com.rola.app.data.database.entities.CognitiveReportEntity
import com.rola.app.data.database.entities.NeuralCognitiveStateEntity
import com.rola.app.data.database.entities.NeuralInteractionEntity
import com.rola.app.data.database.entities.NeuralLearningPredictionEntity
import com.rola.app.data.database.entities.NeuralLearningStateEntity
import com.rola.app.data.database.entities.NeuralProfileEntity
import com.rola.app.neural_ai.cognitive_signal.NeuralSignalSample
import com.rola.app.neural_ai.interaction.NeuralEducationResponse
import com.rola.app.neural_ai.interaction.NeuralInteractionAction
import com.rola.app.neural_ai.learning_state.CognitiveLoadLevel
import com.rola.app.neural_ai.learning_state.CognitiveState
import com.rola.app.neural_ai.learning_state.FocusLevel
import com.rola.app.neural_ai.learning_state.LearningState
import com.rola.app.neural_ai.learning_state.NeuralLearningProfile
import com.rola.app.neural_ai.learning_state.TeachingMethod
import com.rola.app.neural_ai.learning_state.UnderstandingLevel
import com.rola.app.neural_ai.prediction.NeuralLearningPrediction
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

@Singleton
class NeuralAIRepository @Inject constructor(
    private val neuralAIDao: NeuralAIDao,
) {
    fun observeDashboard(userId: String): Flow<NeuralAIDashboardState> =
        combine(
            neuralAIDao.observeProfile(userId).map { it?.toDomain() },
            neuralAIDao.observeLatestCognitiveState(userId).map { it?.toDomain() },
            neuralAIDao.observeLatestLearningState(userId).map { it?.toDomain() },
            neuralAIDao.observeLatestPrediction(userId).map { it?.toDomain() },
            neuralAIDao.observeRecentInteractions(userId),
            neuralAIDao.observeReports(userId),
        ) { values ->
            @Suppress("UNCHECKED_CAST")
            NeuralAIDashboardState(
                profile = values[0] as NeuralLearningProfile?,
                cognitiveState = values[1] as CognitiveState?,
                learningState = values[2] as LearningState?,
                prediction = values[3] as NeuralLearningPrediction?,
                interactions = (values[4] as List<NeuralInteractionEntity>).map { it.toSummary() },
                reports = (values[5] as List<CognitiveReportEntity>).map { it.summary },
            )
        }

    suspend fun saveSignal(sample: NeuralSignalSample) {
        neuralAIDao.upsertBrainSignal(sample.sanitized().toEntity())
    }

    suspend fun saveResponse(response: NeuralEducationResponse, profile: NeuralLearningProfile) {
        neuralAIDao.upsertProfile(profile.toEntity())
        neuralAIDao.upsertCognitiveState(response.cognitiveState.toEntity())
        neuralAIDao.upsertLearningState(
            NeuralLearningStateEntity(
                stateId = "learning-state-${response.responseId}",
                userId = response.userId,
                activeTopic = response.topic,
                recommendedDifficulty = response.optimization.lessonDifficulty,
                recommendedMethod = response.recommendedTeachingMethod().name,
                supportRequired = response.actions.any { it.name.contains("Simplify") || it.name.contains("Practice") },
                adaptationReason = response.optimization.explainableReason,
                updatedAt = response.timestamp,
            ),
        )
        neuralAIDao.upsertInteraction(response.toInteractionEntity())
        neuralAIDao.upsertAttentionRecord(response.toAttentionRecordEntity())
        neuralAIDao.upsertPrediction(response.prediction.toEntity())
        neuralAIDao.upsertReport(response.toReportEntity())
    }
}

data class NeuralAIDashboardState(
    val profile: NeuralLearningProfile? = null,
    val cognitiveState: CognitiveState? = null,
    val learningState: LearningState? = null,
    val prediction: NeuralLearningPrediction? = null,
    val interactions: List<String> = emptyList(),
    val reports: List<String> = emptyList(),
)

private fun NeuralLearningProfile.toEntity(): NeuralProfileEntity =
    NeuralProfileEntity(
        profileId,
        userId,
        cognitivePatterns,
        attentionBehavior,
        memoryResponse,
        learningSpeed,
        preferredTeachingMethod.name,
        knowledgeDevelopment,
        consentGranted,
        updatedAt,
    )

private fun NeuralProfileEntity.toDomain(): NeuralLearningProfile =
    NeuralLearningProfile(
        profileId = profileId,
        userId = userId,
        cognitivePatterns = cognitivePatterns,
        attentionBehavior = attentionBehavior,
        memoryResponse = memoryResponse,
        learningSpeed = learningSpeed,
        preferredTeachingMethod = runCatching { TeachingMethod.valueOf(preferredTeachingMethod) }.getOrDefault(TeachingMethod.Visual),
        knowledgeDevelopment = knowledgeDevelopment,
        consentGranted = consentGranted,
        updatedAt = updatedAt,
    )

private fun NeuralSignalSample.toEntity(): BrainSignalEntity =
    BrainSignalEntity(
        signalId,
        userId,
        sessionId,
        attentionScore,
        engagementScore,
        mentalWorkloadScore,
        fatigueScore,
        signalQuality.name,
        timestamp,
    )

private fun NeuralCognitiveStateEntity.toDomain(): CognitiveState =
    CognitiveState(
        stateId = stateId,
        userId = userId,
        topic = topic,
        attentionPercent = attentionPercent,
        engagementPercent = engagementPercent,
        focusLevel = runCatching { FocusLevel.valueOf(focusLevel) }.getOrDefault(FocusLevel.Moderate),
        cognitiveLoadLevel = runCatching { CognitiveLoadLevel.valueOf(cognitiveLoadLevel) }.getOrDefault(CognitiveLoadLevel.Balanced),
        understandingLevel = runCatching { UnderstandingLevel.valueOf(understandingLevel) }.getOrDefault(UnderstandingLevel.Developing),
        mentalFatiguePercent = mentalFatiguePercent,
        explanation = explanation,
        timestamp = timestamp,
    )

private fun CognitiveState.toEntity(): NeuralCognitiveStateEntity =
    NeuralCognitiveStateEntity(
        stateId,
        userId,
        topic,
        attentionPercent,
        engagementPercent,
        focusLevel.name,
        cognitiveLoadLevel.name,
        understandingLevel.name,
        mentalFatiguePercent,
        explanation,
        timestamp,
    )

private fun NeuralLearningStateEntity.toDomain(): LearningState =
    LearningState(
        stateId = stateId,
        userId = userId,
        activeTopic = activeTopic,
        recommendedDifficulty = recommendedDifficulty,
        recommendedMethod = runCatching { TeachingMethod.valueOf(recommendedMethod) }.getOrDefault(TeachingMethod.Interactive),
        supportRequired = supportRequired,
        adaptationReason = adaptationReason,
        updatedAt = updatedAt,
    )

private fun NeuralLearningPredictionEntity.toDomain(): NeuralLearningPrediction =
    NeuralLearningPrediction(
        predictionId,
        userId,
        topic,
        learningSuccessPercent,
        requiredSupport,
        skillDevelopment,
        knowledgeRetentionPercent,
        longTermRoadmap,
        explanation,
    )

private fun NeuralLearningPrediction.toEntity(): NeuralLearningPredictionEntity =
    NeuralLearningPredictionEntity(
        predictionId,
        userId,
        topic,
        learningSuccessPercent,
        requiredSupport,
        skillDevelopment,
        knowledgeRetentionPercent,
        longTermRoadmap,
        explanation,
    )

private fun NeuralEducationResponse.toInteractionEntity(): NeuralInteractionEntity =
    NeuralInteractionEntity(
        responseId,
        userId,
        topic,
        actions.map { it.name },
        teacherPrompt,
        explainableReason,
        timestamp,
    )

private fun NeuralEducationResponse.recommendedTeachingMethod(): TeachingMethod =
    when {
        NeuralInteractionAction.SimplifyExplanation in actions -> TeachingMethod.SimplifiedExplanation
        NeuralInteractionAction.StartPractice in actions -> TeachingMethod.PracticeBased
        NeuralInteractionAction.AddVisualExample in actions -> TeachingMethod.Visual
        else -> TeachingMethod.Interactive
    }

private fun NeuralInteractionEntity.toSummary(): String =
    "$topic: ${actions.joinToString()} - $teacherPrompt"

private fun NeuralEducationResponse.toAttentionRecordEntity(): AttentionRecordEntity =
    AttentionRecordEntity(
        recordId = "attention-${UUID.randomUUID()}",
        userId = userId,
        topic = topic,
        attentionPercent = cognitiveState.attentionPercent,
        engagementPercent = cognitiveState.engagementPercent,
        focusLevel = cognitiveState.focusLevel.name,
        timestamp = timestamp,
    )

private fun NeuralEducationResponse.toReportEntity(): CognitiveReportEntity =
    CognitiveReportEntity(
        reportId = "cognitive-report-${UUID.randomUUID()}",
        userId = userId,
        summary = explainableReason,
        recommendations = actions.map { it.name },
        privacyMode = "Local educational analysis",
        createdAt = timestamp,
    )
