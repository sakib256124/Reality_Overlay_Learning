package com.rola.app.self_evolving_ai.improvement_engine

import com.rola.app.self_evolving_ai.evolution_core.AIPerformanceReport
import com.rola.app.self_evolving_ai.evolution_core.ImprovementAction
import javax.inject.Inject

class AIImprovementManager @Inject constructor() {
    fun propose(report: AIPerformanceReport): ImprovementAction =
        ImprovementAction(
            actionId = "improvement-${report.reportId}",
            teachingStrategyImprovement = if ("teaching effectiveness" in report.weaknesses) "add clearer examples and mastery checks" else "preserve current teaching strategy",
            recommendationImprovement = if ("recommendation accuracy" in report.weaknesses) "rerank recommendations using learner outcomes" else "cache successful recommendation patterns",
            workflowImprovement = "tighten goal -> plan -> mastery -> feedback loop",
            knowledgeDeliveryImprovement = "prefer concise explanations with optional deeper paths",
            personalizationAccuracy = (report.userSatisfaction + report.learningOutcomes) / 2,
            validationRequired = true,
        )
}
