package com.rola.app.ai_research.scientist

import javax.inject.Inject

class AIResearchScientistAgent @Inject constructor() {
    fun createProject(context: ResearchContext): ResearchProject =
        ResearchProject(
            projectId = "research-project-${context.userId}-${context.domain.lowercase().replace(" ", "-")}",
            title = "AI-assisted discovery in ${context.domain}",
            domain = context.domain,
            roadmap = listOf("review knowledge", "find gap", "form hypothesis", "design experiment", "validate result"),
        )
}
