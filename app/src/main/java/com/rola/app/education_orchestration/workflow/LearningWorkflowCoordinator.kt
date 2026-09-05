package com.rola.app.education_orchestration.workflow

import com.rola.app.education_orchestration.ecosystem_core.EducationAIService
import com.rola.app.education_orchestration.ecosystem_core.LearningWorkflow
import com.rola.app.education_orchestration.ecosystem_core.OrchestrationRequest
import javax.inject.Inject

class LearningWorkflowCoordinator @Inject constructor() {
    fun createWorkflow(request: OrchestrationRequest): LearningWorkflow =
        LearningWorkflow(
            workflowId = "workflow-${request.userId}",
            lifecycleSteps = listOf("goal creation", "planning", "learning", "assessment", "mastery analysis", "improvement"),
            integratedSystems = listOf(EducationAIService.MasteryAI, EducationAIService.PredictiveAI, EducationAIService.EmotionalAI, EducationAIService.CompanionAI),
            currentStage = if (request.learningState.contains("gap", ignoreCase = true)) "improvement" else "learning",
        )
}
