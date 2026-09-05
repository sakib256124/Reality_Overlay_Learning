package com.rola.app.reasoning_ai.reasoning_core

enum class ReasoningDomain { Mathematics, Science, ComputerScience, Engineering, Business, Research }
enum class ReasoningConfidence { Low, Medium, High, Verified }

data class ReasoningRequest(val userId: String, val question: String, val domain: ReasoningDomain, val availableKnowledge: List<String>, val studentAnswer: String?)
data class ReasoningProfile(val profileId: String, val problemSolvingStyle: String, val logicalAbility: Int, val learningMistakes: List<String>, val improvementPlan: List<String>)
data class ReasoningTrace(val traceId: String, val conceptUnderstanding: List<String>, val logicalSteps: List<String>, val knowledgeConnections: List<String>)
data class ProblemSolution(val solutionId: String, val answer: String, val method: String, val confidence: ReasoningConfidence)
data class InferenceRecord(val inferenceId: String, val hiddenDiscoveries: List<String>, val patterns: List<String>, val conclusions: List<String>)
data class ExplanationRecord(val explanationId: String, val steps: List<String>, val beginnerExplanation: String, val expertExplanation: String, val realWorldExample: String)
data class CriticalThinkingReport(val reportId: String, val analysisScore: Int, val logicalThinkingScore: Int, val decisionScore: Int, val recommendations: List<String>)
data class ReasoningDecision(val decisionId: String, val finalRecommendation: String, val verified: Boolean, val humanReviewSupported: Boolean)
data class ReasoningResult(
    val resultId: String,
    val profile: ReasoningProfile,
    val trace: ReasoningTrace,
    val solution: ProblemSolution,
    val inference: InferenceRecord,
    val explanation: ExplanationRecord,
    val criticalThinking: CriticalThinkingReport,
    val decision: ReasoningDecision,
)
