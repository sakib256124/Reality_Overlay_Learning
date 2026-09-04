package com.rola.app.cognitive_ai.decision

import com.rola.app.domain.model.CognitivePermission
import com.rola.app.domain.model.CognitiveSecurityContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CognitivePrivacyGuard @Inject constructor() {
    fun requirePermission(
        context: CognitiveSecurityContext,
        permission: CognitivePermission,
    ) {
        require(permission in context.permissions) { "Missing cognitive permission: ${permission.name}" }
    }

    fun canAnalyze(context: CognitiveSecurityContext): Boolean =
        context.consent.cognitiveAnalysisEnabled && CognitivePermission.AnalyzeLearner in context.permissions

    fun processingMode(context: CognitiveSecurityContext): String = when {
        !context.consent.cognitiveAnalysisEnabled -> "disabled"
        context.consent.cloudProcessingEnabled -> "cloud-ai-processing"
        else -> "local-ai-processing"
    }
}
