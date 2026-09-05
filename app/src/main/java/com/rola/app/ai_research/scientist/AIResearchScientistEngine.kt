package com.rola.app.ai_research.scientist

import com.rola.app.ai_research.analysis.KnowledgeValidationEngine
import com.rola.app.ai_research.analysis.ResearchAnalysisEngine
import com.rola.app.ai_research.collaboration.ResearchCollaborationManager
import com.rola.app.ai_research.discovery.ResearchDiscoveryManager
import com.rola.app.ai_research.experiment.ExperimentDesigner
import com.rola.app.ai_research.hypothesis.HypothesisGenerator
import javax.inject.Inject

class AIResearchScientistEngine @Inject constructor(
    private val agent: AIResearchScientistAgent,
    private val discoveryManager: ResearchDiscoveryManager,
    private val hypothesisGenerator: HypothesisGenerator,
    private val experimentDesigner: ExperimentDesigner,
    private val analysisEngine: ResearchAnalysisEngine,
    private val validationEngine: KnowledgeValidationEngine,
    private val collaborationManager: ResearchCollaborationManager,
    private val mentorAgent: ResearchMentorAgent,
    private val scientificLearningEngine: ScientificLearningEngine,
) {
    fun runResearchCycle(context: ResearchContext): AIResearchResult {
        val project = agent.createProject(context)
        val discovery = discoveryManager.discover(context)
        val hypotheses = hypothesisGenerator.generate(context)
        val experiment = experimentDesigner.design(hypotheses)
        val analysis = analysisEngine.analyze(experiment)
        val validation = validationEngine.validate(analysis)
        return AIResearchResult(
            resultId = "ai-research-${context.userId}",
            project = project,
            discovery = discovery,
            hypotheses = hypotheses,
            experiment = experiment,
            analysis = analysis,
            validation = validation,
            collaboration = collaborationManager.connect(project),
            learningPackage = scientificLearningEngine.createPackage(context),
            mentorPlan = mentorAgent.mentor(project, validation),
            analytics = ResearchAnalytics("research-analytics-${context.userId}", 91, validation.accuracyScore, 89),
        )
    }
}
