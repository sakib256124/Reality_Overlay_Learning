package com.rola.app.unit

import com.rola.app.neural_learning_ai.adaptation.LearningAdaptationEngine
import com.rola.app.neural_learning_ai.cognitive_processing.CognitiveLearningManager
import com.rola.app.neural_learning_ai.intelligence.KnowledgeConnectionEngine
import com.rola.app.neural_learning_ai.intelligence.NeuralIntelligenceAnalyzer
import com.rola.app.neural_learning_ai.intelligence.NeuralLearningAnalytics
import com.rola.app.neural_learning_ai.intelligence.NeuralLearningAssistant
import com.rola.app.neural_learning_ai.knowledge_pathways.KnowledgePathwayEngine
import com.rola.app.neural_learning_ai.memory_network.NeuralMemoryNetwork
import com.rola.app.neural_learning_ai.neural_core.NeuralKnowledgeProcessor
import com.rola.app.neural_learning_ai.neural_core.NeuralLearningEngine
import com.rola.app.neural_learning_ai.neural_core.NeuralLearningRequest
import com.rola.app.neural_learning_ai.neural_core.NeuralLearningStatus
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class NeuralLearningAIPlatformTest {
    private val engine = NeuralLearningEngine(
        NeuralKnowledgeProcessor(),
        CognitiveLearningManager(),
        KnowledgePathwayEngine(),
        NeuralMemoryNetwork(),
        LearningAdaptationEngine(),
        NeuralLearningAssistant(),
        KnowledgeConnectionEngine(),
        NeuralIntelligenceAnalyzer(),
        NeuralLearningAnalytics(),
    )

    @Test
    fun neuralLearning_personalizesPathwaysMemoryAdaptationAndEthics() {
        val result = engine.personalize(
            NeuralLearningRequest(
                learnerId = "neural-learner",
                concept = "AI development",
                priorKnowledge = listOf("programming basics", "algorithms", "data structures"),
                learningPatterns = listOf("visual learner", "project learning"),
                attentionSignals = listOf("focused", "prefers diagrams"),
                emotionalState = "confident",
                masteryLevel = "intermediate",
            ),
        )

        assertEquals(NeuralLearningStatus.Adapted, result.status)
        assertTrue(result.representation.relationships.any { it.contains("algorithms supports AI development") })
        assertTrue(result.cognitiveProfile.privacyProtected)
        assertTrue(result.pathway.optimalSequence.contains("AI development"))
        assertTrue(result.memory.lifelongMemoryIntegrated)
        assertTrue(result.memory.memoryImprovements.isNotEmpty())
        assertTrue(result.adaptation.transparentReason.contains("cognitive profile"))
        assertTrue(result.assistant.learningStrategies.contains("visual analogy and diagram"))
        assertTrue(result.connections.crossDomainConnections.any { it.contains("advanced engineering") })
        assertTrue(result.analytics.knowledgeGrowth >= 90)
        assertTrue(result.analytics.ethicalStatus.contains("Cognitive data privacy"))
    }
}
