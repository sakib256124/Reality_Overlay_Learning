package com.rola.app.unit

import com.rola.app.cognitive_ai.behavior.BehaviorAnalyzer
import com.rola.app.cognitive_ai.decision.CognitivePrivacyGuard
import com.rola.app.cognitive_ai.decision.EducationalDecisionEngine
import com.rola.app.cognitive_ai.emotion.EmotionLearningAnalyzer
import com.rola.app.cognitive_ai.memory.AdaptiveMemoryManager
import com.rola.app.cognitive_ai.memory.LearnerModelManager
import com.rola.app.cognitive_ai.personalization.PersonalizationEngine
import com.rola.app.cognitive_ai.prediction.LearningPredictionEngine
import com.rola.app.cognitive_ai.reasoning.LearningReasoningEngine
import com.rola.app.domain.model.CognitiveActivityType
import com.rola.app.domain.model.CognitiveConsent
import com.rola.app.domain.model.CognitiveLearningActivity
import com.rola.app.domain.model.CognitiveMemoryType
import com.rola.app.domain.model.CognitivePermission
import com.rola.app.domain.model.CognitiveSecurityContext
import com.rola.app.domain.model.EngagementLevel
import com.rola.app.domain.model.MemoryAbility
import com.rola.app.domain.model.PreferredLearningMethod
import com.rola.app.domain.model.SkillLevel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class CognitiveAIPlatformTest {
    private val memoryManager = AdaptiveMemoryManager()
    private val learnerModelManager = LearnerModelManager(memoryManager)
    private val behaviorAnalyzer = BehaviorAnalyzer()
    private val emotionAnalyzer = EmotionLearningAnalyzer()
    private val predictionEngine = LearningPredictionEngine()
    private val reasoningEngine = LearningReasoningEngine()
    private val personalizationEngine = PersonalizationEngine()
    private val decisionEngine = EducationalDecisionEngine(reasoningEngine)
    private val privacyGuard = CognitivePrivacyGuard()

    @Test
    fun learnerProfileCapturesStrengthsWeaknessesStyleAndMemoryAbility() {
        val profile = learnerModelManager.buildProfile("learner-1", sampleActivities())

        assertEquals(SkillLevel.Intermediate, profile.learningLevel)
        assertTrue(profile.knowledgeStrengths.contains("Electricity"))
        assertTrue(profile.knowledgeWeaknesses.contains("Voltage"))
        assertEquals(PreferredLearningMethod.ARModel, profile.preferredLearningMethod)
        assertTrue(profile.memoryAbility == MemoryAbility.Stable || profile.memoryAbility == MemoryAbility.Developing)
    }

    @Test
    fun adaptiveMemoryStoresShortTermLongTermAndMistakes() {
        val records = memoryManager.buildMemoryRecords(sampleActivities())

        assertTrue(records.any { it.memoryType == CognitiveMemoryType.ShortTermObjective })
        assertTrue(records.any { it.memoryType == CognitiveMemoryType.RecentMistake })
        assertTrue(memoryManager.knows(records).contains("Electricity"))
        assertTrue(memoryManager.strugglesWith(records).contains("Voltage"))
    }

    @Test
    fun behaviorAnalyticsRecommendsArForVisualExploration() {
        val report = behaviorAnalyzer.analyze(sampleActivities())

        assertEquals(PreferredLearningMethod.ARModel, report.recommendedMethod)
        assertTrue(report.objectScanningPattern.contains("visual", ignoreCase = true))
    }

    @Test
    fun emotionAnalyzerDetectsFrustrationAndChangesTeachingStyle() {
        val report = emotionAnalyzer.analyze(sampleActivities() + CognitiveLearningActivity("a5", "learner-1", CognitiveActivityType.Quiz, "Voltage", 120_000, 42, "Wrong units"))

        assertTrue(report.frustrationRisk >= 40)
        assertTrue(report.recommendedAdjustment.contains("simpler", ignoreCase = true))
        assertTrue(report.engagement == EngagementLevel.Moderate || report.engagement == EngagementLevel.High)
    }

    @Test
    fun predictionEngineBuildsFutureRoadmapFromWeaknesses() {
        val profile = learnerModelManager.buildProfile("learner-1", sampleActivities())
        val prediction = predictionEngine.predict(profile)

        assertTrue(prediction.predictedKnowledgeGaps.any { it.contains("Voltage") })
        assertTrue(prediction.requiredLearningPath.any { it.contains("Voltage") })
    }

    @Test
    fun reasoningEngineExplainsRepeatedFailureAndSimplifiesNextLesson() {
        val profile = learnerModelManager.buildProfile("learner-1", sampleActivities())
        val reasoning = reasoningEngine.reasonAboutProblem(profile, "Voltage")
        val explanation = reasoningEngine.adjustedExplanation(profile, "Voltage")

        assertTrue(reasoning.any { it.contains("Failure detected") })
        assertTrue(explanation.contains("simple", ignoreCase = true))
    }

    @Test
    fun recommendationAndDecisionUseCognitiveProfile() {
        val profile = learnerModelManager.buildProfile("learner-1", sampleActivities())
        val recommendation = personalizationEngine.recommendation(profile)
        val decision = decisionEngine.decide(profile, recommendation)

        assertEquals("Voltage", recommendation.nextLesson)
        assertEquals(SkillLevel.Beginner, decision.difficultyLevel)
        assertFalse(decision.requiresConsent)
    }

    @Test
    fun privacyGuardRequiresConsentAndPermission() {
        val context = CognitiveSecurityContext(
            userId = "learner-1",
            institutionId = "institution-1",
            permissions = setOf(CognitivePermission.AnalyzeLearner),
            consent = CognitiveConsent(cognitiveAnalysisEnabled = true, emotionAnalysisEnabled = false, cloudProcessingEnabled = false),
        )

        assertTrue(privacyGuard.canAnalyze(context))
        assertEquals("local-ai-processing", privacyGuard.processingMode(context))
    }

    private fun sampleActivities(): List<CognitiveLearningActivity> = listOf(
        CognitiveLearningActivity("a1", "learner-1", CognitiveActivityType.ARObjectExploration, "Electricity", 420_000, 84),
        CognitiveLearningActivity("a2", "learner-1", CognitiveActivityType.Quiz, "Voltage", 180_000, 48, "Confused voltage with current"),
        CognitiveLearningActivity("a3", "learner-1", CognitiveActivityType.TutorConversation, "Voltage", 240_000, 64),
        CognitiveLearningActivity("a4", "learner-1", CognitiveActivityType.Simulation, "Circuit", 360_000, 76),
    )
}
