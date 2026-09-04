package com.rola.app.domain.model

data class SceneContext(
    val sceneId: String,
    val sceneType: String,
    val description: String,
    val confidence: Float,
    val detectedCategories: List<String>,
    val recommendedTopics: List<String>,
    val safetyInformation: List<String>,
    val scientificExplanations: List<String>,
) {
    val confidencePercent: Int
        get() = (confidence * 100f).toInt().coerceIn(0, 100)
}
