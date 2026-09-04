package com.rola.app.domain.model

data class CloudAnalyticsEvent(
    val eventType: String,
    val timestamp: Long = System.currentTimeMillis(),
    val metrics: Map<String, Any?> = emptyMap(),
)

data class AIModelDescriptor(
    val modelId: String,
    val modelFamily: String,
    val version: String,
    val status: String,
    val accuracy: Float,
    val storagePath: String,
    val minAppVersion: String,
    val updatedAt: Long,
)

data class AIModelUpdatePlan(
    val currentModelId: String?,
    val targetModel: AIModelDescriptor?,
    val updateAvailable: Boolean,
    val rollbackModel: AIModelDescriptor? = null,
    val reason: String,
)

data class CloudRecommendationResponse(
    val recommendations: List<Recommendation>,
)

data class CloudTutorRequest(
    val message: String,
    val objectId: String? = null,
    val languageCode: String = "en",
)

data class CloudTutorResponse(
    val response: String,
    val latencyMs: Long,
)

data class CloudTranslationRequest(
    val text: String,
    val targetLanguage: String,
    val sourceLanguage: String? = null,
)

data class CloudTranslationResponse(
    val translatedText: String,
    val targetLanguage: String,
    val provider: String,
    val latencyMs: Long,
)
