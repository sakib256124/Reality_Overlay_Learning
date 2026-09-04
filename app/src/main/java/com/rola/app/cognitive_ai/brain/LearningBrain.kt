package com.rola.app.cognitive_ai.brain

import com.rola.app.cognitive_ai.behavior.BehaviorAnalyzer
import com.rola.app.cognitive_ai.decision.CognitivePrivacyGuard
import com.rola.app.cognitive_ai.decision.EducationalDecisionEngine
import com.rola.app.cognitive_ai.emotion.EmotionLearningAnalyzer
import com.rola.app.cognitive_ai.memory.AdaptiveMemoryManager
import com.rola.app.cognitive_ai.memory.LearnerModelManager
import com.rola.app.cognitive_ai.memory.MemorySystem
import com.rola.app.cognitive_ai.personalization.CognitiveMentorAgent
import com.rola.app.cognitive_ai.personalization.PersonalizationEngine
import com.rola.app.cognitive_ai.prediction.PredictionEngine
import com.rola.app.cognitive_ai.reasoning.ReasoningEngine
import com.rola.app.domain.model.CognitiveActivityType
import com.rola.app.domain.model.CognitiveDashboardState
import com.rola.app.domain.model.CognitiveLearningActivity
import com.rola.app.domain.model.CognitivePermission
import com.rola.app.domain.model.CognitiveSecurityContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LearningBrain @Inject constructor(
    private val learnerModelManager: LearnerModelManager,
    private val adaptiveMemoryManager: AdaptiveMemoryManager,
    private val memorySystem: MemorySystem,
    private val behaviorAnalyzer: BehaviorAnalyzer,
    private val emotionLearningAnalyzer: EmotionLearningAnalyzer,
    private val predictionEngine: PredictionEngine,
    private val personalizationEngine: PersonalizationEngine,
    private val cognitiveMentorAgent: CognitiveMentorAgent,
    private val educationalDecisionEngine: EducationalDecisionEngine,
    private val reasoningEngine: ReasoningEngine,
    private val privacyGuard: CognitivePrivacyGuard,
) {
    fun analyze(
        userId: String,
        activities: List<CognitiveLearningActivity>,
        securityContext: CognitiveSecurityContext,
    ): CognitiveLearningResult {
        privacyGuard.requirePermission(securityContext, CognitivePermission.AnalyzeLearner)
        require(privacyGuard.canAnalyze(securityContext)) { "Cognitive analysis requires learner consent." }
        val profile = learnerModelManager.buildProfile(userId, activities, securityContext.consent)
        val memory = adaptiveMemoryManager.buildMemoryRecords(activities)
        val behavior = behaviorAnalyzer.analyze(activities)
        val emotion = emotionLearningAnalyzer.analyze(activities)
        val prediction = predictionEngine.futureRoadmap(profile)
        val recommendation = personalizationEngine.recommendation(profile)
        val mentorPlan = cognitiveMentorAgent.mentor(profile, prediction)
        val decision = educationalDecisionEngine.decide(profile, recommendation)
        val topic = recommendation.nextLesson
        return CognitiveLearningResult(
            profile = profile,
            memoryEncoding = memorySystem.encode(memory),
            knownConcepts = memorySystem.knownConcepts(memory),
            strugglingConcepts = memorySystem.strugglingConcepts(memory),
            behaviorReport = behavior,
            emotionReport = emotion,
            prediction = prediction,
            recommendation = recommendation,
            mentorPlan = mentorPlan,
            decision = decision,
            reasoning = reasoningEngine.conceptConnections(topic, profile) + decision.explanation,
            processingMode = privacyGuard.processingMode(securityContext),
        )
    }

    fun dashboard(result: CognitiveLearningResult): CognitiveDashboardState =
        CognitiveDashboardState(
            profile = result.profile,
            intelligenceScore = result.profile.intelligenceScore,
            skillMap = result.profile.skillDevelopment,
            knowledgeGrowth = result.knownConcepts.map { "$it is becoming stable knowledge." },
            strengthAreas = result.profile.knowledgeStrengths,
            weakAreas = result.profile.knowledgeWeaknesses,
            futurePredictions = listOf(result.prediction.skillImprovement) + result.prediction.requiredLearningPath,
            personalizedRecommendations = listOf(result.recommendation),
        )

    fun sampleActivities(userId: String): List<CognitiveLearningActivity> = listOf(
        CognitiveLearningActivity("activity-1", userId, CognitiveActivityType.ARObjectExploration, "Electricity", 420_000, 82),
        CognitiveLearningActivity("activity-2", userId, CognitiveActivityType.Quiz, "Voltage", 180_000, 48, "Confused voltage with current"),
        CognitiveLearningActivity("activity-3", userId, CognitiveActivityType.TutorConversation, "Voltage", 240_000, 62),
        CognitiveLearningActivity("activity-4", userId, CognitiveActivityType.Simulation, "Circuit", 360_000, 76),
    )
}

data class CognitiveLearningResult(
    val profile: com.rola.app.domain.model.LearnerCognitiveProfile,
    val memoryEncoding: String,
    val knownConcepts: List<String>,
    val strugglingConcepts: List<String>,
    val behaviorReport: com.rola.app.domain.model.LearningBehaviorReport,
    val emotionReport: com.rola.app.domain.model.EmotionLearningReport,
    val prediction: com.rola.app.domain.model.LearningPrediction,
    val recommendation: com.rola.app.domain.model.CognitiveRecommendation,
    val mentorPlan: com.rola.app.domain.model.PersonalLearningPlan,
    val decision: com.rola.app.domain.model.CognitiveDecision,
    val reasoning: List<String>,
    val processingMode: String,
)
