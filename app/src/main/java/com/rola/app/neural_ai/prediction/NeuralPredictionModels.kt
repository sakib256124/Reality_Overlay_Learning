package com.rola.app.neural_ai.prediction

data class NeuralLearningPrediction(
    val predictionId: String,
    val userId: String,
    val topic: String,
    val learningSuccessPercent: Int,
    val requiredSupport: List<String>,
    val skillDevelopment: List<String>,
    val knowledgeRetentionPercent: Int,
    val longTermRoadmap: List<String>,
    val explanation: String,
)
