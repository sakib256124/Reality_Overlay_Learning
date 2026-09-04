package com.rola.app.quantum_ai.intelligence

enum class QuantumComputeMode {
    Classical,
    QuantumInspired,
    HybridCloud,
    FutureQuantumHardware,
}

enum class QuantumDecisionType {
    NextLearningActivity,
    TeachingMethod,
    AssessmentType,
    CurriculumImprovement,
    KnowledgeExpansion,
}

enum class QuantumSecurityLevel {
    LocalOnly,
    InstitutionScoped,
    CloudApproved,
}

data class QuantumLearningInput(
    val learnerId: String,
    val institutionId: String,
    val topic: String,
    val behaviorSignals: List<String>,
    val cognitiveProfile: String,
    val learningHistoryScores: List<Int>,
    val knowledgeGaps: List<String>,
    val learningGoals: List<String>,
)

data class QuantumLearningProfile(
    val profileId: String,
    val learnerId: String,
    val computeMode: QuantumComputeMode,
    val optimizationReadinessPercent: Int,
    val preferredExplanationStyle: String,
    val activeGoals: List<String>,
    val updatedAt: Long = System.currentTimeMillis(),
)

data class QuantumModelState(
    val modelId: String,
    val name: String,
    val computeMode: QuantumComputeMode,
    val version: String,
    val optimizationScope: List<String>,
)

data class QuantumOptimizationResult(
    val optimizationId: String,
    val learnerId: String,
    val topic: String,
    val learningOptimizationScore: Int,
    val optimizedPath: List<String>,
    val curriculumSequence: List<String>,
    val assessmentStrategy: String,
    val recommendationStrategy: String,
    val explanation: String,
)

data class QuantumPersonalizationPlan(
    val planId: String,
    val learnerId: String,
    val optimalLearningPath: List<String>,
    val explanationStyle: String,
    val activitySelection: List<String>,
    val futurePrediction: String,
)

data class QuantumKnowledgeDiscoveryResult(
    val discoveryId: String,
    val topic: String,
    val hiddenRelationships: List<String>,
    val discoveredConcepts: List<String>,
    val scientificSignals: List<String>,
    val expansionRecommendation: String,
)

data class QuantumCurriculumPlan(
    val planId: String,
    val topic: String,
    val courseStructure: List<String>,
    val lessonSequence: List<String>,
    val difficultyProgression: String,
    val assessmentPlan: String,
    val teacherApprovalRequired: Boolean,
)

data class QuantumSimulationPlan(
    val simulationId: String,
    val topic: String,
    val simulationType: String,
    val virtualExperimentSteps: List<String>,
    val spatialIntegrationHint: String,
)

data class QuantumPrediction(
    val predictionId: String,
    val learnerId: String,
    val topic: String,
    val futurePerformancePercent: Int,
    val skillDevelopment: List<String>,
    val learningChallenges: List<String>,
    val knowledgeRequirements: List<String>,
    val longTermRoadmap: List<String>,
)

data class QuantumAIDecision(
    val decisionId: String,
    val learnerId: String,
    val topic: String,
    val decisionType: QuantumDecisionType,
    val educationalAction: String,
    val confidencePercent: Int,
    val explanation: String,
    val humanControlRequired: Boolean,
)

data class QuantumAnalyticsReport(
    val reportId: String,
    val institutionId: String,
    val learningOptimizationScore: Int,
    val aiImprovementPercent: Int,
    val predictionAccuracyPercent: Int,
    val systemIntelligenceGrowth: List<String>,
    val auditNotes: List<String>,
)

data class QuantumAIResult(
    val resultId: String,
    val profile: QuantumLearningProfile,
    val modelState: QuantumModelState,
    val optimization: QuantumOptimizationResult,
    val personalization: QuantumPersonalizationPlan,
    val knowledgeDiscovery: QuantumKnowledgeDiscoveryResult,
    val curriculumPlan: QuantumCurriculumPlan,
    val simulationPlan: QuantumSimulationPlan,
    val prediction: QuantumPrediction,
    val decision: QuantumAIDecision,
    val analytics: QuantumAnalyticsReport,
)
