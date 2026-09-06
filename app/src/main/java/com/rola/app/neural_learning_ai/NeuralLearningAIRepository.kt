package com.rola.app.neural_learning_ai

import com.rola.app.data.database.NeuralLearningAIDao
import com.rola.app.data.database.entities.NeuralLearningAdaptationHistoryEntity
import com.rola.app.data.database.entities.NeuralLearningCognitiveAnalyticsEntity
import com.rola.app.data.database.entities.NeuralLearningCognitiveModelEntity
import com.rola.app.data.database.entities.NeuralLearningKnowledgePathwayEntity
import com.rola.app.data.database.entities.NeuralLearningMemoryNetworkEntity
import com.rola.app.data.database.entities.NeuralLearningPatternEntity
import com.rola.app.data.database.entities.NeuralLearningProfileEntity
import com.rola.app.neural_learning_ai.neural_core.NeuralLearningResult
import javax.inject.Inject
import javax.inject.Singleton
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

@Singleton
class NeuralLearningAIRepository @Inject constructor(private val dao: NeuralLearningAIDao) {
    fun observeDashboard(): Flow<NeuralLearningDashboardState> =
        combine(
            dao.observeProfile(),
            dao.observeCognitiveModel(),
            dao.observePathway(),
            dao.observeMemory(),
            dao.observePattern(),
            dao.observeAnalytics(),
            dao.observeAdaptation(),
        ) { values ->
            val profile = values[0] as NeuralLearningProfileEntity?
            val model = values[1] as NeuralLearningCognitiveModelEntity?
            val pathway = values[2] as NeuralLearningKnowledgePathwayEntity?
            val memory = values[3] as NeuralLearningMemoryNetworkEntity?
            val pattern = values[4] as NeuralLearningPatternEntity?
            val analytics = values[5] as NeuralLearningCognitiveAnalyticsEntity?
            val adaptation = values[6] as NeuralLearningAdaptationHistoryEntity?
            NeuralLearningDashboardState(
                cognitiveProfile = listOfNotNull(profile?.problemSolvingStyle, "Speed ${profile?.understandingSpeed ?: 0}%", "Memory ${profile?.memoryAbility ?: 0}%"),
                knowledgeGrowth = analytics?.knowledgeGrowth ?: 0,
                cognitiveImprovement = analytics?.cognitiveImprovement ?: 0,
                learningPathways = pathway?.optimalSequence.orEmpty() + pathway?.knowledgeConnections.orEmpty(),
                memoryImprovement = memory?.memoryImprovements.orEmpty() + memory?.reinforcementPlan.orEmpty(),
                aiRecommendations = analytics?.recommendations.orEmpty() + pattern?.learningStrategies.orEmpty(),
                adaptationHistory = listOfNotNull(adaptation?.contentDifficulty, adaptation?.explanationStyle, adaptation?.learningSpeed, adaptation?.practiceFrequency),
                ethicalStatus = analytics?.ethicalStatus.orEmpty(),
                knowledgeConnections = model?.relationships.orEmpty(),
            )
        }

    suspend fun save(result: NeuralLearningResult, learnerId: String) {
        dao.upsertProfile(NeuralLearningProfileEntity(result.cognitiveProfile.profileId, learnerId, result.cognitiveProfile.behavior, result.cognitiveProfile.understandingSpeed, result.cognitiveProfile.memoryAbility, result.cognitiveProfile.problemSolvingStyle, result.cognitiveProfile.attentionPatterns, result.cognitiveProfile.privacyProtected))
        dao.upsertCognitiveModel(NeuralLearningCognitiveModelEntity(result.representation.representationId, result.representation.concepts, result.representation.relationships, result.representation.previousKnowledge, result.representation.cognitiveDifficulty.name, result.representation.learningResponse))
        dao.upsertPathway(NeuralLearningKnowledgePathwayEntity(result.pathway.pathwayId, result.pathway.optimalSequence, result.pathway.conceptDependencies, result.pathway.skillProgression, result.pathway.knowledgeConnections))
        dao.upsertMemory(NeuralLearningMemoryNetworkEntity(result.memory.memoryId, result.memory.retainedConcepts, result.memory.reinforcementPlan, result.memory.forgettingPredictions, result.memory.memoryImprovements, result.memory.lifelongMemoryIntegrated))
        dao.upsertPattern(NeuralLearningPatternEntity(result.assistant.assistantId, result.assistant.thinkingPattern, result.assistant.personalizedGuidance, result.assistant.learningStrategies, result.assistant.understandingImprovements))
        dao.upsertAnalytics(NeuralLearningCognitiveAnalyticsEntity(result.analytics.reportId, result.analytics.knowledgeGrowth, result.analytics.cognitiveImprovement, result.analytics.skillEvolution, result.analytics.recommendations, result.analytics.ethicalStatus))
        dao.upsertAdaptation(NeuralLearningAdaptationHistoryEntity(result.adaptation.adaptationId, result.adaptation.contentDifficulty, result.adaptation.explanationStyle, result.adaptation.learningSpeed, result.adaptation.practiceFrequency, result.adaptation.transparentReason))
    }
}

data class NeuralLearningDashboardState(
    val cognitiveProfile: List<String> = emptyList(),
    val knowledgeGrowth: Int = 0,
    val cognitiveImprovement: Int = 0,
    val learningPathways: List<String> = emptyList(),
    val memoryImprovement: List<String> = emptyList(),
    val aiRecommendations: List<String> = emptyList(),
    val adaptationHistory: List<String> = emptyList(),
    val ethicalStatus: String = "",
    val knowledgeConnections: List<String> = emptyList(),
)
