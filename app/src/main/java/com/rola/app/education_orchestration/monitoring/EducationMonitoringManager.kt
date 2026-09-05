package com.rola.app.education_orchestration.monitoring

import com.rola.app.education_orchestration.ecosystem_core.AIServiceState
import com.rola.app.education_orchestration.ecosystem_core.EducationIntelligenceReport
import com.rola.app.education_orchestration.ecosystem_core.OrchestrationRequest
import javax.inject.Inject

class EducationMonitoringManager @Inject constructor() {
    fun monitor(request: OrchestrationRequest, serviceState: AIServiceState): EducationIntelligenceReport =
        EducationIntelligenceReport(
            reportId = "report-${request.userId}",
            monitoredServices = serviceState.activeServices.map { it.name },
            learningProgress = 78,
            knowledgeGrowth = 82,
            userEngagement = request.userFeedback.coerceIn(0, 100),
            systemHealth = request.systemHealth.coerceIn(0, 100),
        )
}
