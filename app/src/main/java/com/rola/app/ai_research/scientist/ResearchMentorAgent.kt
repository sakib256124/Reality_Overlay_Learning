package com.rola.app.ai_research.scientist

import javax.inject.Inject

class ResearchMentorAgent @Inject constructor() {
    fun mentor(project: ResearchProject, validation: KnowledgeValidation): ResearchMentorPlan =
        ResearchMentorPlan("mentor-${project.projectId}", listOf("tighten variables", "cite verified sources", "keep human approval"), listOf("hypothesis framing", "experiment control", "result interpretation"), project.roadmap + "publication preparation")
}
