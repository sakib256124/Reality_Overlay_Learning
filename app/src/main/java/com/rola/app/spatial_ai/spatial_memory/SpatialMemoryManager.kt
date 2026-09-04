package com.rola.app.spatial_ai.spatial_memory

import com.rola.app.domain.model.ImmersiveLearningReport
import com.rola.app.domain.model.SpatialInteractionEvent
import java.util.UUID
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SpatialMemoryManager @Inject constructor() {
    fun immersiveReport(
        sessionId: String,
        interactions: List<SpatialInteractionEvent>,
        simulationScore: Int,
    ): ImmersiveLearningReport {
        val totalTime = interactions.sumOf { it.durationMillis }
        val difficult = interactions
            .filter { it.successSignal.contains("confused", ignoreCase = true) || it.successSignal.contains("retry", ignoreCase = true) }
            .map { it.objectId }
            .distinct()
        return ImmersiveLearningReport(
            reportId = "immersive-report-${UUID.randomUUID()}",
            sessionId = sessionId,
            objectInteractionTimeMillis = totalTime,
            explorationPattern = if (interactions.map { it.objectId }.distinct().size >= 3) "Broad exploration" else "Focused exploration",
            simulationPerformance = simulationScore.coerceIn(0, 100),
            attentionPattern = if (totalTime >= 180_000) "Sustained spatial attention" else "Short interaction bursts",
            difficultConcepts = difficult,
            recommendations = difficult.map { "Add guided manipulation and AI explanation for $it." }
                .ifEmpty { listOf("Continue with higher-complexity spatial challenges.") },
        )
    }
}
