package com.rola.app.data.database.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "reasoning_profiles", indices = [Index(value = ["logicalAbility"])])
data class ReasoningProfileEntity(@PrimaryKey val profileId: String, val problemSolvingStyle: String, val logicalAbility: Int, val learningMistakes: List<String>, val improvementPlan: List<String>)
@Entity(tableName = "ai_reasoning_history", indices = [Index(value = ["traceId"])])
data class AIReasoningHistoryEntity(@PrimaryKey val traceId: String, val conceptUnderstanding: List<String>, val logicalSteps: List<String>, val knowledgeConnections: List<String>)
@Entity(tableName = "problem_solutions", indices = [Index(value = ["confidence"])])
data class ProblemSolutionEntity(@PrimaryKey val solutionId: String, val answer: String, val method: String, val confidence: String)
@Entity(tableName = "inference_records", indices = [Index(value = ["inferenceId"])])
data class InferenceRecordEntity(@PrimaryKey val inferenceId: String, val hiddenDiscoveries: List<String>, val patterns: List<String>, val conclusions: List<String>)
@Entity(tableName = "explanation_records", indices = [Index(value = ["explanationId"])])
data class ExplanationRecordEntity(@PrimaryKey val explanationId: String, val steps: List<String>, val beginnerExplanation: String, val expertExplanation: String, val realWorldExample: String)
@Entity(tableName = "critical_thinking_analytics", indices = [Index(value = ["logicalThinkingScore"])])
data class CriticalThinkingAnalyticsEntity(@PrimaryKey val reportId: String, val analysisScore: Int, val logicalThinkingScore: Int, val decisionScore: Int, val recommendations: List<String>)
@Entity(tableName = "reasoning_improvements", indices = [Index(value = ["decisionId"])])
data class ReasoningImprovementEntity(@PrimaryKey val decisionId: String, val finalRecommendation: String, val verified: Boolean, val humanReviewSupported: Boolean)
