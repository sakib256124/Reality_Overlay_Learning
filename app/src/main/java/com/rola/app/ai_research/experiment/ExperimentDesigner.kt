package com.rola.app.ai_research.experiment

import com.rola.app.ai_research.scientist.ExperimentPlan
import com.rola.app.ai_research.scientist.ResearchHypothesisSet
import javax.inject.Inject

class ExperimentDesigner @Inject constructor() {
    fun design(hypotheses: ResearchHypothesisSet): ExperimentPlan =
        ExperimentPlan(
            experimentId = "experiment-${hypotheses.hypothesisId}",
            procedures = listOf("define variables", "run baseline", "run AI-assisted simulation", "compare outcomes"),
            resources = listOf("Digital Twin", "Spatial AI", "Simulation Engine", "teacher review"),
            simulationPlans = listOf("virtual lab", "AR scenario", "controlled comparison"),
            expectedResults = hypotheses.hypotheses.map { "Evidence for: $it" },
        )
}
