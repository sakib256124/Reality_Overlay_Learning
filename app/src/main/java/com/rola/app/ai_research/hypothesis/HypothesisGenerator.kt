package com.rola.app.ai_research.hypothesis

import com.rola.app.ai_research.scientist.ResearchContext
import com.rola.app.ai_research.scientist.ResearchHypothesisSet
import javax.inject.Inject

class HypothesisGenerator @Inject constructor() {
    fun generate(context: ResearchContext): ResearchHypothesisSet =
        ResearchHypothesisSet(
            hypothesisId = "hypothesis-${context.userId}",
            questions = listOf(context.question, "Which variable most changes learning outcomes?"),
            hypotheses = listOf("If learners use simulation-first exploration, retention improves.", "If prior misconceptions are surfaced, research quality improves."),
            possibleSolutions = listOf("guided experiment", "digital twin simulation", "teacher-reviewed research plan"),
        )
}
