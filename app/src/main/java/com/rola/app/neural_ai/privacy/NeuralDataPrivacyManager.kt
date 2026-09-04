package com.rola.app.neural_ai.privacy

import java.security.MessageDigest
import javax.inject.Inject
import javax.inject.Singleton

data class NeuralConsentState(
    val userId: String,
    val neuralAnalysisEnabled: Boolean,
    val localProcessingOnly: Boolean,
    val allowCloudSync: Boolean,
    val consentUpdatedAt: Long = System.currentTimeMillis(),
)

data class ExplainableNeuralDecision(
    val decisionId: String,
    val userId: String,
    val reason: String,
    val sourceSignals: List<String>,
    val privacyMode: String,
)

@Singleton
class NeuralDataPrivacyManager @Inject constructor() {
    fun requireConsent(consent: NeuralConsentState) {
        require(consent.neuralAnalysisEnabled) {
            "Neural AI analysis is disabled until the learner grants explicit consent."
        }
    }

    fun anonymizedUserKey(userId: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
            .digest(userId.toByteArray())
        return digest.joinToString(separator = "") { "%02x".format(it) }.take(24)
    }

    fun explainDecision(
        userId: String,
        reason: String,
        sourceSignals: List<String>,
        localProcessingOnly: Boolean,
    ): ExplainableNeuralDecision =
        ExplainableNeuralDecision(
            decisionId = "explainable-neural-${anonymizedUserKey("$userId-$reason")}",
            userId = anonymizedUserKey(userId),
            reason = reason,
            sourceSignals = sourceSignals,
            privacyMode = if (localProcessingOnly) "Local only" else "Cloud sync allowed",
        )
}
