package com.rola.app.education_orchestration.ecosystem_core

import javax.inject.Inject

class EducationOrchestrationEngine @Inject constructor() {
    fun orchestrate(request: OrchestrationRequest): EducationOrchestrationPlan {
        val selected = when {
            request.requirement.contains("machine learning", ignoreCase = true) -> listOf(EducationAIService.PlanningAI, EducationAIService.KnowledgeAI, EducationAIService.TeacherAI, EducationAIService.ResearchAI)
            request.requirement.contains("stress", ignoreCase = true) -> listOf(EducationAIService.EmotionalAI, EducationAIService.TutorAI, EducationAIService.CompanionAI)
            else -> listOf(EducationAIService.CompanionAI, EducationAIService.PlanningAI, EducationAIService.MasteryAI)
        }
        return EducationOrchestrationPlan(
            orchestrationId = "orchestration-${request.userId}",
            selectedCapabilities = (selected + request.activeSystems.take(2)).distinct(),
            coordinationPlan = listOf("analyze requirement", "select AI capabilities", "coordinate services", "return learning result"),
            learningResult = "Coordinated learning support for ${request.requirement}.",
        )
    }
}
