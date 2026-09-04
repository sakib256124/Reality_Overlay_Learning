package com.rola.app.core.agi

import com.rola.app.domain.model.AGIPrivacyMode
import com.rola.app.domain.model.AISafetyReport
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AISafetySystem @Inject constructor() {
    fun verify(
        content: String,
        evidence: List<String>,
        privacyMode: AGIPrivacyMode,
    ): AISafetyReport {
        val hasEvidence = evidence.any { it.isNotBlank() }
        val claimsTooCertain = content.contains("always", ignoreCase = true) || content.contains("never", ignoreCase = true)
        val personalDataRisk = content.contains("@") || content.contains("phone", ignoreCase = true)
        val privacyRisk = when {
            privacyMode == AGIPrivacyMode.Strict && personalDataRisk -> 0.8f
            personalDataRisk -> 0.55f
            privacyMode == AGIPrivacyMode.Strict -> 0.25f
            else -> 0.1f
        }
        val accuracy = when {
            hasEvidence && !claimsTooCertain -> 0.92f
            hasEvidence -> 0.8f
            else -> 0.62f
        }
        val biasRisk = if (content.contains("all students", ignoreCase = true)) 0.45f else 0.15f
        return AISafetyReport(
            reportId = "ai-safety-${UUID.randomUUID()}",
            verified = accuracy >= 0.75f && privacyRisk < 0.6f && biasRisk < 0.5f,
            educationalAccuracy = accuracy,
            biasRisk = biasRisk,
            privacyRisk = privacyRisk,
            requiresHumanApproval = accuracy < 0.8f || privacyRisk >= 0.4f || biasRisk >= 0.4f,
            notes = listOf(
                "Evidence available: $hasEvidence",
                "Privacy mode: ${privacyMode.name}",
                "Human approval required: ${accuracy < 0.8f || privacyRisk >= 0.4f || biasRisk >= 0.4f}",
            ),
        )
    }
}
