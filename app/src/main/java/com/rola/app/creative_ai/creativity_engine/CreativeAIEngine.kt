package com.rola.app.creative_ai.creativity_engine

import com.rola.app.creative_ai.collaboration.HumanAICreativityManager
import com.rola.app.creative_ai.content_generation.CreativePersonalizationEngine
import com.rola.app.creative_ai.content_generation.LearningContentCreator
import com.rola.app.creative_ai.evaluation.CreativeEvaluationEngine
import com.rola.app.creative_ai.innovation.InnovationDiscoveryEngine
import com.rola.app.creative_ai.research_creation.ResearchIdeaGenerator
import com.rola.app.creative_ai.simulation_creation.CreativeSimulationEngine
import javax.inject.Inject

class CreativeAIEngine @Inject constructor(
    private val contentCreator: LearningContentCreator,
    private val knowledgeGenerator: CreativeKnowledgeGenerator,
    private val innovationDiscoveryEngine: InnovationDiscoveryEngine,
    private val researchIdeaGenerator: ResearchIdeaGenerator,
    private val simulationEngine: CreativeSimulationEngine,
    private val personalizationEngine: CreativePersonalizationEngine,
    private val creativityManager: HumanAICreativityManager,
    private val evaluationEngine: CreativeEvaluationEngine,
) {
    fun createEducationPackage(request: CreativeAIRequest): CreativeAIResult {
        val content = contentCreator.create(request)
        val knowledge = knowledgeGenerator.generate(request)
        val innovation = innovationDiscoveryEngine.discover(request)
        val research = researchIdeaGenerator.generate(request)
        val simulation = simulationEngine.create(request)
        val personalization = personalizationEngine.personalize(request)
        val collaboration = creativityManager.collaborate(request)
        val evaluation = evaluationEngine.evaluate(content, innovation)
        return CreativeAIResult("creative-ai-${request.userId}", content, knowledge, innovation, research, simulation, personalization, collaboration, evaluation)
    }
}
