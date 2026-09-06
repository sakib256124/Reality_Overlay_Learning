package com.rola.app.neural_learning_ai.neural_core

enum class NeuralProcessingMode { ConceptEncoding, RelationshipMapping, MemoryReinforcement, AdaptiveResponse }
enum class CognitiveDifficulty { Low, Medium, High }
enum class NeuralLearningStatus { Processing, Adapted, Reinforcing, Complete }

data class NeuralLearningRequest(
    val learnerId: String,
    val concept: String,
    val priorKnowledge: List<String>,
    val learningPatterns: List<String>,
    val attentionSignals: List<String>,
    val emotionalState: String,
    val masteryLevel: String,
)

data class NeuralKnowledgeRepresentation(val representationId: String, val concepts: List<String>, val relationships: List<String>, val previousKnowledge: List<String>, val cognitiveDifficulty: CognitiveDifficulty, val learningResponse: String)
data class CognitiveLearningProfile(val profileId: String, val behavior: List<String>, val understandingSpeed: Int, val memoryAbility: Int, val problemSolvingStyle: String, val attentionPatterns: List<String>, val privacyProtected: Boolean)
data class KnowledgePathway(val pathwayId: String, val optimalSequence: List<String>, val conceptDependencies: List<String>, val skillProgression: List<String>, val knowledgeConnections: List<String>)
data class NeuralMemoryState(val memoryId: String, val retainedConcepts: List<String>, val reinforcementPlan: List<String>, val forgettingPredictions: List<String>, val memoryImprovements: List<String>, val lifelongMemoryIntegrated: Boolean)
data class LearningAdaptationPlan(val adaptationId: String, val contentDifficulty: String, val explanationStyle: String, val learningSpeed: String, val practiceFrequency: String, val transparentReason: String)
data class NeuralAssistantGuidance(val assistantId: String, val thinkingPattern: String, val personalizedGuidance: List<String>, val learningStrategies: List<String>, val understandingImprovements: List<String>)
data class KnowledgeConnectionReport(val connectionId: String, val hiddenRelationships: List<String>, val crossDomainConnections: List<String>, val learningOpportunities: List<String>)
data class NeuralLearningReport(val reportId: String, val knowledgeGrowth: Int, val cognitiveImprovement: Int, val skillEvolution: List<String>, val recommendations: List<String>, val ethicalStatus: String)
data class NeuralLearningResult(
    val resultId: String,
    val representation: NeuralKnowledgeRepresentation,
    val cognitiveProfile: CognitiveLearningProfile,
    val pathway: KnowledgePathway,
    val memory: NeuralMemoryState,
    val adaptation: LearningAdaptationPlan,
    val assistant: NeuralAssistantGuidance,
    val connections: KnowledgeConnectionReport,
    val analytics: NeuralLearningReport,
    val status: NeuralLearningStatus,
)
