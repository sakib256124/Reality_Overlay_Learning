package com.rola.app.digital_twin_ai.education

import com.rola.app.digital_twin_ai.twin_core.DigitalTwinRequest
import com.rola.app.digital_twin_ai.twin_core.TwinAnalysisReport
import com.rola.app.digital_twin_ai.twin_core.TwinLearningSession
import javax.inject.Inject

class TwinLearningManager @Inject constructor() {
    fun teach(request: DigitalTwinRequest, report: TwinAnalysisReport): TwinLearningSession =
        TwinLearningSession(
            sessionId = "learning-${report.reportId}",
            explanations = listOf("Explain how ${request.realWorldObject} works", "Connect behavior to ${request.learningGoal}"),
            guidedExperiments = listOf("change one variable", "observe outcome", "compare with prediction"),
            conceptDemonstrations = report.behaviorPatterns,
            skillDevelopmentTasks = listOf("diagnose system behavior", "write simulation reflection", "design a safer experiment"),
        )
}
