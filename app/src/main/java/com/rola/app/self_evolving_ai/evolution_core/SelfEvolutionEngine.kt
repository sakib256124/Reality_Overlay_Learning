package com.rola.app.self_evolving_ai.evolution_core

import com.rola.app.self_evolving_ai.feedback_system.FeedbackLearningManager
import com.rola.app.self_evolving_ai.governance.EvolutionGovernanceManager
import com.rola.app.self_evolving_ai.improvement_engine.AIEvolutionExperimentEngine
import com.rola.app.self_evolving_ai.improvement_engine.AIImprovementManager
import com.rola.app.self_evolving_ai.learning_optimization.AdaptiveEducationEvolutionEngine
import com.rola.app.self_evolving_ai.learning_optimization.LearningOptimizationEngine
import com.rola.app.self_evolving_ai.model_evolution.ModelEvolutionManager
import com.rola.app.self_evolving_ai.performance_analysis.PerformanceAnalysisEngine
import javax.inject.Inject

class SelfEvolutionEngine @Inject constructor(
    private val performanceAnalysisEngine: PerformanceAnalysisEngine,
    private val improvementManager: AIImprovementManager,
    private val learningOptimizationEngine: LearningOptimizationEngine,
    private val modelEvolutionManager: ModelEvolutionManager,
    private val feedbackLearningManager: FeedbackLearningManager,
    private val experimentEngine: AIEvolutionExperimentEngine,
    private val adaptiveEducationEvolutionEngine: AdaptiveEducationEvolutionEngine,
    private val evolutionMemoryManager: EvolutionMemoryManager,
    private val governanceManager: EvolutionGovernanceManager,
) {
    fun evolve(request: SelfEvolutionRequest): SelfEvolutionResult {
        val report = performanceAnalysisEngine.analyze(request)
        val action = improvementManager.propose(report)
        val optimization = learningOptimizationEngine.optimize(action)
        val model = modelEvolutionManager.evolve(optimization)
        val experiment = experimentEngine.runExperiment(optimization)
        return SelfEvolutionResult(
            resultId = "self-evolution-${request.systemId}",
            performanceReport = report,
            improvementAction = action,
            learningOptimization = optimization,
            modelEvolution = model,
            feedbackLearning = feedbackLearningManager.learn(request),
            experiment = experiment,
            adaptiveEducation = adaptiveEducationEvolutionEngine.evolveEducation(experiment),
            memory = evolutionMemoryManager.remember(action, experiment),
            governance = governanceManager.govern(model),
            status = EvolutionStatus.NeedsHumanApproval,
        )
    }
}
