package com.rola.app.neural_learning_ai.neural_core

import com.rola.app.neural_learning_ai.adaptation.LearningAdaptationEngine
import com.rola.app.neural_learning_ai.cognitive_processing.CognitiveLearningManager
import com.rola.app.neural_learning_ai.intelligence.KnowledgeConnectionEngine
import com.rola.app.neural_learning_ai.intelligence.NeuralIntelligenceAnalyzer
import com.rola.app.neural_learning_ai.intelligence.NeuralLearningAnalytics
import com.rola.app.neural_learning_ai.intelligence.NeuralLearningAssistant
import com.rola.app.neural_learning_ai.knowledge_pathways.KnowledgePathwayEngine
import com.rola.app.neural_learning_ai.memory_network.NeuralMemoryNetwork
import javax.inject.Inject

class NeuralLearningEngine @Inject constructor(
    private val processor: NeuralKnowledgeProcessor,
    private val cognitiveLearningManager: CognitiveLearningManager,
    private val pathwayEngine: KnowledgePathwayEngine,
    private val memoryNetwork: NeuralMemoryNetwork,
    private val adaptationEngine: LearningAdaptationEngine,
    private val assistant: NeuralLearningAssistant,
    private val connectionEngine: KnowledgeConnectionEngine,
    private val analyzer: NeuralIntelligenceAnalyzer,
    private val analytics: NeuralLearningAnalytics,
) {
    fun personalize(request: NeuralLearningRequest): NeuralLearningResult {
        val representation = processor.process(request)
        val profile = cognitiveLearningManager.model(request)
        val pathway = pathwayEngine.generate(representation)
        val memory = memoryNetwork.reinforce(pathway)
        val adaptation = adaptationEngine.adapt(request, profile)
        return NeuralLearningResult(
            resultId = "neural-learning-${request.learnerId}",
            representation = representation,
            cognitiveProfile = profile,
            pathway = pathway,
            memory = memory,
            adaptation = adaptation,
            assistant = assistant.guide(profile, adaptation),
            connections = connectionEngine.discover(representation),
            analytics = analytics.finalize(analyzer.analyze(profile, pathway, memory)),
            status = NeuralLearningStatus.Adapted,
        )
    }
}
