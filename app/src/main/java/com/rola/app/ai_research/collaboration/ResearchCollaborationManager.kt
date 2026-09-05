package com.rola.app.ai_research.collaboration

import com.rola.app.ai_research.scientist.ResearchCollaboration
import com.rola.app.ai_research.scientist.ResearchProject
import javax.inject.Inject

class ResearchCollaborationManager @Inject constructor() {
    fun connect(project: ResearchProject): ResearchCollaboration =
        ResearchCollaboration("collaboration-${project.projectId}", listOf("Students", "Teachers", "Researchers", "AI Agents"), listOf(project.title), "Shared project discussion and knowledge exchange enabled.")
}
