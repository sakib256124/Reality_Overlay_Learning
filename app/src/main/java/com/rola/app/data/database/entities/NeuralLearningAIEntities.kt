package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "neural_learning_profiles", indices = [Index(value = ["privacyProtected"])])
data class NeuralLearningProfileEntity(@PrimaryKey val profileId: String, val learnerId: String, val behavior: List<String>, val understandingSpeed: Int, val memoryAbility: Int, val problemSolvingStyle: String, val attentionPatterns: List<String>, val privacyProtected: Boolean)
@Entity(tableName = "neural_learning_cognitive_models", indices = [Index(value = ["cognitiveDifficulty"])])
data class NeuralLearningCognitiveModelEntity(@PrimaryKey val modelId: String, val concepts: List<String>, val relationships: List<String>, val previousKnowledge: List<String>, val cognitiveDifficulty: String, val learningResponse: String)
@Entity(tableName = "neural_learning_knowledge_pathways", indices = [Index(value = ["pathwayId"])])
data class NeuralLearningKnowledgePathwayEntity(@PrimaryKey val pathwayId: String, val optimalSequence: List<String>, val conceptDependencies: List<String>, val skillProgression: List<String>, val knowledgeConnections: List<String>)
@Entity(tableName = "neural_learning_memory_networks", indices = [Index(value = ["lifelongMemoryIntegrated"])])
data class NeuralLearningMemoryNetworkEntity(@PrimaryKey val memoryId: String, val retainedConcepts: List<String>, val reinforcementPlan: List<String>, val forgettingPredictions: List<String>, val memoryImprovements: List<String>, val lifelongMemoryIntegrated: Boolean)
@Entity(tableName = "neural_learning_patterns", indices = [Index(value = ["patternId"])])
data class NeuralLearningPatternEntity(@PrimaryKey val patternId: String, val thinkingPattern: String, val personalizedGuidance: List<String>, val learningStrategies: List<String>, val understandingImprovements: List<String>)
@Entity(tableName = "neural_learning_cognitive_analytics", indices = [Index(value = ["knowledgeGrowth"]), Index(value = ["cognitiveImprovement"])])
data class NeuralLearningCognitiveAnalyticsEntity(@PrimaryKey val reportId: String, val knowledgeGrowth: Int, val cognitiveImprovement: Int, val skillEvolution: List<String>, val recommendations: List<String>, val ethicalStatus: String)
@Entity(tableName = "neural_learning_adaptation_history", indices = [Index(value = ["contentDifficulty"])])
data class NeuralLearningAdaptationHistoryEntity(@PrimaryKey val adaptationId: String, val contentDifficulty: String, val explanationStyle: String, val learningSpeed: String, val practiceFrequency: String, val transparentReason: String)
