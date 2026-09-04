package com.rola.app.unit

import com.rola.app.quantum_ai.analytics.QuantumAnalyticsEngine
import com.rola.app.quantum_ai.integration.QuantumResponsibleAIManager
import com.rola.app.quantum_ai.intelligence.QuantumCurriculumEngine
import com.rola.app.quantum_ai.intelligence.QuantumDecisionEngine
import com.rola.app.quantum_ai.intelligence.QuantumDecisionType
import com.rola.app.quantum_ai.intelligence.QuantumIntelligenceManager
import com.rola.app.quantum_ai.intelligence.QuantumKnowledgeDiscovery
import com.rola.app.quantum_ai.intelligence.QuantumLearningInput
import com.rola.app.quantum_ai.intelligence.QuantumPredictionEngine
import com.rola.app.quantum_ai.optimization.QuantumLearningOptimizer
import com.rola.app.quantum_ai.optimization.QuantumOptimizationEngine
import com.rola.app.quantum_ai.optimization.QuantumPersonalizationEngine
import com.rola.app.quantum_ai.quantum_engine.QuantumAIEngine
import com.rola.app.quantum_ai.quantum_learning.QuantumLearningProcessor
import com.rola.app.quantum_ai.simulation.QuantumSimulationEngine
import com.rola.app.quantum_ai.simulation.QuantumSimulationManager
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class QuantumAIPlatformTest {
    private val optimizationEngine = QuantumOptimizationEngine()
    private val engine = QuantumAIEngine(
        learningProcessor = QuantumLearningProcessor(),
        intelligenceManager = QuantumIntelligenceManager(),
        learningOptimizer = QuantumLearningOptimizer(optimizationEngine),
        personalizationEngine = QuantumPersonalizationEngine(),
        knowledgeDiscovery = QuantumKnowledgeDiscovery(),
        curriculumEngine = QuantumCurriculumEngine(),
        simulationManager = QuantumSimulationManager(QuantumSimulationEngine()),
        predictionEngine = QuantumPredictionEngine(),
        decisionEngine = QuantumDecisionEngine(),
        analyticsEngine = QuantumAnalyticsEngine(),
        responsibleAIManager = QuantumResponsibleAIManager(),
    )

    @Test
    fun quantumOptimization_prioritizesKnowledgeGapsInLearningPath() {
        val profile = QuantumLearningProcessor().buildProfile(sampleInput())
        val result = optimizationEngine.optimize(sampleInput(), profile)

        assertTrue(result.optimizedPath.any { it.contains("Voltage") })
        assertTrue(result.learningOptimizationScore >= 30)
    }

    @Test
    fun quantumCycle_generatesDecisionPredictionAndDiscovery() {
        val result = engine.runQuantumEducationCycle(sampleInput())

        assertEquals(QuantumDecisionType.NextLearningActivity, result.decision.decisionType)
        assertTrue(result.prediction.learningChallenges.contains("Voltage"))
        assertTrue(result.knowledgeDiscovery.hiddenRelationships.isNotEmpty())
        assertTrue(result.decision.humanControlRequired)
    }

    @Test
    fun curriculumPlan_requiresTeacherApproval() {
        val result = engine.runQuantumEducationCycle(sampleInput())

        assertTrue(result.curriculumPlan.teacherApprovalRequired)
        assertTrue(result.analytics.auditNotes.any { it.contains("Human control", ignoreCase = true) })
    }

    private fun sampleInput(): QuantumLearningInput =
        QuantumLearningInput(
            learnerId = "learner-1",
            institutionId = "school-1",
            topic = "Electric Circuits",
            behaviorSignals = listOf("visual: AR circuit activity", "research: misconception data"),
            cognitiveProfile = "visual learner",
            learningHistoryScores = listOf(62, 58, 70),
            knowledgeGaps = listOf("Voltage", "Current flow"),
            learningGoals = listOf("Explain a closed circuit"),
        )
}
