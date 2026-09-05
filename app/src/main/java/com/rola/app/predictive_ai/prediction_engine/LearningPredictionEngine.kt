package com.rola.app.predictive_ai.prediction_engine

import com.rola.app.predictive_ai.intelligence.LearningPrediction
import com.rola.app.predictive_ai.intelligence.PredictionContext
import javax.inject.Inject

class LearningPredictionEngine @Inject constructor() {
    fun predict(context: PredictionContext): LearningPrediction {
        val avg = context.quizScores.ifEmpty { listOf(70) }.average().toInt()
        return LearningPrediction(
            predictionId = "prediction-${context.userId}",
            futurePerformance = if (avg >= 75) "accelerating" else "needs targeted recovery",
            challenges = listOf("future abstraction load", "retention drift") + context.cognitiveSignals,
            knowledgeGaps = context.learningHistory.filter { it.contains("gap", ignoreCase = true) }.ifEmpty { listOf("next prerequisite check") },
            confidenceScore = (avg + 20).coerceAtMost(96),
        )
    }
}
