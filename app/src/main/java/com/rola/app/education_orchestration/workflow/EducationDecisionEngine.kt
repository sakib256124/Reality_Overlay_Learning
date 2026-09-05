package com.rola.app.education_orchestration.workflow

import com.rola.app.education_orchestration.ecosystem_core.EducationDecision
import com.rola.app.education_orchestration.ecosystem_core.EducationIntelligenceReport
import com.rola.app.education_orchestration.ecosystem_core.OrchestrationRequest
import javax.inject.Inject

class EducationDecisionEngine @Inject constructor() {
    fun decide(request: OrchestrationRequest, report: EducationIntelligenceReport): EducationDecision =
        EducationDecision(
            decisionId = "education-decision-${request.userId}",
            agentToUse = if (request.requirement.contains("machine learning", ignoreCase = true)) "PlanningAI + KnowledgeAI + TeacherAI + ResearchAI" else "PersonalAgent + MasteryAI",
            learningStrategy = if (report.learningProgress < 80) "guided adaptive workflow" else "accelerated project workflow",
            resourceToProvide = "curated knowledge graph resources with teacher explanation and practice tasks",
            adaptationRequired = report.learningProgress < 80 || report.userEngagement < 70,
            transparency = "Decision is based on learner state, system health, service performance, and user feedback.",
        )
}
