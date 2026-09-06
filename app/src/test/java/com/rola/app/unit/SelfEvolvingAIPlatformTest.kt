package com.rola.app.unit

import com.rola.app.self_evolving_ai.evolution_core.EvolutionMemoryManager
import com.rola.app.self_evolving_ai.evolution_core.EvolutionStatus
import com.rola.app.self_evolving_ai.evolution_core.SelfEvolutionEngine
import com.rola.app.self_evolving_ai.evolution_core.SelfEvolutionRequest
import com.rola.app.self_evolving_ai.feedback_system.FeedbackLearningManager
import com.rola.app.self_evolving_ai.governance.EvolutionGovernanceManager
import com.rola.app.self_evolving_ai.improvement_engine.AIEvolutionExperimentEngine
import com.rola.app.self_evolving_ai.improvement_engine.AIImprovementManager
import com.rola.app.self_evolving_ai.learning_optimization.AdaptiveEducationEvolutionEngine
import com.rola.app.self_evolving_ai.learning_optimization.LearningOptimizationEngine
import com.rola.app.self_evolving_ai.model_evolution.ModelEvolutionManager
import com.rola.app.self_evolving_ai.performance_analysis.PerformanceAnalysisEngine
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class SelfEvolvingAIPlatformTest {
    private val engine = SelfEvolutionEngine(
        PerformanceAnalysisEngine(),
        AIImprovementManager(),
        LearningOptimizationEngine(),
        ModelEvolutionManager(),
        FeedbackLearningManager(),
        AIEvolutionExperimentEngine(),
        AdaptiveEducationEvolutionEngine(),
        EvolutionMemoryManager(),
        EvolutionGovernanceManager(),
    )

    @Test
    fun selfEvolution_detectsWeaknessImprovesModelLearnsFeedbackAndKeepsSafetyControls() {
        val result = engine.evolve(
            SelfEvolutionRequest(
                systemId = "rola-self-evolution",
                aiResponseQuality = 78,
                teachingEffectiveness = 76,
                recommendationAccuracy = 74,
                learningOutcomes = 83,
                userSatisfaction = 88,
                systemPerformance = 91,
                feedback = listOf("need clearer examples", "recommendations should match mastery gaps"),
            ),
        )

        assertEquals(EvolutionStatus.NeedsHumanApproval, result.status)
        assertTrue(result.performanceReport.weaknesses.contains("teaching effectiveness"))
        assertTrue(result.improvementAction.validationRequired)
        assertTrue(result.learningOptimization.learningPathOptimization.contains("Mastery AI"))
        assertTrue(result.modelEvolution.rollbackSupported)
        assertTrue(result.feedbackLearning.behaviorImprovement.contains("feedback"))
        assertTrue(result.experiment.improvementScore > result.learningOptimization.qualityScore)
        assertTrue(result.adaptiveEducation.studentExperienceUpdate.contains("learner-controlled"))
        assertTrue(result.memory.successfulStrategies.contains(result.experiment.winningStrategy))
        assertTrue(result.governance.safetyLimits.isNotEmpty())
    }
}
