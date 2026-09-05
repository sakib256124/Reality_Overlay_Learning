package com.rola.app.predictive_ai.intelligence

enum class PotentialBand { Emerging, Strong, Exceptional }

data class PredictionContext(
    val userId: String,
    val topic: String,
    val learningHistory: List<String>,
    val quizScores: List<Int>,
    val cognitiveSignals: List<String>,
    val interests: List<String>,
)

data class LearningPrediction(val predictionId: String, val futurePerformance: String, val challenges: List<String>, val knowledgeGaps: List<String>, val confidenceScore: Int)
data class PotentialProfile(val profileId: String, val strengths: List<String>, val creativityScore: Int, val researchPotential: PotentialBand, val suggestedPath: String)
data class FutureSkillModel(val modelId: String, val futureSkills: List<String>, val technologyRequirements: List<String>, val emergingAreas: List<String>)
data class FutureLearningRoadmap(val roadmapId: String, val longTermPlan: List<String>, val skillRoadmap: List<String>, val researchRoadmap: List<String>, val careerStrategy: String)
data class GrowthOptimization(val optimizationId: String, val studyStrategy: String, val practiceFrequency: String, val resourceSelection: List<String>, val difficultyLevel: String)
data class KnowledgeTrendReport(val reportId: String, val scientificTrends: List<String>, val technologyChanges: List<String>, val educationDemands: List<String>)
data class LearningSimulation(val simulationId: String, val currentPathOutcome: String, val optimizedPathOutcome: String, val futureSuccessProbability: Int)
data class FutureMentorGuidance(val guidanceId: String, val predictedDifficulties: List<String>, val improvements: List<String>, val recommendedSkills: List<String>)
data class PredictionAnalytics(val analyticsId: String, val predictionAccuracy: Int, val growthScore: Int, val privacyProtected: Boolean)

data class PredictiveAIResult(
    val resultId: String,
    val prediction: LearningPrediction,
    val potential: PotentialProfile,
    val skillModel: FutureSkillModel,
    val roadmap: FutureLearningRoadmap,
    val optimization: GrowthOptimization,
    val trendReport: KnowledgeTrendReport,
    val simulation: LearningSimulation,
    val mentorGuidance: FutureMentorGuidance,
    val analytics: PredictionAnalytics,
)
